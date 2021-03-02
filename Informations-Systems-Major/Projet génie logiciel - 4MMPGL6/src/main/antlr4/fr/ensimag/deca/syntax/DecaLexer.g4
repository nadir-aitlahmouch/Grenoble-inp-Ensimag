lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}
// LES fragments
fragment DIGIT : '0' .. '9';
fragment LETTER : 'a' ..  'z' |'A' ..  'Z';
fragment POSITIVE_DIGIT : '1' .. '9';
fragment STRING_CAR : ~('"' | '\\');
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | ;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | );
fragment DIGITHEX : DIGIT | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
// les regles
EOL : '\n' { skip(); };

  // Symbole speciaux
OBRACE : '{';
CBRACE : '}';
SEMI : ';';
COMMA : ',';
EQUALS : '=';
OPARENT : '(';
CPARENT : ')';
OR : '||';
AND : '&&';
EQEQ : '==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
GT : '>';
LT : '<';
PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';
// mots reserves
ASM : 'asm';
CLASS : 'class';
EXTENDS : 'extends';
ELSE : 'else';
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW : 'new';
NULL : 'null';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINT : 'print';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE: 'while';

// litteraux entiers
INT : '0' | POSITIVE_DIGIT DIGIT*;


// Literaux flottants
FLOAT : FLOATDEC | FLOATHEX;


// chaines de carac
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Commentaires
COMMENT : ('//' (~('\n'))*
           | '/*' .*? '*/'){ skip(); };


// Separateurs
WS  :   ( ' '
        | '\t'
        | '\r'
        ) {
              skip(); // avoid producing a token
          }
    ;

// INCLUDE 
INCLUDE : '#include' (' ')* '"' FILENAME '"'{doInclude(getText());};


   // identificateur
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
