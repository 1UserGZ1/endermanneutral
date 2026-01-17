package com.dyxiaojiazi.endermanneutral.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = "endermanneutral", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    public static final ForgeConfigSpec SPEC;
    public static final Client CLIENT;
    public static final Server SERVER;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        CLIENT = new Client(builder);
        SERVER = new Server(builder);
        COMMON = new Common(builder);

        SPEC = builder.build();
    }

    public static class Client {
        public final BooleanValue showDebugMessages;
        public final ConfigValue<String> creeperTextureOverride;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("客户端设置");

            showDebugMessages = builder
                    .comment("是否显示调试信息")
                    .translation("config.endermanneutral.client.show_debug_messages")
                    .define("showDebugMessages", false);

            creeperTextureOverride = builder
                    .comment("苦力怕纹理覆盖（留空使用默认）")
                    .translation("config.endermanneutral.client.creeper_texture_override")
                    .define("creeperTextureOverride", "");

            builder.pop();
        }
    }

    public static class Server {
        public final BooleanValue disableAggression;
        public final BooleanValue requireSneakAttack;
        public final BooleanValue creeperExplodeOnProvoked;
        public final IntValue angerDuration;
        public final IntValue explosionRadius;
        public final BooleanValue explosionDestroysBlocks;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("服务器设置");

            disableAggression = builder
                    .comment("是否禁用苦力怕的主动攻击行为")
                    .translation("config.endermanneutral.server.disable_aggression")
                    .define("disableAggression", true);

            requireSneakAttack = builder
                    .comment("是否需要潜行攻击才能激怒苦力怕")
                    .translation("config.endermanneutral.server.require_sneak_attack")
                    .define("requireSneakAttack", false);

            creeperExplodeOnProvoked = builder
                    .comment("被激怒的苦力怕是否爆炸")
                    .translation("config.endermanneutral.server.explode_on_provoked")
                    .define("creeperExplodeOnProvoked", true);

            angerDuration = builder
                    .comment("苦力怕被激怒后的持续时间（秒）")
                    .translation("config.endermanneutral.server.anger_duration")
                    .defineInRange("angerDuration", 20, 5, 300);

            explosionRadius = builder
                    .comment("苦力怕爆炸半径（如果允许爆炸）")
                    .translation("config.endermanneutral.server.explosion_radius")
                    .defineInRange("explosionRadius", 3, 1, 10);

            explosionDestroysBlocks = builder
                    .comment("苦力怕爆炸是否破坏方块")
                    .translation("config.endermanneutral.server.explosion_destroys_blocks")
                    .define("explosionDestroysBlocks", false);

            builder.pop();
        }
    }

    public static class Common {
        public final BooleanValue enableMod;
        public final ConfigValue<String> modDescription;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("通用设置");

            enableMod = builder
                    .comment("是否启用本模组")
                    .translation("config.endermanneutral.common.enable_mod")
                    .define("enableMod", true);

            modDescription = builder
                    .comment("模组描述")
                    .translation("config.endermanneutral.common.mod_description")
                    .define("modDescription", "使苦力怕成为中立生物，只在被攻击时反击");

            builder.pop();
        }
    }

    // 客户端配置访问方法
    public static boolean showDebugMessages() {
        return CLIENT.showDebugMessages.get();
    }

    public static String getCreeperTextureOverride() {
        return CLIENT.creeperTextureOverride.get();
    }

    // 服务器配置访问方法
    public static boolean disableAggression() {
        return SERVER.disableAggression.get();
    }

    public static boolean requireSneakAttack() {
        return SERVER.requireSneakAttack.get();
    }

    public static boolean creeperExplodeOnProvoked() {
        return SERVER.creeperExplodeOnProvoked.get();
    }

    public static int angerDuration() {
        return SERVER.angerDuration.get();
    }

    public static int explosionRadius() {
        return SERVER.explosionRadius.get();
    }

    public static boolean explosionDestroysBlocks() {
        return SERVER.explosionDestroysBlocks.get();
    }

    // 通用配置访问方法
    public static boolean enableMod() {
        return COMMON.enableMod.get();
    }

    public static String getModDescription() {
        return COMMON.modDescription.get();
    }

    // 获取tick数（用于游戏逻辑）
    public static int getAngerDurationTicks() {
        return angerDuration() * 20; // 秒转tick
    }

    // 处理配置加载事件
    public static class ConfigEvents {
        @SubscribeEvent
        public static void onLoad(final ModConfigEvent.Loading configEvent) {
            // 配置加载时的处理
        }

        @SubscribeEvent
        public static void onReload(final ModConfigEvent.Reloading configEvent) {
            // 配置重新加载时的处理
        }
    }
}