package model.persistence;

import org.json.JSONObject;
import ui.gamedata.Handling;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

// Represents a writer that writes JSON representation of a match log to file (almost all code directly copied from
// JsonSerializationDemo)
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of match log to file
    public void write(HashMap<String, Integer> scores, Handling handling) {
        JSONObject outerObject = new JSONObject();
        JSONObject jsonScores = new JSONObject();
        JSONObject jsonHandling = new JSONObject();

        for (String s : scores.keySet()) {
            jsonScores.put(s, scores.get(s));
        }

        jsonHandling.put("arr", handling.autoRepeatRate());
        jsonHandling.put("das", handling.delayedAutoShift());
        jsonHandling.put("sdf", handling.softDropFactor());

        outerObject.put("scores", jsonScores);
        outerObject.put("handling", jsonHandling);

        saveToFile(outerObject.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
