package parser.grammer.expression.factors;
import java.io.IOException;
import parser.grammer.Grammer;
import parser.grammer.GrammerElement;
import parser.grammer.InvalidGrammerException;
import parser.grammer.expression.Expression;
import parser.grammer.expression.factors.Factor.FactorValue;
import parser.tokenizer.RewindableTokenizer;

final class BooleanLiteralFactor implements FactorValue{
    public final static String TYPE = "BooleanLiteral";
    private final Boolean value;
    public BooleanLiteralFactor(Object value) {
        if(value instanceof Boolean) {
            this.value = (Boolean) value;
        } else {
            throw new RuntimeException("Need Boolean for Boolean Literal");
        }
    }
    public Boolean getValue() {return value;}
    @Override
    public String getType() {
        return TYPE;
    }
    @Override
    public boolean isValid() {
        return value != null;
    }
}

final class IntegerLiteralFactor implements FactorValue{
    public final static String TYPE = "IntegerLiteral";
    private final Integer value;
    public IntegerLiteralFactor (Object value) {
        if(value instanceof Integer) {
            this.value = (Integer) value;
        } else {
            throw new RuntimeException("Need Integer for Boolean Literal");
        }
    }
    public Integer getValue() {return value;}
    @Override
    public String getType() {
        return TYPE;
    }
    @Override
    public boolean isValid() {
        return value != null;
    }
}

final class IdentifierFactor implements FactorValue{
    public final static String TYPE = "Identifier";
    private final String value;
    public IdentifierFactor(String value) {this.value = value;}
    public String getValue() {return value;}
    @Override
    public String getType() {
        return TYPE;
    }
    @Override
    public boolean isValid() {
        return value != null && Grammer.isValidIdentifier(value);
    }
}

final class ExpressionFactor extends GrammerElement implements FactorValue{
    public final static String TYPE = "ExpressionFactor";
    private final Expression value;
    public ExpressionFactor(Expression value) {this.value = value;}
    public Expression getValue() {return value;}
    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        return value.read(toks);
    }
    @Override
    public String getType() {
        return TYPE;
    }
    @Override
    public boolean isValid() {
        return value != null && value.isValid();
    }
}

public class Factor extends GrammerElement {
    public interface FactorValue {
        String getType();
        boolean isValid();
        Object getValue();
    } 

    private FactorValue factorValue;
    private String optionalUnaryOp;
    private Factor child;
    private String childOp;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // Determine the factor type

        if(testNextKindIn(Grammer.UNARY_OPERATORS, toks)) {
            this.optionalUnaryOp = toks.kind();
            toks.next();
        }

        switch(toks.kind()) {
            case Grammer.BOOLEAN_LITERAL_KIND:
                this.factorValue = new BooleanLiteralFactor(toks.value());
                break;
            case Grammer.INTEGER_LITERAL_KIND:
                this.factorValue = new IntegerLiteralFactor(toks.value());
                break;
            case Grammer.IDENTIFIER_KIND:
                this.factorValue = new IdentifierFactor(toks.value().toString());
                break;
            case "(":
                this.factorValue = new ExpressionFactor(new Expression());
                if(!((ExpressionFactor) this.factorValue).read(toks)) {
                    // error 
                    throw new RuntimeException("unknown");
                }
                assertNextKindEquals(")", toks);
                break;
            case Grammer.EOF:
                throw new InvalidGrammerException("Recieved EOF");
            default:
            throw new InvalidGrammerException("Unknown");
        }

        return true;
    }

    public void addChild(Factor factor, String op) {
        this.child = factor;
        this.childOp = op;
    }

    public boolean isValid() {
        return (
            (factorValue != null && factorValue.isValid()) 
            && (child == null || child.isValid())
        );
    }

    public FactorValue getFactorValue() {
        return factorValue;
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