package visitors;

import tokens.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserVisitor implements TokenVisitor {
    private List<Token> result;
    private List<Token> ops;
    private Map<String, Integer> priority;

    public ParserVisitor() {
        result = new ArrayList<>();
        ops = new ArrayList<>();
        priority = new HashMap<>();
        priority.put(new MinusToken().toString(), 1);
        priority.put(new PlusToken().toString(), 1);
        priority.put(new DivToken().toString(), 0);
        priority.put(new MulToken().toString(), 0);
    }

    @Override
    public void visit(NumberToken token) {
        result.add(token);
    }

    @Override
    public void visit(BraceToken token) {
        if (token instanceof LeftBraceToken) {
            ops.add(token);
        } else {
            if (ops.isEmpty()) {
                throw new IllegalArgumentException("Given expression is not correct infix expression");
            }
            while (!ops.isEmpty()) {
                Token last = ops.get(ops.size() - 1);
                ops.remove(ops.size() - 1);
                if (last instanceof LeftBraceToken) {
                    break;
                }
                result.add(last);
                if (ops.isEmpty()) {
                    throw new IllegalArgumentException("Given expression is not correct infix expression");
                }
            }
        }
    }

    @Override
    public void visit(BinaryOperationToken token) {
        while (!ops.isEmpty()) {
            Token last = ops.get(ops.size() - 1);
            if ((last instanceof BinaryOperationToken) &&
                (priority.get(last.toString()) <= priority.get(token.toString()))){
                result.add(last);
                ops.remove(ops.size() - 1);
                continue;
            }
            break;
        }
        ops.add(token);
    }

    public List<Token> visitEnd() {
        while (!ops.isEmpty()) {
            Token last = ops.get(ops.size() - 1);
            ops.remove(ops.size() - 1);
            if (last instanceof LeftBraceToken) {
                throw new IllegalArgumentException("Brace is not closed");
            }
            result.add(last);
        }
        return result;
    }
}
