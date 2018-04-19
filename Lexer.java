package Lexer;

import java.util.*;
import Error.*;

public class Lexer {

	// apenas para verificacao lexica
	public static final boolean DEBUGLEXER = true; 
    
    public Lexer( char []input, CompilerError error )
    {
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
      
        keywordsTable.put( "eof", Symbol.EOF );
        keywordsTable.put( "Ident", Symbol.IDENT);
        keywordsTable.put( "IntNumber", Symbol.INTLITERAL );
        keywordsTable.put( "FloatNumber", Symbol.FLOATLITERAL );
        keywordsTable.put( "StringLiteral", Symbol.STRINGLITERAL );
        keywordsTable.put( "PROGRAM", Symbol.PROGRAM);
        keywordsTable.put( "BEGIN", Symbol.BEGIN );
        keywordsTable.put( "END", Symbol.END);
        keywordsTable.put( "FUNCTION", Symbol.FUNCTION);
        keywordsTable.put( "READ", Symbol.READ );
        keywordsTable.put( "WRITE", Symbol.WRITE );
        keywordsTable.put( "IF", Symbol.IF );
        keywordsTable.put( "THEN", Symbol.THEN);
        keywordsTable.put( "ENDIF", Symbol.ENDIF );
        keywordsTable.put( "return", Symbol.RETURN);
        keywordsTable.put( "STRING", Symbol.STRING);
        keywordsTable.put( "+", Symbol.PLUS);
        keywordsTable.put( "-", Symbol.MINUS);
        keywordsTable.put( "*", Symbol.MULT);
        keywordsTable.put( "/", Symbol.DIV);
        keywordsTable.put( "FOR", Symbol.FOR);
        keywordsTable.put( "ENDFOR", Symbol.ENDFOR);
        keywordsTable.put( "FLOAT", Symbol.FLOAT);
        keywordsTable.put( "INT", Symbol.INT);
        keywordsTable.put( "VOID", Symbol.VOID);
        keywordsTable.put( "=", Symbol.EQUAL);
        keywordsTable.put( "<", Symbol.LT);
        keywordsTable.put( ">", Symbol.GT);
        keywordsTable.put( "(", Symbol.LPAR);
        keywordsTable.put( ")", Symbol.RPAR);
        keywordsTable.put( ":=", Symbol.ASSIGN);
        keywordsTable.put( ",", Symbol.COMMA);
        keywordsTable.put( ";", Symbol.SEMICOLON);
    }
    
    
    public void nextToken() {
        //caracteres que não devem fazer parte
        while(input[tokenPos] == '\n' || input[tokenPos] == '\t' || input[tokenPos] == ' ' )
        {
            if (input[tokenPos] == '\n')
            {
                lineNumber++;
            }
            tokenPos++;
        }

        //fim arquivo
        if (input[tokenPos] == '\0')
        {
            token = Symbol.EOF;
            return;
        }
        
        //comentario
        if (input[tokenPos] == '-' && input[tokenPos+1] == '-')
        {
            while(input[tokenPos] != '\n' && input[tokenPos] != '\0')
            {
                tokenPos++;
            }
            nextToken();
            return;
        }

        //-------  Tokens 'uteis' (funcoes, variaveis, parametros...) -------
        
        //descobrir token
        StringBuffer aux = new StringBuffer();
        Boolean falso = false;
        //verifica se contém número
        while((Character.isDigit(input[tokenPos])) || (input[tokenPos] == '.'))
        {
                if (input[tokenPos] == '.')
                    falso = true;
            //oconcatena na string
                aux = aux.append(input[tokenPos]);
                tokenPos++;
        }

        //verifica se a string contem algo
        if (aux.length() > 0){
            //converte para int
            if (!falso)
            {
                numberValue = Integer.parseInt(aux.toString());
                if (numberValue > MaxValueInteger){
                    error.signal("Numero inteiro maior que o permitido!");
                }
                //o token e um numero
                token = Symbol.INT;
            }
            else
            {
                floatValue = Float.parseFloat(aux.toString());
                if (floatValue > MaxValueFloat){
                    error.signal("Numero float maior que o permitido!");
                }
                //o token e um numero
                token = Symbol.FLOAT;
            }
            
        }
        else {
            //se contiver letras ao inves de numeros
            while (Character.isLetter(input[tokenPos])){
                //concatena num string
                aux = aux.append(input[tokenPos]);
                tokenPos++;
            }
            
            //verifica se a string nao esta vazia
            if (aux.length() > 0){
                Symbol temp;
                //verifica se e uma palavra reservada
                temp = keywordsTable.get(aux.toString());
                if (temp == null){ 
                    //se nao for palavra reservada entao e ID
                    token = Symbol.IDENT;
                    //salvo o identificador
                    stringValue = aux.toString();
                }
                else {
                    //e palavra reservada entao mando pro token
                    token = temp;
                }
            //caso nao seja NUMBER nem uma PALAVRA entao e simbolo/operador
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
    
    // return the line number of the last token got with getToken
    public int getLineNumber(){
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

    public float getFloat()
    {
        return floatValue;
    }
    
    public char getCharValue() {
        return charValue;
    }
    // current token
    public Symbol token;
    private String stringValue;
    private int numberValue;
    private float floatValue;
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
    private static final float MaxValueFloat = 32768;
}

