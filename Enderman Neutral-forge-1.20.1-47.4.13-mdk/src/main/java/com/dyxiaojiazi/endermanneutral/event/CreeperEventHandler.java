package com.dyxiaojiazi.endermanneutral.event;

import com.dyxiaojiazi.endermanneutral.config.ModConfig;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = com.dyxiaojiazi.endermanneutral.EndermanNeutral.MODID)
public class CreeperEventHandler {

    @SubscribeEvent
    public static void onCreeperUpdate(LivingEvent.LivingTickEvent event) {
        // 检查模组是否启用
        if (!ModConfig.enableMod()) return;

        if (event.getEntity() instanceof Creeper creeper && !creeper.level().isClientSide) {
            // 清除苦力怕的目标，使其不再主动攻击玩家
            if (creeper.getTarget() instanceof Player && !wasProvoked(creeper)) {
                if (ModConfig.disableAggression()) {
                    creeper.setTarget(null);
                    creeper.setAggressive(false);
                }

                // 显示调试信息
                if (ModConfig.showDebugMessages()) {
                    com.dyxiaojiazi.endermanneutral.EndermanNeutral.LOGGER.debug("苦力怕重置目标: {}", creeper.getUUID());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreeperTarget(LivingChangeTargetEvent event) {
        if (!ModConfig.enableMod()) return;

        if (event.getNewTarget() instanceof Player && event.getEntity() instanceof Creeper creeper) {
            // 只有当苦力怕被激怒时才允许设置目标
            if (!wasProvoked(creeper) && ModConfig.disableAggression()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamageCreeper(LivingDamageEvent event) {
        if (!ModConfig.enableMod()) return;

        if (event.getEntity() instanceof Creeper creeper &&
                event.getSource().getEntity() instanceof Player player) {

            // 检查是否需要潜行攻击
            if (ModConfig.requireSneakAttack() && !player.isCrouching()) {
                return; // 非潜行攻击不激怒
            }

            // 当玩家攻击苦力怕时，标记为被激怒
            setProvoked(creeper, player);

            // 设置苦力怕目标为攻击者
            creeper.setTarget(player);
            creeper.setAggressive(true);

            // 显示调试信息
            if (ModConfig.showDebugMessages()) {
                com.dyxiaojiazi.endermanneutral.EndermanNeutral.LOGGER.debug("苦力怕被激怒: {} by {}", creeper.getUUID(), player.getName().getString());
            }
        }
    }

    private static boolean wasProvoked(Creeper creeper) {
        return creeper.getPersistentData().getBoolean("endermanneutral_provoked");
    }

    private static void setProvoked(Creeper creeper, Player player) {
        creeper.getPersistentData().putBoolean("endermanneutral_provoked", true);
        // 使用配置中的持续时间
        int durationTicks = ModConfig.getAngerDurationTicks();
        creeper.getPersistentData().putLong("endermanneutral_provoke_time",
                creeper.level().getGameTime() + durationTicks);

        // 保存激怒者信息
        creeper.getPersistentData().putString("endermanneutral_provoker", player.getStringUUID());
    }

    @SubscribeEvent
    public static void onCreeperTickReset(LivingEvent.LivingTickEvent event) {
        if (!ModConfig.enableMod()) return;

        if (event.getEntity() instanceof Creeper creeper && !creeper.level().isClientSide) {
            // 检查是否应该重置激怒状态
            if (wasProvoked(creeper)) {
                long resetTime = creeper.getPersistentData().getLong("endermanneutral_provoke_time");
                if (creeper.level().getGameTime() > resetTime) {
                    resetCreeper(creeper);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreeperDeath(LivingDeathEvent event) {
        if (!ModConfig.enableMod()) return;

        if (event.getEntity() instanceof Creeper creeper) {
            // 苦力怕死亡时清理数据
            creeper.getPersistentData().remove("endermanneutral_provoked");
            creeper.getPersistentData().remove("endermanneutral_provoke_time");
            creeper.getPersistentData().remove("endermanneutral_provoker");
        }
    }

    private static void resetCreeper(Creeper creeper) {
        creeper.getPersistentData().remove("endermanneutral_provoked");
        creeper.getPersistentData().remove("endermanneutral_provoke_time");
        creeper.getPersistentData().remove("endermanneutral_provoker");
        creeper.setTarget(null);
        creeper.setAggressive(false);

        if (ModConfig.showDebugMessages()) {
            com.dyxiaojiazi.endermanneutral.EndermanNeutral.LOGGER.debug("苦力怕重置激怒状态: {}", creeper.getUUID());
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(net.minecraftforge.event.level.ExplosionEvent.Detonate event) {
        if (!ModConfig.enableMod()) return;

        // 检查爆炸的实体是否是苦力怕
        if (event.getExplosion().getExploder() instanceof Creeper) {
            // 如果配置为不破坏方块，清空爆炸的方块列表
            if (!ModConfig.explosionDestroysBlocks()) {
                event.getAffectedBlocks().clear();
            }
        }
    }
}