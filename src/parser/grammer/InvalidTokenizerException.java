package parser.grammer;
import parser.tokenizer.Position;

public class InvalidTokenizerException extends InvalidGrammerException {

    public InvalidTokenizerException(String message, Position pos) {
        super(message, pos);
    }

    @Override
    public String toString() {
        if(this.getPos() != null) {
            return String.format(
                "InvalidTokenizerException: {\n\tmsg: '%s',\n\tpos: '%s'\n\tcharAt: %c\n}", 
                this.getMessage(), 
                this.getPos(), this.getPos().charAt);
        }
        return String.format(
            "InvalidTokenizerException: {\n\tmsg: '%s',\n\tpos: unknown\n}", 
            this.getMessage());
    }
}
