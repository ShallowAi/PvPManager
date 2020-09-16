package net.saopj.pvpmanager;

import net.saopj.pvpmanager.CommandHander.CommandHander;
import net.saopj.pvpmanager.EventChcker.EventChecker;
import net.saopj.pvpmanager.File.MyFile;
import net.saopj.pvpmanager.Runnables.AutoSaveRunnable;
import net.saopj.pvpmanager.Runnables.MainRunnable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Main ins;

    public MainRunnable mainRunnable;
    public AutoSaveRunnable autoSaveRunnable;
    public MyFile saveFile;
    public MyFile config;

    // 插件启用操作
    public void onEnable() {
        saveDefaultConfig();
        saveDefaultFile("save.yml");
        reloadConfig();
        this.saveFile = new MyFile(getDataFolder(), "save.yml");
        Bukkit.getPluginManager().registerEvents(new EventChecker(config, saveFile), this);
        Bukkit.getPluginCommand("pvpmanger").setExecutor(new CommandHander(saveFile, config));
        this.config = new MyFile(getDataFolder(), "config.yml");
        this.mainRunnable = new MainRunnable(config, saveFile);
        this.autoSaveRunnable = new AutoSaveRunnable(saveFile);
        mainRunnable.runTaskTimerAsynchronously(this,0L,config.getLong("MainRunnablePeriod", 20L));
        autoSaveRunnable.runTaskTimer(this, 0L,config.getLong("AutoRunnablePeriod",12000L)); // 默认10分钟一次自动存档
        Bukkit.getLogger().info("PVP 管理插件已启动.");
    }

    // 插件禁用操作
    public void onDisable() {
        saveFile.mySave();
        Bukkit.getLogger().info("PVP 管理插件已关闭");
    }

    // 将resources文件夹中的原始文件保存到插件配置文件夹中(如果该文件已经存在，就不会执行操作)
    public void saveDefaultFile(String fn) {
        File f = new File(getDataFolder(), fn);
        if (!f.exists()) {
            saveResource(fn, false);
        }
    }
}
