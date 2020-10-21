import list_visitor.ListVisitor;
import tokens.Token;
import tokens.Tokenizer;
import visitors.CalcVisitor;
import visitors.ParserVisitor;
import visitors.PrintVisitor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        List<Token> tokens = new Tokenizer().tokenize(s);
        ParserVisitor p = new ParserVisitor();
        ListVisitor.visitListTokens(tokens, p);
        List<Token> rpn = p.visitEnd();
        PrintVisitor pr = new PrintVisitor();
        CalcVisitor cc = new CalcVisitor();
        ListVisitor.visitListTokens(rpn, cc);
        for (Token t : rpn) {
            t.accept(pr);
            System.out.print(" ");
        }
        System.out.println();
        System.out.println(cc.visitEnd());
    }
}
