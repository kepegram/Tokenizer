package parser.grammer.expression.factors;

import java.io.IOException;

import parser.InvalidGrammerException;
import parser.RewindableTokenizer;
import parser.grammer.GrammerElement;

final class BooleanLiteralFactorValue {
    private final boolean value;
    public BooleanLiteralFactorValue(boolean value) {this.value = value;}
    public boolean isTrue() {return value;}
    public boolean isFalse() {return !value;}
}

public class Factor extends GrammerElement {
    public interface FactorValue {} 

    private FactorValue factorValue;
    private String optionalUnaryOp;
    private Factor child;
    private String childOp;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // TODO Auto-generated method stub
        return false;
    }

    public void addChild(Factor curFactor, String linkingMultiplicativeOperator) {
    }

    public boolean isValid() {
        return false;
    }

    public String getOptionalUnaryOp() {
        return optionalUnaryOp;
    }

    public Factor getChild() {
        return child;
    }

    public String getChildOp() {
        return childOp;
    }
}