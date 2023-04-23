# denotation of a block of statements
def block_denotation(stmt_list, state):
    # evaluate each statement in the list in order
    for stmt in stmt_list:
        state = stmt[0](stmt[1], state)
    return state

# denotation of an if statement
def if_stmt_denotation(if_stmt, state):
    (bool_expr, block1, block2) = if_stmt
    if bool_expr_denotation(bool_expr, state):
        state = block_denotation(block1, state)
    elif block2 is not None:
        state = block_denotation(block2, state)
    return state

# denotation of a while loop
def while_loop_denotation(while_loop, state):
    (bool_expr, block) = while_loop
    while bool_expr_denotation(bool_expr, state):
        state = block_denotation(block, state)
    return state

# denotation of a statement list
def stmt_list_denotation(stmt_list, state):
    # evaluate each statement in the list in order
    for stmt in stmt_list:
        state = stmt_denotation(stmt, state)
    return state

# denotation of a boolean expression
def bool_expr_denotation(bool_expr, state):
    if bool_expr[0] == 'BTERM':
        return bterm_denotation(bool_expr[1], state)
    else:
        return bool_expr_denotation(bool_expr[1][0], state) and bool_expr_denotation(bool_expr[1][1], state)

# denotation of a boolean term
def bterm_denotation(bterm, state):
    if bterm[0] == 'BAND':
        return band_denotation(bterm[1], state)
    else:
        return bterm_denotation(bterm[1][0], state) or bterm_denotation(bterm[1][1], state)

# denotation of a boolean and
def band_denotation(band, state):
    (bool_expr1, bool_expr2) = band
    return bool_expr_denotation(bool_expr1, state) and bool_expr_denotation(bool_expr2, state)

# denotation of a boolean or
def bor_denotation(bor, state):
    (bool_expr1, bool_expr2) = bor
    return bool_expr_denotation(bool_expr1, state) or bool_expr_denotation(bool_expr2, state)

# denotation of a declaration statement
def declare_denotation(declare_stmt, state):
    (datatype, varnames) = declare_stmt
    for varname in varnames:
        state[varname] = None
    return state

# denotation of an assignment statement
def assign_denotation(assign_stmt, state):
    (varname, expr) = assign_stmt
    state[varname] = expr_denotation(expr, state)
    return state

# denotation of an expression
def expr_denotation(expr, state):
    if expr[0] == 'TERM':
        return term_denotation(expr[1], state)
    else:
        (term, op, expr) = expr[1]
        if op == '+':
            return expr_denotation(('EXPR', (term, op, expr[1])), state) + term_denotation(term, state)
        elif op == '-':
            return expr_denotation(('EXPR', (term, op, expr[1])), state) - term_denotation(term, state)
        elif op == '*':
            return expr_denotation(('EXPR', (term, op, expr[1])), state) * term_denotation(term, state)
        elif op == '/':
            return expr_denotation(('EXPR', (term, op, expr[1])), state) / term_denotation(term, state)
        else:
         return expr_denotation(('EXPR', (term, op, expr[1])), state) % term_denotation(term, state)
#denotation of a term
def term_denotation(term, state):
      if term[0] == 'FACT':
        return fact_denotation(term[1], state)
      else:
       (fact, op, term) = term[1]
      if op == '*':
       return term_denotation(('TERM', (fact, op, term[1])), state) * fact_denotation(fact, state)
      elif op == '/':
       return term_denotation(('TERM', (fact, op, term[1])), state) / fact_denotation(fact, state)
      else:
       return term_denotation(('TERM', (fact, op, term[1])), state) % fact_denotation(fact, state)

#denotation of a factor
def fact_denotation(fact, state):
    if fact[0] == 'ID':
     return state[fact[1]]
    elif fact[0] == 'INT_LIT':
     return int(fact[1])
    elif fact[0] == 'FLOAT_LIT':
     return float(fact[1])
    else:
     return expr_denotation(fact[1], state)