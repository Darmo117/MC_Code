grammar MCCode;

WS     : [ \n\r\t]+ -> skip;
COMMENT: '#'.+?[\n\r] -> skip;

LPAREN: '(';
RPAREN: ')';
LBRACK: '[';
RBRACK: ']';
LCURL : '{';
RCURL : '}';
COMMA : ',';
COLON : ':';
ASSIGN: ':=';
SEMIC : ';';
DOT   : '.';

PLUS  : '+';
MINUS : '-';
MUL   : '*';
DIV   : '/';
INTDIV: '//';
MOD   : '%';
POWER : '^';

PLUSA : '+=';
MINUSA: '-=';
MULA  : '*=';
DIVA  : '/=';
INTDIVA: '//=';
MODA  : '%=';
POWERA: '^=';

EQUAL : '=';
NEQUAL: '!=';
GT    : '>';
GE    : '>=';
LT    : '<';
LE    : '<=';
IN    : 'in';

NOT   : 'not';
AND   : 'and';
OR    : 'or';

NULL  : 'null';
TRUE  : 'true';
FALSE : 'false';
INT   : [0-9]+;
FLOAT : [0-9]+'.'?[0-9]*;
STRING: '"'([^"\n\r]|'\\"')+'"';
IDENT : [a-zA-Z_][a-zA-Z0-9_]*;

IMPORT : 'import';
AS     : 'as';
SCHED  : 'schedule';
VAR    : 'var';
CONST  : 'const';
EDITABLE: 'editable';
PUBLIC : 'public';
FUNC   : 'function';
RETURN : 'return';
IF     : 'if';
THEN   : 'then';
ELSE   : 'else';
ELIF   : 'elseif';
WHILE  : 'while';
FOR    : 'for';
DO     : 'do';
END    : 'end';
DELETE : 'del';
BREAK  : 'break';
CONTINUE: 'continue';
WAIT   : 'wait';

// TODO authorize function declaration in global scope only (no nested functions, no anonymous functions)
module:
  SCHED ticks=INT SEMIC // TODO no statement, set property in interpreter instance
  (IMPORT IDENT (DOT IDENT)* AS alias=IDENT SEMIC)* // ID: 0
  global_statement* EOF;

global_statement:
    statement # Stmt
  | PUBLIC EDITABLE? VAR name=IDENT ASSIGN value=expr SEMIC # DeclareGlobalVariable // ID: 10
  | PUBLIC CONST name=IDENT ASSIGN value=expr SEMIC         # DeclareGlobalConstant // ID: 10
;

statement:
    (VAR | CONST) name=IDENT ASSIGN value=expr SEMIC # DeclareVariableStatement // ID: 10
  | FUNC name=IDENT def=function_def                 # DefineFunctionStatement // ID: 11
  | name=IDENT operator=(ASSIGN | PLUSA | MINUSA | MULA | DIVA | INTDIVA | MODA | POWERA) value=expr SEMIC                         # VariableAssignmentStatement // ID: 12
  | target=expr LBRACK key=expr RBRACK operator=(ASSIGN | PLUSA | MINUSA | MULA | DIVA | INTDIVA | MODA | POWERA) value=expr SEMIC # SetItemStatement // ID: 13
  | target=expr DOT name=IDENT operator=(ASSIGN | PLUSA | MINUSA | MULA | DIVA | INTDIVA | MODA | POWERA) value=expr SEMIC         # SetPropertyStatement // ID: 14
  | DELETE name=IDENT SEMIC # DeleteStatement // ID: 20
  | DELETE target=expr LBRACK key=expr RBRACK SEMIC # DeleteItemStatement // ID: 21
  | expr SEMIC # ExpressionStatement // ID: 30
  | IF if_cond=expr THEN if_stmts=statement* (ELIF elif_cond=expr THEN elif_stmts=statement*)* (ELSE else_stmts=statement*) END # IfStatement // ID: 40
  | WHILE cond=expr DO loop_stmt* END                  # WhileLoopStatement // ID: 41
  | FOR variable=IDENT IN range=expr DO loop_stmt* END # ForLoopStatement // ID: 42
  | WAIT expr SEMIC # WaitStatement // ID: 50 Raises error if present in function
;

loop_stmt:
    statement # Statement_
  | BREAK     # BreakStatement // ID: 60
  | CONTINUE  # ContinueStatement // ID: 61
;

function_def: LPAREN IDENT (COMMA IDENT)* COMMA? RPAREN statement*
  (RETURN (returned=expr)? SEMIC)? // ID: 62
  END;

expr:
    LPAREN exp=expr RPAREN # Parentheses
  | NULL   # NullLiteral // ID: 0
  | TRUE   # BoolLiteral // ID: 1
  | FALSE  # BoolLiteral // ID: 1
  | INT    # IntLiteral // ID: 2
  | FLOAT  # FloatLiteral // ID: 3
  | STRING # StringLiteral // ID: 4
  | LBRACK (expr (COMMA expr)* COMMA?)? RBRACK                       # ListLiteral // ID: 5
  | LCURL (IDENT COLON expr (COMMA IDENT COLON expr)* COMMA?)? RCURL # MapLiteral // ID: 6
  | LCURL expr (COMMA expr)* COMMA? RCURL                            # SetLiteral // ID: 7
  | FUNC def=function_def  # FunctionLiteral // ID: 8
  | IDENT  # Variable // ID: 100
  | object=IDENT DOT property=IDENT                 # GetProperty // ID: 101
  // TODO interdire la récupération des méthodes en tant que valeurs
  | object=IDENT DOT property=IDENT LPAREN expr (COMMA expr)* COMMA? RPAREN # MethodCall // ID: 103
  | expr LPAREN expr (COMMA expr)* COMMA? RPAREN    # FunctionCall // ID: 104
  | LPAREN type=IDENT RPAREN exp=expr               # CastValue // ID: 200
  | operator=(MINUS | NOT) operand=expr             # UnaryOperator // IDs: 201
  | source=expr LBRACK key=expr RBRACK              # GetItem // ID: 202
  | left=expr operator=POWER right=expr             # BinaryOperator // ID: 202
  | left=expr operator=(MUL | DIV | INTDIV | MOD) right=expr # BinaryOperator // ID: 202
  | left=expr operator=(PLUS | MINUS) right=expr    # BinaryOperator // ID: 202
  | left=expr NOT? IN right=expr                    # BinaryOperator // ID: 202
  | left=expr operator=(EQUAL | NEQUAL | GT | GE | LT | LE) right=expr # BinaryOperator // ID: 202
  | left=expr operator=AND right=expr               # BinaryOperator // ID: 202
  | left=expr operator=OR right=expr                # BinaryOperator // ID: 202
;
