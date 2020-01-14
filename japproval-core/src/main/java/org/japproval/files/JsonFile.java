package org.japproval.files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.japproval.exceptions.FileCreationFailedException;

/**
 * {@code JsonFile} is a wrapper around TextFile that provides functionality to read and write data
 * of {@code JsonFile}s.
 */
public class JsonFile {

    private TextFile textFile;

    public JsonFile(String path) {
        this.textFile = new TextFile(path);
    }

    /**
     * Stores a {@link JsonNode} in the {@link JsonFile}.
     *
     * @param data the {@link JsonNode} that should be stored
     * @throws FileNotFoundException   thrown if the file not exists
     * @throws JsonProcessingException thrown if an error with the json object occurs
     */
    public void writeData(JsonNode data) throws FileNotFoundException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.textFile.writeData(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
    }

    /**
     * Reads the data of the {@link JsonFile} and returns a {@link JsonNode}.
     *
     * @return the content of the {@link JsonFile} in a {@link JsonNode}
     */
    public JsonNode readData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(this.textFile.readData());
        } catch (IOException e) {
            throw new RuntimeException("Error while reading Json file");
        }
    }

    /**
     * Checks if the File already exists and creates a new one if not.
     *
     * @throws IOException thrown if the file cannot be created
     */
    public void create() throws FileCreationFailedException {
        this.textFile.create();
    }

    /**
     * Returns the name of the corresponding {@link TextFile}.
     *
     * @return the name of the corresponding {@link TextFile}.
     */
    public String getName() {
        return this.textFile.getName();
    }

    /**
     * Returns true if this file exists.
     *
     * @return true if this file exists.
     */
    public boolean exists() {
        return this.textFile.exists();
    }
}
