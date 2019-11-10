package com.github.euonmyoji.yysplayer;

import com.github.euonmyoji.yysplayer.command.YYSPlayerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;

/**
 * @author yinyangshi
 */
public class YYSPlayer implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistry.INSTANCE.register(false, YYSPlayerCommand::register);
    }
}
