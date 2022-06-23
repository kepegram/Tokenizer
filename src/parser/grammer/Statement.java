package parser.grammer;

import java.io.IOException;
import java.util.List;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.Main;
import parser.Position;
import parser.RewindableTokenizer;
import parser.grammer.expression.Expression;

abstract class Statement extends GrammerElement{
    static class StatementBuilder extends GrammerElement {

        private Statement statement;
        private Program program = Main.program;

        @Override
        public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

            // make sure this is not the end 
            if(!testNextKindNotIn(Grammer.STATEMENT_TERMINATORS_KINDS, toks)) {
                toks.rewind();
                return false;
            }

            // handle the various statement types
            Statement stmnt = null;
            String kind = toks.kind();
            toks.rewind();
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
                throw new RuntimeException();
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

        assertNextKindEquals(Grammer.ASSIGNMENT_OPERATOR_KIND, toks);

        // a valid Expression must follow
        this.expression = new Expression();

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
    private final Expression predicateExpression;
    private final Body body;
    private Body elseBody;

    public ConditionalStatement() {
        this.predicateExpression = new Expression();
        this.body = new Body();
        this.elseBody = null;
    }

    private static final String[] XXX = {Grammer.ELSE_KIND, Grammer.FI_KIND};

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // starts with a valid if
        assertNextKindEquals(Grammer.IF_KIND, toks);

        Position startPredicatePos = toks.position();
        if(!this.predicateExpression.read(toks)) {
            throw new InvalidGrammerException("invalid expression", startPredicatePos);
        }

        // contains a then
        assertNextKindEquals(Grammer.THEN_KIND, toks);

        Position startBodyPos = toks.position();
        if(!this.body.read(toks)) {
            throw new InvalidGrammerException("invalid conditional body", startBodyPos);
        }

        assertNextKindIn(XXX, toks);

        if(toks.kind() == Grammer.ELSE_KIND) {
            this.elseBody = new Body();
            Position startElseBodyPos = toks.position();
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

        // contains a then
        assertNextKindEquals(Grammer.DO_KIND, toks);

        Position startBodyPos = toks.position();
        if(!this.body.read(toks)) {
            throw new InvalidGrammerException("invalid conditional body", startBodyPos);
        }

        assertNextKindEquals(Grammer.END_KIND, toks);

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
        // a valid Expression must follow
        this.expression = new Expression();
        this.expression.read(toks);
        return this.expression.isValid();
    }
    public Expression getExpression() {
        return expression;
    }
}