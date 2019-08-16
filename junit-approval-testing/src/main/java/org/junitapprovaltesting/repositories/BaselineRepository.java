package org.junitapprovaltesting.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.FileCreationFailedException;
import org.junitapprovaltesting.files.JsonFile;
import org.junitapprovaltesting.files.TextFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A central administration of the files.
 */
public class BaselineRepository {

    private String baselineDirectory;
    private String baselineCandidateDirectory;

    public BaselineRepository(ApprovalTestingConfiguration config) {
        baselineDirectory = config.getBaselineDirectory();
        baselineCandidateDirectory = config.getBaselineCandidateDirectory();
    }

    /**
     * Creates a new {@link TextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link TextFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link TextFile}
     */
    public TextFile createBaselineCandidate(String data, String baselineName) {
        TextFile baselineCandidate = new TextFile(baselineCandidateDirectory + baselineName);
        try {
            baselineCandidate.create();
            baselineCandidate.writeData(data);
        } catch (IOException e) {
            throw new FileCreationFailedException(baselineCandidate.getName());
        }
        return baselineCandidate;
    }

    /**
     * Creates a new {@link JsonFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link JsonFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link JsonFile}
     */
    public JsonFile createBaselineCandidate(JsonNode data, String baselineName) {
        JsonFile baselineCandidate = new JsonFile(baselineCandidateDirectory + baselineName);
        try {
            baselineCandidate.create();
            baselineCandidate.writeData(data);
        } catch (IOException e) {
            throw new FileCreationFailedException(baselineCandidate.getName());
        }
        return baselineCandidate;
    }

    /**
     * Removes an {@link TextFile} by its name
     *
     * @param filename the name of the file that should be removed
     * @return true if the {@link TextFile} has successfully been removed, false otherwise.
     */
    public boolean removeBaselineCandidate(String filename) {
        try {
            TextFile baselineCandidate = getBaselineCandidate(filename);
            return baselineCandidate.delete();
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns an {@link TextFile} by its name
     *
     * @param filename the filename
     * @return the {@link TextFile} by its name
     * @throws FileNotFoundException if no {@link TextFile} has been found
     */
    public TextFile getBaselineCandidate(String filename) throws FileNotFoundException {
        File directory = new File(baselineCandidateDirectory);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(baselineCandidateDirectory + filename)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException(filename);
    }

    /**
     * Returns a list of all {@link TextFile}s
     *
     * @return a list of all {@link TextFile}s
     */
    public List<TextFile> getBaselineCandidates() {
        File directory = new File(baselineCandidateDirectory);
        List<TextFile> files = new ArrayList<>();
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                files.add(new TextFile(file.getPath()));
            }
        }
        return files;
    }

    /**
     * Returns a {@link TextFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link TextFile} if exists
     * @throws FileNotFoundException if the {@link TextFile} not exists
     */
    public TextFile getTextBaseline(String baselineName) throws FileNotFoundException {
        TextFile baseline = new TextFile(baselineDirectory + baselineName);
        if (!baseline.exists()) {
            throw new FileNotFoundException("Baseline does not exist");
        }
        return baseline;
    }

    /**
     * Returns a {@link JsonFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link JsonFile} if exists
     * @throws FileNotFoundException if the {@link JsonFile} not exists
     */
    public JsonFile getJsonBaseline(String baselineName) throws FileNotFoundException {
        JsonFile baseline = new JsonFile(baselineDirectory + baselineName);
        if (!baseline.exists()) {
            throw new FileNotFoundException("Baseline does not exist");
        }
        return baseline;
    }

    /**
     * Returns an {@link TextFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link TextFile} if exists
     * @throws FileNotFoundException if the {@link TextFile}not exists
     */
    public TextFile getBaseline(String baselineName) throws FileNotFoundException {
        File directory = new File(baselineDirectory);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(baselineDirectory + baselineName)) {
                    return new TextFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException("Found no approved version for passed file " + baselineName);
    }

    /**
     * Creates a new {@link TextFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the created {@link TextFile}
     */
    public TextFile createBaseline(String baselineName) {
        TextFile baseline = new TextFile(baselineDirectory + baselineName);
        try {
            baseline.create();
        } catch (IOException e) {
            throw new FileCreationFailedException(baseline.getName());
        }
        return baseline;
    }

}
