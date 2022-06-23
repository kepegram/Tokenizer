package parser.grammer.expression;

import java.io.IOException;
import java.util.Arrays;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;
import parser.grammer.GrammerElement;

class SimpleExpression extends GrammerElement {
    // internal
    private final Term rootTerm;

    // child
    private String childOp;
    private SimpleExpression child;

    public SimpleExpression() {
        this.rootTerm = new Term();
        this.childOp = null;
        this.child = null;
    }

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        Term curTerm = this.rootTerm;
        Term parent = null;
        String linkingAdditiveOperator = null;
        while(curTerm.read(toks)) {
            if(parent != null) {
                parent.addChild(curTerm, linkingAdditiveOperator);
            }

            // check if this simple expression is followed by a relational operator
            if(!testNextKindIn(Grammer.ADDITIVE_OPERATORS, toks)) {
                toks.rewind();
                break;
            }
            linkingAdditiveOperator = toks.kind();
            curTerm = new Term();
        }
        return isValid();
    }

    public void addChild(SimpleExpression child, String childOp) {
        // assert that this is a relational operator
        if(!Arrays.asList(Grammer.RELATIONAL_OPERATORS).contains(childOp)) {
            throw new RuntimeException("Not a relational operator - " + childOp);
        }
        this.childOp = childOp;
        this.child = child;
    }

    public Term getRootTerm() {
        return rootTerm;
    }

    public boolean isValid() {
        Term cur = this.rootTerm;
        String opStr = null;
        while(cur != null) {
            if(!cur.isValid() || (opStr!=null && !Arrays.asList(Grammer.ADDITIVE_OPERATORS).contains(opStr))) {
                return false;
            }
            cur = cur.getChild();
            if(cur != null) opStr = cur.getChildOp();
        }
        return true;
    }

    public String getChildOp() {
        return childOp;
    }

    public SimpleExpression getChild() {
        return child;
    }
}