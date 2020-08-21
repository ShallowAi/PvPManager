package net.saopj.pvpmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.saopj.pvpmanager.util.*;

import java.io.File;

public class Main extends JavaPlugin {
    public static Main ins;

    // 插件启用操作
    public void onEnable() {
        saveDefaultConfig();
        saveDefaultFile("saves.yml");
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(new Checker(), this);
        Bukkit.getLogger().info("PVP 管理插件已启动.");
    }

    // 插件禁用操作
    public void onDisable() {

    }

    // 将resources文件夹中的原始文件保存到插件配置文件夹中(如果该文件已经存在，就不会执行操作)
    public void saveDefaultFile(String fn) {
        File f = new File(getDataFolder(), fn);
        if (!f.exists()) {
            saveResource(fn, false);
        }
    }

    public File getSaveFile() {
        return new File(getDataFolder(), "saves.yml");
    }

    public static Main getInstance(){
        return ins;
    }

}
