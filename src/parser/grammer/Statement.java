package parser.grammer;

import java.io.IOException;

import parser.Grammer;
import parser.InvalidGrammerException;
import parser.RewindableTokenizer;

abstract class Statement extends GrammerElement{
    static class StatementBuilder extends GrammerElement {

        private Statement statement;

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
            }

            if(!stmnt.read(toks)) {
                throw new RuntimeException();
            } 
            
            this.statement = stmnt;
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

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // TODO Auto-generated method stub
        return false;
    }
}

class IterativeStatement extends Statement {

    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {
        // TODO Auto-generated method stub
        return false;
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