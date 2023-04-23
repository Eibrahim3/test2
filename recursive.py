import re

class Token:
    def __init__(self, type, value):
        self.type = type
        self.value = value

class Parser:
    def __init__(self, tokens):
        self.tokens = tokens
        self.current_token = None
        self.index = -1

    @staticmethod
    def tokenize(code):
        token_specification = [
            ('FLOAT_LIT', r'\d+\.\d+'),
            ('INT_LIT', r'\d+'),
            ('ID', r'[a-zA-Z_]\w*'),
            ('ASSIGN', r'='),
            ('PLUS', r'\+'),
            ('MINUS', r'-'),
            ('MUL', r'\*'),
            ('DIV', r'/'),
            ('MOD', r'%'),
            ('LPAREN', r'\('),
            ('RPAREN', r'\)'),
            ('LBRACE', r'\{'),
            ('RBRACE', r'\}'),
            ('COMMA', r','),
            ('SEMICOLON', r';'),
            ('DOUBLE_EQUALS', r'=='),
            ('NOT_EQUALS', r'!='),
            ('GREATER_THAN_EQUALS', r'>='),
            ('LESS_THAN_EQUALS', r'<='),
            ('GREATER_THAN', r'>'),
            ('LESS_THAN', r'<'),
            ('AND', r'&&'),
            ('OR', r'\|\|'),
            ('IF', r'if'),
            ('ELSE', r'else'),
            ('WHILE', r'while'),
            ('DATATYPE', r'int|float|bool')
        ]
        tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in token_specification)
        tokens = []
        for mo in re.finditer(tok_regex, code):
            kind = mo.lastgroup
            value = mo.group()
            tokens.append(Token(kind, value))
        return tokens
    
    def parse(self):
        self.advance()
        result = self.stmt_list()
        if self.current_token is not None:
            raise ValueError("Unexpected token: " + self.current_token.value)
        return result

    def advance(self):
        self.index += 1
        if self.index < len(self.tokens):
            self.current_token = self.tokens[self.index]
        else:
            self.current_token = None

    def stmt_list(self):
        stmts = []
        while self.current_token is not None:
            stmt = self.stmt()
            stmts.append(stmt)
            if self.current_token is not None and self.current_token.type == "SEMICOLON":
                self.advance()
            else:
                break
        return stmts

    def stmt(self):
        if self.current_token.type == "IF":
            return self.if_stmt()
        elif self.current_token.type == "LBRACE":
            return self.block()
        elif self.current_token.type == "ID":
            return self.assign()
        elif self.current_token.type == "DATATYPE":
            return self.declare()
        elif self.current_token.type == "WHILE":
            return self.while_loop()
        else:
            raise ValueError("Invalid statement: " + self.current_token.value)

    def if_stmt(self):
        self.expect("IF")
        self.expect("LPAREN")
        bool_expr = self.bool_expr()
        self.expect("RPAREN")
        if_block = self.block()
        else_block = None
        if self.current_token is not None and self.current_token.type == "ELSE":
            self.advance()
            else_block = self.block()
        return ("IF", bool_expr, if_block, else_block)

    def block(self):
        self.expect("LBRACE")
        stmt_list = self.stmt
    
    def band(self):
      not_factor = False
    if self.current_token is not None and self.current_token.type == "NOT":
        self.advance()
        not_factor = True
    factor = self.factor()
    if not_factor:
          return ("UNARY_OP", Token("NOT", "not"), factor)
    while self.current_token is not None and self.current_token.type == "AND":
        op_token = self.current_token
        self.advance()
        factor2 = self.factor()
        factor = ("BINARY_OP", op_token, factor, factor2)
          return factor

def factor(self):
    if self.current_token.type == "LEFT_PAREN":
        self.advance()
        expr = self.expr()
        self.expect("RIGHT_PAREN")
        return expr
    elif self.current_token.type == "INT_LIT":
        token = self.current_token
        self.advance()
        return ("INT_LIT", token)
    elif self.current_token.type == "FLOAT_LIT":
        token = self.current_token
        self.advance()
        return ("FLOAT_LIT", token)
    elif self.current_token.type == "BOOL_LIT":
        token = self.current_token
        self.advance()
        return ("BOOL_LIT", token)
    elif self.current_token.type == "ID":
        id_token = self.current_token
        self.advance()
        if self.current_token is not None and self.current_token.type == "LEFT_PAREN":
            return self.function_call(id_token)
        else:
            return ("VAR", id_token)
    else:
        raise ValueError("Invalid expression: " + self.current_token.value)

def function_call(self, id_token):
    self.expect("LEFT_PAREN")
    args = []
    while self.current_token is not None and self.current_token.type != "RIGHT_PAREN":
        expr = self.expr()
        args.append(expr)
        if self.current_token is not None and self.current_token.type == "COMMA":
            self.advance()
    self.expect("RIGHT_PAREN")
    return ("FUNC_CALL", id_token, args)

def expr(self):
    term = self.term()
    while self.current_token is not None and self.current_token.type in ["PLUS", "MINUS"]:
        op_token = self.current_token
        self.advance()
        term2 = self.term()
        term = ("BINARY_OP", op_token, term, term2)
    return term

def term(self):
    factor = self.factor()
    while self.current_token is not None and self.current_token.type in ["MUL", "DIV", "MOD"]:
        op_token = self.current_token
        self.advance()
        factor2 = self.factor()
        factor = ("BINARY_OP", op_token, factor, factor2)
    return factor

def expect(self, token_type):
    if self.current_token is not None and self.current_token.type == token_type:
        self.advance()
    else:
        raise ValueError("Expected token type: " + token_type)