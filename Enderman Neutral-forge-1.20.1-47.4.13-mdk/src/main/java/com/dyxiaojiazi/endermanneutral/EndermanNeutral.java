package com.dyxiaojiazi.endermanneutral;

import com.dyxiaojiazi.endermanneutral.command.ModCommands;
import com.dyxiaojiazi.endermanneutral.config.ModConfig;
import com.dyxiaojiazi.endermanneutral.event.CreeperEventHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(EndermanNeutral.MODID)
public class EndermanNeutral {
    public static final String MODID = "endermanneutral";
    public static final String MOD_NAME = "苦力怕中立";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EndermanNeutral() {
        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(new CreeperEventHandler());

        // 注册命令
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        LOGGER.info("{} 模组加载成功！作者: {}", MOD_NAME, "DY_XiaoJiaZi");
        LOGGER.info("配置系统已初始化，可在游戏内修改设置");
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

}