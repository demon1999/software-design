package visitors;

import tokens.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class CalcVisitor implements TokenVisitor {
    private List<Integer> stack;
    private Map<String, BiFunction<Integer, Integer, Integer>> ops;

    public CalcVisitor() {
        stack = new ArrayList<>();
        ops = new HashMap<>();
        ops.put(new PlusToken().toString(), Integer::sum);
        ops.put(new MinusToken().toString(), (i, j) -> i - j);
        ops.put(new MulToken().toString(), (i, j) -> i * j);
        ops.put(new DivToken().toString(), (i, j) -> i / j);
    }

    @Override
    public void visit(NumberToken token) {
        stack.add(token.getValue());
    }

    @Override
    public void visit(BraceToken token) {
        throw new IllegalArgumentException("Reverse polish notation shouldn't contain braces");
    }

    @Override
    public void visit(BinaryOperationToken token) {
        if (stack.size() < 2) {
            throw new IllegalArgumentException("Wrong expression in reverse polish notation");
        }
        Integer x = stack.get(stack.size() - 2),
                y = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        stack.remove(stack.size() - 1);
        if ((token instanceof DivToken) && y == 0)
            throw new IllegalArgumentException("Division by zero!!!");
        stack.add(ops.get(token.toString()).apply(x, y));
    }

    public Integer visitEnd() {
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Illegal expression in reverse polish notation");
        }
        Integer res = stack.get(0);
        stack.remove(0);
        return res;
    }
}
