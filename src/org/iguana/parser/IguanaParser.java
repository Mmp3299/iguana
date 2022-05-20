/*
 * Copyright (c) 2015, Ali Afroozeh and Anastasia Izmaylova, Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 *    list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 */

package org.iguana.parser;

import iguana.utils.input.Input;
import org.iguana.grammar.Grammar;
import org.iguana.grammar.runtime.RuntimeGrammar;
import org.iguana.parsetree.DefaultParseTreeBuilder;
import org.iguana.parsetree.ParseTreeNode;
import org.iguana.result.ParserResultOps;
import org.iguana.sppf.NonterminalNode;
import org.iguana.traversal.AmbiguousSPPFToParseTreeVisitor;
import org.iguana.traversal.DefaultSPPFToParseTreeVisitor;
import org.iguana.util.Configuration;

public class IguanaParser extends IguanaRecognizer {

    private static final ParserResultOps parserResultOps = new ParserResultOps();

    private ParseTreeNode parseTree;
    private NonterminalNode sppf;
    private Input input;

    public IguanaParser(Grammar grammar) {
        this(grammar, grammar.getStartSymbol().getStartSymbol(), Configuration.load());
    }

    public IguanaParser(Grammar grammar, String startNonterminal, Configuration config) {
        super(grammar, startNonterminal, config);
    }

    public IguanaParser(RuntimeGrammar grammar, Configuration config) {
        super(grammar, config);
    }

    public IguanaParser(RuntimeGrammar grammar) {
        super(grammar, Configuration.load());
    }

    public void parse(Input input) {
        parse(input, new ParseOptions.Builder().setMap(globals).setGlobal(false).build());
    }

    public void parse(Input input, ParseOptions options) {
        clear();
        this.input = input;
        IguanaRuntime<?> runtime = new IguanaRuntime<>(config, parserResultOps);
        long start = System.nanoTime();
        this.sppf = (NonterminalNode) runtime.run(input, grammarGraph, options.getMap(), options.isGlobal());
        long end = System.nanoTime();
        this.statistics = runtime.getStatistics();
        this.parseError = runtime.getParseError();
        if (parseError != null) {
            System.out.println(parseError);
        } else {
            System.out.println("Parsing finished in " + (end - start) / 1000_000 + "ms.");
        }
    }

    @Override
    protected void clear() {
        super.clear();
        this.sppf = null;
        this.parseTree = null;
        this.input = null;
    }

    public NonterminalNode getSPPF() {
        return sppf;
    }

    public ParseTreeNode getParseTree() {
        return getParseTree(false, true);
    }

    public ParseTreeNode getParseTree(boolean ambiguous, boolean ignoreLayout) {
        if (parseTree != null) {
            return parseTree;
        }

        if (sppf == null) {
            return null;
        }

        if (ambiguous) {
            AmbiguousSPPFToParseTreeVisitor<ParseTreeNode> visitor = new AmbiguousSPPFToParseTreeVisitor<>(new DefaultParseTreeBuilder(input), ignoreLayout, parserResultOps);
            long start = System.nanoTime();
            ParseTreeNode node = (ParseTreeNode) sppf.accept(visitor).getValues().get(0);
            long end = System.nanoTime();
            System.out.println("Parse tree creation finished in " + (end - start) / 1000_000 + "ms.");
            return node;
        }

        DefaultSPPFToParseTreeVisitor<?> converter = new DefaultSPPFToParseTreeVisitor<>(new DefaultParseTreeBuilder(input), input, ignoreLayout, parserResultOps);
        long start = System.nanoTime();
        this.parseTree = (ParseTreeNode) converter.convertNonterminalNode(sppf);
        long end = System.nanoTime();
        System.out.println("Parse tree creation finished in " + (end - start) / 1000_000 + "ms.");

        return parseTree;
    }

    public ParseStatistics getStatistics() {
        return (ParseStatistics) statistics;
    }
}
