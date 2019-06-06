package org.junitapprovaltesting.model;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextFile extends File {

    private static final long serialVersionUID = 3594041246590645171L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TextFile.class);

    public TextFile(String path) {
        super(path);
    }

    public void writeData(List<String> data) throws FileNotFoundException {
        LOGGER.info("Write Data into " + this.getPath());
        PrintWriter out = new PrintWriter(this);
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

}
