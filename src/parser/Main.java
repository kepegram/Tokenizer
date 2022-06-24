package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import parser.grammer.InvalidGrammerException;
import parser.grammer.Program;
import parser.tokenizer.RewindableTokenizer;
import parser.tokenizer.RewindableTokenizerIMPL;
import parser.tokenizer.TokenizerIMPL;

public class Main {
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
        String fileName = getFileName(args[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            try {

                // create a Tokenizer object from our buffered reader
                RewindableTokenizer toks = new RewindableTokenizerIMPL(new TokenizerIMPL(reader));
        
                // create a Program object as the parser root
                Program program = new Program();

                // try to parse and output the result to the console
                System.out.println(program.read(toks));
            } catch (InvalidGrammerException e) {
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
