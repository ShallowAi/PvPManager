package net.saopj.pvpmanager.Runnable;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.saopj.pvpmanager.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainRunnable extends BukkitRunnable {

    YamlConfiguration config;
    File saveFile;
    YamlConfiguration saveYaml;
    Permission permManger;
    String defaultGroupName;
    Location prison_out;

    Main ins;

    public MainRunnable() {
        this.config = (YamlConfiguration) ins.getConfig();
        this.saveFile = ins.getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        this.permManger = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.defaultGroupName = saveYaml.getString("default-group-name","default");
        this.prison_out = new Location(Bukkit.getServer().getWorld(config.getString("prison_world")), config.getIntegerList("prison_out_loc").get(0), config.getIntegerList("prison_out_loc").get(1), config.getIntegerList("prison_out_loc").get(2));
    }

    @Override
    public void run() {
        Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
        for (String puuid:evilPlayers.keySet()) {
            List<String> evilInfo = evilPlayers.get(puuid);
            evilInfo.set(1, String.valueOf(Long.parseLong(evilInfo.get(1))-1000L));
            if (Long.parseLong(evilPlayers.get(puuid).get(1)) <= 0L) {
                evilPlayers.remove(puuid);
                Player p = Bukkit.getServer().getPlayer(UUID.fromString(puuid));
                permManger.playerAddGroup(p,defaultGroupName);
                p.teleport(prison_out);
                p.sendMessage("你现在可以离开了");
            }
        }
    }

    public void saveSaveFile() {
        try { saveYaml.save(saveFile); } catch (IOException e){}
    }
}
