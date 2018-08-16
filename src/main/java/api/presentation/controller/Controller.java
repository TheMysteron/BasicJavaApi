package api.presentation.controller;

import api.business.Service;
import api.business.model.Response;
import api.common.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final Logger logger = LoggerFactory
            .getLogger(Controller.class);

    @Autowired
    Service service;

    // ID must contain up to 3 letters then numbers, using regex
    private static final String ID_REGEX = "^[a-zA-Z]{1,3}[0-9]+";

    /**
     * represents the format mask of the dates entered as query parameters
     */
    private static final String DATE_FORMAT_MASK = "dd-MM-yyyy";

    @RequestMapping(method = RequestMethod.GET, value = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_USER")
    public ResponseEntity<Response> getInfo(
            @RequestHeader(value = "CorrelationId", defaultValue = ("undefined")) String correlationId,
            @RequestParam(value = "id", required = true) String id) {

        logger.info("CorrelationId: {} Request received", correlationId);

        if (!id.matches(ID_REGEX)){
            throw new BadRequestException("The ID must contain up to 3 letters, then numbers");
        }

        // Where no tax year has been specified retrieve for the current tax year
        Response responseBody = service.getInfo(id);

        // Build and dispatch the HTTP response
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("CorrelationId", correlationId);
        ResponseEntity<Response> response = new ResponseEntity<Response>(responseBody, responseHeaders, HttpStatus.OK);

        logger.info("CorrelationId: {} Dispatching response", correlationId);
        logger.debug("CorrelationId: {} Response detail {}", response.toString());

        return response;
    }
}