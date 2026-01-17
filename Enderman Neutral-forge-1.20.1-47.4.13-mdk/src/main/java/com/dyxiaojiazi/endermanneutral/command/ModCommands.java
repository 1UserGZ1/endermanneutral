package com.dyxiaojiazi.endermanneutral.command;

import com.dyxiaojiazi.endermanneutral.EndermanNeutral;
import com.dyxiaojiazi.endermanneutral.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("endermanneutral")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(context -> {
                            // 重新加载配置
                            context.getSource().sendSuccess(() ->
                                    Component.literal("§6配置已重新加载§r"), true);
                            return 1;
                        })
                )
                .then(Commands.literal("status")
                        .executes(context -> {
                            // 显示模组状态
                            boolean enabled = ModConfig.enableMod();
                            boolean aggressionDisabled = ModConfig.disableAggression();

                            context.getSource().sendSuccess(() ->
                                    Component.literal("§6[苦力怕中立]§r\n" +
                                            "模组状态: " + (enabled ? "§a启用" : "§c禁用") + "\n" +
                                            "主动攻击: " + (aggressionDisabled ? "§c禁用" : "§a启用") + "\n" +
                                            "激怒爆炸: " + (ModConfig.creeperExplodeOnProvoked() ? "§a是" : "§c否") + "\n" +
                                            "持续时间: " + ModConfig.angerDuration() + "秒\n" +
                                            "需要潜行激怒: " + (ModConfig.requireSneakAttack() ? "§a是" : "§c否") + "\n" +
                                            "爆炸破坏: " + (ModConfig.explosionDestroysBlocks() ? "§a是" : "§c否")), false);
                            return 1;
                        })
                )
                .then(Commands.literal("set")
                        .then(Commands.literal("duration")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(5, 300))
                                        .executes(context -> {
                                            int seconds = IntegerArgumentType.getInteger(context, "seconds");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6激怒持续时间已设置为: §e" + seconds + "秒§r"), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("enabled")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6模组状态已设置为: §e" + (value ? "启用" : "禁用") + "§r"), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("aggression")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6主动攻击已设置为: §e" + (value ? "启用" : "禁用") + "§r"), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("explode")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6激怒爆炸已设置为: §e" + (value ? "启用" : "禁用") + "§r"), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("sneak")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6需要潜行已设置为: §e" + (value ? "启用" : "禁用") + "§r"), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("destroy")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            // 显示设置成功信息
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("§6爆炸破坏已设置为: §e" + (value ? "启用" : "禁用") + "§r"), true);
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}