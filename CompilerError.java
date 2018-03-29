package Error;

import Lexer.*;
import java.io.*;

public class CompilerError {
    
    public CompilerError( Lexer lexer ) {
          // output of an error is done in out
        this.lexer = lexer;
        thereWasAnError = false;
    }
    
    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }
    
    public boolean wasAnErrorSignalled() {
        return thereWasAnError;
    }

    public void show( String strMessage ) {
        show( strMessage, false );
    }
    
    public void show( String strMessage, boolean goPreviousToken ) {
        // is goPreviousToken is true, the error is signalled at the line of the
        // previous token, not the last one.
        if ( goPreviousToken ) {
          System.out.println("Error at line " + lexer.getCurrentLine() + ": ");
        }
        else {
          System.out.println("Error at line " + lexer.getLineNumber() + ": ");
          System.out.println(lexer.getCurrentLine());
        }
        
        System.out.println( strMessage );
        
        thereWasAnError = true;
    }
       
    public void signal( String strMessage ) {
        show( strMessage );
        thereWasAnError = true;
        throw new RuntimeException();
    }
    
    private Lexer lexer;
    private boolean thereWasAnError;
}
    
