package Bob;

public class SyntaxError extends Exception {
    private final String errorMessage = "You bum, I don't know what that means";

    public String getErrorMessage() {
        return errorMessage;
    }
}
