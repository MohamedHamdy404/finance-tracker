package com.finance.tracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API
 * Returns consistent error format across all endpoints
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // =========================
    // 404
    // =========================

    /**
     * Handle 404 - Resource Not Found (domain-level)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());

        return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    /**
     * Handle 404 - No static resource found
     * (Spring Boot 3 throws NoResourceFoundException for missing static resources/paths)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex, WebRequest request) {
        log.debug("No resource found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Not Found", "No handler found for this path", request);
    }

    /**
     * Handle 404 - No handler found (if configured)
     * Note: this requires spring.mvc.throw-exception-if-no-handler-found=true
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, WebRequest request) {
        log.debug("No handler found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Not Found", "No handler found for this path", request);
    }

    // =========================
    // 409
    // =========================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request);
    }

    // =========================
    // 400
    // =========================

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(
            InvalidRequestException ex, WebRequest request) {
        log.warn("Invalid request: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException ex, WebRequest request) {
        log.warn("Missing required header: {}", ex.getHeaderName());
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Required header '" + ex.getHeaderName() + "' is missing",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String message = err.getDefaultMessage();
            if (err instanceof FieldError fe) {
                validationErrors.put(fe.getField(), message);
            } else {
                // Object-level error (rare but possible)
                validationErrors.put(err.getObjectName(), message);
            }
        });

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid input data")
                .path(extractPath(request))
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String expectedType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format(
                "Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), expectedType
        );

        log.warn("Type mismatch: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", message, request);
    }

    // =========================
    // 403
    // =========================

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        log.warn("Forbidden: {}", ex.getMessage());
        return buildError(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), request);
    }

    // =========================
    // Spring "problem detail" style exceptions
    // =========================

    /**
     * Handles Spring's ErrorResponseException (can be thrown by framework)
     * and maps it to our ErrorResponse shape.
     */
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleErrorResponseException(
            ErrorResponseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Spring 6 provides ProblemDetail via getBody()
        String message = "Request failed";
        if (ex.getBody() != null && ex.getBody().getDetail() != null) {
            message = ex.getBody().getDetail();
        }

        log.warn("Framework error: status={}, detail={}", status.value(), message);
        return buildError(status, status.getReasonPhrase(), message, request);
    }

    // =========================
    // 500
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        // Keep stack trace in logs, but do not leak details to client
        log.error("Unexpected error occurred", ex);

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please contact support.",
                request
        );
    }

    // =========================
    // Helpers
    // =========================

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String error,
            String message,
            WebRequest request
    ) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(extractPath(request))
                .build();

        return new ResponseEntity<>(body, status);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
