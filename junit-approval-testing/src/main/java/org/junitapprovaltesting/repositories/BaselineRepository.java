package org.junitapprovaltesting.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import org.junitapprovaltesting.exceptions.*;
import org.junitapprovaltesting.files.JsonFile;
import org.junitapprovaltesting.files.TextFile;
import org.junitapprovaltesting.model.BaselineCandidate;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A central administration point for the baselines and the baseline candidates.
 */
public interface BaselineRepository {
    /**
     * Creates a new {@link TextFile} and writes the data into this file.
     *
     * @param data The data that should be stored in the {@link TextFile}
     * @param name The name of the current baseline
     * @throws BaselineCandidateCreationFailedException if the baseline candidate cannot be created
     */
    void createBaselineCandidate(String data, String name) throws BaselineCandidateCreationFailedException;

    /**
     * Creates a new {@link JsonFile} and writes the data into this file.
     *
     * @param data The data that should be stored in the {@link JsonFile}
     * @param name The name of the current baseline
     * @throws BaselineCandidateCreationFailedException if the baseline candidate cannot be created
     */
    void createBaselineCandidate(JsonNode data, String name) throws BaselineCandidateCreationFailedException;

    /**
     * Removes an baseline candidate by its name.
     *
     * @param name the name of the baseline candidate that should be removed
     * @return true if the baseline candidate has successfully been removed, false otherwise.
     */
    boolean removeBaselineCandidate(String name);

    /**
     * Returns a list of all {@link BaselineCandidate}s.
     *
     * @return a list of all {@link BaselineCandidate}s
     */
    List<BaselineCandidate> getBaselineCandidates();

    /**
     * Returns the content of the requested baseline.
     *
     * @param baseline the baseline for which the content should be returned
     * @return the content of the baseline
     * @throws BaselineNotFoundException if the baseline cannot be found
     */
    JsonNode getContentOfJsonBaseline(String baseline) throws BaselineNotFoundException;

    /**
     * Returns the content of the requested baseline.
     *
     * @param baseline the baseline for which the content should be returned
     * @return the content of the baseline
     * @throws BaselineNotFoundException if the baseline cannot be found
     */
    String getContentOfTextBaseline(String baseline) throws BaselineNotFoundException;

    /**
     * Copies the content of the baseline candidate to the baseline.
     *
     * @param baselineCandidateName the name of the baseline candidate
     * @throws BaselineCandidateNotFoundException if the candidate of the baseline cannot be found
     * @throws BaselineCreationFailedException    if the baseline cannot be created
     * @throws CopyingFailedException             in the case the content of the baseline candidate cannot be copied
     *                                            to the baseline
     */
    void copyBaselineCandidateToBaseline(String baselineCandidateName)
            throws BaselineCandidateNotFoundException, BaselineCreationFailedException, CopyingFailedException;

    /**
     * Computes differences of the baseline candidate and the baseline.
     *
     * @param baselineCandidate the baseline candidate
     * @return a string with the differences
     * @throws BaselineCandidateNotFoundException if the candidate of the baseline cannot be found
     * @throws BaselineNotFoundException          if the baseline cannot be found
     */
    String getDifferences(BaselineCandidate baselineCandidate)
            throws BaselineCandidateNotFoundException, BaselineNotFoundException;

    /**
     * Returns true if the baseline exists and false otherwise.
     *
     * @param baselineCandidate the baseline candidate for which the baseline should be found.
     * @return true if the baseline exists and false otherwise.
     */
    boolean baselineExists(BaselineCandidate baselineCandidate);

    /**
     * Returns the content of the baseline candidate in a file.
     *
     * @param baselineCandidateName the name of the baseline candidate.
     * @return the content of the baseline candidate in a file.
     * @throws IOException in the case of problems with the created file.
     */
    File getBaselineCandidateAsFile(String baselineCandidateName) throws IOException;

    /**
     * Returns the content of the baseline in a file.
     *
     * @param baseline the name of the baseline.
     * @return the content of the baseline in a file.
     * @throws IOException in the case of problems with the created file.
     */
    File getBaselineAsFile(String baseline) throws IOException;
}
