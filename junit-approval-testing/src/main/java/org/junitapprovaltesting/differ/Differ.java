package org.junitapprovaltesting.differ;

import org.apache.commons.io.FilenameUtils;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.ApprovedFileNotFoundException;
import org.junitapprovaltesting.exceptions.DiffingFailedException;
import org.junitapprovaltesting.exceptions.UnapprovedFileNotFoundException;
import org.junitapprovaltesting.files.ApprovableFile;
import org.junitapprovaltesting.services.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Differ {

    private ApprovalTestingConfiguration config;
    private FileService fileService;

    public Differ(ApprovalTestingConfiguration config) {
        this.config = config;
        fileService = new FileService(config);
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
            throw new UnapprovedFileNotFoundException(baselineName);
        }
        ApprovableFile baseline;
        try {
            baseline = fileService
                    .getBaseline(FilenameUtils.getBaseName(unapprovedFile.getPath())
                                              .replace(config.getToApproveExtension(), ""));
        } catch (FileNotFoundException e) {
            throw new ApprovedFileNotFoundException(baselineName);
        }
        String diffTool = config.getDiffTool();
        String cmd = diffTool + " " + unapprovedFile.getPath() + " " + baseline.getPath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new DiffingFailedException("Diff tool " + diffTool + " not found!");
        }
    }
}
