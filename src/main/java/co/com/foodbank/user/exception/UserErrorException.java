package co.com.foodbank.user.exception;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.exception 27/06/2021
 */
public class UserErrorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserErrorException(String err) {
        super(err);
    }
}
