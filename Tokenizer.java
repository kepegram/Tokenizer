import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;

class Position {
    public final int lineNumber;
    public final int linePosition;

    public Position(int lineNumber, int linePosition) {
        this.lineNumber = lineNumber;
        this.linePosition = linePosition;
    }
}

enum LexemeType {
    ID, NUM
}

public class Tokenizer {
    public static final String EOF = "is-end-of-file";
    public static final String ERROR = "is-invalid-character";
    private BufferedReader reader;
    private int lineNumber, linePosition;
    private String line;

    private boolean isEOF;
    private boolean isError;
    public String currentToken;
    private Position startPosition;
    private String kind;
    private Object value;

    public Tokenizer (BufferedReader reader) throws IOException{
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
        if(isError) {
            return;
        }

        if(isEOF) {
            this.kind = Tokenizer.EOF;
            return;
        }

        this.startPosition = null;
        this.currentToken = null;
        this.kind = null;
        this.value = null;

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
                startPosition = new Position(lineNumber + 1, linePosition + 1);
                if(isOperator()) {
                    // is operator-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isOperator());

                    this.kind = builder.toString();
                } else if(isDigit()) {
                    // is operator-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isDigit());

                    this.kind = "NUM";
                    this.value = Integer.parseInt(builder.toString());
                } else if(isIdentifier()){
                    // is identifier-like
                    do {
                        builder.append(peekNextChar());
                    } while (nextChar() && isIdentifier());

                    if(isKeyWord(builder.toString())) {
                        this.kind = builder.toString();
                    } else {
                        this.kind = "ID";
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
            if(this.isEOF) return Tokenizer.EOF;
            if(this.isError) return Tokenizer.ERROR;
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

    public char peekNextChar() {
        // if no line available - or we are at the end of the line
        if(line != null && linePosition < line.length()) {
            return line.charAt(linePosition);
        }

        return '\n';
    }

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

    private boolean isWhiteSpace() {
        return Character.isWhitespace(peekNextChar());
    }

    public static char[] SEPARATORS = {';', '(', ')'};

    private boolean isSeparator(char ch) {
        for(char sep: SEPARATORS) {
            if(sep == ch) return true;
        }
        return false;
    }
    private boolean isSeparator() {
        char ch = peekNextChar();
        return isSeparator(ch);
    }

    char operatorChars[] = {':', '=', '<', '>', '!', '*', '+', '-', '/'};
    // function to test for one of the following operators (and test next char is valid)
    private boolean isOperator(char ch) {
        for(char op: operatorChars) {
            if(op == ch) return true;
        }
        return false;
    }

    private boolean isOperator() {
        return isOperator(peekNextChar());
    }

    String[] keyWords = {"if", "fi", "true"};

    private boolean isKeyWord(String s) {
        for(String kw: keyWords) {
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

    private boolean isIdentifier(char ch) {
        return Character.isAlphabetic(ch) || Character.isDigit(ch) ||ch == '_';
    }

    private boolean isIdentifier() {
        return isIdentifier(peekNextChar());
    }
}