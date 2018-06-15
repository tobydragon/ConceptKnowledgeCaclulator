package edu.ithaca.dragon.tecmap.data;

import java.io.IOException;
import java.nio.file.*;

public class FileCheck {

    /**
     * Creates a new filename in if the given one in path already exists
     * ADDITION TO FILENAME IS IN "-backup-#" FORM WHERE NUMBER IS THE NEXT AVAILABLE NUMBER
     * STARTING FROM 0, EVEN IF THERE ARE NUMBERS THAT EXIST ABOVE IT!
     * @param filename
     * @return new name if file exists, otherwise null
     */
    public static String getNewName(String filename) {
        int i = 0;
        String newFilename = null;
        Path path = Paths.get(filename);
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            while (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                int lastPeriod = filename.lastIndexOf(".");
                newFilename = filename.substring(0, lastPeriod) + "-backup-" + i + filename.substring(lastPeriod);
                path = Paths.get(newFilename);
                i++;
            }
            return newFilename;
        } else {
            return newFilename;
        }
    }

    public static void backup(String filename) throws IOException {
        String newFilename = getNewName(filename);
        if (newFilename != null) {
            Path originalPath = Paths.get(filename);
            Path newPath = Paths.get(newFilename);
            Files.copy(originalPath, newPath, StandardCopyOption.COPY_ATTRIBUTES);
        }
    }

}
