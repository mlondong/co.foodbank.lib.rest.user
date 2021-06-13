package co.com.foodbank.user.exception;

public class ContributionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContributionNotFoundException(String email) {
        super(email);
    }
}
