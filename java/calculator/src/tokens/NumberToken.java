package tokens;

import visitors.TokenVisitor;

public class NumberToken implements Token {
    Integer value;

    public NumberToken(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NUMBER(" + value + ")";
    }
}
