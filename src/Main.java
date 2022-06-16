import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

abstract class GrammerElement {
    public abstract boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException;

    protected boolean testNextKindEquals(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        return expected.equals(actual);
    }

    protected void assertNextKindEquals(String expected, Tokenizer toks)  throws InvalidGrammerException, IOException{
        if(!testNextKindEquals(expected, toks)) {
        String actual = toks.kind();
            throw new InvalidGrammerException(
                String.format("expected kind is '%s', found '%s'", expected, actual), 
                toks.position());
        }
    }

    protected boolean testNextKindIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        toks.next();
        String actual = toks.kind();
        return Arrays.asList(expectedValues).contains(actual);
    }

    protected void assertNextKindIn(String[] expectedValues, Tokenizer toks)  throws InvalidGrammerException, IOException{
        if(!testNextKindIn(expectedValues, toks)) {
            String actual = toks.kind();
            throw new InvalidGrammerException(
                String.format("expected kind should be one of '%s', found '%s'",
                String.join("'', '", expectedValues), actual), 
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
    private String programName = null;
    private Body body;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // step 1 read preamble
        {
            // starts with body
            assertNextKindEquals("program", toks);
            this.programName = toks.value().toString();

            // valid identifier
            assertNextKindEquals(Grammer.IDENTIFIER_KIND, toks);

            // ends with ":"
            assertNextKindEquals(":", toks);
        }

        // step 2 read body
        this.body = new Body();
        body.read(toks);

        // step 3 read end
        //assertNextKindEquals(Grammer.END_KEYWORD, toks);

        // step 4 verify at end
        //assertNextEOF(toks);

        return true;
    }

    public String getProgramName() { return this.programName;}
}

class Body extends GrammerElement {
    private List<Declaration> declarations;
    private List<Statement> statements;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // read the declarations
        Declaration d = new Declaration();
        while(d.read(toks)) {
            declarations.add(d);
            d = new Declaration();
        }

        // read the statements
        Statement s = new Statement();
        while(s.read(toks)) {
            statements.add(s);
            s = new Statement();
        }

        return true;
    }
}

class Declaration extends GrammerElement {
    private String type = null;
    private String identifierName = null;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // test for a declaration - A. (starts with type)
        if(!testNextKindIn(Grammer.DECLARATION_TYPES, toks)) {
            toks.rewind();
            return false;
        }
        this.type = toks.kind();

        // test for a declaration - B. (contains an identifier)
        if(!testNextKindEquals(Grammer.IDENTIFIER_KIND, toks)) {
            toks.rewind();
            toks.rewind();
            return false;
        }
        this.identifierName = toks.value().toString();

        // test for a declaration - C. (ends with ";")
        if(!testNextKindEquals(Grammer.DECLARATION_TERM_KIND, toks)) {
            toks.rewind();
            toks.rewind();
            toks.rewind();
            return false;
        }

        return true;
    }

    public String getType() {
        return type;
    }

    public String getIdentifierName() {
        return identifierName;
    }
}

class Statement extends GrammerElement {

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // read the declarations

        // read the statements
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
            RewindableTokenizer tokenizer = new RewindableTokenizerIMPL(new TokenizerIMPL(reader));

            Program p = new Program();

            try {
                boolean result = p.read(tokenizer);

                if(result) {
                    System.out.println("valid file");

                    while(tokenizer.kind() != Grammer.EOF && !tokenizer.isError()) {
                        String msg = String.format("%d:%d:'%s' %s",
                            tokenizer.position().lineNumber, tokenizer.position().linePosition,
                            tokenizer.kind(), (tokenizer.value() != null)?tokenizer.value():"");
                        System.out.println(msg);
                        tokenizer.next();
                    }

                    if(tokenizer.isError()) {
                        String msg = String.format("%d:%d:>>>>> Illegal character '%c'",
                            tokenizer.position().lineNumber, tokenizer.position().linePosition,
                            tokenizer.position().charAt);
                        System.out.print(msg);
                    } else {
                        System.out.print("end-of-text");
                    }
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
