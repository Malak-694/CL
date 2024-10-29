import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatTest {
    private CommandLineInterpreter terminal;
    private Path testDir;

    @BeforeEach
    public void before() throws IOException {
        terminal = new CommandLineInterpreter();
        testDir = Files.createTempDirectory("testDir");
    }

    @AfterEach
    public void clear() throws IOException {
        Files.list(testDir).forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Files.delete(testDir);
    }

    @Test
    public void catNOfileTest() {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream("Hello\nWorld\ndone\n".getBytes()));

        List<String> result = terminal.cat();

        assertEquals(2, result.size());
        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));

        System.setIn(stdin);
    }

    @Test
    public void catFileTestt() throws IOException {
        File tempFile = new File(testDir.toFile(), "testFile.txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("Hello from file");
        }

        List<String> result = terminal.cat(tempFile.getAbsolutePath());

        assertEquals(1, result.size());
        assertEquals("Hello from file", result.get(0));
    }

    @Test
    public void CatNonExistingFileTest() {
        List<String> result = terminal.cat("non_existing_file.txt");
        assertTrue(result.isEmpty(), "Expected an empty list for non-existing file");
    }

    @Test
    public void CatWithM_FilesTest() throws IOException {
        File file1 = new File(testDir.toFile(), "file1.txt");
        try (PrintWriter writer = new PrintWriter(file1)) {
            writer.println("Content of file 1");
        }

        File file2 = new File(testDir.toFile(), "file2.txt");
        try (PrintWriter writer = new PrintWriter(file2)) {
            writer.println("Content of file 2");
        }

        File file3 = new File(testDir.toFile(), "file3.txt");
        try (PrintWriter writer = new PrintWriter(file3)) {
            writer.println("Content of file 3");
        }

        List<String> result = terminal.cat(file1.getAbsolutePath(), file2.getAbsolutePath(), file3.getAbsolutePath());
        assertEquals(3, result.size());
        assertEquals("Content of file 1", result.get(0));
        assertEquals("Content of file 2", result.get(1));
        assertEquals("Content of file 3", result.get(2));
    }
}
