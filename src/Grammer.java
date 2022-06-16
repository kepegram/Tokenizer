public abstract class Grammer {
    public static final String EOF = "is-end-of-file";
    public static final String ERROR = "is-invalid-character";
    public static final String IDENTIFIER_KIND = "IDENTIFIER";
    public static final String END_KEYWORD = "end";

    public static final String[] DECLARATION_TYPES = {"int", "bool"};
    public static final String DECLARATION_TERM_KIND = ";";

    public static char[] SEPARATORS = {';', '(', ')'};
    public static char[] operatorChars = {':', '=', '<', '>', '!', '*', '+', '-', '/'};
    public static String[] keyWords = {"if", "fi", "true", "else", "bool", "int", "program", "end", "do", "od", "print", "or"};
    public static String[] operators = {":", ":=", "<", "=<", "=", "!=", ">=", ">", "+", "-", "*", "/"};
}
