import org.example.CommandLineInterpreter;
import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RedirectTest {


    @BeforeEach
    void before() throws IOException {
        new File(Main.curren_dir,"testRd.txt").createNewFile();
        // Main.curren_dir = testDir.getAbsolutePath();
    }
    @AfterEach
    void after() throws IOException {

        File testRd = new File("testRd.txt");
        if(testRd.exists()){
            testRd.delete();
        }
    }

    @Test
    void appRedirectWithLsTest() throws IOException {
        CommandLineInterpreter ci = new CommandLineInterpreter();
        String command = "ls > testRd.txt";
        ci.redirect(command);
        File testfile = new File(Main.curren_dir,"testRd.txt");

        List<String> lines = new ArrayList<String>();
        if(testfile.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(testfile))){
                String line ;
                while((line = br.readLine())!=null){
                    lines.add(line);
                }
            }catch(IOException e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        String[] exepected = ci.ls("" , "");
        assertArrayEquals(lines.toArray(new String[0]),exepected);
    }

}