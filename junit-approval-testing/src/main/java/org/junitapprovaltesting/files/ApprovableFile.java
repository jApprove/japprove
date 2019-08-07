package org.junitapprovaltesting.files;

import java.io.File;
import java.io.IOException;

/**
 * An abstract super class of files that are approved or can be approved.
 */
public abstract class ApprovableFile extends File {

    static final String TO_APPROVE_FILE = "_toApprove";
    static final String BASELINE_DIRECTORY = "baselines" + File.separator;
    static final String TO_APPROVE_DIRECTORY = "build" + File.separator + "approvals" + File.separator;

    public ApprovableFile(String pathname) {
        super(pathname);
    }

    /**
     * Checks if the File already exists and creates a new one if not.
     *
     * @throws IOException
     */
    public void create() throws IOException {
        if (!exists()) {
            File parent = getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new RuntimeException("Cannot create directory: " + parent);
            }
            createNewFile();
        }
    }
}
