package org.leng;

import org.bukkit.plugin.java.JavaPlugin;

public class LengContainerPreview extends JavaPlugin {

    private LCPDataManager dataManager;

    @Override
    public void onEnable() {
        // 初始化数据管理器
        dataManager = new LCPDataManager();

        // 注册命令处理器
        this.getCommand("lcp").setExecutor(new LCPCommand(this, dataManager));

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new LCPListener(this, dataManager), this);

        getLogger().info("LengContainerPreview 已启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("LengContainerPreview 已禁用！");
    }
}