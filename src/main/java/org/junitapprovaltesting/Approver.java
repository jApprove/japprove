package org.junitapprovaltesting;

import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * The {@link Approver} is able to approve data by comparing the data with a baseline.
 */
public class Approver {

    /**
     * Approve a list of strings by comparing the data with a baseline.
     *
     * @param data a list of strings
     */
    public void approve(List<String> data) {
        Assertions.fail("Not approved");
        /*
        TODO:
        1. compare the list of data to a baseline
        2. highlight the differences
        3. in the case of approval: write this list in the baseline
        4. otherwise: fail the test
         */
    }
}
