import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class uniqTest
{
    @Test
    void uniq_array(){
        CommandLineInterpreter termainal = new CommandLineInterpreter();
        String[] testArray = {"text.txt" , "text.txt","text1.txt","text1.txt"};
        String[] result = termainal.uniq(testArray) ;
        String[] expectedArray = {"text.txt"  ,"text1.txt" };
        assertArrayEquals(result , expectedArray);
    }

}

