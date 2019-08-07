package org.junitapprovaltesting.files;

/**
 * The {@link ApprovedJsonFile} is a special {@link JsonFile} that is stored in a baseline directory.
 */
public class ApprovedJsonFile extends JsonFile {

    public ApprovedJsonFile(String fileName) {
        super(BASELINE_DIRECTORY + fileName + JSON_ENDING);
    }
}
