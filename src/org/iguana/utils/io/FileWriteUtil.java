package org.iguana.utils.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileWriteUtil {

    public static void writeFile(String content, String path) throws IOException {
        try (Writer out = new FileWriter(path)) {
            out.write(content);
        }
    }

}
