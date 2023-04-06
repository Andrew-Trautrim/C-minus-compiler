
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
      int err = result.accept(analyzer, 0, false);

      /* Code Generation */
      if (err == 0 && Arrays.stream(argv).anyMatch("-c"::equals)) {
        int dotIndex = argv[0].lastIndexOf('.');
        String filename = ((dotIndex == -1) ? argv[0] : argv[0].substring(0, dotIndex)) + ".tm";
        
        CodeGenerator generator = new CodeGenerator(filename);
        generator.visit(result);
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}


