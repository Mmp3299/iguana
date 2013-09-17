package org.jgll.parser;

import org.jgll.grammar.Grammar;
import org.jgll.parser.lookup.LevelBasedLookupTable;
import org.jgll.parser.lookup.RecursiveDescentLookupTable;


public class ParserFactory {
	
	public static GLLParser recursiveDescentParser(Grammar grammar) {
		return new GLLParserImpl(new RecursiveDescentLookupTable(grammar));
	}
	
	public static GLLParser levelParser(Grammar grammar) {
		return new GLLParserImpl(new LevelBasedLookupTable(grammar));
	}
	
}
