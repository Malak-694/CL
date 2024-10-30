import org.example.CommandLineInterpreter;
import java.io.*;
import java.util.*;

import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class pwdTest {
    @Test
    public void pwd(){
        CommandLineInterpreter termainal = new CommandLineInterpreter();
        String testpath = termainal.pwd();
        String currentpath = Main.curren_dir;
        assertTrue(testpath.equals(currentpath),"the current directory is :"+ Main.curren_dir);
    }
}
