package parser.tokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import parser.grammer.Grammer;

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

    // initializing
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
    
    // moves to the next token in the file. if a valid token, inputs to a string builder which is then given to the terminal
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

    // return the kind of token read
    public String kind() {
        if(this.currentToken == null) {
            if(this.isEOF) return Grammer.EOF;
            if(this.isError) return Grammer.ERROR;
            throw new RuntimeException("Not ready");
        }

        return kind;
    }

    // returns the value of the token read whether that be a ID or NUM
    public Object value() {
        if(this.currentToken == null) {
            throw new RuntimeException("Not ready");
        }

        return value;
    }

    // returns position of the lexeme/token read
    public Position position() {
        if(this.startPosition == null) {
            throw new RuntimeException("Not ready");
        }

        return this.startPosition;
    }

    // in the case of an error tokenizing the document
    public boolean isError() {
        return isError;
    }

    // toString function used for console output. uses decimal and string formats to construct console output
    public String tokenToString() {
        return String.format("%d:%d:'%s' %s",
        position().lineNumber, position().linePosition,
        kind(), (value() != null)?value():"");
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

    // if no line available - or we are at the end of the line
    private boolean nextChar() throws IOException{
        linePosition++;
        if(line == null || linePosition >= line.length()) {
            nextLine();
            return false;
        } 

        return true;
    }

    // if no line available - or we are at the end of the line
    private char peekNextChar() {
        
        if(line != null && linePosition < line.length()) {
            return line.charAt(linePosition);
        }

        return '\n';
    }

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
        for(String op: Grammer.OPERATORS) {
            if(op.equals(testOp)) {
                return true;
            }
        }
        return false;
    }

    // function to test for one of the following operators (and test next char is valid)
    private boolean isOperatorChar(char ch) {
        for(char op: Grammer.OPERATOR_CHARS) {
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