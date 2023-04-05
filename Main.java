
import java.util.Arrays;
import java.util.stream.*;
import java.io.*;
import absyn.*;
   
class Main {
  static public void main(String argv[]) {    
    /* Start the parser */
    try {
      /* Create Syntax Tree */
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);

      /* Display Syntax Tree */ 
      if (Arrays.stream(argv).anyMatch("-a"::equals) && result != null) {
         System.out.println("The abstract syntax tree is:");
         ShowTreeVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0, false); 
      }

      /* Semantic Analysis */
      SemanticAnalyzer analyzer = new SemanticAnalyzer(Arrays.stream(argv).anyMatch("-s"::equals));
      result.accept(analyzer, 0, false);

      /* Code Generation */
      if (Arrays.stream(argv).anyMatch("-c"::equals)) {
        CodeGenerator generator = new CodeGenerator("test.tm");
        generator.visit(result);
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}


