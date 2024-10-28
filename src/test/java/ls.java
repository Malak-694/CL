import org.example.CommandLineInterpreter;
import java.io.*;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class ls {
    @BeforeEach
    public void before() throws IOException{
        File testDir = new File("testDir");
        if(!testDir.exists()){
            testDir.mkdir();
        }
        new File(testDir, "file1.txt").createNewFile();
        new File(testDir, "zile2.txt").createNewFile();
        new File(testDir, ".file3.txt").createNewFile();
    }
    @AfterEach
    public void clear(){
        File testDir = new File("testDir");
        File[] files = testDir.listFiles();
        if(files!=null) {
            for (File file : files) {
                file.delete();
            }
            testDir.delete();
        }
    }
    @Test
    public void lcTest(){

        CommandLineInterpreter terminal = new CommandLineInterpreter();
        String specification="";
        String path="testDir";
        String[] file = terminal.ls(specification,path);
        assertTrue(file.length==2,"this command should return the list of files in the directory");
    }
    @Test
    public void lc_aTest(){

        CommandLineInterpreter terminal = new CommandLineInterpreter();
        String specification="-a";
        String path="testDir";
        String[] files = terminal.ls(specification,path);
        boolean foundWithDot = Arrays.stream(files).anyMatch(file->file.equals(".file3.txt"));
        assertTrue(foundWithDot,"this command should return the list of all files in the directory even if it starts with .");
    }
    @Test
    public void lc_rTest(){

        CommandLineInterpreter terminal = new CommandLineInterpreter();
        String specification="-r";
        String path="testDir";
        String[] files = terminal.ls(specification,path);
        boolean foundZfirst = files[0].equals("zile2.txt");

        assertTrue(foundZfirst,"this command should return the list of all files in the directory even if it starts with .");
    }


}
