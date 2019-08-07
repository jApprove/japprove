package org.junitapprovaltesting.errors;

import java.io.FileNotFoundException;

/**
 * This Exception is thrown if a wanted approved file can not be found.
 */
public class ApprovedFileNotFoundException extends FileNotFoundException {

    public ApprovedFileNotFoundException(String message) {
        super(message);
    }
}
