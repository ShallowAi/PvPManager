package net.saopj.pvpmanager.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class AutoSaveRunnable extends BukkitRunnable {

    public File saveFile;
    public FileConfiguration saveYaml;

    public AutoSaveRunnable(File saveFile, FileConfiguration saveYaml) {
        this.saveFile = saveFile;
        this.saveYaml = saveYaml;
    }

    @Override
    public void run() {
        try {
            saveYaml.save(saveFile);
        } catch (IOException e) {
            Bukkit.getLogger().info("存档时发生异常！");
        }
    }
}
