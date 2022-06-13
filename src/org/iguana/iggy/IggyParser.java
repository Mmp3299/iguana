package org.iguana.iggy;

import org.iguana.utils.input.Input;
import org.iguana.grammar.Grammar;
import org.iguana.parser.IguanaParser;
import org.iguana.parsetree.ParseTreeNode;
import org.iguana.util.serialization.JsonSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.iguana.utils.io.FileUtils.readFile;

public class IggyParser {

    private static IguanaParser iggyParser;

    public static IguanaParser iggyParser() {
        if (iggyParser == null) {
            iggyParser = new IguanaParser(iggyGrammar());
        }
        return iggyParser;
    }

    public static Grammar iggyGrammar() {
        try {
            String content = readFile(IggyParser.class.getResourceAsStream("/iggy.json"));
            return JsonSerializer.deserialize(content, Grammar.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        String path = Paths.get("src/resources/Iguana.iggy").toAbsolutePath().toString();
        Grammar grammar = getGrammar(path);
//        System.out.println(JsonSerializer.toJSON(grammar));
        JsonSerializer.serialize(grammar, Paths.get("src/resources/iggy.json").toAbsolutePath().toString());
    }

    public static Grammar fromGrammar(String grammar)  {
        return getGrammar(Input.fromString(grammar));
    }

    public static Grammar getGrammar(String path) throws IOException {
        return getGrammar(Input.fromFile(new File(path)));
    }

    private static Grammar getGrammar(Input input) {
        IguanaParser parser = iggyParser();
        parser.parse(input);
        if (parser.hasParseError()) {
            System.out.println(parser.getParseError());
            throw new RuntimeException(parser.getParseError().toString());
        }

        ParseTreeNode parseTree = parser.getParseTree();

        return (Grammar) parseTree.accept(new IggyToGrammarVisitor());
    }
}
