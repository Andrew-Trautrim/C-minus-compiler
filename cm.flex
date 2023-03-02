/*
  Created By: Andrew Trautrim
  File Name: cm.flex
  To Build: jflex cm.flex

  and then after the parser is created
    javac Lexer.java
*/
   
/* --------------------------Usercode Section------------------------ */
   
import java_cup.runtime.*;
      
%%
   
/* -----------------Options and Declarations Section----------------- */
   
/* 
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java. 
*/
%class Lexer

%eofval{
  return null;
%eofval};

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column
    
/* 
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup
   
/*
  Declarations
   
  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.  
*/
%{   
    /* To create a new java_cup.runtime.Symbol with information about
       the current token, the token will have no value in this
       case. */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Also creates a new java_cup.runtime.Symbol with information
       about the current token, but this object has a value. */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   
// REGEX

identifier = [_a-zA-Z][_a-zA-Z0-9]*
number = [0-9]+
truth = "false"|"true"

lineTerminator = \r|\n|\r\n
whitespace = {lineTerminator} | [ \t\f]
comment = "/*"[^"*/"]*"*/"
   
%%
/* ------------------------Lexical Rules Section---------------------- */
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */

"bool"               { return symbol(sym.BOOL); }
"else"               { return symbol(sym.ELSE); }
"if"                 { return symbol(sym.IF); }
"int"                { return symbol(sym.INT); }
"return"             { return symbol(sym.RETURN); }  
"void"               { return symbol(sym.VOID); }  
"while"              { return symbol(sym.WHILE); }  
"+"                  { return symbol(sym.PLUS); }
"-"                  { return symbol(sym.MINUS); }
"*"                  { return symbol(sym.TIMES); }
"/"                  { return symbol(sym.DIVIDE); }
"<"                  { return symbol(sym.LT); }
"<="                 { return symbol(sym.LEQ); }
">"                  { return symbol(sym.GT); }
">="                 { return symbol(sym.GEQ); }
"=="                 { return symbol(sym.EQ); }
"!="                 { return symbol(sym.NEQ); }
"~"                  { return symbol(sym.TILDE); }
"||"                 { return symbol(sym.OR); }
"&&"                 { return symbol(sym.AND); }
"="                  { return symbol(sym.ASSIGN); }
";"                  { return symbol(sym.SEMI); }
","                  { return symbol(sym.COMMA); }
"("                  { return symbol(sym.LPAREN); }
")"                  { return symbol(sym.RPAREN); }
"["                  { return symbol(sym.LSQUARE); }
"]"                  { return symbol(sym.RSQUARE); }
"{"                  { return symbol(sym.LCURLY); }
"}"                  { return symbol(sym.RCURLY); }
{identifier}         { return symbol(sym.ID, yytext()); }        
{number}             { return symbol(sym.NUM, yytext()); }
{truth}              { return symbol(sym.TRUTH, yytext()); }
{whitespace}         { /* skip whitespace */ }
{comment}            { /* skip comments */ }
.                    { return symbol(sym.ERROR); }
