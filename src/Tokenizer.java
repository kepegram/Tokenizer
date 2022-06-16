import java.io.IOException;

public interface Tokenizer {
    
    void next() throws IOException;
    String kind();
    public Object value();
    public Position position();
    public boolean isError();
}