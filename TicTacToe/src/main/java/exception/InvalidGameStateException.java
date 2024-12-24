package exception;

public class InvalidGameStateException extends Exception {

    public InvalidGameStateException(String messege){
        super(messege);
    }
}
