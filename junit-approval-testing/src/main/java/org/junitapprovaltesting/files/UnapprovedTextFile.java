package org.junitapprovaltesting.files;

/**
 * The {@link UnapprovedTextFile} is a special {@link TextFile} that is stored in a toApprove subdirectory of the
 * build directory.
 */
public class UnapprovedTextFile extends TextFile {

    public UnapprovedTextFile(String fileName) {
        super(TO_APPROVE_DIRECTORY + fileName + TO_APPROVE_FILE + TXT_ENDING);
    }
}
