package org.junitapprovaltesting.services;

import org.junitapprovaltesting.errors.ApprovedFileNotFoundException;
import org.junitapprovaltesting.files.ApprovedTextFile;
import org.junitapprovaltesting.files.TextFile;
import org.junitapprovaltesting.files.UnapprovedTextFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * A central service that provides features to create and remove {@link TextFile}s.
 */
public class TextFileService {

    /**
     * Creates a new {@link UnapprovedTextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link UnapprovedTextFile}
     * @param baselineName The name of the current baseline
     * @return the {@link UnapprovedTextFile}
     */
    public UnapprovedTextFile createUnapprovedTextFile(String data, String baselineName) {
        UnapprovedTextFile toApprove = new UnapprovedTextFile(baselineName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        }
        return toApprove;
    }

    /**
     * Creates a new {@link UnapprovedTextFile} and writes the data into this file.
     *
     * @param data         The data that should be stored in the {@link UnapprovedTextFile}
     * @param baselineName The name of the current baseline
     * @return the {@link UnapprovedTextFile}
     */
    public UnapprovedTextFile createUnapprovedTextFile(List<String> data, String baselineName) {
        UnapprovedTextFile toApprove = new UnapprovedTextFile(baselineName);
        try {
            toApprove.create();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + toApprove);
        }
        try {
            toApprove.writeData(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + toApprove + " not found.");
        }
        return toApprove;
    }

    /**
     * Returns an {@link ApprovedTextFile} for a baseline name.
     *
     * @param baselineName the name of the baseline
     * @return the {@link ApprovedTextFile} if exists
     * @throws ApprovedFileNotFoundException if the {@link ApprovedTextFile} not exists
     */
    public ApprovedTextFile getApprovedTextFile(String baselineName) throws ApprovedFileNotFoundException {
        ApprovedTextFile baseline = new ApprovedTextFile(baselineName);
        if (!baseline.exists()) {
            throw new ApprovedFileNotFoundException("Baseline does not exist");
        }
        return baseline;
    }

    /**
     * Removes an {@link UnapprovedTextFile} by its name
     *
     * @param name the name of the file that should be removed
     * @return true if the {@link UnapprovedTextFile} has successfully been removed, false otherwise.
     */
    public boolean removeUnapprovedTextFile(String name) {
        UnapprovedTextFile toApprove = new UnapprovedTextFile(name);
        return toApprove.delete();
    }

}
