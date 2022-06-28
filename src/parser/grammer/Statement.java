package parser.grammer;
import java.io.IOException;
import parser.grammer.expression.Expression;
import parser.tokenizer.Position;
import parser.tokenizer.RewindableTokenizer;

abstract class Statement extends GrammerElement{
    static class StatementBuilder extends GrammerElement {

        private Statement statement;

        @Override
        public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

            // first test if the next lexeme is a part of this statement 
            if(!testNextKindNotIn(Grammer.STATEMENT_TERMINATORS_KINDS, toks)) {
                toks.rewind();
                return false;
            }

            // detemine the statement type
            Statement stmnt = null;
            String kind = toks.kind();
            toks.rewind(); // so the specific statement can validate
            Position expressionStartingPosition = toks.position();
            switch(kind) {
                case Grammer.IDENTIFIER_KIND:
                    stmnt = new AssignmentStatement();
                    break;
                case Grammer.IF_KIND:
                    stmnt = new ConditionalStatement();
                    break;
                case Grammer.WHILE_KIND:
                    stmnt = new IterativeStatement();
                    break;
                case Grammer.PRINT_KIND:
                    stmnt = new PrintStatement();
                    break;
                default:
                    break;
            }

            if(stmnt == null || !stmnt.read(toks)) {
                throw new InvalidGrammerException("unknown statement type", expressionStartingPosition);
            } 
            
            this.statement = stmnt;

            // remove any final semicolon
            if(!testNextKindEquals(";", toks)) {
                toks.rewind();
            }

            return true;
        }

        public Statement getStatement() {
            return statement;
        }
    }

}

class AssignmentStatement extends Statement {
    private String identifier;
    private Expression expression;

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // starts with a valid identifier
        assertNextKindEquals(Grammer.IDENTIFIER_KIND, toks);
        this.identifier = toks.value().toString();

        // has a valid asignment operator
        assertNextKindEquals(Grammer.ASSIGNMENT_OPERATOR_KIND, toks);

        // a valid Expression must follow
        this.expression = new Expression();

        // try to read the expression
        this.expression.read(toks);

        return this.expression.isValid();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }
}

class ConditionalStatement extends Statement {
    private Position startPredicatePos;
    private Position startBodyPos;
    private Position startElseBodyPos;
    private final Expression predicateExpression;
    private final Body body;
    private Body elseBody;

    public ConditionalStatement() {
        this.predicateExpression = new Expression();
        this.body = new Body();
        this.elseBody = null;
    }

    private static final String[] GrammerString = {Grammer.ELSE_KIND, Grammer.FI_KIND};

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // starts with a valid if
        assertNextKindEquals(Grammer.IF_KIND, toks);

        // read the predicate Expression
        this.startPredicatePos = toks.position();
        if(!this.predicateExpression.read(toks)) {
            throw new InvalidGrammerException("invalid expression", startPredicatePos);
        }

        // contains a then
        assertNextKindEquals(Grammer.THEN_KIND, toks);

        // read the body
        this.startBodyPos = toks.position();
        if(!this.body.read(toks)) {
            throw new InvalidGrammerException("invalid conditional body", startBodyPos);
        }

        assertNextKindIn(GrammerString, toks);

        if(Grammer.ELSE_KIND.equals(toks.kind())) {
            this.elseBody = new Body();
            this.startElseBodyPos = toks.position();
            if(!this.elseBody.read(toks)) {
                throw new InvalidGrammerException("invalid conditional else body", startElseBodyPos);
            }
        } else {
            toks.rewind();
        }

        assertNextKindEquals(Grammer.FI_KIND, toks);

        return true;
    }

    public Expression getPredicateExpression() {
        return predicateExpression;
    }

    public Body getBody() {
        return body;
    }

    public Body getElseBody() {
        return elseBody;
    }
}

class IterativeStatement extends Statement {
    private final Expression predicateExpression;
    private final Body body;

    public IterativeStatement() {
        this.predicateExpression = new Expression();
        this.body = new Body();
    }

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // starts with a valid while
        assertNextKindEquals(Grammer.WHILE_KIND, toks);

        Position startPredicatePos = toks.position();
        if(!this.predicateExpression.read(toks)) {
            throw new InvalidGrammerException("invalid expression", startPredicatePos);
        }

        // contains a do
        assertNextKindEquals(Grammer.DO_KIND, toks);

        Position startBodyPos = toks.position();
        if(!this.body.read(toks)) {
            throw new InvalidGrammerException("invalid conditional body", startBodyPos);
        }

        assertNextKindEquals(Grammer.OD_KIND, toks);

        return true;
    }

    public Expression getPredicateExpression() {
        return predicateExpression;
    }

    public Body getBody() {
        return body;
    }
}

class PrintStatement extends Statement {
    private Expression expression;
    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // starts with a valid print
        assertNextKindEquals(Grammer.PRINT_KIND, toks);

        // a valid Expression must follow
        this.expression = new Expression();
        this.expression.read(toks);
        return this.expression.isValid();
    }
    public Expression getExpression() {
        return expression;
    }
}