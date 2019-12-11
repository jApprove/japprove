package org.approvaltesting;

import java.util.ArrayList;
import java.util.List;

public class Formatter {
    public static List<String> formatStrings(List<String> names) {
        List<String> result = new ArrayList<>();
        for (String name : names) {
            name = name.toLowerCase();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            name = name.replaceAll(" ","");
            result.add(name);
        }
        return result;
    }
}
