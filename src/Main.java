import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

abstract class GrammerElement {
    public abstract boolean read(Tokenizer toks) throws InvalidGrammerException, IOException;

    protected void assertNextKindEquals(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        if(!expected.equals(actual)) {
            throw new InvalidGrammerException(
                String.format("expected kind is '%s', found '%s'", expected, actual), 
                toks.position());
        }
    }

    protected void assertNextEOF(Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        if(toks.kind() != Grammer.EOF) {
            throw new InvalidGrammerException("Expected EOF: " + toks.kind(), toks.position());
        }
    }
}

class Program extends GrammerElement {
    Body body;

    @Override
    public boolean read(Tokenizer toks) throws InvalidGrammerException, IOException {

        // step 1 read preamble
        {
            // starts with body
            assertNextKindEquals("program", toks);

            // valid identifier
            assertNextKindEquals(Grammer.IDENTIFIER_KIND, toks);

            // ends with ":"
            assertNextKindEquals(":", toks);
        }

        // step 2 read body
        this.body = new Body();
        body.read(toks);

        // step 3 read end
        assertNextKindEquals(Grammer.END_KEYWORD, toks);

        // step 4 verify at end
        assertNextEOF(toks);

        return true;
    }
}

class Body extends GrammerElement {

    @Override
    public boolean read(Tokenizer toks) throws InvalidGrammerException, IOException {

        return true;
    }
}

public class Main {
    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("Usage need to enter input filename");
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))/*(new FileReader(inputFileName)))*/){

            // create a Tokenizer object from our buffered reader
            Tokenizer tokenizer = new Tokenizer(reader);

            Program p = new Program();

            try {
                boolean result = p.read(tokenizer);

                if(result) {
                    System.out.println("valid file");
                }
            } catch (InvalidGrammerException e) {
                System.out.println("\t\t************** invalid file **************");
                System.out.println(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
