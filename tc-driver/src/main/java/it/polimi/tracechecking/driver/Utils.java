package it.polimi.tracechecking.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by filippo on 05/04/17.
 */
public class Utils {
    static List<String> searchFiles(File file, String pattern, List<String> result) throws FileNotFoundException {

        if (!file.isDirectory()) {
            throw new IllegalArgumentException("file has to be a directory");
        }

        if (result == null) {
            result = new ArrayList<String>();
        }

        File[] files = file.listFiles();

        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    searchFiles(currentFile, pattern, result);
                } else {
                    Scanner scanner = new Scanner(currentFile);
                    if (scanner.findWithinHorizon(pattern, 0) != null) {
                        result.add(currentFile.getName());
                    }
                    scanner.close();
                }
            }
        }
        return result;
    }
}
