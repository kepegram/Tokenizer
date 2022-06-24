package parser.grammer;
import java.io.IOException;

import parser.tokenizer.RewindableTokenizer;

public class Program extends GrammerElement {
    private String programName = null;
    private Body body;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // step 1 read preamble
        {
            // starts with body
            assertNextKindEquals(Grammer.PROGRAM_KIND, toks);

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
        assertNextKindEquals(Grammer.END_KIND, toks);

        // step 4 verify at end
        assertNextEOF(toks);

        return true;
    }

    public String getProgramName() { return this.programName;}
}