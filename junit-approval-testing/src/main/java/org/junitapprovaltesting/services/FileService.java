package org.junitapprovaltesting.services;

import com.fasterxml.jackson.databind.JsonNode;
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

    private static final String BASELINE_DIRECTORY = "baselines" + File.separator;
    private static final String TO_APPROVE_FILE = "_toApprove";
    private static final String TXT_ENDING = ".txt";
    private static final String TO_APPROVE_DIRECTORY = "build" + File.separator + "approvals" + File.separator;

    /**
     * Creates a new {@link TextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link TextFile}
     * @param baselineName The name of the current baseline
     * @return the created {@link TextFile}
     */
    public TextFile createUnapprovedFile(String data, String baselineName) {
        TextFile toApprove = new TextFile(TO_APPROVE_DIRECTORY + baselineName + TO_APPROVE_FILE + TXT_ENDING);
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
        TextFile toApprove = new TextFile(TO_APPROVE_DIRECTORY + baselineName + TO_APPROVE_FILE + TXT_ENDING);
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
        JsonFile toApprove = new JsonFile(TO_APPROVE_DIRECTORY + baselineName + TO_APPROVE_FILE + TXT_ENDING);
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
        ApprovableFile unapprovedFile =
                new ApprovableFile(TO_APPROVE_DIRECTORY + filename + TO_APPROVE_FILE + TXT_ENDING);
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
        File directory = new File(TO_APPROVE_DIRECTORY);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(TO_APPROVE_DIRECTORY + filename + TO_APPROVE_FILE + TXT_ENDING)) {
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
        File directory = new File(TO_APPROVE_DIRECTORY);
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
        TextFile baseline = new TextFile(BASELINE_DIRECTORY + baselineName + TXT_ENDING);
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
        JsonFile baseline = new JsonFile(BASELINE_DIRECTORY + baselineName + TXT_ENDING);
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
        File directory = new File(BASELINE_DIRECTORY);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.getPath().equals(BASELINE_DIRECTORY + baselineName + TXT_ENDING)) {
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
        ApprovableFile baseline = new ApprovableFile(BASELINE_DIRECTORY + baselineName + TXT_ENDING);
        try {
            baseline.create();
        } catch (IOException e) {
            throw new FileCreationFailedException(baseline.getName());
        }
        return baseline;
    }
}
