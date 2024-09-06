package model.persistence;

import org.json.JSONObject;
import ui.gamedata.Handling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

// Represents a reader that reads a badminton match log from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file (copied from JsonSerializationDemo)
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: Reads a match log from a file and returns it;
    // throws IOException if an error occurs reading data from file (copied mostly from JsonSerializationDemo)
    public HashMap<String, Object> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONObject jsonScores = (JSONObject) jsonObject.get("scores");
        JSONObject jsonHandling = (JSONObject) jsonObject.get("handling");
        Handling handling = new Handling((Integer) jsonHandling.get("arr"), (Integer) jsonHandling.get("das"), (Integer) jsonHandling.get("sdf"));

        HashMap<String, Integer> scores = new HashMap<>();

        for (String s : jsonScores.keySet()) {
            scores.put(s, (Integer) jsonScores.get(s));
        }

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("scores", scores);
        ret.put("handling", handling);

        return ret;
    }


    // EFFECTS: reads source file as string and returns it (copied mostly from JsonSerializationDemo)
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

}