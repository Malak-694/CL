
import org.example.CommandLineInterpreter;
import java.io.*;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class touch {

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
    public void newFileTest(){
        CommandLineInterpreter terminal = new CommandLineInterpreter();
        String specification="";
        String path="testDir";
        File [] filesBefore = terminal.ls(specification,path);
        terminal.touch("testDir\\y.txt");
        File [] filesAfter = terminal.ls(specification,path);
        boolean newFileExists = new File("testDir\\y.txt").exists();
        assertTrue(filesAfter.length==filesBefore.length+1&&newFileExists,"the file has been created");


    }
    @Test
    public void existFileTest(){
        CommandLineInterpreter terminal = new CommandLineInterpreter();
        File file = new File("testDir\\file1.txt");
        long beforeTime = 0;
        if (file.exists()){
             beforeTime = file.lastModified();
        }
        String specification="";
        String path="testDir";
        File [] filesBefore = terminal.ls(specification,path);
        terminal.touch("testDir\\file1.txt");
        File [] filesAfter = terminal.ls(specification,path);
        long afterTime = file.lastModified();
        assertTrue(afterTime>beforeTime||filesBefore==filesAfter, " file already exist but modified recently ");
    }
}
