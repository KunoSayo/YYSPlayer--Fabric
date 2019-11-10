package com.github.euonmyoji.yysplayer.songplayer;

import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.io.IOException;

import static org.lwjgl.openal.AL11.*;

/**
 * @author yinyangshi
 */
public class AudioSource {

    private final int source;
    private final AudioData data;

    public AudioSource(FFmpegFrameGrabber grabber) throws IOException {
        this.source = alGenSources();
        this.data = new AudioData(grabber);
        alSourcei(source, AL_BUFFER, data.getBuffer());
        setGain(1.0f);
        setPosition(0, 0, 0);
        setSpeed(0, 0, 0);
    }

    public void seek(float second) {
        alSourcef(source, AL_SAMPLE_OFFSET, data.sampleRate * second);
    }

    public float curPos() {
        return alGetSourcef(source, AL_SAMPLE_OFFSET) / data.sampleRate;
    }

    public void setPitch(float s) {
        alSourcef(source, AL_PITCH, s);
    }


    public void setPosition(float x, float y, float z) {
        alSource3f(source, AL_POSITION, x, y, z);
    }

    public void setSpeed(float vx, float vy, float vz) {
        alSource3f(source, AL_VELOCITY, vx, vy, vz);
    }

    public AudioSource setGain(float gain) {
        alSourcef(source, AL_GAIN, gain);
        return this;
    }

    public void setProperty(int param, float value) {
        alSourcef(source, param, value);
    }

    public void play() {
        alSourcePlay(source);
    }

    public boolean isPlaying() {
        int state = alGetSourcei(source, AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(source);
    }


    public void stop() {
        alSourceStop(source);
    }

    public void cleanup() {
        stop();
        alDeleteSources(source);
        data.cleanup();
    }

    public float getPlayedSeconds() {
        return alGetSourcef(source, AL_SEC_OFFSET);
    }
}
