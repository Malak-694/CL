import org.example.CommandLineInterpreter;
import java.io.*;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class pipeTest {

    @BeforeEach
    public void befor() throws IOException {
        FileWriter test = new FileWriter("test.txt" , true);
        test.write("test3\n");
        test.write("test4\n");
        test.write("test4\n");
        test.write("test3\n");
        test.write("test1\n");
        test.write("test2\n");
        test.write("test1\n");
        test.write("test2\n");
        test.close();
    }

    @AfterEach
    public void after() throws IOException {
        File afterTest = new File("test.txt");
        if (afterTest.exists()) {
            afterTest.delete();
        }
    }
    @Test
    public void sortUniqueArray(){
        CommandLineInterpreter termainal = new CommandLineInterpreter();
        String[] result = termainal.pipe("sort test.txt | uniq");
        String[] expectedArray = {"test1"  ,"test2" ,"test3","test4" };
        assertArrayEquals(result , expectedArray);
    }
}
