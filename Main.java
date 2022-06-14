import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main 
{
    //private static String inputFileName;
    public static void main(String[] args) 
    {
        if(args.length != 1) {
            System.err.println("Usage need to enter input filename");
            System.exit(1);
        }
        
        /*
        try
        {
            Scanner input = new Scanner(System.in);

            System.out.println("Type the file name here: ");
            inputFileName = input.nextLine();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        */

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))/*(new FileReader(inputFileName))*/){

            // create a Tokenizer object from our buffered reader
            Tokenizer tokenizer = new Tokenizer(reader);

            // start off the tokenizer (read first token)
            tokenizer.next();

            while(tokenizer.kind() != Tokenizer.EOF && !tokenizer.isError()) {
                String msg = String.format("%d:%d:'%s' %s",
                    tokenizer.position().lineNumber, tokenizer.position().linePosition,
                    tokenizer.kind(), (tokenizer.value() != null)?tokenizer.value():"");
                System.out.println(msg);
                tokenizer.next();
            }

            if(tokenizer.isError()) {
                String msg = String.format("%d:%d:>>>>> Illegal character '%c'",
                    tokenizer.position().lineNumber, tokenizer.position().linePosition,
                    tokenizer.peekNextChar());
                System.out.print(msg);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}