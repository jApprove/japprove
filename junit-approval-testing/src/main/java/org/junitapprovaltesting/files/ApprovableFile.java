package org.junitapprovaltesting.files;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.junitapprovaltesting.exceptions.DiffingFailedException;
import org.junitapprovaltesting.exceptions.FileCreationFailedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract super class of files that are approved or can be approved.
 */
public class ApprovableFile extends File {

    public ApprovableFile(String pathname) {
        super(pathname);
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
     * Computes the differences of this and another {@link ApprovableFile}.
     *
     * @param other the file this file should be compared to
     * @return a list of the differences
     */
    public List<String> computeDifferences(ApprovableFile other) {
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
