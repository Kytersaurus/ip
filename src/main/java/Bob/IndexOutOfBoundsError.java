package Bob;

public class IndexOutOfBoundsError extends Exception {

    private static final String errorMessage = "No such item exists at the specified index";

    public IndexOutOfBoundsError() {
        super(errorMessage);
    }

}
