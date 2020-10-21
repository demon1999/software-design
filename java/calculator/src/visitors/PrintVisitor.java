package visitors;

import tokens.BinaryOperationToken;
import tokens.BraceToken;
import tokens.NumberToken;

public class PrintVisitor implements TokenVisitor {
    @Override
    public void visit(NumberToken token) {
        System.out.print(token.toString());
    }

    @Override
    public void visit(BraceToken token) {
        System.out.print(token.toString());
    }

    @Override
    public void visit(BinaryOperationToken token) {
        System.out.print(token.toString());
    }
}
