package org.junitapprovaltesting.sorter;

import java.util.Collections;
import java.util.List;

public class StringSorter {

    public List<String> sort(List<String> names) {
        Collections.sort(names);
        return names;
    }
}
