package org.junitapprovaltesting.model;

/**
 * A transfer object for the baseline candidates.
 */
public class BaselineCandidate {

    private String name;

    public BaselineCandidate(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the baseline candidate.
     *
     * @return the name of the baseline candidate.
     */
    public String getName() {
        return name;
    }
}
