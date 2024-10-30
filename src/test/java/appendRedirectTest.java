import org.example.CommandLineInterpreter;
import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class appendRedirectTest {


    @BeforeEach
    void before() throws IOException {
        File testDir = new File("test1Dir");
        if(!testDir.exists()){
            testDir.mkdir();
        }
        new File(testDir, "file1.txt").createNewFile();
        new File(testDir, "zip.txt").createNewFile();
        new File(testDir, "file3.txt").createNewFile();
        new File(Main.curren_dir,"testRd.txt").createNewFile();
        // Main.curren_dir = testDir.getAbsolutePath();
    }
    @AfterEach
    void after() throws IOException {
        File testDir = new File("test1Dir");
        File[] files = testDir.listFiles();
        if(files!=null) {
            for (File file : files) {
                file.delete();
            }
            testDir.delete();
        }
        File testRd = new File("testRd.txt");
        if(testRd.exists()){
            testRd.delete();
        }
    }

    @Test
    void appRedirectWithLsTest() throws IOException {
        CommandLineInterpreter ci = new CommandLineInterpreter();
        String command = "ls >> testRd.txt";
        ci.appendRedirect(command);
        File testfile = new File(Main.curren_dir,"testRd.txt");
        assertTrue(testfile.exists(), "Redirect file testRd.txt was not created.");

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