package testutils;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testutils.JsonFileReader.readFile;

public class RequestGenerator {
    /**
     * @param mockMvc       mock of the controller
     * @param controllerURL url of the controller
     * @param jsonDirectory path starting at src/test/resources. Expects a request.json and a response.json in the directory.
     * @throws Exception multiple possible exceptions
     */
    public static void testValidPostRequest(MockMvc mockMvc, String controllerURL, String jsonDirectory) throws Exception {
        mockMvc.perform(
                post(controllerURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(readFile(jsonDirectory + "/request.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(jsonDirectory + "/response.json")));
    }

    /**
     * @param mockMvc       mock of the controller
     * @param controllerURL url of the controller
     * @param jsonRequest   path starting at src/test/resources to a json file
     * @throws Exception multiple possible exceptions
     */
    public static void testValidPostRequestOk(MockMvc mockMvc, String controllerURL, String jsonRequest) throws Exception {
        mockMvc.perform(
                post(controllerURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(readFile(jsonRequest)))
                .andExpect(status().isOk());
    }

    /**
     * @param mockMvc       mock of the controller
     * @param controllerURL url of the controller
     * @param jsonResponse  path starting at src/test/resources to a json file
     * @throws Exception multiple possible exceptions
     */
    public static void testValidGetRequest(MockMvc mockMvc, String controllerURL, String jsonResponse) throws Exception {
        mockMvc.perform(get(controllerURL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(readFile(jsonResponse)));
    }

    /**
     * @param mockMvc        mock of the controller
     * @param controllerURL  url of the controller
     * @param expectedStatus expected {@link HttpStatus} for example 404 not found.
     * @throws Exception multiple possible exceptions
     */
    public static void testInvalidGetRequest(MockMvc mockMvc, String controllerURL, HttpStatus expectedStatus) throws Exception {
        mockMvc.perform(get(controllerURL))
                .andExpect(status().is(expectedStatus.value()));
    }

    /**
     * @param mockMvc           mock of the controller
     * @param controllerURL     url of the controller
     * @param jsonDirectory     path starting at src/test/resources. Expects a request.json and a response.json in the directory.
     * @param ignoredAttributes comma separated strings as parameters, read in as stream. These attributes are ignored in the test.
     * @throws Exception multiple possible exceptions
     */
    public static void testValidRequest(MockMvc mockMvc, String controllerURL, String jsonDirectory, String... ignoredAttributes) throws Exception {
        mockMvc.perform(
                post(controllerURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(readFile(jsonDirectory + "/request.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String actual = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8).replaceAll("\\P{Print}", "");
                    String expected = readFile(jsonDirectory + "/request.json").replaceAll("\\P{Print}", "");

                    JSONAssert.assertEquals("", actual, expected, new DefaultComparator(JSONCompareMode.LENIENT) {
                        @Override
                        public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
                            if (Arrays.stream(ignoredAttributes).anyMatch(prefix::contains)) {
                                assertNotNull(actualValue);
                            } else {
                                super.compareValues(prefix, expectedValue, actualValue, result);
                            }
                        }

                        @Override
                        protected boolean areNotSameDoubles(Object expectedValue, Object actualValue) {
                            double expected = ((Number) expectedValue).doubleValue();
                            double actual = ((Number) actualValue).doubleValue();
                            // Compare up to 4 decimal places
                            return Math.round(actual * 10000) != Math.round(expected * 10000);
                        }
                    });
                });
    }
}
