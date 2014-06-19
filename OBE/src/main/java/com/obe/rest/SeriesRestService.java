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

import com.obe.data.SeriesDAO;
import com.obe.model.Series;
import com.obe.service.SeriesRegistration;

@Path("/Series")
@RequestScoped
public class SeriesRestService {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private SeriesDAO repository;

    @Inject
    SeriesRegistration registration;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Series> listAllSeriess() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{idSeries:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Series lookupSeriesById(@PathParam("idSeries") int idSeries) {
        Series series = repository.findById(idSeries);
        if (series == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return series;
    }
    
//    @PUT
//    @Path("/{idSeries:[0-9][0-9]*}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Series changeSeriesById(@PathParam("idSeries") int idSeries, Series newSeries) {
//    	Series databaseSeries = repository.findById(newSeries.getIdSeries());
//    	
//    	if (databaseSeries == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//    	
//    	newSeries.setFirstName(newSeries.getFirstName());
//    	newSeries.setLastName(newSeries.getLastName());
//    	
////    	repository.updateSeries(newSeries);
//    	
//    	return newSeries;
//    }
    
    /**
     * Creates a new Series from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSeries(Series series) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates series using bean validation
//            validateSeries(series);

            registration.register(series);

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
     * Validates the given series variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing series with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param Series series to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If series with the same email already exists
     */
    private void validateSeries(Series series) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Series>> violations = validator.validate(series);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(series.getIdSeries())) {
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
     * Checks if a series with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Series class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(int email) {
        Series series = null;
        try {
            series = repository.findById(email);
        } catch (NoResultException e) {
            // ignore
        }
        return series != null;
    }
}
