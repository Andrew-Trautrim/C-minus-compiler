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

invalid_id = [0-9]+[_a-zA-Z0-9]*
error = . | {invalid_id}
   
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
"+"                  { return symbol(sym.PLUS, yytext()); }
"-"                  { return symbol(sym.MINUS, yytext()); }
"*"                  { return symbol(sym.TIMES, yytext()); }
"/"                  { return symbol(sym.DIVIDE, yytext()); }
"<"                  { return symbol(sym.LT, yytext()); }
"<="                 { return symbol(sym.LEQ, yytext()); }
">"                  { return symbol(sym.GT, yytext()); }
">="                 { return symbol(sym.GEQ, yytext()); }
"=="                 { return symbol(sym.EQ, yytext()); }
"!="                 { return symbol(sym.NEQ, yytext()); }
"~"                  { return symbol(sym.NOT, yytext()); }
"||"                 { return symbol(sym.OR, yytext()); }
"&&"                 { return symbol(sym.AND, yytext()); }
"="                  { return symbol(sym.ASSIGN, yytext()); }
";"                  { return symbol(sym.SEMI); }
","                  { return symbol(sym.COMMA); }
"("                  { return symbol(sym.LPAREN); }
")"                  { return symbol(sym.RPAREN); }
"["                  { return symbol(sym.LSQUARE); }
"]"                  { return symbol(sym.RSQUARE); }
"{"                  { return symbol(sym.LCURLY); }
"}"                  { return symbol(sym.RCURLY); }
{truth}              { return symbol(sym.TRUTH, yytext()); }
{identifier}         { return symbol(sym.ID, yytext()); }        
{number}             { return symbol(sym.NUM, yytext()); }
{whitespace}         { /* skip whitespace */ }
{comment}            { /* skip comments */ }
{error}              { return symbol(sym.ERROR, yytext()); }
