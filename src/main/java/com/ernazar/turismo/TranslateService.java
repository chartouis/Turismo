package com.ernazar.turismo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateService {

    private static final String API_KEY = "b1g53llh2duvnmejo9r6";  // Replace with your Yandex API key
    private static final String DETECT_URL = "https://translate.yandex.net/api/v1.5/tr.json/detect";
    private static final String TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Function to detect language
    public static String detectLanguage(String text) throws IOException {
        String query = "key=" + API_KEY + "&text=" + encodeURIComponent(text);
        String response = sendRequest(DETECT_URL, query);

        // Parse the JSON response to get the detected language
        JsonNode jsonResponse = objectMapper.readTree(response);
        return jsonResponse.get("lang").asText();
    }

    // Function to translate text to English
    public static String translateToEnglish(String text, String sourceLang) throws IOException {
        if ("en".equals(sourceLang)) {
            return text;  // If the text is already in English, no need to translate
        }

        String query = "key=" + API_KEY + "&text=" + encodeURIComponent(text) + "&lang=" + sourceLang + "-en";
        String response = sendRequest(TRANSLATE_URL, query);

        // Parse the JSON response to get the translated text
        JsonNode jsonResponse = objectMapper.readTree(response);
        return jsonResponse.get("text").get(0).asText();
    }

    // Helper function to send an HTTP request
    private static String sendRequest(String url, String query) throws IOException {
        RequestBody body = RequestBody.create(query, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    // Helper function to URL-encode text
    private static String encodeURIComponent(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    // Example usage
    public static void main(String[] args) {
        try {
            String userInput = "Hola mundo";  // Spanish example
            String detectedLanguage = detectLanguage(userInput);
            System.out.println("Detected language: " + detectedLanguage);

            String translatedText = translateToEnglish(userInput, detectedLanguage);
            System.out.println("Translated text: " + translatedText);  // Output: "Hello world"
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
