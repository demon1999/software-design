package list_visitor;

import tokens.Token;
import visitors.TokenVisitor;

import java.util.List;

public class ListVisitor {
    public static void visitListTokens(List<Token> tokens, TokenVisitor tv) {
        for (Token t : tokens) {
            t.accept(tv);
        }
    }
}
