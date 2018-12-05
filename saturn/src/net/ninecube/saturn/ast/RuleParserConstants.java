/* Generated By:JJTree&JavaCC: Do not edit this line. RuleParserConstants.java */
package net.ninecube.saturn.ast;

public interface RuleParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 4;
  int FORMAL_COMMENT = 5;
  int MULTI_LINE_COMMENT = 6;
  int _DEFAULT = 7;
  int DO = 8;
  int ELSE = 9;
  int EXTENDS = 10;
  int FALSE = 11;
  int IF = 12;
  int IMPORT = 13;
  int SWITCH = 14;
  int TRUE = 15;
  int CASE = 16;
  int regularRule = 17;
  int restrictedRule = 18;
  int cycleRule = 19;
  int frequency = 20;
  int agg = 21;
  int order = 22;
  int desc = 23;
  int FUN_MAP_CALL_LITERAL = 24;
  int NATIONALOP = 25;
  int GT = 26;
  int LT = 27;
  int EQ = 28;
  int LE = 29;
  int GE = 30;
  int NE = 31;
  int ASSIGNOP = 32;
  int ASSIGN = 33;
  int PLUSASSIGN = 34;
  int MINUSASSIGN = 35;
  int STARASSIGN = 36;
  int SLASHASSIGN = 37;
  int REMASSIGN = 38;
  int ADDOP = 39;
  int PLUS = 40;
  int MINUS = 41;
  int UNARYOP = 42;
  int BANG = 43;
  int MULTIOP = 44;
  int STAR = 45;
  int SLASH = 46;
  int REM = 47;
  int TILDE = 48;
  int HOOK = 49;
  int COLON = 50;
  int SC_OR = 51;
  int SC_AND = 52;
  int INCR = 53;
  int DECR = 54;
  int LSHIFT = 55;
  int RSIGNEDSHIFT = 56;
  int RUNSIGNEDSHIFT = 57;
  int INTEGER_LITERAL = 58;
  int DECIMAL_LITERAL = 59;
  int HEX_LITERAL = 60;
  int OCTAL_LITERAL = 61;
  int FLOATING_POINT_LITERAL = 62;
  int EXPONENT = 63;
  int STRING_LITERAL = 64;
  int STRING_LITERALCONTENT = 65;
  int STRING_LITERALCONTENT_SINGLEQUOTE = 66;
  int IDENTIFIER = 67;
  int LETTER = 68;
  int DIGIT = 69;
  int LPAREN = 70;
  int RPAREN = 71;
  int LBRACE = 72;
  int RBRACE = 73;
  int LBRACKET = 74;
  int RBRACKET = 75;
  int SEMICOLON = 76;
  int COMMA = 77;
  int DOT = 78;
  int CRLF = 79;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<FORMAL_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "\"default\"",
    "\"do\"",
    "<ELSE>",
    "\"extends\"",
    "\"false\"",
    "<IF>",
    "\"import\"",
    "\"switch\"",
    "\"true\"",
    "\"case\"",
    "\"regularRule\"",
    "\"restrictedRule\"",
    "\"cycleRule\"",
    "\"frequency\"",
    "\"agg\"",
    "\"order\"",
    "\"desc\"",
    "<FUN_MAP_CALL_LITERAL>",
    "<NATIONALOP>",
    "\">\"",
    "\"<\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "<ASSIGNOP>",
    "<ASSIGN>",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"%=\"",
    "<ADDOP>",
    "\"+\"",
    "\"-\"",
    "<UNARYOP>",
    "\"not\"",
    "<MULTIOP>",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "<SC_OR>",
    "<SC_AND>",
    "\"++\"",
    "\"--\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<STRING_LITERAL>",
    "<STRING_LITERALCONTENT>",
    "<STRING_LITERALCONTENT_SINGLEQUOTE>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "<CRLF>",
  };

}