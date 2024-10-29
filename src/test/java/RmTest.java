import org.example.CommandLineInterpreter;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RmTest {
    //delete file
    //delete more files
    //delete direction
    //delete nonexistent file
    private CommandLineInterpreter terminal;

    @BeforeEach
    public void before() throws IOException {
        terminal = new CommandLineInterpreter();
        File testDir = new File("testDir");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        new File(testDir, "file1.txt").createNewFile();
        new File(testDir, "file2.txt").createNewFile();

    }

    @AfterEach
    public void clear() {
        File testDir = new File("testDir");
        File[] files = testDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        testDir.delete();
    }

    @Test
    public void deleteingFileTest() {
        File file = new File("testDir/file1.txt");
        assertTrue(file.exists(), "File exist before deletion");

        terminal.rm("testDir/file1.txt");
        assertFalse(file.exists(), "File deleted");
    }
    @Test
    public void M_FilesDeletionTest() throws IOException {
        File file1 = new File("testDir/file1.txt");
        File file2 = new File("testDir/file2.txt");

        assertTrue(file1.exists(), "file1 exist before deletion");
        assertTrue(file2.exists(), "file2 exist before deletion");

        terminal.rm("testDir/file1.txt", "testDir/file2.txt");
        assertFalse(file1.exists(), "file1 deleted");
        assertFalse(file2.exists(), "file2  deleted");
    }
    @Test
    public void directorydeletionTest() {
        File dir = new File("testDir");
        assertTrue(dir.exists() && dir.isDirectory(), "Directory should exist");

        terminal.rm("testDir");
        assertTrue(dir.exists(), "Directory should not be deleted by rm");
    }
    @Test
    public void deleteNonExistentFileTest() {

        File nonExistentFile = new File("testDir/nonexistent.txt");
        assertFalse(nonExistentFile.exists(), "Non-existent file should not exist");

        terminal.rm("testDir/nonexistent.txt");
    }


}


