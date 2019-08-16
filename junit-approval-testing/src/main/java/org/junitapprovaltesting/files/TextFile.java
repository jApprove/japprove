package org.junitapprovaltesting.files;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.junitapprovaltesting.exceptions.DiffingFailedException;
import org.junitapprovaltesting.exceptions.FileCreationFailedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of an {@link File} that provides functionality to read and write data of {@link TextFile}.
 */
public class TextFile extends File {

    public TextFile(String path) {
        super(path);
    }

    /**
     * Stores a String in the {@link TextFile}
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
     * @throws IOException
     */
    public void create() throws IOException {
        if (!exists()) {
            File parent = getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new FileCreationFailedException(parent.getName());
            }
            createNewFile();
        }
    }

    /**
     * Computes the differences of this and another {@link TextFile}.
     *
     * @param other the file this file should be compared to
     * @return a list of the differences
     */
    public List<String> computeDifferences(TextFile other) {
        try {
            List<String> original = readAllLines(other.getPath());
            List<String> revised = readAllLines(getPath());
            Patch<String> patch = DiffUtils.diff(original, revised);
            return UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
        } catch (DiffException | IOException e) {
            throw new DiffingFailedException("Cannot compute differences! " + e);
        }
    }

    private List<String> readAllLines(String fileName) throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        reader.close();
        return result;
    }

}
