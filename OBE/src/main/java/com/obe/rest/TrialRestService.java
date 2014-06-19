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

import com.obe.data.TrialDAO;
import com.obe.model.Trial;
import com.obe.service.TrialRegistration;

@Path("/trials")
@RequestScoped
public class TrialRestService {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private TrialDAO repository;

    @Inject
    TrialRegistration registration;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trial> listAllTrials() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{idTrial:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Trial lookupTrialById(@PathParam("idTrial") int idTrial) {
        Trial trial = repository.findById(idTrial);
        if (trial == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return trial;
    }
    
//    @PUT
//    @Path("/{idTrial:[0-9][0-9]*}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Trial changeTrialById(@PathParam("idTrial") int idTrial, Trial newTrial) {
//    	Trial databaseTrial = repository.findById(newTrial.getIdTrial());
//    	
//    	if (databaseTrial == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//    	
//    	newTrial.setFirstName(newTrial.getFirstName());
//    	newTrial.setLastName(newTrial.getLastName());
//    	
////    	repository.updateTrial(newTrial);
//    	
//    	return newTrial;
//    }
    
    /**
     * Creates a new Trial from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrial(Trial trial) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates trial using bean validation
//            validateTrial(trial);

            registration.register(trial);

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
     * Validates the given trial variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing trial with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param Trial trial to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If trial with the same email already exists
     */
    private void validateTrial(Trial trial) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Trial>> violations = validator.validate(trial);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(trial.getIdTrial())) {
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
     * Checks if a trial with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Trial class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(int email) {
        Trial trial = null;
        try {
            trial = repository.findById(email);
        } catch (NoResultException e) {
            // ignore
        }
        return trial != null;
    }
}
