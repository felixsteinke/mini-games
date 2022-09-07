package testutils;

import org.apache.logging.log4j.core.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonFileReader {
    /**
     * Reads a file from resources
     *
     * @param file path starting at src/test/resources
     * @return content (json) as String
     * @throws IOException file not found
     */
    public static String readFile(String file) throws IOException {
        InputStream stream = JsonFileReader.class.getClassLoader().getResourceAsStream(file);
        assert stream != null;
        return IOUtils.toString(new InputStreamReader(stream));
    }
}
