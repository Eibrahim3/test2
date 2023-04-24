package extra;

import java.util.*;

public class SyntaxAnalyzer {
    
    private List<String> tokens;
    private int currentTokenIndex;
    
    public SyntaxAnalyzer(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }
    
    public void run() {
        try {
            parseProgram();
            System.out.println("No syntax errors found.");
        } catch (Exception e) {
            System.out.println("Syntax error: " + e.getMessage());
        }
    }
    
    private void parseProgram() throws Exception {
        // Check for a valid program header
        if (!match("program")) {
            throw new Exception("Expected 'program' keyword.");
        }
        if (!match("<identifier>")) {
            throw new Exception("Expected program identifier.");
        }
        if (!match(";")) {
            throw new Exception("Expected semicolon after program identifier.");
        }
        
        // Check for one or more variable declarations
        while (match("var")) {
            parseVariableDeclaration();
        }
        
        // Check for a valid begin-end block
        if (!match("begin")) {
            throw new Exception("Expected 'begin' keyword.");
        }
        parseStatement();
        while (match(";")) {
            parseStatement();
        }
        if (!match("end")) {
            throw new Exception("Expected 'end' keyword.");
        }
        if (!match(".")) {
            throw new Exception("Expected period after 'end' keyword.");
        }
    }
    
    private void parseVariableDeclaration() throws Exception {
        if (!match("<identifier>")) {
            throw new Exception("Expected variable identifier.");
        }
        while (match(",")) {
            if (!match("<identifier>")) {
                throw new Exception("Expected variable identifier after comma.");
            }
        }
        if (!match(":")) {
            throw new Exception("Expected colon after variable identifiers.");
        }
        if (!match("<type>")) {
            throw new Exception("Expected variable type.");
        }
        if (!match(";")) {
            throw new Exception("Expected semicolon after variable declaration.");
        }
    }
    
    private void parseStatement() throws Exception {
        if (match("if")) {
            parseIfStatement();
        } else if (match("while")) {
            parseWhileStatement();
        } else if (match("<identifier>")) {
            parseAssignmentStatement();
        } else {
            throw new Exception("Expected statement.");
        }
    }
    
    private void parseIfStatement() throws Exception {
        parseBooleanExpression();
        if (!match("then")) {
            throw new Exception("Expected 'then' keyword after boolean expression.");
        }
        parseStatement();
        if (match("else")) {
            parseStatement();
        }
    }
    
    private void parseWhileStatement() throws Exception {
        parseBooleanExpression();
        if (!match("do")) {
            throw new Exception("Expected 'do' keyword after boolean expression.");
        }
        parseStatement();
    }
    
    private void parseAssignmentStatement() throws Exception {
        if (!match(":=")) {
            throw new Exception("Expected ':=' operator after variable identifier.");
        }
        parseExpression();
    }
    
    private void parseBooleanExpression() throws Exception {
        parseExpression();
        if (!match("<relational-operator>")) {
            throw new Exception("Expected relational operator.");
        }
        parseExpression();
    }
    
    private void parseExpression() throws Exception {
        parseTerm();
        while (match("<additive-operator>")) {
            parseTerm();
        }
    }
    
    private void parseTerm() throws Exception {
        	parsePrimary();
        	while (match("<multiplicative-operator>")) {
        	parsePrimary();
        	}
        	}
        private void parsePrimary() throws Exception {
            if (match("<identifier>")) {
                if (match("(")) {
                    parseExpression();
                    while (match(",")) {
                        parseExpression();
                    }
                    if (!match(")")) {
                        throw new Exception("Expected closing parenthesis.");
                    }
                }
            } else if (match("<integer>")) {
                // do nothing
            } else if (match("(")) {
                parseExpression();
                if (!match(")")) {
                    throw new Exception("Expected closing parenthesis.");
                }
            } else {
                throw new Exception("Expected primary expression.");
            }
        }

        private boolean match(String token) {
            if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(token)) {
                currentTokenIndex++;
                return true;
            } else {
                return false;
            }
        }

        public static void main(String[] args) {
            // Example input: ["program", "test", ";", "var", "x", ",", "y", ":", "integer", ";", "begin", "x", ":=", "1", ";", "if", "x", "<", "y", "then", "y", ":=", "2", "else", "y", ":=", "3", ";", "end", "."]
            List<String> tokens = Arrays.asList(args);
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(tokens);
            syntaxAnalyzer.run();
        }
    }