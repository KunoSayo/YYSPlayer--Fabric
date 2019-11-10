package com.github.euonmyoji.yysplayer.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author yinyangshi
 */
class ShowLyricsTask {
    private volatile boolean running = true;
    private ShowLyricsRepeatTask repeatTask;

    ShowLyricsTask(long id, AudioSource source) {
        new Thread(() -> {
            try {
                Iterator<String> list = get(id).iterator();
                while (running && list.hasNext()) {
                    String raw = list.next();
                    try {
                        String timeStr = raw.substring(1, 9);
                        String lyrics = raw.substring(10);
                        if (lyrics.substring(0, 1).matches("[0-9]") && lyrics.substring(1, 2).startsWith("]")) {
                            lyrics = lyrics.substring(2);
                        }

                        if (lyrics.startsWith("]")) {
                            lyrics = lyrics.substring(1);
                        }
                        if (timeStr.matches("[0-9][0-9]:[0-9][0-9]\\.[0-9][0-9]")) {
                            long timeToShow = Long.parseLong(timeStr.substring(0, 2)) * 60 + Long.parseLong(timeStr.substring(3, 5));
                            while (running && timeToShow > source.getPlayedSeconds()) {
                                try {
                                    Thread.sleep(249);
                                } catch (InterruptedException ignore) {
                                }
                            }
                            if (!running) {
                                if (repeatTask != null) {
                                    repeatTask.running = false;
                                }
                                return;
                            }
                            if (SongPlayer.showLyrics) {
                                if (repeatTask != null) {
                                    repeatTask.running = false;
                                }
                                MinecraftClient.getInstance().inGameHud.setOverlayMessage("§l" + lyrics, true);
                                repeatTask = new ShowLyricsRepeatTask("§l" + lyrics);
                            } else {
                                if (repeatTask != null) {
                                    repeatTask.running = false;
                                }
                                MinecraftClient.getInstance().inGameHud.setOverlayMessage("", true);
                            }
                        }
                    } catch (IndexOutOfBoundsException ignore) {
                    }
                }
                if (repeatTask != null) {
                    repeatTask.running = false;
                }
                MinecraftClient.getInstance().inGameHud.setOverlayMessage("", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static List<String> get(long id) throws IOException {
        String urlB = "http://music.163.com/api/song/lyric";
        String p = String.format("id=%s&lv=-1", id);
        URL u = new URL(urlB);
        URLConnection c = u.openConnection();
        c.setRequestProperty("User-Agent", "");
        c.setDoOutput(true);
        try (OutputStreamWriter o = new OutputStreamWriter(c.getOutputStream(), StandardCharsets.UTF_8)) {
            o.write(p);
            o.flush();
        }
        InputStreamReader reader = new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8);
        JsonObject j = new JsonParser().parse(reader).getAsJsonObject().getAsJsonObject("lrc");
        return j == null ? Collections.emptyList() :
                Arrays.asList(j.get("lyric").getAsString().split("\n"));
    }

    void cancel() {
        running = false;
        if (repeatTask != null) {
            repeatTask.running = false;
        }
    }
}
