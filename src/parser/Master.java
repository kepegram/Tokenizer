package parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import parser.grammer.Grammer;
import parser.grammer.InvalidGrammerException;
import parser.grammer.InvalidTokenizerException;
import parser.grammer.Program;
import parser.tokenizer.RewindableTokenizer;
import parser.tokenizer.RewindableTokenizerIMPL;
import parser.tokenizer.Tokenizer;
import parser.tokenizer.TokenizerIMPL;

// master java file used to tokenize and parse
public class Master {
    // prompt the user for a file name if none is provided
    private static String getFileName(String fileName) {
        if(fileName != null) return fileName;
        try(Scanner input = new Scanner(System.in)) {
            System.out.println("Type the file name here: ");
            return input.nextLine();
        }
    }
    
    public static void main(String[] args) 
    {
        // The file to parse
        String fileName = getFileName((args.length > 0)?args[0]:null);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            try {

                // create a Tokenizer object from our buffered reader
                RewindableTokenizer toks = new RewindableTokenizerIMPL(new TokenizerIMPL(reader));
        
                // create a Program object as the parser root
                Program program = new Program();

                // try to parse and output the result to the console
                System.out.println(program.read(toks));

                try (BufferedReader reader2 = new BufferedReader(new FileReader(fileName))){

                    // create a Tokenizer object from our buffered reader
                    Tokenizer tokenizer = new RewindableTokenizerIMPL(new TokenizerIMPL(reader2));
        
                    // start off the tokenizer (read first token)
                    tokenizer.next();
        
                    // continue looping until we finad an error or reach the end of the file
                    while(tokenizer.kind() != Grammer.EOF && !tokenizer.isError()) {
                        // output the details of the current token (lexeme)
                        System.out.println(tokenizer.tokenToString());
                        // jump to the next token
                        tokenizer.next();
                    }
        
                    // Check if the tokenizer is in an error state
                    if(tokenizer.isError()) {
                        // if so print out a formatted error message
                        String msg = String.format("%d:%d:>>>>> Illegal character '%c'",
                            tokenizer.position().lineNumber, tokenizer.position().linePosition,
                            tokenizer.position().charAt);
                        System.out.print(msg);
                    } else {
                        // otherwise print "end-of-text"
                        System.out.print("end-of-text");
                    }
        
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (InvalidTokenizerException e) {
                // Output a message to the console for failure
                System.out.println("\t\t************** invalid file **************");
                System.out.println(e);
            }catch (InvalidGrammerException e) {
                // Output a message to the console for failure
                System.out.println("\t\t************** invalid file **************");
                System.out.println(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
