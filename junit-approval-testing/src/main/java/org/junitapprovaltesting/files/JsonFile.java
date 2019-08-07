package org.junitapprovaltesting.files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * An extension of an {@link ApprovableFile} that provides functionality to read and write data of {@link JsonFile}s.
 */
public abstract class JsonFile extends ApprovableFile {

    static final String JSON_ENDING = ".json";

    public JsonFile(String path) {
        super(path);
    }

    /**
     * Stores a {@link JsonNode} in the {@link JsonFile}
     *
     * @param data the {@link JsonNode} that should be stored
     * @throws FileNotFoundException thrown if the file not exists
     */
    public void writeData(JsonNode data) throws FileNotFoundException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = new PrintWriter(this);
        out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
        out.close();
    }

    /**
     * Reads the data of the {@link JsonFile} and returns a {@link JsonNode}.
     *
     * @return the content of the {@link JsonFile} in a {@link JsonNode}
     */
    public JsonNode readData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(FileUtils.readFileToString(this, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error while reading Json file");
        }
    }
}
