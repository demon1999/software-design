package tokens;

import visitors.TokenVisitor;

public abstract class BinaryOperationToken implements Token {
    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public abstract String toString();
}
