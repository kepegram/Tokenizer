package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import parser.grammer.InvalidGrammerException;
import parser.grammer.Program;

public class Main {
    public static void main(String args[]) {
        if(args.length != 1) {
            System.err.println("Usage need to enter input filename");
            System.exit(1);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))/*(new FileReader(inputFileName)))*/){

            try {
                Program program = Parser.parse(reader);
                System.out.println(program.toString());
            } catch (InvalidGrammerException e) {
                System.out.println("\t\t************** invalid file **************");
                System.out.println(e);
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
