package test;

import list_visitor.ListVisitor;
import org.junit.Assert;
import org.junit.Test;
import tokens.*;
import visitors.ParserVisitor;

import java.util.ArrayList;
import java.util.List;

public class ParserVisitorTest {
    @Test
    public void testBasic() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new PlusToken());
        tokens.add(new NumberToken(-2));
        tokens.add(new MulToken());
        tokens.add(new NumberToken(1));
        tokens.add(new MinusToken());
        tokens.add(new LeftBraceToken());
        tokens.add(new NumberToken(2));
        tokens.add(new PlusToken());
        tokens.add(new NumberToken(-2));
        tokens.add(new RightBraceToken());
        ParserVisitor pv = new ParserVisitor();
        ListVisitor.visitListTokens(tokens, pv);
        tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new NumberToken(1));
        tokens.add(new MulToken());
        tokens.add(new PlusToken());
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new PlusToken());
        tokens.add(new MinusToken());
        Assert.assertEquals(pv.visitEnd().size(), tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            Assert.assertEquals(pv.visitEnd().get(i).toString(), tokens.get(i).toString());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParenthesis() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new RightBraceToken());
        ParserVisitor pv = new ParserVisitor();
        ListVisitor.visitListTokens(tokens, pv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongParenthesis2() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new LeftBraceToken());
        ParserVisitor pv = new ParserVisitor();
        ListVisitor.visitListTokens(tokens, pv);
        pv.visitEnd();
    }

}
