package ewm.errors;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
@Slf4j
public class ErrorControllerHandler {
    @ExceptionHandler
    public ResponseEntity<?> catchNotFoundException(final NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchValidationException(final ValidationException e) {
        String reason = "";

        log.error(e.getMessage());

        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchConflictError(final ConflictErrorException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleOtherException(final Throwable e) {
        String reason = "Something is bad";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());

        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(final IllegalArgumentException e) {
        String reason = "IllegalArgumentException";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIData(final InvalidDataAccessApiUsageException e) {
        String reason = "InvalidDataAccessApiUsageException";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleConstraintViolation(final ConstraintViolationException e) {
        String reason = "ConstraintViolationException";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDataIntegrity(final DataIntegrityViolationException e) {
        String reason = "DataIntegrityViolationException";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<?> handleValidException(final MethodArgumentNotValidException e) {
        String reason = "DataIntegrityViolationException";
        e.printStackTrace();
        Errors errors = Errors.makeRequest(null, reason, e.getMessage(), HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleConditionException(final MissingServletRequestParameterException e) {
        String reason = "MissingServletRequestParameterException";
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        log.error(reason + e.getMessage());
        Errors error = Errors.builder()
                .reason(reason)
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.toString())
                .timestamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(timestamp))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> validationHandler(final HttpMessageNotReadableException e) {
        String reason = "";
        String message = e.getMessage();
        log.error(reason + message);

        Errors error = Errors.makeRequest(null, reason, e.getMessage(),HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleEntityNotFound(final EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

