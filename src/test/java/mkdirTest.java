import java.io.IOException;
import java.util.*;

import org.example.CommandLineInterpreter;
import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class mkdirTest {
    @Test
    public void mkdir(){
        CommandLineInterpreter termainal = new CommandLineInterpreter();
        String testpath = termainal.mkdir("direc1");
        boolean found =false;
        try {
            termainal.cd(Main.curren_dir,"direc1");
            found=true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //String currentpath = Main.curren_dir;
        assertTrue(found,"the current directory is :"+ Main.curren_dir);
    }
}
