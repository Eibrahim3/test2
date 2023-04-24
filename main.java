package extra;

import java.util.Arrays;
import java.util.List;

public class main {

	 public static void main(String[] args) {
         List<String> tokens = Arrays.asList(
                 "program", "myProgram", ";",
                 "var", "x", ",", "y", ":", "integer", ";",
                 "begin",
                 "x", ":=", "5", ";",
                 "if", "x", "<", "10", "then",
                 "y", ":=", "10", ";",
                 "end", "."
         );
         SemanticAnalyzer analyzer = new SemanticAnalyzer(tokens);
         analyzer.run();
     }
 }

