package net.saopj.pvpmanager.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import net.saopj.pvpmanager.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Runnable extends BukkitRunnable {

    private File saveFile;
    private YamlConfiguration saveYaml;

    public Runnable() {
        this.saveFile = Main.getInstance().getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
    }

    @Override
    public void run() {
        Date time = new Date();
        Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
        List<String> needRemove = new ArrayList<String>();
        for (String puuid:evilPlayers.keySet()) {
            if (Long.parseLong(evilPlayers.get(puuid).get(1)) <= time.getTime()) {
                needRemove.add(puuid);
            }
        }
        for (String puuid:needRemove) {
            evilPlayers.remove(puuid);
        }
    }
}
