package parser;
import java.io.BufferedReader;
import java.io.IOException;

enum LexemeType {
    ID, NUM
}

public class TokenizerIMPL implements Tokenizer {

    private BufferedReader reader;
    private int lineNumber, linePosition;
    private String line;

    private boolean isEOF;
    private boolean isError;
    public String currentToken;
    private Position startPosition;
    private String kind;
    private Object value;

    public TokenizerIMPL (BufferedReader reader) throws IOException{
        this.reader = reader;
        this.lineNumber = -1;
        this.linePosition = -1;
        this.line = null;
        this.currentToken = null;
        this.startPosition = null;
        this.isEOF = false;
        this.isError = false;
        this.kind = null;
        this.value = null;
        nextLine();
    }
    
    public void next() throws IOException {

        this.currentToken = null;
        this.kind = Grammer.ERROR;
        this.value = null;

        if(isError) {
            return;
        }

        this.startPosition = null;
        this.kind = Grammer.EOF;

        if(isEOF) {
            return;
        }

        // scan ahead for whitespace and/or comments
        while(!isEOF && (isWhiteSpace() || peekNextChar() == '\n')) {
            nextChar();
        }
        // deal with an EOF condition
        if(!isEOF) {
            // deal with comments first, move ahead to next line
            if (isStartComment()) {
                nextLine();
                // call next again
                next();
            } else {
                StringBuilder builder = new StringBuilder();
                startPosition = new Position(lineNumber + 1, linePosition + 1, peekNextChar());
                if(isOperatorChar()) {
                    // is operator-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isOperatorChar());

                    String token = builder.toString();
                    if(isValidOperator(token)) {
                        this.kind = builder.toString();
                    } else {
                        this.isError = true;
                    }
                } else if(isDigit()) {
                    // is operator-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isDigit());

                    this.kind = Grammer.INTEGER_LITERAL_KIND;
                    this.value = Integer.parseInt(builder.toString());
                } else if(isIdentifier()){
                    // is identifier-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isIdentifier());

                    if(isBooleanLiteral(builder.toString())){
                        this.kind = Grammer.BOOLEAN_LITERAL_KIND;
                        this.value = Boolean.parseBoolean(builder.toString());
                    } else if (Grammer.isKeyWord(builder.toString())) {
                        this.kind = builder.toString();
                    } else {
                        this.kind = Grammer.IDENTIFIER_KIND;
                        this.value = builder.toString();
                    }
                } else if(isSeparator()){
                    builder.append(peekNextChar());
                    this.kind = builder.toString();
                    nextChar();
                } else {
                    // error case
                    this.currentToken = null;
                    isError = true;
                }
                if(builder.length() > 0 && !isError) {
                    this.currentToken = builder.toString();
                }
            }
        }
    }

    public String kind() {
        if(this.currentToken == null) {
            if(this.isEOF) return Grammer.EOF;
            if(this.isError) return Grammer.ERROR;
            throw new RuntimeException("Not ready");
        }

        return kind;
    }

    public Object value() {
        if(this.currentToken == null) {
            throw new RuntimeException("Not ready");
        }

        return value;
    }

    public Position position() {
        if(this.startPosition == null) {
            throw new RuntimeException("Not ready");
        }

        return this.startPosition;
    }

    public boolean isError() {
        return isError;
    }

    private boolean isStartComment() {
        // if no line available - or we are at the end of the line
        if(line != null && linePosition < line.length() -1) {
            return line.charAt(linePosition) == '/' && line.charAt(linePosition + 1) == '/';
        }

        return false;
    }

    private void nextLine() throws IOException {
        if(!isEOF){
            line = reader.readLine();
            if(line != null) {
                lineNumber++;
                linePosition = 0;
            } else {
                isEOF = true;
            }
        }
    }

    private boolean nextChar() throws IOException{
        // if no line available - or we are at the end of the line
        linePosition++;
        if(line == null || linePosition >= line.length()) {
            nextLine();
            return false;
        } 

        return true;
    }

    private char peekNextChar() {
        // if no line available - or we are at the end of the line
        if(line != null && linePosition < line.length()) {
            return line.charAt(linePosition);
        }

        return '\n';
    }
    
    /*
    private boolean isValidIdentifierPartial(String token) throws IOException {
        // make sure there are more characters in the line
        if(line == null) {
            throw new RuntimeException("Not ready");
        }
        if(isEOF || isWhiteSpace() || !nextChar()) return false;

        char nextChar = peekNextChar();

        // rules for parsing, from the grammer
        if(isSeparator()) return false;

        // first complete symbols
        if(isSeparator(token.charAt(0))) return false;

        return true;
    }
    */

    private boolean isWhiteSpace() {
        return Character.isWhitespace(peekNextChar());
    }

    private boolean isSeparator(char ch) {
        for(char sep: Grammer.SEPARATORS) {
            if(sep == ch) return true;
        }
        return false;
    }
    private boolean isSeparator() {
        char ch = peekNextChar();
        return isSeparator(ch);
    }

    private boolean isValidOperator(String testOp) {
        for(String op: Grammer.operators) {
            if(op.equals(testOp)) {
                return true;
            }
        }
        return false;
    }

    // function to test for one of the following operators (and test next char is valid)
    private boolean isOperatorChar(char ch) {
        for(char op: Grammer.operatorChars) {
            if(op == ch) return true;
        }
        return false;
    }

    private boolean isOperatorChar() {
        return isOperatorChar(peekNextChar());
    }

    private boolean isBooleanLiteral(String s) {
        for(String kw: Grammer.BOOLEAN_LITERAL_VALUES) {
            if(kw.equals(s)) {
                return true;
            }
        }
        return false;
    } 

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isDigit() {
        return isDigit(peekNextChar());
    }

    private boolean isIdentifier() {
        return Grammer.isValidIdentifierChar(peekNextChar());
    }
}