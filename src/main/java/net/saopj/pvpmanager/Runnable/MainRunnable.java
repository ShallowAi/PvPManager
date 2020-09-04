package net.saopj.pvpmanager.Runnable;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainRunnable extends BukkitRunnable {

    FileConfiguration config;
    File saveFile;
    FileConfiguration saveYaml;
    Permission permManger;
    String defaultGroupName;
    Location prison_out;
    Long period;
    int times; // 这是进监狱以后的时间倍率

    public MainRunnable(Long period, FileConfiguration config,File saveFile, FileConfiguration saveYaml) {
        this.config = config;
        this.saveFile = saveFile;
        this.saveYaml = saveYaml;
        this.permManger = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.defaultGroupName = saveYaml.getString("default-group-name","default");
        this.prison_out = new Location(Bukkit.getServer().getWorld(config.getString("prison_world")), config.getIntegerList("prison_out_loc").get(0), config.getIntegerList("prison_out_loc").get(1), config.getIntegerList("prison_out_loc").get(2));
        this.period = period;
        this.times = config.getInt("times");
    }

    @Override
    public void run() {
        Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
        for (String puuid:evilPlayers.keySet()) {
            List<String> evilInfo = evilPlayers.get(puuid);
            if (evilInfo.get(2) == "1") {
                evilInfo.set(1, String.valueOf(Long.parseLong(evilInfo.get(1))-1000L*period/20L*times));
            } else {
                evilInfo.set(1, String.valueOf(Long.parseLong(evilInfo.get(1))-1000L*period/20L));
            }

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
