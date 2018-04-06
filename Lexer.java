/*
 Program ::= 'begin' VarDecList ';' AssignStatment 'end'
 VarDecList ::= Variable | Variable ',' VarDecList
 Variable ::= Letter {Letter}
 Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | ... |'z'
 AssignStatment ::= Variable '=' Expr ';'
 Expr ::= Oper  Expr Expr  | Number
 Oper ::= '+' | '-' | '*' | '/'
 Number ::= Digit {Digit}
 Digit ::= '0'| '1' | ... | '9'
 */

package Lexer;

import java.util.*;
import Error.*;

public class Lexer {

	// apenas para verificacao lexica
	public static final boolean DEBUGLEXER = true; 
    
    public Lexer( char []input, CompilerError error ) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        this.error = error;
    }
    
    // contains the keywords
    static private Hashtable<String, Symbol> keywordsTable;
    
    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable<String, Symbol>();
        //Palavras chaves
        keywordsTable.put("eof", Symbol.EOF);
        keywordsTable.put( "Ident", Symbol.IDENT );
        keywordsTable.put("IntNumber", Symbol.INTLITERAL);
        keywordsTable.put( "FloatNumber", Symbol.FLOATLITERAL );
        keywordsTable.put("StringLiteral", Symbol.STRINGLITERAL);
        keywordsTable.put( "program", Symbol.PROGRAM );
        keywordsTable.put("end", Symbol.END);
        keywordsTable.put( "begin", Symbol.BEGIN );
        keywordsTable.put( "function", Symbol.FUNCTION );
        keywordsTable.put( "read", Symbol.READ );
        keywordsTable.put( "write", Symbol.WRITE );        
        keywordsTable.put( "if", Symbol.IF );
        keywordsTable.put( "then", Symbol.THEN );
        keywordsTable.put( "else", Symbol.ELSE );
        keywordsTable.put( "endif", Symbol.ENDIF );
        keywordsTable.put( "return", Symbol.RETURN );
        keywordsTable.put( "for", Symbol.FOR );
        keywordsTable.put( "endfor", Symbol.ENDFOR );
        keywordsTable.put( "float", Symbol.FLOAT );
        keywordsTable.put( "int", Symbol.INT );        
        keywordsTable.put( "void", Symbol.VOID );
        keywordsTable.put( "string", Symbol.STRING );
        //Operadores
        keywordsTable.put( "+", Symbol.PLUS );
        keywordsTable.put( "-", Symbol.MINUS );
        keywordsTable.put( "*", Symbol.MULT );
        keywordsTable.put( "/", Symbol.DIV );
        keywordsTable.put( "=", Symbol.EQUAL );
        keywordsTable.put( "<", Symbol.LT );        
        keywordsTable.put( ">", Symbol.GT );
        keywordsTable.put( "(", Symbol.LPAR );  
        keywordsTable.put( ")", Symbol.RPAR );
        keywordsTable.put( ":=", Symbol.ASSIGN );
        keywordsTable.put( ",", Symbol.COMMA );
        keywordsTable.put( ";", Symbol.SEMICOLON );
    }
    
    
    public void nextToken() {

        while(input[tokenPos] == ' ' || input[tokenPos] == '\n' || input[tokenPos] == '\t' ){
            if (input[tokenPos] == '\n'){
                lineNumber++;
            }
            tokenPos++;
        }
        
        //final arquivo
        if (input[tokenPos] == '\0'){
            token = Symbol.EOF;
            return;
        }
        
        //verificar se eh um comentario
        if (input[tokenPos] == '/' && input[tokenPos+1] == '/'){
            //enquanto nao houver quebra de linha ou fim do arquivo
            //ainda eh um comentario
            while(input[tokenPos] != '\n' && input[tokenPos] != '\0'){
                tokenPos++;
            }
            nextToken();
            return;
        }

        //-------  Tokens ------- //
        
        //reconhece o token
        StringBuffer aux = new StringBuffer();
        while (Character.isDigit(input[tokenPos])){
            //concatena em uma string
            aux = aux.append(input[tokenPos]);
            tokenPos++;
        }

        //se a string nao for nula
        if (aux.length() > 0){
            //converte para inteiro
            numberValue = Integer.parseInt(aux.toString());
            if (numberValue > MaxValueInteger){
                error.signal("Numero inteiro maior que o permitido!");
            }
            token = Symbol.NUMBER;
        
        // se a string estava vazia
        } else {
            //se houver letras 
            while (Character.isLetter(input[tokenPos])){
                //concatena em uma string
                aux = aux.append(input[tokenPos]);
                tokenPos++;
            }
            
            //se a string no estiver vazia
            if (aux.length() > 0){
                Symbol temp;
                //verifica se e uma palavra reservada
                temp = keywordsTable.get(aux.toString());
                if (temp == null){ 
                    //entao e um identificador
                    token = Symbol.IDENT;
                    //salvo o nome do identificador
                    stringValue = aux.toString();
                }
                else {
                    token = temp;
                }
            //entao e um simbolo ou operador
            } else {
                switch (input[tokenPos]){
                    case '+':
                        token = Symbol.PLUS;
                        break;
                    case '-':
                        token = Symbol.MINUS;
                        break;
                    case '*':
                        token = Symbol.MULT;
                        break;
                    case '/':
                        token = Symbol.DIV;
                        break;
                    case '=':
                        token = Symbol.EQUAL;
                        break;
                    case '<':
                        token = Symbol.LT;
                        break;
                    case '>':
                        token = Symbol.GT;
                        break;
                    case '(':
                        token = Symbol.LPAR;
                        break;
                    case ')':
                        token = Symbol.RPAR;
                        break;
                    case ':':
                        if(input[tokenPos+1] == '='){
                            token = Symbol.ASSIGN;
                            tokenPos++;
                        }    
                        break;
                    case ',':
                        token = Symbol.COMMA;
                        break;
                    case ';':
                        token = Symbol.SEMICOLON;
                        break;
                    default:
                        error.signal("erro lexico");
                }
                tokenPos++;
            }
        }
		if (DEBUGLEXER)
			System.out.println(token.toString());
        lastTokenPos = tokenPos - 1;
    }
    
    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getCurrentLine() {
        int i = lastTokenPos;
        if ( i == 0 )
            i = 1;
        else
            if ( i >= input.length )
                i = input.length;
        
        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
            i--;
        if ( input[i] == '\n' )
            i++;
        // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public int getNumberValue() {
        return numberValue;
    }
    
    public char getCharValue() {
        return charValue;
    }
    // current token
    public Symbol token;
    private String stringValue;
    private int numberValue;
    private char charValue;
    
    private int  tokenPos;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char []input;
    
    // number of current line. Starts with 1
    private int lineNumber;
    
    private CompilerError error;
    private static final int MaxValueInteger = 32768;
}
