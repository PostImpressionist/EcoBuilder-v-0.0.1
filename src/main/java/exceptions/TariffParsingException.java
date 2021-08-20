package exceptions;

public class TariffParsingException extends Exception{
    public TariffParsingException(String path, Exception e) {
        System.err.println("Error while parsing tariff excel file with path:\n" + path + "\n" +
                e.getMessage());
    }

}
