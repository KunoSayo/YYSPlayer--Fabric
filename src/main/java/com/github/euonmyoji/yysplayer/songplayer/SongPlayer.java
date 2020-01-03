package com.github.euonmyoji.yysplayer.songplayer;

import com.github.euonmyoji.yysplayer.util.SongData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AlUtil;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinyangshi
 */
public class SongPlayer {
    private static final Object LOCK = new Object();
    static volatile boolean showLyrics = true;
    private static SongData curData;
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 1,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "yysplayer - songPlayer thread"));
    private static ConcurrentHashMap<String, AudioSource> audios = new ConcurrentHashMap<>();
    private static ShowLyricsTask showLyricsTask;

    private static float curGain = 0.5f;
    private static float curPitch = 1f;

    /**
     * load audio from stream and will close the stream when it's done
     *
     * @param name the audio name to play
     * @param in   the stream
     * @return the task for the future (source)
     */
    public static AudioSource load(String name, InputStream in) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(in)) {
            grabber.setCloseInputStream(true);
            grabber.start();
            AudioSource source = new AudioSource(grabber);
            AudioSource old = audios.put(name, source);
            if (old != null) {
                old.cleanup();
            }
            return source;
        }
    }

    public static void play(String name) {
        AudioSource source = audios.get(name);
        if (source != null) {
            source.play();
        }
    }

    public static void playSong(SongData data, URL u, long id) {
        executor.execute(() -> {
            synchronized (LOCK) {
                try {
                    curData = data;
                    URLConnection con = u.openConnection();
                    con.setRequestProperty("User-Agent", "");
                    AudioSource source = load("bgm", new BufferedInputStream(con.getInputStream(), 8192));
                    source.setGain(curGain).setPitch(curPitch);
                    source.play();
                    if (showLyricsTask != null) {
                        showLyricsTask.cancel();
                    }
                    if (showLyrics) {
                        showLyricsTask = new ShowLyricsTask(id, source);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void sendInfo() {
        if (curData != null) {
            MinecraftClient.getInstance().player.sendMessage(Texts.toText(() -> String.format("s-歌名:§6%s§r, artist:%s,\n-- 长度(秒):%s, id:%s, 点歌者:§3%s",
                    curData.getName(), "[§6" + curData.getArtists().stream().reduce((s, s2) -> s + "§4,§6" + s2).orElse("") + "§r]",
                    curData.getSeconds(), curData.getId(), curData.getFrom())));
            MinecraftClient.getInstance().player.sendMessage(new TranslatableText("message.songInfo.seekInfo", getPlayedSeconds(), curData.getSeconds()));
        }
        MinecraftClient.getInstance().player.sendMessage(new TranslatableText("message.songInfo.gainInfo", curGain, 1));
        MinecraftClient.getInstance().player.sendMessage(new TranslatableText("message.songInfo.pitchInfo", curPitch, 2));
    }

    public static float getPlayedSeconds() {
        AudioSource source = audios.get("bgm");
        if (source != null) {
            return source.getPlayedSeconds();
        }
        return 0;
    }

    public static void clearUp() {
        audios.values().forEach(AudioSource::cleanup);
        audios.clear();
    }

    public static void seek(float second) {
        AudioSource source = audios.get("bgm");
        if (source != null) {
            source.seek(second);
        }
    }

    public static void setPitch(float pitch) {
        AudioSource source = audios.get("bgm");
        curPitch = pitch;
        if (source != null) {
            source.setPitch(pitch);
        }
    }

    public static void setGain(float gain) {
        AudioSource source = audios.get("bgm");
        curGain = gain;
        if (source != null) {
            source.setGain(gain);
        }
    }

    public static void toggleLyrics() {
        //noinspection NonAtomicOperationOnVolatileField
        showLyrics = !showLyrics;
    }

    public static void togglePause() {
        AudioSource source = audios.get("bgm");
        if (source != null) {
            if (source.isPlaying()) {
                source.pause();
            } else {
                source.play();
            }
        }
    }
}
