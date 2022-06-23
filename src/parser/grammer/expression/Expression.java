package parser.grammer.expression;

import java.io.IOException;
import java.util.Arrays;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;
import parser.grammer.GrammerElement;

public class Expression extends GrammerElement {
    private final SimpleExpression rootSimpleExpression;

    public Expression() {
        this.rootSimpleExpression = new SimpleExpression();
    }

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // An expression is a collection of SimpleExpressions linked by relational operators
        SimpleExpression simp = this.rootSimpleExpression;
        SimpleExpression parent = null;
        String linkingRelationalOperator = null;
        while(simp.read(toks)) {
            if(parent != null) {
                parent.addChild(simp, linkingRelationalOperator);
            }

            // check if this simple expression is followed by a relational operator
            if(!testNextKindIn(Grammer.RELATIONAL_OPERATORS, toks)) {
                toks.rewind();
                break;
            }
            linkingRelationalOperator = toks.kind();
            simp = new SimpleExpression();
        }
        return isValid();
    }
    
    public boolean isValid() {
        SimpleExpression cur = this.rootSimpleExpression;
        String opStr = null;
        while(cur != null) {
            if(!cur.isValid() || (opStr!=null && !Arrays.asList(Grammer.RELATIONAL_OPERATORS).contains(opStr))) {
                return false;
            }
            cur = cur.getChild();
            if(cur != null) opStr = cur.getChildOp();
        }
        return true;
    }

    public SimpleExpression getRootSimpleExpression() {

        return rootSimpleExpression;
    }
}
