package rs.ac.bg.etf.pp1;
import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

 " " 	{ }
 "\b" 	{ }
 "\t" 	{ }
 "\r\n" 	{ }
 "\f" 	{ }


 "program"   { return new_symbol(sym.PROG, yytext());}
 "return" 	{ return new_symbol(sym.RETURN, yytext()); }
 "void"      { return new_symbol(sym.VOID, yytext()); }
 "print"     { return new_symbol(sym.PRINT, yytext()); }
 "final"   	{ return new_symbol(sym.FINAL, yytext());}
 "read"      { return new_symbol(sym.READ, yytext()); }

 "new"       { return new_symbol(sym.NEW, yytext()); }
 "const"     { return new_symbol(sym.CONST, yytext()); }
 "true"       {return new_symbol(sym.BOOL, new String(yytext()));}
 "false"    {return new_symbol(sym.BOOL, new String(yytext()));}
 "goto"		{ return new_symbol(sym.GOTO, yytext()); }

 "("			{ return new_symbol(sym.LPAREN, yytext()); }
 ")"			{ return new_symbol(sym.RPAREN, yytext()); }
 "{"         { return new_symbol(sym.LBRACE, yytext()); }
 "}"         { return new_symbol(sym.RBRACE, yytext()); }
 "["         { return new_symbol(sym.LSQUAREBRACE, yytext()); }
 "]"         { return new_symbol(sym.RSQUAREBRACE, yytext()); }

 ";"         { return new_symbol(sym.SEMI, yytext()); }
 ","         { return new_symbol(sym.COMMA, yytext()); }
 ":"			{ return new_symbol(sym.COLON, yytext()); }

 "="         { return new_symbol(sym.EQUAL, yytext()); }
 "@"         { return new_symbol(sym.MONKEY, yytext()); }
 "#"         { return new_symbol(sym.HASH, yytext()); }
 "+"         { return new_symbol(sym.PLUS, yytext()); }
 "-"         { return new_symbol(sym.MINUS, yytext()); }
 "*"         { return new_symbol(sym.MUL, yytext()); }
 "/"         { return new_symbol(sym.DIV, yytext()); }
 "%"         { return new_symbol(sym.MOD, yytext()); }
 "++"        { return new_symbol(sym.INC, yytext()); }
 "--"        { return new_symbol(sym.DEC, yytext()); }
 "^"        { return new_symbol(sym.CARET, yytext()); }


 [:letter:]([:letter:]|[:digit:]|_)* 	{return new_symbol (sym.IDENT, yytext()); }
 ("true"|"false")  				{return new_symbol(sym.BOOL, new String(yytext()));}
 [0-9]+                          		{ return new_symbol(sym.NUMBER, new Integer (yytext())); }
 '[\x20-\x7E]' 							{return new_symbol(sym.CHAR, new Character(yytext().charAt(1)));}


 "//" 	{yybegin(COMMENT);}
<COMMENT> . 		{yybegin(COMMENT);}
<COMMENT> "\r\n" 	{ yybegin(YYINITIAL); }

. { 
	
	System.err.println("Greska prilikom leksicke analize ("+ yytext() + ") na liniji "+ (yyline + 1)); 
}


