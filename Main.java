import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main 
{
    public static void main(String[] args) 
    {
        String inputFileName = "./examples/test.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))){

            // create a Tokenizer object from our buffered reader
            Tokenizer tokenizer = new Tokenizer(reader);

            // start off the tokenizer (read first token)
            tokenizer.next();

            while(tokenizer.kind() != Tokenizer.EOF) {
                String msg = String.format("%2d:%2d  -  (kind =\"%s\", value = \"%s\"): %s",
                    tokenizer.position().lineNumber, tokenizer.position().linePosition,
                    tokenizer.kind(), tokenizer.value(), tokenizer.currentToken);
                System.out.println(msg);
                tokenizer.next();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}