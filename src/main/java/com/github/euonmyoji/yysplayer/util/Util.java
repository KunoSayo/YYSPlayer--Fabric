package com.github.euonmyoji.yysplayer.util;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * @author yinyangshi
 */
public class Util {
    public static JsonArray getSongJsonArray(String s) throws IOException {
//        String urlB = String.format("http://up.chinesety.com:25589/search?keywords=%s&limit=1&type=1", URLEncoder.encode(s, "UTF-8"))
        String urlB = String.format("http://music.163.com/api/search/pc?s=%s&type=1&limit=1", URLEncoder.encode(s, "UTF-8"));
        URL url = new URL(urlB);
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        InputStreamReader reader = new InputStreamReader(con.getInputStream(), Charsets.UTF_8);
        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
        return json.get("result")
                .getAsJsonObject()
                .get("songs")
                .getAsJsonArray();
    }

    public static ShortBuffer expandBuffer(ShortBuffer old) {
        old.flip();
        ShortBuffer expanded = BufferUtils.createShortBuffer((int) (old.capacity() * 1.5));
        expanded.put(old);
        old.clear();
        return expanded;
    }

    public static ByteBuffer expandBuffer(ByteBuffer old, int least) {
        old.flip();
        int newSize = (int) (old.capacity() * 1.5f) + least;
        if (newSize < 0) {
            newSize = Integer.MAX_VALUE;
        }
        System.out.println("expanded to " + newSize);
        ByteBuffer expanded = BufferUtils.createByteBuffer(newSize);
        expanded.put(old);
        return expanded;
    }

}
