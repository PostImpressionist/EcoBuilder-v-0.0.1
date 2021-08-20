package exceptions;

public class LogicOperationException extends Exception{
    public LogicOperationException(String message) {
        System.err.println(message);
    }
}
