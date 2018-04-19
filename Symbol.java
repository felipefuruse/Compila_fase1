package Lexer;

public enum Symbol {

	EOF("eof"),
	IDENT("Ident"),
	INTLITERAL("IntNumber"),
	FLOATLITERAL("FloatNumber"),
	STRINGLITERAL("StringLiteral"),
	PROGRAM("PROGRAM"),
	BEGIN("BEGIN"),
	END("END"),
	FUNCTION("FUNCTION"),
	READ("READ"),
	WRITE("WRITE"),
	IF("IF"),
	THEN("THEN"),
	ELSE("ELSE"),
	ENDIF("ENDIF"),
	RETURN("return"),
	FOR("FOR"),
	ENDFOR("ENDFOR"),
	FLOAT("FLOAT"),
	INT("INT"),
	VOID("VOID"),
	STRING("STRING"),
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/"),
	EQUAL("="),
	LT("<"),
	GT(">"),
	LPAR("("),
	RPAR(")"),
	ASSIGN(":="),
	COMMA(","),
	SEMICOLON(";");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;

}
