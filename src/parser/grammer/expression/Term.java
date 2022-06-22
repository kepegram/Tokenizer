package parser.grammer.expression;

import java.io.IOException;
import java.util.Arrays;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;
import parser.grammer.GrammerElement;
import parser.grammer.expression.factors.Factor;

public class Term extends GrammerElement {
    private final Factor rootFactor;
    private Term child;
    private String childOp;

    public Term() {
        this.rootFactor = new Factor();
        this.child = null;
        this.childOp = null;
    }

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        Factor curFactor = this.rootFactor;
        Factor parent = null;
        String linkingMultiplicativeOperator = null;
        while(curFactor.read(toks)) {
            if(parent != null) {
                parent.addChild(curFactor, linkingMultiplicativeOperator);
            }

            // check if this simple expression is followed by a relational operator
            if(!testNextKindIn(Grammer.MULTIPLICATIVE_OPERATORS, toks)) {
                toks.rewind();
                break;
            }
            linkingMultiplicativeOperator = toks.kind();
            curFactor = new Factor();
        }
        return isValid();
    }

    public void addChild(Term term, String op) {
        this.child = term;
        this.childOp = op;
    }

    public boolean isValid() {
        Factor cur = this.rootFactor;
        String opStr = null;
        while(cur != null) {
            if(!cur.isValid() || (opStr!=null && !Arrays.asList(Grammer.MULTIPLICATIVE_OPERATORS).contains(opStr))) {
                return false;
            }
            cur = cur.getChild();
            opStr = cur.getChildOp();
        }
        return true;
    }

    public Factor getRootFactor() {
        return rootFactor;
    }

    public Term getChild() {
        return child;
    }

    public String getChildOp() {
        return childOp;
    }
}