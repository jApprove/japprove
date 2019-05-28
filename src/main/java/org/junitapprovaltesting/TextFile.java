package org.junitapprovaltesting;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextFile extends File {
    private static final long serialVersionUID = 3594041246590645171L;

    public TextFile(String pathname) {
        super(pathname);
    }

    public void writeData(List<String> data) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(this);
        for (String name : data) {
            out.println(name);
        }
        out.close();
    }

    public boolean equals(TextFile other) throws IOException {
        return FileUtils.contentEqualsIgnoreEOL(this, other, "utf-8");
    }

    public void cleanUp() throws FileNotFoundException {
        if (this.delete()) {
            System.out.println("Deleted TextFile " + this.getPath());
        } else {
            throw new FileNotFoundException(this.getPath());
        }
    }
}
