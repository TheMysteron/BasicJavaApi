package api.presentation;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Component
public class LoggingResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingResponseEntityExceptionHandler.class);
	
	private String defaultCorrelationID="undefined";

	private final List<MediaType> supportedMediaTypes = Arrays.asList(MediaType.APPLICATION_JSON);

	@Autowired
	private ErrorAttributes errorAttributes;
	
	/**
	 * Catch all exception types here. This is the initial route into the
	 * exception handler. Takes account of {@link ResponseStatus} exception
	 * types. Respects JSON/XML content negotiation with the client by setting
	 * an appropriate Content-Type on the HTTP response.
	 * 
	 * The application itself should ensure that the exception is thrown with an
	 * appropriate message i.e. JSON or XML depending on what type of request it
	 * is servicing.
	 */
	@ExceptionHandler
	protected ResponseEntity<Object> handle(Exception ex, WebRequest request, HttpServletRequest servletRequest) {

		String correlationId = StringUtils.hasText(request.getHeader("CorrelationId")) ? request.getHeader("CorrelationId") : defaultCorrelationID;

		// Handle any REST @ResponseStatus annotated exceptions
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			HttpHeaders headers = setContentType(new HttpHeaders(), request);
			logger.debug("CorrelationId: {} Generating REST response for exception: [{}]", correlationId, ex.getClass().getSimpleName());
			headers.add("CorrelationId", correlationId);
			Object body = getErrorResponseBody(responseStatus.value(), ex, servletRequest, correlationId);
			if (responseStatus.value().is5xxServerError()) {
				logger.error("CorrelationId: {} Unexpected exception: ", correlationId, ex);
			}
			if (responseStatus.value().is4xxClientError()) {
				logger.warn("CorrelationId: {} Client exception: {}", correlationId, ex.getMessage());
			}
			return new ResponseEntity<Object>(body, headers, responseStatus.value());
		} else {
			return super.handleException(ex, request);
		}
	}
	
	/**
	 * Override to log the error and set the response body and correlation ID header for internal server errors.
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {		

		String correlationId = StringUtils.hasText(request.getHeader("CorrelationId")) ? request.getHeader("CorrelationId") : defaultCorrelationID;
		if (status.is5xxServerError()) {
			logger.error("CorrelationId: {} Unexpected exception: ", correlationId, ex);
		}
		if (status.is4xxClientError()) {
			logger.warn("CorrelationId: {} Client exception: {}", correlationId, ex.getMessage());
		}
		
		setContentType(headers, request);
		headers.add("CorrelationId", correlationId);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		body = getErrorResponseBody(status, ex, servletRequest, correlationId);
		return new ResponseEntity<Object>(body, headers, status);
	}
	
	/**
	 * Constructs a response body similar to that produced by Spring's default error controller.
	 * Provides some consistency between any error view returned by Spring and those returned here ourselves.
	 * The main difference here is that we extend the response to include a correlationId.
	 * @see org.springframework.boot.autoconfigure.web.DefaultErrorAttributes
	 * @see org.springframework.boot.autoconfigure.web.BasicErrorController
	 */
	private Object getErrorResponseBody(HttpStatus status, Exception ex, HttpServletRequest request, String correlationId) {
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("correlationId", correlationId);
		errorAttributes.put("timestamp", new Date());
		errorAttributes.put("status", status.value());
		errorAttributes.put("error", status.getReasonPhrase());
		ex = unwrapException(ex, true);
		errorAttributes.put("exception", ex.getClass().getName());
		errorAttributes.put("message", ex.getMessage());
		errorAttributes.put("path", request.getContextPath() + request.getServletPath());
		return errorAttributes;
	}
	
	/** 
	 * Helper to unwrap an Exception to its underlying cause.
	 */
	private Exception unwrapException(Exception ex, boolean fully) {
		while (ex.getCause() != null && ex.getCause() instanceof Exception) {
			ex = (Exception)(ex.getCause());
			if (!fully) {
				break; // Stop at the first level
			}
		}
		return ex;
	}
	
	/**
	 * Set content type on response based on accept header in request.
	 */
	private HttpHeaders setContentType(HttpHeaders headers, WebRequest request) {
		String accept = request.getHeader("Accept");
		try {
			List<MediaType> mediaTypes = MediaType.parseMediaTypes(accept);
			for (MediaType mt : mediaTypes) {
				if (supportedMediaTypes.contains(mt)) {
					headers.setContentType(mt);
				}						
			}
		} catch (Exception e) {
			// Well, we tried
		}
		return headers;
	}
}