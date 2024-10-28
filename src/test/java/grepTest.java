import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class grepTest {

    @Test
    void grep_with_11(){
        CommandLineInterpreter terminal = new CommandLineInterpreter();
        String[] array_test = {"file1.txt" ,"file2.txt" ,"file11.txt","file5.txt" };
        String[] result = terminal.grep(array_test,"11");
        String[] expected_array = {"file11.txt"};
        assertArrayEquals(result,expected_array);
    }
}
