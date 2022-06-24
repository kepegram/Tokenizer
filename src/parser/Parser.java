package parser;

import java.io.BufferedReader;
import java.io.IOException;

import parser.grammer.InvalidGrammerException;
import parser.grammer.Program;
import parser.tokenizer.RewindableTokenizer;
import parser.tokenizer.RewindableTokenizerIMPL;
import parser.tokenizer.TokenizerIMPL;

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