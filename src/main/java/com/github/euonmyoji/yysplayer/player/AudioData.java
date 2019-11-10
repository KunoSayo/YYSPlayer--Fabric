package com.github.euonmyoji.yysplayer.player;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ShortBuffer;

import static com.github.euonmyoji.yysplayer.util.Util.expandBuffer;
import static org.lwjgl.openal.AL10.*;

/**
 * @author yinyangshi
 */
class AudioData {
    private static int largestCap = 8 * 1024 * 1024;

    final int sampleRate;
    private final int channels;
    private final ShortBuffer rawData;
    private int buffer;

    AudioData(FFmpegFrameGrabber grabber) throws IOException {
        sampleRate = grabber.getSampleRate();
        channels = grabber.getAudioChannels();
        ShortBuffer rawData = BufferUtils.createShortBuffer(largestCap);

        Frame f;
        while ((f = grabber.grab()) != null) {
            for (Buffer buffer : f.samples) {
                try {
                    ShortBuffer shortBuffer = ((ShortBuffer) buffer);
                    while (rawData.remaining() < shortBuffer.limit()) {
                        rawData = expandBuffer(rawData);
                    }
                    rawData.put(shortBuffer);
                } finally {
                    buffer.clear();
                }
            }
        }

        rawData.flip();
        this.rawData = rawData;
        if (rawData.limit() > largestCap) {
            largestCap = rawData.limit();
        }
        this.buffer = alGenBuffers();
        AL11.alBufferData(buffer, channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, rawData, sampleRate);
    }

    void cleanup() {
        rawData.clear();
        alDeleteBuffers(this.buffer);
    }

    int getBuffer() {
        return buffer;
    }
}