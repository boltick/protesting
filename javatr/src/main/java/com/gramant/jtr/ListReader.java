/*
 * ListReader
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public class ListReader {
    public static List getSuitesFiles(String suitesListFileName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(suitesListFileName));
        String line;
        List <String> files = new ArrayList();
        while ((line = fileReader.readLine()) != null)
            if (!line.startsWith("#") && !line.trim().equals(""))
                files.add(line);
        return files;
    }
}
