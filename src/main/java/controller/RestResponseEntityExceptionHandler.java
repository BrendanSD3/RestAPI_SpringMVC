/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author brend
 */
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice

public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {
//
       @ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(NotFoundException e) {
		CustomErrorResponse error = new CustomErrorResponse("NOT_FOUND_ERROR", e.getMessage());
		//error.setTimestamp(LocalDateTime.now());
		error.setStatus((HttpStatus.NOT_FOUND.value()));
		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
	}	
    
  // 400
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String bodyOfResponse = "<h3>The following Exception was thrown:</h3> <br> " + ex ;
        // ex.getCause() instanceof JsonMappingException, JsonParseException // for additional information later on
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String bodyOfResponse = "Bad Request ";
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
//
    @Override
     protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
         //final String responsebody="Error " + status.toString();
         //return handleExceptionInternal(ex,responsebody,headers,status,request);
         Long len = headers.getContentLength();
         String error= status.name();
         if(len >= 1)
         {
             
        } else {
             error="Content length is 0, a Post Method requires a body ";
           }
         final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(),error);
         
         return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }

  
//
    // 500

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<CustomErrorResponse> handleInternal(final RuntimeException ex, final WebRequest request) {
        System.out.println("500 Error"+ ex);
       // final String bodyOfResponse = "500 error was thrown  <br>" + ex.getLocalizedMessage() ;
        
        //return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        CustomErrorResponse error = new CustomErrorResponse("ERROR", ex.getMessage());
		//error.setTimestamp(LocalDateTime.now());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    
}


