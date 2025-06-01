package software.pxel.banksystem.api.exception;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String NOT_FOUND = "Entity not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already in use";
    public static final String PHONE_ALREADY_EXISTS = "Phone is already in use";
    public static final String USER_MUST_HAVE_AT_LEAST_ONE_EMAIL = "At least one email is required";
    public static final String USER_MUST_HAVE_AT_LEAST_ONE_PHONE = "At least one phone is required";
    public static final String USER_IS_NOT_AUTHENTICATED = "Unauthenticated user";
    public static final String USER_CAN_ONLY_MODIFY_OWN_DATA = "User can only modify their own data";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String TRANSFER_TO_YOURSELF = "Cannot transfer to yourself";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String INVALID_OR_EXPIRED_JWT_TOKEN = "Invalid or expired JWT token";

}
