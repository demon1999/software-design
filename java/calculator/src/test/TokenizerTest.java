package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.Token;
import tokens.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class TokenizerTest {
    @Test
    public void testBasics() {
        Tokenizer t = new Tokenizer();
        List<Token> ts = t.tokenize("(1 +2) -1 + -1 - 4    * 5");
        List<String> result = new ArrayList<>();
        result.add("LEFT");
        result.add("NUMBER(1)");
        result.add("PLUS");
        result.add("NUMBER(2)");
        result.add("RIGHT");
        result.add("MINUS");
        result.add("NUMBER(1)");
        result.add("PLUS");
        result.add("NUMBER(-1)");
        result.add("MINUS");
        result.add("NUMBER(4)");
        result.add("MUL");
        result.add("NUMBER(5)");
        Assert.assertEquals(result.size(), ts.size());
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(ts.get(i).toString(), result.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNegative() {
        Tokenizer t = new Tokenizer();
        List<Token> ts = t.tokenize("-");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNegative2() {
        Tokenizer t = new Tokenizer();
        List<Token> ts = t.tokenize("1 + -");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongSymbol() {
        Tokenizer t = new Tokenizer();
        List<Token> ts = t.tokenize("ii");
    }
}
