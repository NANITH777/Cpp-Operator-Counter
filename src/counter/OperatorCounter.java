package counter;

import java.io.*;
import java.util.regex.*;

public class OperatorCounter {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.print("Please enter the full path of the C++ file: ");
            String filePath = reader.readLine();
            
            String content = readFile(filePath);
            content = removeComments(content);
            content = removeStrings(content);
            content = removeIncludeDirectives(content);
            
            int[] counters = countOperators(content);
            
            System.out.println("Single Operator Count: " + counters[0]);
            System.out.println("Binary Operator Count: " + counters[1]);
            System.out.println("Ternary Operator Count: " + counters[2]);
            
        } catch (IOException e) {
            System.out.println("File reading error: " + e.getMessage());
        }
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String removeComments(String content) {
        // Remove multi-line comments
        content = content.replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", "");
        // Remove single-line comments
        content = content.replaceAll("//.*", "");
        return content;
    }

    private static String removeStrings(String content) {
        // Remove string literals
        return content.replaceAll("\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\"", "");
    }

    private static String removeIncludeDirectives(String content) {
        // Remove #include directives and their < > parts
        return content.replaceAll("#include\\s*<[^>]*>", "");
    }

    private static int[] countOperators(String content) {
        int[] counters = new int[3]; // Counters for single, binary, and ternary operators
        
        // Single operators (++, --, !, ~, &, *, +, -)
        Pattern singlePattern = Pattern.compile("(?<![-+!~&*])\\+\\+|--(?![-+])|!(?![=])|~(?![=])|(?<![&])&(?![&=*])|\\*(?![*=])(?!\\s*[a-zA-Z0-9(])|(?<!\\d)[+-]\\d+");
        Matcher singleMatcher = singlePattern.matcher(content);
        while (singleMatcher.find()) {
            counters[0]++;
        }

        // Binary operators
        Pattern binaryPattern = Pattern.compile(
            "\\+=|-=|\\*=|/=|%=|==|!=|<=|>=|&&|\\|\\||<<|>>|&=|\\|=|\\^=|" + // Comparison and assignment operators
            "(?<![=!<>])=(?![=])|" + // Simple assignment operator (=)
            "(?<![+])\\+(?![+=\\d])|" + // Arithmetic operator (+), not followed by a digit
            "(?<![-])\\-(?![-=\\d])|" + // Arithmetic operator (-), not followed by a digit
            "(?<![\\w\\*])\\*(?![=\\w])|" + // Arithmetic operator (*), excluding pointers
            "/(?!=)|" +               // Arithmetic operator (/)
            "%(?!=)|" +               // Arithmetic operator (%)
            "(?<!<)>(?![>=])|" +      // Comparison operator (>)
            "<(?![<=])"               // Comparison operator (<)
        );
        Matcher binaryMatcher = binaryPattern.matcher(content);
        while (binaryMatcher.find()) {
            counters[1]++;
        }

        // Ternary operator
        Pattern ternaryPattern = Pattern.compile("\\?.*?:");
        Matcher ternaryMatcher = ternaryPattern.matcher(content);
        while (ternaryMatcher.find()) {
            counters[2]++;
        }

        return counters;
    }
}
