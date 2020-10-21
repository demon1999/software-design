package test;

import org.junit.Assert;
import org.junit.Test;
import tokens.*;

public class TokenToStringTest {
    @Test
    public void test() {
        Assert.assertEquals(new PlusToken().toString(), "PLUS");
        Assert.assertEquals(new MinusToken().toString(), "MINUS");
        Assert.assertEquals(new MulToken().toString(), "MUL");
        Assert.assertEquals(new DivToken().toString(), "DIV");
        Assert.assertEquals(new NumberToken(-3).toString(), "NUMBER(-3)");
        Assert.assertEquals(new RightBraceToken().toString(), "RIGHT");
        Assert.assertEquals(new LeftBraceToken().toString(), "LEFT");
    }
}
