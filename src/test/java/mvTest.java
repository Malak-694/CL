import java.io.IOException;

import org.example.CommandLineInterpreter;
import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class mvTest{
    private CommandLineInterpreter terminal;
    private File test_file;
    private File dest_dir;
    @BeforeEach
    public void setUp() {
        terminal = new CommandLineInterpreter();
        test_file = new File(Main.curren_dir, "test_file.txt");
        dest_dir = new File(Main.curren_dir, "dest_dir");

        try {
            if (!test_file.exists()) {
                test_file.createNewFile();
            }
            if (!dest_dir.exists()) {
                dest_dir.mkdir();
            }
        }
        catch (IOException e) {
            fail("setup failed: " + e.getMessage());
        }
    }

    @Test
    public void test_mv_file() {
        terminal.mv(test_file.getAbsolutePath(), dest_dir.getAbsolutePath());
        File mv_file = new File(dest_dir, test_file.getName());
        assertTrue(mv_file.exists(), "file should be moved to the destination directory.");
        assertFalse(test_file.exists(), "source file should no longer exist.");
    }

    @Test
    public void test_mv_overwrite() {
        File overwrite_file= new File(dest_dir, test_file.getName());
        try {
            if (!overwrite_file.exists()) {
                overwrite_file.createNewFile();
            }
        }
        catch (IOException e) {
            fail("Failed to set up existing file in destination directory: " + e.getMessage());
        }
        terminal.mv("-f", test_file.getAbsolutePath(), dest_dir.getAbsolutePath());
        assertTrue(overwrite_file.exists(), "file in the destination directory should exist.");
        assertFalse(test_file.exists(), "source file should no longer exist after moving.");
    }

    @AfterEach
    public void delete_file_and_dir() {
        if (test_file.exists()) {
            test_file.delete();
        }
        if (dest_dir.exists()) {
            File movedFile = new File(dest_dir, test_file.getName());
            if (movedFile.exists()) {
                movedFile.delete();
            }
            dest_dir.delete();
        }
    }
}
