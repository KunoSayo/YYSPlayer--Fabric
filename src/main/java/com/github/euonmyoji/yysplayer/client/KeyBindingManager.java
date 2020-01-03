package com.github.euonmyoji.yysplayer.client;

import com.github.euonmyoji.yysplayer.songplayer.SongPlayer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * @author yinyangshi
 */
public class KeyBindingManager {
    private static final String CATEGORY = "key.categories.playpause";
    private static FabricKeyBinding PLAY_PAUSE = FabricKeyBinding.Builder
            .create(new Identifier("yysplayer", "play/pause"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P,
                    CATEGORY).build();
    private static boolean pressedPlayPause = false;

    public static void register() {
        KeyBindingRegistry.INSTANCE.register(PLAY_PAUSE);
        ClientTickCallback.EVENT.register(KeyBindingManager::tick);
    }

    private static void tick(MinecraftClient client) {
        if(PLAY_PAUSE.isPressed()) {
            if(!pressedPlayPause) {
                pressedPlayPause = true;
                SongPlayer.togglePause();
            }
        } else {
            pressedPlayPause = false;
        }
    }
}
