package com.github.euonmyoji.yysplayer.util;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinyangshi
 */
public class SongData {
    private final long id;
    private final long seconds;
    private final String from;
    private final String name;
    private final List<String> artists = new ArrayList<>();

    public SongData(JsonObject json, String from) {
        id = json.get("id").getAsLong();
        seconds = json.get("duration").getAsLong() / 1000;
        name = json.get("name").getAsString();
        json.getAsJsonArray("artists").forEach(jsonElement -> artists.add(jsonElement.getAsJsonObject().get("name").getAsString()));
        this.from = from;
    }

    public long getId() {
        return this.id;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getArtists() {
        return new ArrayList<>(this.artists);
    }

    public String getFrom() {
        return this.from;
    }
}
