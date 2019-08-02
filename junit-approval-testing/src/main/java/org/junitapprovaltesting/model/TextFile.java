package org.junitapprovaltesting.model;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

/**
 * An extension of a {@link File} that is used to store Strings or list of Strings. It is also possible to compare
 * such files and list differences.
 */
public class TextFile extends File {

    private static final long serialVersionUID = 3594041246590645171L;

    public TextFile(String path) {
        super(path);
    }

    /**
     * Stores a String in the TextFile
     *
     * @param data the String that should be stored
     * @throws FileNotFoundException thrown if the file not exists
     */
    public void writeData(String data) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(this);
        out.println(data);
        out.close();
    }

    /**
     * Stores a list of Strings in the TextFile
     *
     * @param data the list of Strings that should be stored
     * @throws FileNotFoundException thrown if the file not exists
     */
    public void writeData(List<String> data) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(this);
        for (String name : data) {
            out.println(name);
        }
        out.close();
    }

    /**
     * Checks if two TextFiles are equal
     *
     * @param other the file this file should be compared to
     * @return true if the files are equal, false otherwise
     * @throws IOException
     */
    public boolean equals(TextFile other) throws IOException {
        return FileUtils.contentEqualsIgnoreEOL(this, other, "utf-8");
    }

    /**
     * Checks if the TextFile already exists and creates a new one if not.
     *
     * @throws IOException
     */
    public void create() throws IOException {
        if (!this.exists()) {
            File parent = this.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new RuntimeException("Cannot create directory: " + parent);
            }
            this.createNewFile();
        }
    }

    /**
     * Computes the differences of this and another TextFile.
     *
     * @param other the file this file should be compared to
     * @return a list of the differences
     */
    public List<String> computeDifferences(TextFile other) {
        try {
            List<String> original = Files.readAllLines(other.toPath());
            List<String> revised = Files.readAllLines(this.toPath());
            Patch<String> patch = DiffUtils.diff(original, revised);
            return UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
        } catch (IOException | DiffException e) {
            throw new RuntimeException("Cannot compute differences! " + e);
        }
    }

}
