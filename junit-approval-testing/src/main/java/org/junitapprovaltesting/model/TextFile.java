package org.junitapprovaltesting.model;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

public class TextFile extends File {

    private static final long serialVersionUID = 3594041246590645171L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TextFile.class);

    public TextFile(String path) {
        super(path);
    }

    public void writeData(String data) throws FileNotFoundException {
        LOGGER.info("Write Data into " + this.getPath());
        PrintWriter out = null;
        out = new PrintWriter(this);
        out.println(data);
        out.close();
    }

    public void writeData(List<String> data) throws FileNotFoundException {
        LOGGER.info("Write Data into " + this.getPath());
        PrintWriter out = null;
        out = new PrintWriter(this);
        for (String name : data) {
            out.println(name);
        }
        out.close();
    }

    public boolean equals(TextFile other) throws IOException {
        LOGGER.info("Compare " + this.getPath() + " to " + other.getPath());
        if (FileUtils.contentEqualsIgnoreEOL(this, other, "utf-8")) {
            LOGGER.info(this.getPath() + " is equal to " + other.getPath());
            return true;
        } else {
            LOGGER.info(this.getPath() + " is not equal to " + other.getPath());
            return false;
        }
    }

    public void create() throws IOException {
        if (!this.exists()) {
            File parent = this.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                LOGGER.info("Cannot create directory: " + parent);
                throw new RuntimeException("Cannot create directory: " + parent);
            }
            if (this.createNewFile()) {
                LOGGER.info("Creating file " + this);
            }
        } else {
            LOGGER.info("File " + this + " already exists! Use existing one!");
        }
    }

    public List<String> computeDifferences(TextFile other) {
        LOGGER.info("Compute differences of " + this.toPath() + " and " + other.toPath());
        List<String> original;
        List<String> revised;
        Patch<String> patch;
        try {
            original = Files.readAllLines(other.toPath());
            revised = Files.readAllLines(this.toPath());
            patch = DiffUtils.diff(original, revised);
        } catch (IOException | DiffException e) {
            throw new RuntimeException();
        }
        return UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
    }

}
