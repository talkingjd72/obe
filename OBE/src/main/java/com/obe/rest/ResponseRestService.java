package com.obe.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.obe.data.ResponseDAO;
import com.obe.model.ResponseModel;
import com.obe.service.ResponseRegistration;

@Path("/responses")
@RequestScoped
public class ResponseRestService {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private ResponseDAO repository;

    @Inject
    ResponseRegistration registration;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResponseModel> listAllResponses() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{idResponse:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel lookupResponseById(@PathParam("idResponse") int idResponse) {
        ResponseModel response = repository.findById(idResponse);
        if (response == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return response;
    }
    
//    @PUT
//    @Path("/{idResponse:[0-9][0-9]*}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response changeResponseById(@PathParam("idResponse") int idResponse, Response newResponse) {
//    	Response databaseResponse = repository.findById(newResponse.getIdResponse());
//    	
//    	if (databaseResponse == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//    	
//    	newResponse.setFirstName(newResponse.getFirstName());
//    	newResponse.setLastName(newResponse.getLastName());
//    	
////    	repository.updateResponse(newResponse);
//    	
//    	return newResponse;
//    }
    
    /**
     * Creates a new Response from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createResponse(ResponseModel response) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates response using bean validation
//            validateResponse(response);

            registration.register(response);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given response variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing response with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param ResponseModel response to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If response with the same email already exists
     */
    private void validateResponse(ResponseModel response) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<ResponseModel>> violations = validator.validate(response);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(response.getIdResponse())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a response with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the response class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(int email) {
        ResponseModel response = null;
        try {
            response = repository.findById(email);
        } catch (NoResultException e) {
            // ignore
        }
        return response != null;
    }
}
