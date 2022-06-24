package parser.tokenizer;
import java.io.IOException;

public interface Tokenizer {
    
    void next() throws IOException;
    String kind();
    Object value();
    Position position();
    boolean isError();
    String tokenToString();
}