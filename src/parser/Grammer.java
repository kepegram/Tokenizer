package parser;
public abstract class Grammer {
    public static final String EOF = "is-end-of-file";
    public static final String ERROR = "is-invalid-character";

    public static final String STATEMENT_BREAK_KIND = ";";
    public static final String IDENTIFIER_KIND = "IDENTIFIER";
    public static final String ASSIGNMENT_OPERATOR_KIND = ":=";
    public static final String PRINT_KIND = "print";
    public static final String IF_KIND = "if";
    public static final String WHILE_KIND = "while";
    public static final String END_KIND = "end";

    public static final String[] STATEMENT_TERMINATORS_KINDS = {
        Grammer.END_KIND};

    public static final String[] DECLARATION_TYPES = {"int", "bool"};

    public static char[] SEPARATORS = {';', '(', ')'};
    public static char[] operatorChars = {':', '=', '<', '>', '!', '*', '+', '-', '/'};
    public static String[] keyWords = {"if", "fi", "true", "else", "bool", "int", "program", "end", "do", "od", "print", "or"};
    public static String[] operators = {":", ":=", "<", "=<", "=", "!=", ">=", ">", "+", "-", "*", "/"};
}
