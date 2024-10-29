import org.example.CommandLineInterpreter;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RmdirTest {
    //remove empty file
    //remove nonempty
    //remove nonexistent directory
    //remove multiple directory
    //remove nondirectory
    private CommandLineInterpreter terminal;

    @BeforeEach
    public void before() throws IOException {
        terminal = new CommandLineInterpreter();

        File testDir = new File("testDir");
        if (!testDir.exists()) {
            testDir.mkdir();
        }

        File emptyDir = new File("testDir/emptyDir");
        File emptyDir1 = new File("testDir/emptyDir1 ");
        File emptyDir2 = new File("testDir/emptyDir2");
        emptyDir1.mkdir();
        emptyDir2.mkdir();
        emptyDir.mkdir();

        File nonEmptyDir = new File("testDir/nonEmptyDir");
        nonEmptyDir.mkdir();
        new File(nonEmptyDir, "dummyFile.txt").createNewFile(); //to make nonempty

        new File("testDir/file1.txt").createNewFile();
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
    public void deleteingDirTest() {
        File emptyDir = new File("testDir/emptyDir");
        assertTrue(emptyDir.exists() && emptyDir.isDirectory(), "emptyDir exist before deletion");
        terminal.rmdir("testDir/emptyDir");
        assertFalse(emptyDir.exists(), "emptyDir deleted");
    }
    @Test
    public void RemoveNonEmptyDirectory() {
        File nonEmptyDir = new File("testDir/nonEmptyDir");
        assertTrue(nonEmptyDir.exists() && nonEmptyDir.isDirectory(), "nonEmptyDir exist before deleting");
        terminal.rmdir("testDir/nonEmptyDir");
        assertTrue(nonEmptyDir.exists(), "nonEmptyDir not  deleted as it contains files");
    }

    @Test
    public void RemoveNonExistentDirectory() {
        File nonExistentDir = new File("testDir/nonexistentDir");
        assertFalse(nonExistentDir.exists(), "nonexistentDir should not exist");
        terminal.rmdir("testDir/nonexistentDir");
        assertFalse(nonExistentDir.exists(), "nonexistent dir");
    }
    @Test
    public void M_DirDeletionTest() {
        File emptyDir1 = new File("testDir/emptyDir1 ");
        File emptyDir2 = new File("testDir/emptyDir2");

        assertTrue(emptyDir1.exists() && emptyDir1.isDirectory(), "emptyDir1 exist before deletion");
        assertTrue(emptyDir2.exists() && emptyDir2.isDirectory(), "emptyDir2 exist before deletion");

        terminal.rmdir("testDir/emptyDir1 ");
        terminal.rmdir("testDir/emptyDir2");

        assertFalse(emptyDir1.exists(), "emptyDir1 deleted");
        assertFalse(emptyDir2.exists(), "emptyDir2 deleted");
    }
    @Test
    public void filedeletionTest() {
        File file1 = new File("testDir/file1.txt");
        assertTrue(file1.exists() && file1.isFile(), "file1 exist as a file before deletion ");
        terminal.rmdir("testDir/file1.txt");
        assertTrue(file1.exists(), "file1 not deleted as it's not a directory");
    }
}

