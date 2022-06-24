package parser.tokenizer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.grammer.Grammer;

class LexemeInfo {
    String kind;
    Object value;
    Position pos;

    public LexemeInfo(String kind, Object value, Position pos) {
        this.kind = kind;
        this.value = value;
        this.pos = pos;
    }
}

public class RewindableTokenizerIMPL implements RewindableTokenizer {

    private final List<LexemeInfo> lexemeList;
    private final Tokenizer srcTokenizer;
    private int currentLexeme;
    private boolean errorFlag;

    public RewindableTokenizerIMPL(Tokenizer srcTokenizer) {
        this.srcTokenizer = srcTokenizer;
        this.lexemeList = new ArrayList<>();
        this.currentLexeme = -1;
        this.errorFlag = false;
    }

    @Override
    public void rewind() throws IndexOutOfBoundsException {
        if(this.currentLexeme <= 0) {
            throw new IndexOutOfBoundsException();
        }

        this.currentLexeme--;
    }

    @Override
    public boolean isError() {
        return this.errorFlag;
    }

    @Override
    public void next() throws IOException {
        if(this.currentLexeme < this.lexemeList.size() - 1) {
            this.currentLexeme++;
        } else {
            if(this.srcTokenizer.isError()) {
                if(this.errorFlag != true) {
                    this.errorFlag = true;
                    this.lexemeList.add(new LexemeInfo(null, null, this.srcTokenizer.position()));
                    this.currentLexeme = this.lexemeList.size() - 1;
                }
            } else {
                if(this.lexemeList.size() == 0 || this.srcTokenizer.kind() != Grammer.EOF) {
                    this.srcTokenizer.next();
                    if(this.srcTokenizer.kind() != Grammer.EOF) {
                        this.lexemeList.add(new LexemeInfo(
                            this.srcTokenizer.kind(), this.srcTokenizer.value(), this.srcTokenizer.position()));
                    } else {
                        this.lexemeList.add(new LexemeInfo(Grammer.EOF, null, null));
                    }
                    this.currentLexeme = this.lexemeList.size() - 1;
                }
            }
        }
    }

    @Override
    public Position position() {
        return this.lexemeList.get(this.currentLexeme).pos;
    }

    @Override
    public Object value() {
        return this.lexemeList.get(this.currentLexeme).value;
    }

    @Override
    public String kind() {
        return this.lexemeList.get(this.currentLexeme).kind;
    }
}
