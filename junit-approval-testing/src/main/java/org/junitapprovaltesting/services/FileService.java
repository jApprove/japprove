package org.junitapprovaltesting.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FilenameUtils;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.ApprovedFileNotFoundException;
import org.junitapprovaltesting.exceptions.FileCreationFailedException;
import org.junitapprovaltesting.files.ApprovableFile;
import org.junitapprovaltesting.files.JsonFile;
import org.junitapprovaltesting.files.TextFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    private String baselineDirectory;
    private String toApproveDirectory;

    public FileService(ApprovalTestingConfiguration config) {
        baselineDirectory = config.getBaselineDirectory();
        toApproveDirectory = config.getToApproveDirectory();
    }

    /**
     * Creates a new {@link TextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link TextFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link TextFile}
     */
    public TextFile createUnapprovedFile(String data, String baselineName) {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        TextFile toApprove = new TextFile(toApproveDirectory + baselineName);
        try {
            toApprove.create();
            toApprove.writeData(data);
        } catch (IOException e) {
            throw new FileCreationFailedException(toApprove.getName());
        }
        return toApprove;
    }

    /**
     * Creates a new {@link TextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link TextFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link TextFile}
     */
    public TextFile createUnapprovedFile(List<String> data, String baselineName) {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        TextFile toApprove = new TextFile(toApproveDirectory + baselineName);
        try {
            toApprove.create();
            toApprove.writeData(data);
        } catch (IOException e) {
            throw new FileCreationFailedException(toApprove.getName());
        }
        return toApprove;
    }

    /**
     * Creates a new {@link JsonFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link JsonFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link JsonFile}
     */
    public JsonFile createUnapprovedFile(JsonNode data, String baselineName) {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        JsonFile toApprove = new JsonFile(toApproveDirectory + baselineName);
        try {
            toApprove.create();
            toApprove.writeData(data);
        } catch (IOException e) {
            throw new FileCreationFailedException(toApprove.getName());
        }
        return toApprove;
    }

    /**
     * Removes an {@link ApprovableFile} by its name
     *
     * @param filename the name of the file that should be removed
     * @return true if the {@link ApprovableFile} has successfully been removed, false otherwise.
     */
    public boolean removeUnapprovedFile(String filename) {
        if (FilenameUtils.getExtension(filename).isEmpty()) {
            filename += ".txt";
        }
        ApprovableFile unapprovedFile = new ApprovableFile(toApproveDirectory + filename);
        return unapprovedFile.delete();
    }

    /**
     * Returns an {@link ApprovableFile} by its name
     *
     * @param filename the filename
     * @return the {@link ApprovableFile} by its name
     * @throws FileNotFoundException if no {@link ApprovableFile} has been found
     */
    public ApprovableFile getUnapprovedFile(String filename) throws FileNotFoundException {
        if (FilenameUtils.getExtension(filename).isEmpty()) {
            filename += ".txt";
        }
        File directory = new File(toApproveDirectory);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(toApproveDirectory + filename)) {
                    return new ApprovableFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException(filename);
    }

    /**
     * Returns a list of all {@link ApprovableFile}s
     *
     * @return a list of all {@link ApprovableFile}s
     */
    public List<ApprovableFile> getUnapprovedFiles() {
        File directory = new File(toApproveDirectory);
        List<ApprovableFile> files = new ArrayList<>();
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                files.add(new ApprovableFile(file.getPath()));
            }
        }
        return files;
    }

    /**
     * Returns a {@link TextFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link TextFile} if exists
     * @throws ApprovedFileNotFoundException if the {@link TextFile} not exists
     */
    public TextFile getTextBaseline(String baselineName) throws FileNotFoundException {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
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
     * @throws ApprovedFileNotFoundException if the {@link JsonFile} not exists
     */
    public JsonFile getJsonBaseline(String baselineName) throws FileNotFoundException {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        JsonFile baseline = new JsonFile(baselineDirectory + baselineName);
        if (!baseline.exists()) {
            throw new FileNotFoundException("Baseline does not exist");
        }
        return baseline;
    }

    /**
     * Returns an {@link ApprovableFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link ApprovableFile} if exists
     * @throws ApprovedFileNotFoundException if the {@link ApprovableFile}not exists
     */
    public ApprovableFile getBaseline(String baselineName) throws FileNotFoundException {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        File directory = new File(baselineDirectory);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(baselineDirectory + baselineName)) {
                    return new ApprovableFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException("Found no approved version for passed file " + baselineName);
    }

    /**
     * Creates a new {@link ApprovableFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the created {@link ApprovableFile}
     */
    public ApprovableFile createBaseline(String baselineName) {
        if (FilenameUtils.getExtension(baselineName).isEmpty()) {
            baselineName += ".txt";
        }
        ApprovableFile baseline = new ApprovableFile(baselineDirectory + baselineName);
        try {
            baseline.create();
        } catch (IOException e) {
            throw new FileCreationFailedException(baseline.getName());
        }
        return baseline;
    }

}
