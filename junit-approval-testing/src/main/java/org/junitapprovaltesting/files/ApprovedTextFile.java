package org.junitapprovaltesting.files;

/**
 * The {@link ApprovedTextFile} is a special {@link TextFile} that is stored in a baseline directory.
 */
public class ApprovedTextFile extends TextFile {

    public ApprovedTextFile(String fileName) {
        super(BASELINE_DIRECTORY + fileName + TXT_ENDING);
    }
}
