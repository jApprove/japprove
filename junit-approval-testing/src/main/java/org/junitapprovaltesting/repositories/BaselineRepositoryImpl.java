package org.junitapprovaltesting.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import org.junitapprovaltesting.config.ApprovalTestingConfiguration;
import org.junitapprovaltesting.exceptions.*;
import org.junitapprovaltesting.files.JsonFile;
import org.junitapprovaltesting.files.TextFile;
import org.junitapprovaltesting.model.BaselineCandidate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the {@link BaselineRepository} with a text file based approach.
 */
public class BaselineRepositoryImpl implements BaselineRepository {

    private final String TXT_EXTENSION = ".txt";
    private String baselineDirectory;
    private String baselineCandidateDirectory;

    public BaselineRepositoryImpl(ApprovalTestingConfiguration config) {
        baselineDirectory = config.getBaselineDirectory();
        baselineCandidateDirectory = config.getBaselineCandidateDirectory();
    }

    @Override
    public void createBaselineCandidate(String data, String name) throws BaselineCandidateCreationFailedException {
        TextFile baselineCandidate = new TextFile(baselineCandidateDirectory + name + TXT_EXTENSION);
        try {
            baselineCandidate.create();
            baselineCandidate.writeData(data);
        } catch (IOException | FileCreationFailedException e) {
            throw new BaselineCandidateCreationFailedException(baselineCandidate.getName());
        }
    }

    @Override
    public void createBaselineCandidate(JsonNode data, String name) throws BaselineCandidateCreationFailedException {
        JsonFile baselineCandidate = new JsonFile(baselineCandidateDirectory + name + TXT_EXTENSION);
        try {
            baselineCandidate.create();
            baselineCandidate.writeData(data);
        } catch (FileCreationFailedException | FileNotFoundException | JsonProcessingException e) {
            throw new BaselineCandidateCreationFailedException(baselineCandidate.getName());
        }
    }

    @Override
    public boolean removeBaselineCandidate(String name) {
        try {
            return getFile(name, baselineCandidateDirectory).delete();
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    public List<BaselineCandidate> getBaselineCandidates() {
        File directory = new File(baselineCandidateDirectory);
        List<BaselineCandidate> baselineCandidates = new ArrayList<>();
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                baselineCandidates.add(new BaselineCandidate(file.getName().replace(TXT_EXTENSION, "")));
            }
        }
        return baselineCandidates;
    }

    @Override
    public JsonNode getContentOfJsonBaseline(String baseline) throws BaselineNotFoundException {
        return this.getJsonBaseline(baseline).readData();
    }

    @Override
    public String getContentOfTextBaseline(String baseline) throws BaselineNotFoundException {
        try {
            return this.getTextBaseline(baseline).readData();
        } catch (IOException e) {
            throw new BaselineNotFoundException(baseline);
        }
    }

    @Override
    public void copyBaselineCandidateToBaseline(String baselineCandidateName)
            throws BaselineCandidateNotFoundException, BaselineCreationFailedException, CopyingFailedException {
        TextFile baselineCandidate;
        try {
            baselineCandidate = getFile(baselineCandidateName, baselineCandidateDirectory);
        } catch (FileNotFoundException e) {
            throw new BaselineCandidateNotFoundException(baselineCandidateName);
        }
        TextFile baseline;
        try {
            baseline = getFile(baselineCandidateName, baselineDirectory);
        } catch (FileNotFoundException e) {
            try {
                baseline = createBaseline(baselineCandidateName + TXT_EXTENSION);
            } catch (BaselineCreationFailedException ex) {
                throw new BaselineCreationFailedException(baselineCandidateName);
            }
        }
        try {
            copyFiles(baselineCandidate, baseline);
        } catch (IOException e) {
            throw new CopyingFailedException("Cannot copy content of " + baselineCandidateName + " to the baseline");
        }
    }

    @Override
    public String getDifferences(BaselineCandidate baselineCandidate)
            throws BaselineCandidateNotFoundException, BaselineNotFoundException {
        TextFile baselineCandidateFile;
        try {
            baselineCandidateFile = getFile(baselineCandidate.getName(), baselineCandidateDirectory);
        } catch (FileNotFoundException e) {
            throw new BaselineCandidateNotFoundException(baselineCandidate.getName());
        }
        TextFile baseline;
        try {
            baseline = getFile(baselineCandidate.getName(), baselineDirectory);
        } catch (FileNotFoundException e) {
            throw new BaselineNotFoundException(baselineCandidate.getName());
        }
        return formatDifferences(computeDifferences(baselineCandidateFile, baseline));
    }

    @Override
    public boolean baselineExists(BaselineCandidate baselineCandidate) {
        try {
            getFile(baselineCandidate.getName(), baselineDirectory);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public File getBaselineCandidateAsFile(String name) throws IOException {
        return getFile(name, baselineCandidateDirectory);
    }

    @Override
    public File getBaselineAsFile(String name) throws IOException {
        return getFile(name, baselineDirectory);
    }

    private List<String> computeDifferences(TextFile revisedFile, TextFile originalFile) {
        try {
            List<String> original = readFileLineByLine(originalFile.getPath());
            List<String> revised = readFileLineByLine(revisedFile.getPath());
            Patch<String> patch = DiffUtils.diff(original, revised);
            return UnifiedDiffUtils.generateUnifiedDiff("Baseline", "toApprove", original, patch, 0);
        } catch (DiffException | IOException e) {
            throw new DiffingFailedException("Cannot compute differences! " + e);
        }
    }

    private List<String> readFileLineByLine(String fileName) throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            result.add(line);
        }
        reader.close();
        return result;
    }

    private String formatDifferences(List<String> differences) {
        StringBuilder builder = new StringBuilder();
        for (String difference : differences) {
            builder.append(difference);
            builder.append("\n");
        }
        return builder.toString();
    }

    private TextFile getTextBaseline(String baselineName) throws BaselineNotFoundException {
        TextFile baseline = new TextFile(baselineDirectory + baselineName + TXT_EXTENSION);
        if (!baseline.exists()) {
            throw new BaselineNotFoundException(baselineName);
        }
        return baseline;
    }

    private JsonFile getJsonBaseline(String baselineName) throws BaselineNotFoundException {
        JsonFile baseline = new JsonFile(baselineDirectory + baselineName + TXT_EXTENSION);
        if (!baseline.exists()) {
            throw new BaselineNotFoundException(baselineName);
        }
        return baseline;
    }

    private TextFile createBaseline(String baselineName) throws BaselineCreationFailedException {
        TextFile baseline = new TextFile(baselineDirectory + baselineName);
        try {
            baseline.create();
        } catch (FileCreationFailedException e) {
            throw new BaselineCreationFailedException(baseline.getName());
        }
        return baseline;
    }

    private TextFile getFile(String baselineCandidateName, String directoryPath) throws FileNotFoundException {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if ((file.getPath().equals(directoryPath + baselineCandidateName)) ||
                        (file.getPath().equals(directoryPath + baselineCandidateName + TXT_EXTENSION))) {
                    return new TextFile(file.getPath());
                }
            }
        }
        throw new FileNotFoundException(baselineCandidateName);
    }

    private void copyFiles(TextFile from, TextFile to) throws IOException {
        FileInputStream inputStream = new FileInputStream(from);
        FileOutputStream outputStream = new FileOutputStream(to);
        inputStream.getChannel().transferTo(0, from.length(), outputStream.getChannel());
        inputStream.close();
        outputStream.close();
    }
}
