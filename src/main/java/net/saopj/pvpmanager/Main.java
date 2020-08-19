package net.saopj.pvpmanager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main ins;

    // 插件启用操作
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        Bukkit.getLogger().info("PVP 管理插件已启动.");
    }

    // 插件禁用操作
    public void onDisable() {

    }

    public static Main getInstance(){
        return ins;
    }

}
