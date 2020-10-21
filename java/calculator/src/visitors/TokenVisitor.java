package visitors;

import tokens.BinaryOperationToken;
import tokens.BraceToken;
import tokens.NumberToken;

public interface TokenVisitor {
    void visit(NumberToken token);

    void visit(BraceToken token);

    void visit(BinaryOperationToken token);
}
