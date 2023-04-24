package extra;

import java.util.*;

public class SemanticAnalyzer {

    private List<String> tokens;
    private int currentTokenIndex;
    private Set<String> variableIdentifiers;

    public SemanticAnalyzer(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.variableIdentifiers = new HashSet<>();
    }

    public void run() {
        try {
            parseProgram();
            System.out.println("No semantic errors found.");
        } catch (Exception e) {
            System.out.println("Semantic error: " + e.getMessage());
        }
    }

    private void parseProgram() throws Exception {
        // Check for a valid program header
        if (!match("program")) {
            throw new Exception("Expected 'program' keyword.");
        }
        String programIdentifier = matchIdentifier();
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

        // Check that all declared variables have been used
        for (String variableIdentifier : variableIdentifiers) {
            if (!tokens.contains(variableIdentifier)) {
                throw new Exception("Variable '" + variableIdentifier + "' declared but never used.");
            }
        }
    }

    private void parseVariableDeclaration() throws Exception {
        String variableIdentifier = matchIdentifier();
        while (match(",")) {
            variableIdentifier = matchIdentifier();
        }
        if (!match(":")) {
            throw new Exception("Expected colon after variable identifiers.");
        }
        if (!matchType()) {
            throw new Exception("Expected variable type.");
        }
        if (!match(";")) {
            throw new Exception("Expected semicolon after variable declaration.");
        }

        // Add declared variable identifiers to set for later use
        variableIdentifiers.add(variableIdentifier);
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
        String variableIdentifier = matchIdentifier();
        if (!variableIdentifiers.contains(variableIdentifier)) {
            throw new Exception("Variable '" + variableIdentifier + "' used before declaration.");
        }
        if (!match(":=")) {
            throw new Exception("Expected ':=' operator after variable identifier.");
        }
        parseExpression();
    }

    private void parseBooleanExpression() throws Exception {
        parseExpression();
        if (!match("<relational-operator>")){
        	throw new Exception("Expected relational operator in boolean expression.");
        }
        parseExpression();
        }

        private void parseExpression() throws Exception {
        parseTerm();
        while (match("+") || match("-")) {
        parseTerm();
        }
        }

        private void parseTerm() throws Exception {
        parseFactor();
        while (match("*") || match("/")) {
        parseFactor();
        }
        }

        private void parseFactor() throws Exception {
        if (match("<integer>")) {
        // Do nothing
        } else if (match("<identifier>")) {
        String variableIdentifier = tokens.get(currentTokenIndex - 1);
        if (!variableIdentifiers.contains(variableIdentifier)) {
        throw new Exception("Variable '" + variableIdentifier + "' used before declaration.");
        }
        } else if (match("(")) {
        parseExpression();
        if (!match(")")) {
        throw new Exception("Expected closing parenthesis.");
        }
        } else {
        throw new Exception("Expected integer, identifier, or opening parenthesis.");
        }
        }

        private boolean match(String expectedToken) {
        if (currentTokenIndex >= tokens.size()) {
        return false;
        }
        if (tokens.get(currentTokenIndex).equals(expectedToken)) {
        currentTokenIndex++;
        return true;
        }
        return false;
        }

        private String matchIdentifier() throws Exception {
        if (!match("<identifier>")) {
        throw new Exception("Expected identifier.");
        }
        return tokens.get(currentTokenIndex - 1);
        }

        private boolean matchType() {
        return match("integer") || match("boolean") || match("real");
        }
        
        public class Main {

        }
        }