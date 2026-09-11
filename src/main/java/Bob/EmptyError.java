package Bob;

public class EmptyError extends Exception {
    private final String errorMessage = "BRUH!!! Todo what?.";

    public String getErrorMessage() {
        return errorMessage;
    }
}
