package com.github.euonmyoji.yysplayer.player;

import net.minecraft.client.MinecraftClient;

/**
 * @author yinyangshi
 */
class ShowLyricsRepeatTask {
    volatile boolean running = true;

    ShowLyricsRepeatTask(String s) {
        new Thread(() -> {
            while (running) {
                if (SongPlayer.showLyrics) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(s, true);
                }
                try {
                    Thread.sleep(2499);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void finalize() {
        running = false;
    }
}
