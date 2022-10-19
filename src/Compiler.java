import frontend.ast.node.ProgNode;
import frontend.parser.MeteorParser;
import frontend.parser.Stardust;
import frontend.parser.MeteorLexer;
import frontend.ast.controller.*;
import frontend.semantic.SemanticChecker;
import frontend.semantic.SymbolCollector;
import org.antlr.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class Compiler {
  public static void main(String[] args) throws Exception {
    InputStream input = System.in;

    MeteorLexer lexer = new MeteorLexer(CharStreams.fromStream(input));
    lexer.removeErrorListeners();
    lexer.addErrorListener(new AntlrErrorListener());

    MeteorParser parser = new MeteorParser(new CommonTokenStream(lexer));
    parser.removeErrorListeners();
    parser.addErrorListener(new AntlrErrorListener());
    MeteorParser.ProgContext parserRoot = parser.prog();

    ASTBuilder astBuilder = new ASTBuilder();
    ProgNode builderRoot = (ProgNode) astBuilder.visitProg(parserRoot);

    SymbolCollector symbolCollector = new SymbolCollector();
    symbolCollector.visit(builderRoot);

    SemanticChecker semanticChecker = new SemanticChecker();
    semanticChecker.visit(builderRoot);
  }
}