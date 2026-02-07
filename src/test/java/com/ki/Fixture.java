package com.ki;

import java.io.File;
import java.io.IOException;

public class Fixture {

    public static String getPath(String filename) {
        String selfPath = null;
        try {
            selfPath = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selfPath + "/src/test/fixtures/" + filename;
    }

}
