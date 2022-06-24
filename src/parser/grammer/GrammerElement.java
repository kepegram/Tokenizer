package parser.grammer;

import java.io.IOException;
import java.util.Arrays;

import parser.tokenizer.RewindableTokenizer;
import parser.tokenizer.Tokenizer;

public abstract class GrammerElement {
    public abstract boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException;

    protected static boolean testNextKindEquals(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        return expected.equals(actual);
    }

    protected static boolean testNextKindNot(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        return !expected.equals(actual);
    }

    protected static boolean testNextKindNotIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        return !testNextKindIn(expectedValues, toks);
    }

    protected static void assertNextKindNotIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        if(!testNextKindNotIn(expectedValues, toks)) {
        String actual = toks.kind();
            throw new InvalidGrammerException(
                String.format("expected kind should be one of '%s', found '%s'",
                String.join("'', '", expectedValues), actual), 
                toks.position());
        }
    }

    protected static void assertNextKindEquals(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        if(!testNextKindEquals(expected, toks)) {
        String actual = toks.kind();
            throw new InvalidGrammerException(
                String.format("expected kind is '%s', found '%s'", expected, actual), 
                toks.position());
        }
    }

    protected static boolean testNextKindIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        return Arrays.asList(expectedValues).contains(actual);
    }

    protected static void assertNextKindIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        if(!testNextKindIn(expectedValues, toks)) {
            String actual = toks.kind();
            throw new InvalidGrammerException(
                String.format("expected kind should be one of '%s', found '%s'",
                String.join("'', '", expectedValues), actual), 
                toks.position());
        }
    }

    protected static void assertNextEOF(Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        if(toks.kind() != Grammer.EOF) {
            throw new InvalidGrammerException("Expected EOF: " + toks.kind(), toks.position());
        }
    }
}