package tokens;

import tokens.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {
    private Map<Character, Token> p;
    private int pos;

    public Tokenizer() {
        p = new HashMap<>();
        p.put('(', new LeftBraceToken());
        p.put(')', new RightBraceToken());
        p.put('*', new MulToken());
        p.put('+', new PlusToken());
        p.put('/', new DivToken());
    }

    private long readNumber(String s) {
        long res = 0;
        int lpos = pos;
        while (pos < s.length() &&
                (s.charAt(pos) >= '0' && s.charAt(pos) <= '9')) {
            res = res * 10 + (int)(s.charAt(pos) - '0');
            pos++;
        }
        if (lpos == pos)
            throw new IllegalArgumentException("Wrong format of expression");
        return res;
    }

    public List<Token> tokenize(String s) {
        List<Token> res = new ArrayList<>();
        pos = 0;
        while (pos < s.length()) {
            char c = s.charAt(pos);
            pos++;
            if (c == ' ') continue;
            if (p.containsKey(c)) {
                res.add(p.get(c));
                continue;
            }
            if (c == '-') {
                if (res.size() > 0 && !(res.get(res.size() - 1) instanceof BinaryOperationToken)) {
                    res.add(new MinusToken());
                    continue;
                }
                long rs = -readNumber(s);
                res.add(new NumberToken((int) rs));
                continue;
            }
            if (c >= '0' && c <= '9') {
                pos--;
                long rs = readNumber(s);
                res.add(new NumberToken((int) rs));
                continue;
            }
            throw new IllegalArgumentException("Wrong symbol at pos " + Integer.toString(pos - 1));
        }
        return res;
    }

}
