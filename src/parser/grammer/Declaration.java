package parser.grammer;

import java.io.IOException;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;

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
