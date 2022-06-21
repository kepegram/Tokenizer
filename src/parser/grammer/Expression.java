package parser.grammer;

import java.io.IOException;

import parser.InvalidGrammerException;
import parser.RewindableTokenizer;

public class Expression extends GrammerElement {

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // TODO Auto-generated method stub
        return false;
    }
    
    public boolean isValid() {
        return true;
    }
}
