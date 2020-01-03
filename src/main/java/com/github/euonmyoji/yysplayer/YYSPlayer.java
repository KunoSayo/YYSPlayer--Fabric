package com.github.euonmyoji.yysplayer;

import com.github.euonmyoji.yysplayer.client.KeyBindingManager;
import com.github.euonmyoji.yysplayer.command.YYSPlayerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author yinyangshi
 */
public class YYSPlayer implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistry.INSTANCE.register(false, YYSPlayerCommand::register);
        KeyBindingManager.register();
    }
}
