package org.japprove.exceptions;

/**
 * This Exception is thrown if a file can not be created.
 */
public class FileCreationFailedException extends Exception {

    public FileCreationFailedException(String filename) {
        super(filename + " cannot be created");
    }
}
