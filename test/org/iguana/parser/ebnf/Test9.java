package org.iguana.parser.ebnf;

import iguana.regex.Char;
import iguana.utils.input.Input;
import org.iguana.grammar.Grammar;
import org.iguana.grammar.symbol.*;
import org.iguana.grammar.transformation.DesugarStartSymbol;
import org.iguana.grammar.transformation.EBNFToBNF;
import org.iguana.parser.Iguana;
import org.iguana.parser.ParseResult;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 *
 * S ::= A* A*
 * A ::= 'a'
 *
 */
public class Test9 {

    static Nonterminal S = Nonterminal.withName("S");
    static Nonterminal A = Nonterminal.withName("A");
    static Terminal a = Terminal.from(Char.from('a'));

    static Rule r1 = Rule.withHead(S).addSymbols(Star.from(A), Star.from(A)).build();
    static Rule r2 = Rule.withHead(A).addSymbols(a).build();

    static Start start = Start.from(S);
    private static Grammar grammar = new DesugarStartSymbol().transform(EBNFToBNF.convert(Grammar.builder().addRules(r1, r2).setStartSymbol(start).build()));

    static Input input0 = Input.empty();
    static Input input1 = Input.fromString("a");
    static Input input2 = Input.fromString("aa");
    static Input input3 = Input.fromString("aaa");
    static Input input4 = Input.fromString("aaaaaaaaa");

    @Test
    public void testParse0() {
        grammar = EBNFToBNF.convert(grammar);
        ParseResult result = Iguana.parse(input0, grammar, S);
        assertTrue(result.isParseSuccess());
    }

    @Test
    public void testParse1() {
        grammar = EBNFToBNF.convert(grammar);
        ParseResult result = Iguana.parse(input1, grammar, S);
        assertTrue(result.isParseSuccess());
    }

    @Test
    public void testParse2() {
        grammar = EBNFToBNF.convert(grammar);
        ParseResult result = Iguana.parse(input2, grammar, S);
        assertTrue(result.isParseSuccess());
    }

    @Test
    public void testParse3() {
        grammar = EBNFToBNF.convert(grammar);
        ParseResult result = Iguana.parse(input3, grammar, S);
        assertTrue(result.isParseSuccess());
    }

    @Test
    public void testParse4() {
        grammar = EBNFToBNF.convert(grammar);
        ParseResult result = Iguana.parse(input4, grammar, S);
        assertTrue(result.isParseSuccess());
    }


}
