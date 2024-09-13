package com.example.photocontest.external.services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ImageGenerator {
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/images/generations";
    private static final String IMAGE_SAVE_PATH = "src/main/resources/static/AI_category_generated_images/";

    public static String generateImageForCategory(String categoryName) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String prompt = "Generate an image, hyper-realistic and real photographic representing the theme " + categoryName;

        JSONObject json = new JSONObject();
        json.put("prompt", prompt);
        json.put("n", 1);
        json.put("size", "1024x1024");

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseData);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            if (!dataArray.isEmpty()) {
                String imageUrl = dataArray.getJSONObject(0).getString("url");
                return saveImage(imageUrl, categoryName);
            } else {
                throw new IOException("No image URL found in the response.");
            }
        }
    }

    private static String saveImage(String imageUrl, String categoryName) throws IOException {
        String fileName = categoryName.replaceAll("\\s+", "_") + ".jpg";
        File outputFile = new File(IMAGE_SAVE_PATH + fileName);

        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        }

        return outputFile.getPath();
    }
}