import java.io.*;

public class Main {
    public static void main( String []args ) {
        
        File file;
        FileReader stream;
        int numChRead;
        
        if ( args.length != 1 ) 
            System.out.println("Use only one parameter, the file to be compiled");
        else {
           file = new File(args[0]);
           if ( ! file.exists() || ! file.canRead() ) {
             System.out.println("Either the file " + args[0] + " does not exist or it cannot be read");
             throw new RuntimeException();
           }
           try { 
             stream = new FileReader(file);  
            } catch ( FileNotFoundException e ) {
                System.out.println("Something wrong: file does not exist anymore");
                throw new RuntimeException();
            }
                // one more character for '\0' at the end that will be added by the
                // compiler
            char []input = new char[ (int ) file.length() + 1 ];
            
            try {
              numChRead = stream.read( input, 0, (int ) file.length() );
            } catch ( IOException e ) {
                System.out.println("Error reading file " + args[0]);
                throw new RuntimeException();
            }
                
            if ( numChRead != file.length() ) {
                System.out.println("Read error");
                throw new RuntimeException();
            }
            try {
              stream.close();
            } catch ( IOException e ) {
                System.out.println("Error in handling the file " + args[0]);
                throw new RuntimeException();
            }
                

            Compiler compiler = new Compiler();
        
            compiler.compile(input);
            }
    }
}
        
