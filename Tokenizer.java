import java.io.BufferedReader;
import java.io.IOException;

import javax.management.RuntimeErrorException;

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
    private BufferedReader reader;
    private int lineNumber, linePosition;
    private String line;

    private boolean isEOF;
    public String currentToken;
    private Position startPosition;

    public Tokenizer (BufferedReader reader) throws IOException{
        this.reader = reader;
        this.lineNumber = -1;
        this.linePosition = -1;
        this.line = null;
        this.currentToken = null;
        this.startPosition = null;
        this.isEOF = false;
        nextLine();
    }
    
    public void next() throws IOException {
        // scan ahead for whitespace and/or comments
        while(!isEOF) {
            if (!isWhiteSpace() && peekNextChar() != '\n') {
                break;
            }
            nextChar();
        }
        // deal with an EOF condition
        if(isEOF) {
            this.currentToken = null;
            this.startPosition = null;
            return;
        } else {
            // deal with comments first, move ahead to next line
            if (isStartComment()) {
                nextLine();
                // call next again
                next();
            } else {

                if(isOperator()) {

                } if(isKeyWord()) {

                }else {
                    // is identifier
                    StringBuilder builder = new StringBuilder();
                    startPosition = new Position(lineNumber + 1, linePosition + 1);
                    do {
                        builder.append(peekNextChar());
                    } while (isValidIdentifierPartial(builder.toString()));

                    if(builder.length() > 0) {
                        this.currentToken = builder.toString();
                    }
                }
            }
        }
    }

    public String kind() {
        if(this.currentToken == null) {
            if(this.isEOF) return Tokenizer.EOF;
            throw new RuntimeException("Not ready");
        }

        return "unknown";
    }

    public Object value() {
        if(this.currentToken == null) {
            throw new RuntimeException("Not ready");
        }

        return "unknown";
    }

    public Position position() {
        if(this.currentToken == null) {
            throw new RuntimeException("Not ready");
        }

        return this.startPosition;
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

    public static char[] SEPARATORS = {';', '(', ')', '/', '+', '-', '*'};

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

    char special[] = {':', '=', '<', '>', '!'};
    // function to test for one of the following operators (and test next char is valid)
    private boolean isOperator() {
        /* 
            AssignmentOperator  =  ":="
            RelationalOperator  =  "<" | "=<" | "=" | "!=" | ">=" | ">"
            AdditiveOperator  =  "+" | "-" | "or"
            MultiplicativeOperator  =  "*" | "/" | "and"
            UnaryOperator  =  "-" | "not"
            BooleanLiteral  =  "false"  |  "true"
        */

        return false;
    }

    private boolean isKeyWord() {
        return false;
    }

    private boolean isDigit() {
        return false;
    }

    private boolean isIdentifier() {
        return false;
    }
}