package test;

import list_visitor.ListVisitor;
import org.junit.Assert;
import org.junit.Test;
import tokens.*;
import visitors.CalcVisitor;
import visitors.TokenVisitor;

import java.util.ArrayList;
import java.util.List;

public class CalcVisitorTest {

    @Test
    public void testPlus() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new PlusToken());
        CalcVisitor cv = new CalcVisitor();
        ListVisitor.visitListTokens(tokens, cv);
        Assert.assertEquals(cv.visitEnd(), new Integer(0));
    }

    @Test
    public void testMinus() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new MinusToken());
        CalcVisitor cv = new CalcVisitor();
        ListVisitor.visitListTokens(tokens, cv);
        Assert.assertEquals(cv.visitEnd(), new Integer(4));
    }

    @Test
    public void testMul() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new MulToken());
        CalcVisitor cv = new CalcVisitor();
        ListVisitor.visitListTokens(tokens, cv);
        Assert.assertEquals(cv.visitEnd(), new Integer(-4));
    }

    @Test
    public void testDiv() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new DivToken());
        CalcVisitor cv = new CalcVisitor();
        ListVisitor.visitListTokens(tokens, cv);
        Assert.assertEquals(cv.visitEnd(), new Integer(-1));
    }

    @Test
    public void testComplex() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(-2));
        tokens.add(new DivToken());
        tokens.add(new NumberToken(1));
        tokens.add(new NumberToken(-1));
        tokens.add(new NumberToken(-2));
        tokens.add(new MulToken());
        tokens.add(new PlusToken());
        tokens.add(new MinusToken());
        CalcVisitor cv = new CalcVisitor();
        ListVisitor.visitListTokens(tokens, cv);
        Assert.assertEquals(cv.visitEnd(), new Integer(-4));
    }

    @Test
    public void testDivByZero() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(2));
        tokens.add(new NumberToken(0));
        tokens.add(new DivToken());
        CalcVisitor cv = new CalcVisitor();
        try {
            ListVisitor.visitListTokens(tokens, cv);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Division by zero!!!");
        }
    }

    @Test
    public void testBracket() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new LeftBraceToken());
        CalcVisitor cv = new CalcVisitor();
        try {
            ListVisitor.visitListTokens(tokens, cv);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Reverse polish notation shouldn't contain braces");
        }
    }

    @Test
    public void testWrongRPN() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(3));
        tokens.add(new MulToken());
        CalcVisitor cv = new CalcVisitor();
        try {
            ListVisitor.visitListTokens(tokens, cv);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Wrong expression in reverse polish notation");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongRPN2() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new NumberToken(3));
        tokens.add(new NumberToken(3));
        CalcVisitor cv = new CalcVisitor();
        try {
            ListVisitor.visitListTokens(tokens, cv);
            cv.visitEnd();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Illegal expression in reverse polish notation");
            throw e;
        }
    }
}
