package tokens;

import visitors.TokenVisitor;

public interface Token {
    public void accept(TokenVisitor visitor);

    public String toString();
}

