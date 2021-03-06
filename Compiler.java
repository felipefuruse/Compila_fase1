import Lexer.*;
import Error.*;


public class Compiler {
    // para geracao de codigo
    public static final boolean GC = false; 

      public void compile( char []p_input ) {
          lexer = new Lexer(p_input, error);
          error = new CompilerError(lexer);
          error.setLexer(lexer);
         
          lexer.nextToken();

          program();
      }
      
      /* --------------------------------------------------------------------- Program --------------------------------------------------------------------- */

      // Program -> id BEGIN pgm_body END
      //Iago
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

         
      // id -> IDENTIFIER
      //Iago
      public void id(){
        if(lexer.token != Symbol.IDENT)
          error.signal("Id incorreto");
        lexer.nextToken(); 
      }

      //pgm_body -> decl func_declarations
      //iago
      public void pgm_body(){
       
        decl();
        func_decl();     
      }
      
      //decl -> string_decl_list {decl} | var_decl_list {decl} | empty
      //FuRuSe 
      public void decl(){
        //declara string    
        if (lexer.token == Symbol.STRING){ 
          System.out.println("1");
              string_decl_list();                    
        }
        //declara int ou float   
        else if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
          System.out.println("2");
            var_decl_list();
        }
        //repeticao do {decl}
        while(lexer.token == Symbol.STRING || lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
          System.out.println("3");
            decl();
        }
      }

      /* --------------------------------------------------- Global String Declaration -------------------------------------------------------------- */

      //string_decl_list -> string_decl {string_decl_tail}
      //FuRuSe
      public void string_decl_list(){
          
          string_decl();
          while(lexer.token == Symbol.STRING){
              string_decl_tail();
          } 
      }

      //string_decl-> STRING id := str ; | empty
      //igor
      //verificacao se é string, se nao for tem que colocar msg de erro (????)
      public void string_decl () {
        if (lexer.token == Symbol.STRING){
          lexer.nextToken();
          id();
        
          if(lexer.token != Symbol.ASSIGN)
            error.signal("Faltou ASSIGN");
          lexer.nextToken();
          str();

          if(lexer.token != Symbol.SEMICOLON)
            error.signal("Faltou ';'");
        }
      }

      //str-> STRINGLITERAL
      //igor
      public void str()
      {
        if (lexer.token != Symbol.STRINGLITERAL)
          error.signal ("Declaração de String incorreta");
        lexer.nextToken();
      }

      //string_decl_tail -> string_decl {string_decl_tail}
      //FuRuSe
      public void string_decl_tail(){
          
          string_decl();
          while(lexer.token == Symbol.STRING){
              string_decl_tail();
          }
      }

  /* ------------------------------------------------------------------- Variable Declaration ------------------------------------------------------------------*/
      /* Variable Declaration */
      //var_decl_list-> var_decl {var_decl_tail}
      //Igor - incompleto / FuRuSe
      public void var_decl_list()
      {
        var_decl();
        while(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
          var_decl_tail();
        } 
      }
      //var_decl-> var_type id_list ; | empty
      //igor - incompleto / FuRuSe
      public void var_decl()
      {
          var_type();
          id_list();
          if(lexer.token != Symbol.SEMICOLON)
            error.signal("FALTOU ';'");
          lexer.nextToken();
      }

      //var_type-> FLOAT | INT
      //igor
      public void var_type ()
      {
        if (lexer.token != Symbol.FLOAT && lexer.token != Symbol.INT)
          error.signal ("Tipo de variável incorreto");
        lexer.nextToken();
      }

      //any_type-> var_type | VOID
      //Igor - duvida / FuRuSe
      public void any_type ()
      {
        if (lexer.token != Symbol.VOID){
          var_type();
        }
        else
        {
          lexer.nextToken();
        }
      }

      //id_list-> id id_tail
      //Igor
      public void id_list()
      {
        id();
        id_tail();  
      }

      //id_tail-> , id id_tail | empty
      //Igor
      public void id_tail()
      {
        if (lexer.token == Symbol.COMMA)
          lexer.nextToken();
          id();
          id_tail();
      }

      //var_decl_tail-> var_decl {var_decl_tail}
      //Igor - FuRuSe
      public void var_decl_tail()
      {
        var_decl();
        while(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
          var_decl_tail();
        }
      }

      /* ---------------------------------------------------------- Function Paramater List --------------------------------------------------------------------*/
      //param_decl_list-> param_decl param_decl_tail
      //Igor
      public void param_decl_list()
      {
        param_decl();
        param_decl_tail();
      }

      //param_decl-> var_type id
      //Igor
      public void param_decl()
      {
        var_type();
        id();
      }

      //param_decl_tail-> , param_decl param_decl_tail | empty
      //Igor
      public void param_decl_tail()
      {
        if(lexer.token == Symbol.COMMA)
          lexer.nextToken();
          param_decl();
          param_decl_tail();
      }

  /* --------------------------------------------------------------- Function Declarations ---------------------------------------------------------------------*/

      //  func_declarations -> func_decl {func_decl_tail}
      //Igor - incompleto
      public void func_declarations()
      {
        func_decl();
        func_decl_tail();
      }

      // func_decl-> FUNCTION any_type id ({param_decl_list}) BEGIN func_body END | empty
      //Igor
      public void func_decl()
      {
        if(lexer.token == Symbol.FUNCTION){
          lexer.nextToken();
          any_type();
          id();

          if (lexer.token != Symbol.LPAR)
          	error.signal("Faltou o '('");
          lexer.nextToken();
          
          param_decl_list();

	      if(lexer.token != Symbol.RPAR)
	        error.signal("Faltou o ')'");
	        
          if (lexer.token != Symbol.BEGIN)
            error.signal("Faltou o BEGIN");
          lexer.nextToken();
          func_body();
            
          if (lexer.token != Symbol.END)
            error.signal ("Faltou o END");
          lexer.nextToken();
        }
      }

      //func_decl_tail-> func_decl {func_decl_tail}
      //Igor - Incompleto
      public void func_decl_tail()
      {
        func_decl();
        func_decl_tail();
      }
      //func_body-> decl stmt_list
      //Igor
      public void func_body()
      {
        decl();
        stmt_list();
      }
      /* ---------------------------------------------------------------------- Statement List ---------------------------------------------------------------- */

      //stmt_list-> stmt stmt_tail | empty
      //Igor - Incompleto
      public void stmt_list()
      {
          stmt();
          stmt_tail();
      }

      //stmt_tail-> stmt stmt_tail | empty
      //Igor - Incompleto
      public void stmt_tail()
      {
          stmt();
          stmt_tail();
      }
      //stmt-> assign_stmt | read_stmt | write_stmt | return_stmt | if_stmt | for_s
      //Igor
      public void stmt()
      {
        /*if (lexer.token == )
          assign_stmt();
        else if(lexer.token ==)
          read_stmt();
        else if(lexer.token ==)
          write_stmt();
        else if(lexer.token ==)
          return_stmt();
        else if(lexer.token ==)
          if_stmt();
        else if(lexer.token ==)
          for_stmt();
        else
          error.signal("Erro de declaração");*/
      }

      /* ------------------------------------------------------------------ Basic Statements ------------------------------------------------------------------ */
      //assign_stmt-> assign_expr ;
      //Igor
      public void assign_stmt()
      {
        assign_expr();
        if(lexer.token != Symbol.SEMICOLON)
          error.signal("Faltou ';'");
        lexer.nextToken();
      }
      
      //assign_expr-> id := expr
      //Igor
      public void assign_expr()
      {
        id();
        lexer.nextToken();
        if(lexer.token != Symbol.ASSIGN)
          error.signal("Faltou o :=");
        lexer.nextToken();
        expr();
      }

      //read_stmt-> READ ( id_list );
      //Igor
      public void read_stmt()
      {
        if(lexer.token != Symbol.READ)
          error.signal("Faltou o READ");
        lexer.nextToken();
        
        if(lexer.token != Symbol.LPAR)
          error.signal("Faltou o '('");
        lexer.nextToken();
        id_list();
        
        if(lexer.token != Symbol.RPAR)
          error.signal("Faltou o ')'");
        lexer.nextToken();
      }
      
      //write_stmt-> WRITE ( id_list );
      //Igor
      public void write_stmt()
      {
        if(lexer.token != Symbol.WRITE)
          error.signal("Faltou o WRITE");
        lexer.nextToken();
       
        if(lexer.token != Symbol.LPAR)
          error.signal("Faltou o '('");
        lexer.nextToken();
        id_list();
       
        if(lexer.token != Symbol.RPAR)
          error.signal("Faltou o ')'");
        lexer.nextToken();
      }

      //return_stmt-> RETURN expr ;
      //Igor
      public void return_stmt()
      {
        if (lexer.token != Symbol.RETURN)
          error.signal("Faltou RETURN");
        
        lexer.nextToken();
        expr();
        
        if (lexer.token!= Symbol.SEMICOLON)
          error.signal("Faltou ';'");
        lexer.nextToken();        
      }

      /* --------------------------------------------------------------- Expressions -------------------------------------------------------------------------- */

      //expr-> factor expr_tail
      //Igor
      public void expr()
      {
        factor();
        expr_tail();
      }

      //expr_tail-> addop factor expr_tail | empty
      //Igor - Incompleto
      public void expr_tail()
      {
        addop();
        factor();
        expr_tail();
      }

      //factor-> postfix_expr factor_tail
      //Igor
      public void factor ()
      {
        postfix_expr();
        factor_tail();
      }

      //factor_tail-> mulop postfix_expr factor_tail | empty
      //Igor - Incompleto
      public void factor_tail()
      {
          mulop();
          postfix_expr();
          factor_tail();
      }
      
      //postfix_expr-> primary | call_expr
      //Igor - Incompleto
      public void postfix_expr()
      {
        if(lexer.token == Symbol.IDENT)
          call_expr();
        else
            primary();
      }

      //call_expr-> id ( {expr_list} )
      //Igor - Incompleto
      
      public void call_expr()
      {
        id();
        
        if(lexer.token != Symbol.LPAR)
          error.signal("Faltou '('");
        lexer.nextToken();

        expr_list();

        if(lexer.token != Symbol.RPAR)
          error.signal("Faltou ')'");
        lexer.nextToken();
      }
      //expr_list-> expr expr_list_tail
      //Igor
      public void expr_list()
      {
        expr();
        expr_list_tail();
      }

      //expr_list_tail-> , expr expr_list_tail | empty
      //Igor
      public void expr_list_tail()
      {
        if(lexer.token == Symbol.COMMA)
          lexer.nextToken();
          expr();
          expr_list_tail();
      }
      //primary-> (expr) | id | INTLITERAL | FLOATLITERAL
      //Igor - Iago
      public void primary ()
      {
        if(lexer.token == Symbol.IDENT)
        	id();
        else if(lexer.token == Symbol.INTLITERAL || lexer.token == Symbol.FLOATLITERAL)
        	var_type();
	    else{  	
	        if (lexer.token == Symbol.LPAR)
	          lexer.nextToken();
	        expr();
	        if(lexer.token != Symbol.RPAR)
	        	error.signal("Faltou o ')'");
	        lexer.nextToken();
	    }    
      }
      //Igor - Incompleto (?)
      public void addop ()
      {
        if (lexer.token != Symbol.MINUS && lexer.token != Symbol.PLUS)
          error.signal("Operador incorreto. Deve ser '+' ou '-'"); 
        lexer.nextToken();
      }

      //mulop -> * | /
      //Igor - Incompleto (?)
      public void mulop ()
      {
        if (lexer.token != Symbol.MULT && lexer.token != Symbol.DIV)
          error.signal("Operador incorreto. Dever ser '*' ou '/'"); 
        lexer.nextToken();
      }

      /* ----------------------------------------------------- Complex Statements and Condition --------------------------------------------------------------- */
      //if_stmt->IF ( cond ) THEN stmt_list else_part ENDIF
      //Igor
      public void if_stmt()
      {
        if (lexer.token != Symbol.IF)
          error.signal("Faltou o IF");
        lexer.nextToken();
       
        if (lexer.token != Symbol.LPAR)
          error.signal("Faltou o '('");
        lexer.nextToken();
        cond();
       
        if (lexer.token != Symbol.RPAR)
          error.signal("Faltou o ')'");
        lexer.nextToken();
       
        if (lexer.token != Symbol.THEN)
          error.signal("Faltou o THEN");
        lexer.nextToken();
        stmt_list();
        else_part();
       
        if (lexer.token != Symbol.ENDIF)
          error.signal("Faltou o ENDIF");
        lexer.nextToken();
      }

      //else_part->ELSE stmt_list | empty
      //Igor
      public void else_part()
      {
        if(lexer.token == Symbol.ELSE)
          lexer.nextToken();
        stmt_list();
      }

      //cond->expr compop expr
      //Igor
      public void cond()
      {
        expr();
        compop();
        expr(); 
      }

      //compop -> < | > | =
      //Igor
      public void compop()
      {
        if((lexer.token!= Symbol.LT) && lexer.token != Symbol.GT && lexer.token != Symbol.EQUAL)
          error.signal("Comparador incorreto. Use '<', '>' ou '='");
        lexer.nextToken();
      }

      //for_stmt -> FOR ({assign_expr}; {cond}; {assign_expr}) stmt_list ENDFOR
      //Igor - incompleto
      public void for_stmt()
      {
        if (lexer.token != Symbol.FOR)
          error.signal("Faltou FOR");
        lexer.nextToken();

        if (lexer.token != Symbol.LPAR)
          error.signal("Faltou '('");
        lexer.nextToken();

        assign_expr();

        if (lexer.token != Symbol.SEMICOLON)
          error.signal("Faltou ';'");
        lexer.nextToken();

        cond();

        if (lexer.token != Symbol.SEMICOLON)
          error.signal("Faltou ';'");
        lexer.nextToken();

        assign_expr();

        if (lexer.token != Symbol.RPAR)
          error.signal("Faltou ')'");
        lexer.nextToken();
        stmt_list();

        if(lexer.token != Symbol.ENDFOR)
          error.signal("Faltou ENDFOR");
        lexer.nextToken();
      }

    private Lexer lexer;
      private CompilerError error;
}
