package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileCheckTest {

    @Test
    public void getNewName() {
        //Test a file that doesn't exist
        String originalFileName = Settings.DEFAULT_TEST_DATASTORE_PATH + "originalTestFile";
        assertNull(FileCheck.getNewName(originalFileName));

        //Test a file that exists
        String needsNewName = Settings.DEFAULT_TEST_DATASTORE_PATH + "test-example-file.txt";
        assertEquals(Settings.DEFAULT_TEST_DATASTORE_PATH + "test-example-file-backup-0.txt", FileCheck.getNewName(needsNewName));
    }

    @Test
    public void backup() throws IOException {
        //Can't test a file that doesn't need to be backed up

        //Test a file that exists and needs to be backed up
        String needsToBeBackedup = Settings.DEFAULT_TEST_DATASTORE_PATH + "test-example-file.txt";
        FileCheck.backup(needsToBeBackedup);

        Path pathOfBackup = Paths.get(Settings.DEFAULT_TEST_DATASTORE_PATH + "test-example-file-backup-0.txt");

        assertTrue(Files.deleteIfExists(pathOfBackup));
    }

}
