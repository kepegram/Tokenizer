package parser.grammer;
import java.io.IOException;
import parser.tokenizer.RewindableTokenizer;

// elements that form a declaration
class Declaration extends GrammerElement {
    private String type = null;
    private String identifierName = null;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidTokenizerException, IOException {

        // test for a declaration - A. (starts with type)
        // rewinds if token is not a declaration type to see if its a statement
        if(!testNextKindIn(Grammer.DECLARATION_TYPES, toks)) {
            toks.rewind();
            return false;
        }
        this.type = toks.kind();

        // test for a declaration - B. (contains an identifier)
        // rewinds twice due to reading identifier and declaration type
        if(!testNextKindEquals(Grammer.IDENTIFIER_KIND, toks)) {
            toks.rewind();
            toks.rewind();
            return false;
        }
        this.identifierName = toks.value().toString();

        // test for a declaration - C. (ends with ";")
        // reads identifier, declaration type and identifier kind and rewinds three times in not a term kind
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
