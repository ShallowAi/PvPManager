package net.saopj.pvpmanager.Runnables;

import net.milkbowl.vault.permission.Permission;
import net.saopj.pvpmanager.File.MyFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainRunnable extends BukkitRunnable {

    FileConfiguration config;
    MyFile saveFile;
    Permission permManger;
    String defaultGroupName;
    Location prison_out;
    Long period;
    float speed; // 这是进监狱以后的时间倍率

    public MainRunnable(FileConfiguration config, MyFile saveFile) {
        this.config = config;
        this.saveFile = saveFile;
        this.permManger = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.defaultGroupName = config.getString("default-group-name","default");
        this.prison_out = new Location(Bukkit.getServer().getWorld(config.getString("prison_world")), config.getIntegerList("prison_out_loc").get(0), config.getIntegerList("prison_out_loc").get(1), config.getIntegerList("prison_out_loc").get(2));
        this.period = config.getLong("MainRunnablePeriod", 20L);
        this.speed = config.getInt("speed");
    }

    @Override
    public void run() {
        Map<String, List<String>> criminals = (Map<String, List<String>>) saveFile.get("Criminals");
        for (String puuid: criminals.keySet()) {
            List<String> evilInfo = criminals.get(puuid);
            if (evilInfo.get(2) == "1") {
                evilInfo.set(1, String.valueOf(Long.parseLong(evilInfo.get(1))-1000L*period/20L*speed));
            } else {
                evilInfo.set(1, String.valueOf(Long.parseLong(evilInfo.get(1))-1000L*period/20L));
            }
            if (Long.parseLong(criminals.get(puuid).get(1)) <= 0L) {
                criminals.remove(puuid);
                Player p = Bukkit.getServer().getPlayer(UUID.fromString(puuid));
                permManger.playerAddGroup(p,defaultGroupName);
                p.teleport(prison_out);
                p.sendMessage("你现在可以离开了");
            }
        }
    }
}
