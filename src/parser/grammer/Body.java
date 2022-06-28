package parser.grammer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.tokenizer.RewindableTokenizer;

class Body extends GrammerElement {
    private final List<Declaration> declarations;
    private final List<Statement> statements;

    // collection of declarations and statements from grammer
    public Body() {
        this.declarations = new ArrayList<>();
        this.statements = new ArrayList<>();
    }

    // follows order of grammer
    @Override
    public boolean read(RewindableTokenizer toks) throws InvalidGrammerException, IOException {

        // read the declarations
        Declaration d = new Declaration();
        while(d.read(toks)) {
            declarations.add(d);
            d = new Declaration();
        }

        // read the statements
        Statement.StatementBuilder s = new Statement.StatementBuilder();
        while(s.read(toks)) {
            statements.add(s.getStatement());
            s = new Statement.StatementBuilder();
        }

        return true;
    }
}