package org.junitapprovaltesting.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junitapprovaltesting.errors.ApprovedFileNotFoundException;
import org.junitapprovaltesting.files.ApprovedJsonFile;
import org.junitapprovaltesting.files.JsonFile;
import org.junitapprovaltesting.files.UnapprovedJsonFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A central service that provides features to create and remove {@link JsonFile}s.
 */
public class JsonFileService {

    /**
     * Creates a new {@link UnapprovedJsonFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link UnapprovedJsonFile}
     * @param baselineName The name of the current baseline
     * @return the {@link UnapprovedJsonFile}
     */
    public UnapprovedJsonFile createUnapprovedTextFile(JsonNode data, String baselineName) {
        UnapprovedJsonFile toApprove = new UnapprovedJsonFile(baselineName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException | JsonProcessingException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        }
        return toApprove;
    }

    /**
     * Returns an {@link ApprovedJsonFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link ApprovedJsonFile} if exists
     * @throws ApprovedFileNotFoundException if the {@link ApprovedJsonFile} not exists
     */
    public ApprovedJsonFile getApprovedJsonFile(String baselineName) throws ApprovedFileNotFoundException {
        ApprovedJsonFile baseline = new ApprovedJsonFile(baselineName);
        if (!baseline.exists()) {
            throw new ApprovedFileNotFoundException("Baseline does not exist");
        }
        return baseline;
    }

    /**
     * Removes an {@link UnapprovedJsonFile} by its name
     *
     * @param name the name of the file that should be removed
     * @return true if the {@link UnapprovedJsonFile} has successfully been removed, false otherwise.
     */
    public boolean removeUnapprovedJsonFile(String name) {
        UnapprovedJsonFile toApprove = new UnapprovedJsonFile(name);
        return toApprove.delete();
    }

}
