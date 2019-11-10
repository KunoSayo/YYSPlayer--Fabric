package com.github.euonmyoji.yysplayer.command;

import com.github.euonmyoji.yysplayer.player.SongPlayer;
import com.github.euonmyoji.yysplayer.util.SongData;
import com.github.euonmyoji.yysplayer.util.Util;
import com.google.gson.JsonArray;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.io.IOException;
import java.net.URL;

/**
 * @author yinyangshi
 */
public class YYSPlayerCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("yysplayer")
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("play").then(
                        CommandManager.argument("name", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    try {
                                        JsonArray array = Util.getSongJsonArray(name);
                                        if (array.size() > 0) {
                                            SongData data = new SongData(array.get(0).getAsJsonObject(), context.getSource().getName());
                                            SongPlayer.playSong(data, new URL(String.format("http://music.163.com/song/media/outer/url?id=%s.mp3", data.getId())), data.getId());
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("pitch").then(
                        CommandManager.argument("pitch", FloatArgumentType.floatArg(0, 2))
                                .executes(context -> {
                                    SongPlayer.setPitch(FloatArgumentType.getFloat(context, "pitch"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("seek").then(
                        CommandManager.argument("seek", FloatArgumentType.floatArg(0))
                                .executes(context -> {
                                    SongPlayer.seek(FloatArgumentType.getFloat(context, "seek"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("toggle").then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("lyrics")
                                .executes(context -> {
                                    SongPlayer.toggleLyrics();
                                    return Command.SINGLE_SUCCESS;
                                })).then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("pause")
                                .executes(context -> {
                                    SongPlayer.togglePause();
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("info").executes(context -> {
                    SongPlayer.sendInfo();
                    return Command.SINGLE_SUCCESS;
                }))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("gain").then(
                        CommandManager.argument("gain", FloatArgumentType.floatArg(0, 1))
                                .executes(context -> {
                                    SongPlayer.setGain(FloatArgumentType.getFloat(context, "gain"));
                                    return Command.SINGLE_SUCCESS;
                                })
                )));
    }
}
