package parser;

public class InvalidGrammerException extends Exception {
    private final Position pos;

    public InvalidGrammerException(Position pos) {
        this.pos = pos;
    }

    public InvalidGrammerException(String message, Position pos) {
        super(message);
        this.pos = pos;
    }

    public InvalidGrammerException(Throwable cause, Position pos) {
        super(cause);
        this.pos = pos;
    }

    public InvalidGrammerException(String message, Throwable cause, Position pos) {
        super(message, cause);
        this.pos = pos;
    }

    public InvalidGrammerException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, Position pos) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.pos = pos;
    }

    public Position getPos() {
        return new Position(pos);
    }

    @Override
    public String toString() {
        return String.format("InvalidGrammerException: {\n\tmsg: '%s',\n\tpos: '%s'\n}", this.getMessage(), this.pos.toString());
    }
}
