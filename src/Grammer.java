public abstract class Grammer {
    public static final String EOF = "is-end-of-file";
    public static final String ERROR = "is-invalid-character";
    public static final String IDENTIFIER_KIND = "IDENTIFIER";
    public static final String END_KEYWORD = "end";

    public static char[] SEPARATORS = {';', '(', ')'};
    public static char operatorChars[] = {':', '=', '<', '>', '!', '*', '+', '-', '/'};
    public static String[] keyWords = {"if", "fi", "true", "else", "bool", "int", "program", "end", "do", "od"};
    public static String[] operators = {":", ":=", "<", "=<", "=", "!=", ">=", ">", "+", "-", "*", "/"};
}
