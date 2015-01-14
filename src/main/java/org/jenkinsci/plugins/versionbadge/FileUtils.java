package org.jenkinsci.plugins.versionbadge;

import hudson.FilePath;

import java.io.*;

/**
 * Created by Marek on 2015-01-14.
 */
public class FileUtils {

    public static String readTextFile(FilePath filePath) throws IOException {
        return readTextFile(new File(filePath.getRemote()));
    }

    public static String readTextFile(String path) throws IOException {
        return readTextFile(new File(path));
    }

    public static String readTextFile(File file) throws IOException {
        return readTextFile(file, "UTF-8");
    }

    public static String readTextFile(File file, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        try {
            StringBuilder builder = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                builder.append(line);
                builder.append("\r\n");
            }
            return builder.toString();
        } finally {
            reader.close();
        }
    }

    public static void writeTextFile(File file, String content) throws IOException {
        writeTextFile(file, content, "UTF-8");
    }

    public static void writeTextFile(File file, String content, String charset) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
        try {
            writer.write(content);
        } finally {
            writer.close();
        }
    }
}
