package org.junitapprovaltesting.files;

/**
 * The {@link UnapprovedJsonFile} is a special {@link JsonFile} that is stored in a toApprove subdirectory of the
 * build directory.
 */
public class UnapprovedJsonFile extends JsonFile {

    public UnapprovedJsonFile(String fileName) {
        super(TO_APPROVE_DIRECTORY + fileName + TO_APPROVE_FILE + JSON_ENDING);
    }
}
