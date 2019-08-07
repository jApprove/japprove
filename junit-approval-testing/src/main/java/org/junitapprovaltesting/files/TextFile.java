package org.junitapprovaltesting.files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of an {@link ApprovableFile} that provides functionality to read and write data of {@link TextFile}.
 */
public abstract class TextFile extends ApprovableFile {

    static final String TXT_ENDING = ".txt";

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
        out.println(data);
        out.close();
    }

    /**
     * Stores a list of Strings in the {@link TextFile}
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
     * Reads the data of the {@link TextFile} line by line.
     *
     * @return the content of the {@link TextFile} in a list
     * @throws IOException
     */
    public List<String> readData() throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(getPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        reader.close();
        return result;
    }

}
