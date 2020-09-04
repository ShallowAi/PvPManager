package net.saopj.pvpmanager;

import net.saopj.pvpmanager.EventChcker.EventChecker;
import net.saopj.pvpmanager.Runnable.AutoSaveRunnable;
import net.saopj.pvpmanager.Runnable.MainRunnable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    public static Main ins;

    public MainRunnable mainRunnable;
    public AutoSaveRunnable autoSaveRunnable;
    public File saveFile;
    public FileConfiguration config;
    public FileConfiguration saveYaml;

    // 插件启用操作
    public void onEnable() {
        saveDefaultConfig();
        saveDefaultFile("save.yml");
        reloadConfig();
        this.saveFile = getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        Bukkit.getPluginManager().registerEvents(new EventChecker(), this);
        this.config = getConfig();
        this.mainRunnable = new MainRunnable(
                config.getLong("MainRunnablePeriod", 20L),
                config,
                saveFile,
                saveYaml
        );
        this.autoSaveRunnable = new AutoSaveRunnable(saveFile,saveYaml);
        mainRunnable.runTaskTimerAsynchronously(this,0L,config.getLong("MainRunnablePeriod", 20L));
        autoSaveRunnable.runTaskTimer(this, 0L,config.getLong("AutoRunnablePeriod",12000L)); // 默认10分钟一次自动存档
        Bukkit.getLogger().info("PVP 管理插件已启动.");
    }

    // 插件禁用操作
    public void onDisable() {
        try {
            saveYaml.save(saveFile);
        } catch (IOException e) {
            Bukkit.getLogger().info("存档时发生异常！");
        }
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
