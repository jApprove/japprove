package org.junitapprovaltesting.util;

import org.junitapprovaltesting.model.TextFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static TextFile getFile(String pathToDirectory, String filename) {
        File directory = new File(pathToDirectory);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().startsWith(pathToDirectory + filename)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        return null;
    }

    public static void copyFile(TextFile toApprove, TextFile baseline) throws IOException {
        FileInputStream inputStream = new FileInputStream(toApprove);
        FileOutputStream outputStream = new FileOutputStream(baseline);
        inputStream.getChannel().transferTo(0, toApprove.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
    }
}
