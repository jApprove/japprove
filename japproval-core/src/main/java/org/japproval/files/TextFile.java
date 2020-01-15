package org.japproval.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.japproval.exceptions.FileCreationFailedException;

/**
 * An extension of an {@link File} that provides functionality to read and write data of {@link
 * TextFile}.
 */
public class TextFile extends File {

    public TextFile(String path) {
        super(path);
    }

    /**
     * Stores a String in the {@link TextFile}.
     *
     * @param data the String that should be stored
     * @throws FileNotFoundException thrown if the file not exists
     */
    public void writeData(String data) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(this);
        out.print(data);
        out.close();
    }

    /**
     * Reads the data of the {@link TextFile} line by line.
     *
     * @return the content of the {@link TextFile} in a list
     * @throws IOException thrown if the file cannot be created
     */
    public String readData() throws IOException {
        return Files.readString(this.toPath(), StandardCharsets.US_ASCII);
    }

    /**
     * Checks if the File already exists and creates a new one if not.
     *
     * @throws FileCreationFailedException thrown if the file cannot be created
     */
    public void create() throws FileCreationFailedException {
        if (!exists()) {
            File parent = getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new FileCreationFailedException(parent.getName());
            }
            try {
                createNewFile();
            } catch (IOException e) {
                throw new FileCreationFailedException(parent.getName());
            }
        }
    }
}
