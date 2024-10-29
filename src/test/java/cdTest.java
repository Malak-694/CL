import org.example.CommandLineInterpreter;
import org.example.Main;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class cdTest {

    @Test
    void chang_directory(){
        CommandLineInterpreter ci = new CommandLineInterpreter();
        String dirctory = System.getProperty("user.dir");
        int i = dirctory.lastIndexOf("\\");
        String exepected_dir = dirctory.substring(0,i);
        try {
            ci.cd(dirctory , "..");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(exepected_dir, Main.curren_dir);
    }
    @Test
    void chang_dir_with_path() throws IOException {
        CommandLineInterpreter ci = new CommandLineInterpreter();
        String dirctory = System.getProperty("user.dir");
        String path = "\\src\\main";
        String expected = dirctory + path;
        ci.cd(dirctory,"src/main");
        assertEquals(expected,Main.curren_dir);
    }
}
