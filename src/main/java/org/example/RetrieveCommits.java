package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RetrieveCommits {




    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONArray(jsonText);
        }
    }


    public static void main(String[] args) throws IOException, JSONException {

        Scanner input =new Scanner(System.in);

        System.out.println("Inserire il proprietario del repository da analizzare");
        String owner = input.next();

        System.out.println("Inserire il nome del repository da analizzare");
        String repository = input.next();

        System.out.println("Inserire la stringa da cercare nei messaggi dei commit");
        String word = input.next();

        input.close();

        int i = 0, total;
        String url = "https://api.github.com/repos/"+owner+"/"+repository+"/commits";
        JSONArray commits = readJsonArrayFromUrl(url);
        total=commits.length();
        for (; i < total; i++) {
            //Iterate through each commit
            JSONObject commit= commits.getJSONObject(i).getJSONObject("commit");
            String message = commit.get("message").toString();
            //check if commit i-th contains the word "added"
            if (message.contains(word)){
                String id = commits.getJSONObject(i).get("sha").toString();
                JSONObject committer = commit.getJSONObject("author");
                String author =committer.get("name").toString();
                String date =committer.get("date").toString();
                System.out.println("Id del commit: "+id);
                System.out.println("Autore: " +author+"; data commit: "+date);
                System.out.println("Messaggio: "+ message);
                System.out.println();
            }

        }
    }


}
