/*
    comp5
 Vamos aprimorar o analisador lexico. Na linguagem abaixo, temos palavras-chave com mais que um caracter,
 nomes de variaveis formado por 1 ou mais caracteres e numeros inteiros positivos.
 
 Note que nesta linguagem eh necessario declarar pelo menos uma variavel. Alem disso, esta linguagem aceita como comentario
 uma linha que inicia com //. 
 
 A mensagem de erro eh de responsabilidade da classe CompileError.
 O codigo fonte pode ser escrito em um arquivo separado.
 Utilize o GC para geracao de codigo em C e DEBUGLEXER para imprimir os tokens reconhecidos.
        
    Grammar:
       Program ::= 'begin' VarDecList ';' AssignStatement 'end'
       VarDecList ::= Variable | Variable ',' VarDecList
       Variable ::= Letter {Letter}
       Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | ... |'z'
       AssignStatement ::= Variable '=' Expr ';'      
       Expr ::= Oper  Expr Expr  | Number | Variable
       Oper ::= '+' | '-' | '*' | '/'
       Number ::= Digit {Digit} 
       Digit ::= '0'| '1' | ... | '9'
*/

import Lexer.*;
import Error.*;

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = false; 

    public void compile( char []p_input ) {
        lexer = new Lexer(p_input, error);
        lexer.nextToken();
        program();
    }
    
    // Program ::= VarDecList ';' AssignStatement
    public void program(){

    }
       
	// VarDecList ::= Variable | Variable ',' VarDecList
	public void varDecList(){
 
	}

    //AssignStatement ::= Variable '=' Expr ';'
    public void assignStatement(){

        
    }
    
    // Expr ::= Oper  Expr Expr  | Number
    public void expr(){

        
    }
    
	private Lexer lexer;
    private CompilerError error;

}
