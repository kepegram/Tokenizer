package parser.tokenizer;

public interface RewindableTokenizer extends Tokenizer {
    void rewind() throws IndexOutOfBoundsException;
}