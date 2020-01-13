package org.approvaltesting;

import java.util.ArrayList;
import java.util.List;

public class Formatter {
    public static List<String> formatStrings(List<String> names) {
        List<String> result = new ArrayList<>();
        for (String name : names) {
            if (!name.equals("")) {
                String temp2 = "";
                for (int i = 0; i < name.length(); i++) {
                    if (name.charAt(i) == ' ') {
                        continue;
                    } else {
                        temp2 = temp2 + name.charAt(i);
                    }
                }
                name = temp2;
            }
            if (!name.equals("")) {
                String temp = "";
                for (int i = 0; i < name.length(); i++)
                    temp = temp + ((i == 0) ? name.substring(i, i + 1).toUpperCase() :
                            (i != name.length() - 1) ? name.substring(i, i + 1).toLowerCase() : name.substring(i, i + 1).toLowerCase().toLowerCase() + " ");
                name = temp;
            }
            result.add(name);
        }
        return result;
    }
}
