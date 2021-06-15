package co.com.foodbank.user.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {



    /**
     * Method to handle user not found by dni, email andcuit.
     */
    @ExceptionHandler(value = ContributionNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            ContributionNotFoundException ex) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }



    /**
     * Method to handle constrains Exceptions.
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleExceptionConstrains(
            ConstraintViolationException ex) {

        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(),
                apiError.getStatus());

    }



    /**
     * Method to handle MethodArgumentNotValidException.
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<String>();

        for (ObjectError violation : ex.getFieldErrors()) {
            errors.add(violation.getCode());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                this.fieldError(ex.getAllErrors()), errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(),
                apiError.getStatus());
    }



    private String fieldError(List<ObjectError> list) {
        return list.stream().map(d -> d.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
    }



    /*
     * @ExceptionHandler(value = UserNotFoundException.class) public
     * ResponseEntity<Object> handleNotFoundException( UserNotFoundException ex)
     * {
     * 
     * /* Map<String, Object> body = new LinkedHashMap<>(); body.put("Message",
     * "Not found " + ex.getMessage());
     * 
     * return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
     * 
     * }
     * 
     * @ExceptionHandler(value = ConstraintViolationException.class) public
     * ResponseEntity<Object> handleExceptionConstrains(
     * ConstraintViolationException ex) {
     * 
     * /* Map<String, Object> body = new LinkedHashMap<>();
     * Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
     * 
     * for (ConstraintViolation<?> p : violations) { body.put("Message",
     * p.getMessage()); }
     * 
     * return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); }
     */



}
