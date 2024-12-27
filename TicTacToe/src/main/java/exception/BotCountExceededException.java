package exception;

public class BotCountExceededException extends Exception {
    public BotCountExceededException(String message) {
        super(message);
    }
}
