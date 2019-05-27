package org.junitapprovaltesting;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 * The {@link Approver} is able to approve data by comparing the data with a baseline.
 */
public class Approver {

    private static final String KDIFF = "C:\\Program Files (x86)\\KDiff3\\kdiff3";
    private static final String BASELINE = "baseline.txt";
    private static final String TO_APPROVE = "toApprove.txt";

    /**
     * Approve a list of strings by comparing the data with a baseline.
     *
     * @param data a list of strings
     */
    public void approve(List<String> data) {

        File baseline = new File(BASELINE);
        try {
            if (baseline.createNewFile()) {
                System.out.println("Created baseline.txt!");
            } else {
                System.out.println("Use existing baseline!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File toApprove = new File(TO_APPROVE);
        try {
            if (toApprove.createNewFile()) {
                System.out.println("Created toApprove.txt!");
            } else {
                System.out.println("toApprove.txt already exists... Use existing one!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(TO_APPROVE);
            for (String name : data) {
                out.println(name);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

        String cmd = KDIFF + " " + TO_APPROVE + " " + BASELINE;
        System.out.println("Executing command: " + cmd);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Approve? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        scanner.close();

        try {
            out = new PrintWriter(BASELINE);
            if (input.equals("y")) {
                for (String name : data) {
                    out.println(name);
                }
            } else {
                Assertions.fail("Not approved");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (toApprove.delete()) {
                System.out.println("Deleted toApprove.txt!");
            } else {
                System.out.println("toApprove.txt does not exist!");
            }
            out.close();
        }
    }
}
