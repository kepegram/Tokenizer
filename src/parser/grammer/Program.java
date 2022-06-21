package parser.grammer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;

public class Program extends GrammerElement {
    private String programName = null;
    private Body body;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // step 1 read preamble
        {
            // starts with body
            assertNextKindEquals("program", toks);

            // valid identifier
            assertNextKindEquals(Grammer.IDENTIFIER_KIND, toks);
            this.programName = toks.value().toString();

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