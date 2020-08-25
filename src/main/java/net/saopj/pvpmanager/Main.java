package net.saopj.pvpmanager;

import net.saopj.pvpmanager.util.Runnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.saopj.pvpmanager.util.*;

import java.io.File;

public class Main extends JavaPlugin {
    public static Main ins;

    // 插件启用操作
    public void onEnable() {
        saveDefaultConfig();
        saveDefaultFile("save.yml");
        reloadConfig();
        Bukkit.getPluginManager().registerEvents(new EventChecker(), this);
        Runnable runnable = new Runnable();
        runnable.runTaskTimerAsynchronously(this,0L,20L);
        Bukkit.getLogger().info("PVP 管理插件已启动.");
    }

    // 插件禁用操作
    public void onDisable() {
        Bukkit.getLogger().info("PVP 管理插件已关闭");
    }

    // 将resources文件夹中的原始文件保存到插件配置文件夹中(如果该文件已经存在，就不会执行操作)
    public void saveDefaultFile(String fn) {
        File f = new File(getDataFolder(), fn);
        if (!f.exists()) {
            saveResource(fn, false);
        }
    }

    public File getSaveFile() {
        return new File(getDataFolder(), "save.yml");
    }

    public static Main getInstance(){
        return ins;
    }
}
