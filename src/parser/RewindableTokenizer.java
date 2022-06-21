package parser;
public interface RewindableTokenizer extends Tokenizer {
    void rewind() throws IndexOutOfBoundsException;
}