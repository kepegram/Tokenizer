package parser;
public abstract class Grammer {
    public static final String EOF = "is-end-of-file";
    public static final String ERROR = "is-invalid-character";

    public static final String IDENTIFIER_KIND = "IDENTIFIER";
    public static final String DECLARATION_TERM_KIND = ";";
    public static final String ASSIGNMENT_OPERATOR_KIND = ":=";
    public static final String PRINT_KIND = "print";
    public static final String IF_KIND = "if";
    public static final String THEN_KIND = "then";
    public static final String ELSE_KIND = "else";
    public static final String FI_KIND = "fi";
    public static final String WHILE_KIND = "while";
    public static final String DO_KIND = "do";
    public static final String OD_KIND = "od";
    public static final String END_KIND = "end";

    public static final String[] STATEMENT_TERMINATORS_KINDS = {
        Grammer.END_KIND, 
        Grammer.FI_KIND,
        Grammer.OD_KIND
    };

    public static final String[] RELATIONAL_OPERATORS = {"<", "=<", "=", "!=", ">=", ">"};
    public static final String[] ADDITIVE_OPERATORS = {"+", "-", "or"};
    public static final String[] MULTIPLICATIVE_OPERATORS = {"*", "/", "and"};
    public static final String[] UNARY_OPERATORS = {"-", "not"};

    public static final String[] DECLARATION_TYPES = {"int", "bool"};
    public static final String INTEGER_LITERAL_KIND = "INTEGER_LITERAL";
    public static final String BOOLEAN_LITERAL_KIND = "BOOLEAN_LITERAL";
    public static final String[] BOOLEAN_LITERAL_VALUES = {"true", "false"};

    public static char[] SEPARATORS = {';', '(', ')'};
    public static char[] operatorChars = {':', '=', '<', '>', '!', '*', '+', '-', '/'};
    public static String[] keyWords = {"if", "fi", "true", "else", "bool", "int", "program", "end", "do", "od", "print", "or"};
    public static String[] operators = {":", ":=", "<", "=<", "=", "!=", ">=", ">", "+", "-", "*", "/"};

    

    public static boolean isKeyWord(String s) {
        for(String kw: Grammer.keyWords) {
            if(kw.equals(s)) {
                return true;
            }
        }
        return false;
    } 

    public static boolean isValidIdentifierChar(char ch) {
        return Character.isAlphabetic(ch) || Character.isDigit(ch) ||ch == '_';
    }

    public static boolean isValidIdentifier(String s) {
        for(char ch: s.toCharArray()) {
            if(!isValidIdentifierChar(ch)) {
                return false;
            }
        }
        return true;
    }
}
