package parser;

import java.io.BufferedReader;
import java.io.IOException;

import parser.grammer.Program;

public abstract class Parser {

    public static Program parse(BufferedReader reader) throws InvalidGrammerException, IOException{

        // create a Tokenizer object from our buffered reader
        RewindableTokenizer toks = new RewindableTokenizerIMPL(new TokenizerIMPL(reader));

        Program p = new Program();

        if(!p.read(toks)) {

        }

        return p;
    }
}