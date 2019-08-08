package org.junitapprovaltesting.tools;

import org.apache.commons.io.FilenameUtils;
import org.junitapprovaltesting.files.ApprovableFile;
import org.junitapprovaltesting.services.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Differ {

    private static final String IDEA_DIFF =
            "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.1.3\\bin\\idea64 diff";
    private FileService fileService;

    public Differ() {
        fileService = new FileService();
    }

    /**
     * Computes the differences of two {@link ApprovableFile}s by the baselineName.
     *
     * @param baselineName the name of the baseline for which the differences should be computed
     */
    public void diff(String baselineName) {
        ApprovableFile unapprovedFile;
        try {
            unapprovedFile = fileService.getUnapprovedFile(baselineName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Found no unapproved version for passed file " + baselineName);
        }
        ApprovableFile baseline;
        try {
            baseline = fileService
                    .getBaseline(FilenameUtils.getBaseName(unapprovedFile.getPath()).replace("_toApprove", ""));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Found no approved version for passed file " + baselineName);
        }
        String cmd = IDEA_DIFF + " " + unapprovedFile.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Diff tool " + IDEA_DIFF + " not found!");
        }
    }
}
