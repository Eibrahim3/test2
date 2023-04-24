package extra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexicalAnalyzer {

    private static final List<String> KEYWORDS = Arrays.asList("var", "if", "else", "while", "print", "true", "false");
    private static final List<Character> OPERATORS = Arrays.asList('=', '+', '-', '*', '/', '<', '>', '!');
    private static final List<Character> PUNCTUATION = Arrays.asList('(', ')', '{', '}', ';');
    private static final List<Character> WHITESPACE = Arrays.asList(' ', '\t', '\n', '\r');
    
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isLetter(c) || c == '_') { // Identifier or keyword
                StringBuilder sb = new StringBuilder();
                while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                    sb.append(input.charAt(i));
                    i++;
                }
                String lexeme = sb.toString();
                if (KEYWORDS.contains(lexeme)) {
                    tokens.add(new Token(TokenType.KEYWORD, lexeme));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, lexeme));
                }
            } else if (Character.isDigit(c)) { // Integer or real literal
                StringBuilder sb = new StringBuilder();
                while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    sb.append(input.charAt(i));
                    i++;
                }
                String lexeme = sb.toString();
                if (lexeme.contains(".")) {
                    tokens.add(new Token(TokenType.REAL_LITERAL, Double.parseDouble(lexeme)));
                } else {
                    tokens.add(new Token(TokenType.INTEGER_LITERAL, Integer.parseInt(lexeme)));
                }
            } else if (c == '"') { // String literal
                StringBuilder sb = new StringBuilder();
                i++; // Skip the opening quote
                while (i < input.length() && input.charAt(i) != '"') {
                    sb.append(input.charAt(i));
                    i++;
                }
                i++; // Skip the closing quote
                String lexeme = sb.toString();
                tokens.add(new Token(TokenType.STRING_LITERAL, lexeme));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') { // Arithmetic operator
                tokens.add(new Token(TokenType.ARITHMETIC_OPERATOR, Character.toString(c)));
                i++;
            } else if (c == '<' || c == '>' || c == '=' || c == '!') { // Comparison or logical operator
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                i++;
                if (i < input.length() && input.charAt(i) == '=') {
                    sb.append(input.charAt(i));
                    i++;
                }
                String lexeme = sb.toString();
                tokens.add(new Token(TokenType.COMPARISON_OPERATOR, lexeme));
            } else if (PUNCTUATION.contains(c)) { // Punctuation
                tokens.add(new Token(TokenType.PUNCTUATION, Character.toString(c)));
                i++;
            } else if (WHITESPACE.contains(c)) { // Whitespace
                i++;
            } else {
                throw new RuntimeException("Invalid character: " + c);
            }
        }
        return tokens;
    }
    
    public static void main(String[] args) {
        String input = "var age = 25;\n double pi = 3.14;\n" +
                "var name = \"John Doe\";\n" +
                "print(\"Hello, \" + name + \"!\");\n" +
                "if (age < 18) {\n" +
                "    print(\"You're not old enough!\");\n" +
                "} else {\n" +
                "    print(\"Welcome!\");\n" +
                "}";
 List<Token> tokens = tokenize(input);
 for (Token token : tokens) {
     System.out.println(token);
 }
}
}

enum TokenType {
KEYWORD,
IDENTIFIER,
ARITHMETIC_OPERATOR,
COMPARISON_OPERATOR,
INTEGER_LITERAL,
REAL_LITERAL,
STRING_LITERAL,
BOOLEAN_LITERAL,
PUNCTUATION
}

class Token {
private TokenType type;
private Object value;
public Token(TokenType type, Object value) {
    this.type = type;
    this.value = value;
}

public TokenType getType() {
    return type;
}

public Object getValue() {
    return value;
}

@Override
public String toString() {
    return "Token{" +
            "type=" + type +
            ", value=" + value +
            '}';
}
}
