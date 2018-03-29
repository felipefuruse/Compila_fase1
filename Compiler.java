/*
    comp5
 Vamos aprimorar o analisador lexico. Na linguagem abaixo, temos palavras-chave com mais que um caracter, nomes de variaveis formado por 1 ou mais caracteres e numeros inteiros positivos. Note que nesta linguagem eh necessario declarar pelo menos uma variavel. Alem disso, esta linguagem aceita como comentario uma linha que inicia com //. 
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
    
    // Program id BEGIN pgm_body END
    public void program(){
      if(lexer.token != Symbol.PROGRAM)
        error.signal("Faltou o PROGRAM");
      lexer.nextToken();
      id();
      if(lexer.token != Symbol.BEGIN)
        error.signal("Faltou o BEGIN");
      lexer.nextToken();
      pgm_body();
      if(lexer.token != Symbol.END)
        error.signal("Faltou o END");
      lexer.nextToken();
    }
       
  	// id ::= IDENTIFIER
  	public void id(){
      if(lexer.token != Symbol.IDENT)
        error.signal("Id incorreto");
      lexer.nextToken(); 
  	}

    //pgm_body ::= decl func_declaration
    public void pgm_body(){
      decl();
      func_declaration();        
    }
    
    // Expr ::= Oper  Expr Expr  | Number
    public void expr(){

        
    }
    
	private Lexer lexer;
    private CompilerError error;

}
    
    
    
