package net.saopj.pvpmanager.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;

import net.saopj.pvpmanager.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Checker implements Listener {

    long duration;

    YamlConfiguration saveYaml;
    FileConfiguration config;
    File saveFile;
    Main ins;

    public Checker() {
        this.ins = Main.getInstance();
        this.saveFile = ins.getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        this.config = ins.getConfig();
        this.duration = config.getLong("duration-kill",2400000); // 单位是毫秒
    }

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Damageable victim = (Damageable)e.getEntity();
        Date time = new Date();
        if(victim.getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            String puuid = damager.getUniqueId().toString();
            if(e.getFinalDamage() >= victim.getHealth()) {
                Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
                if (!evilPlayers.containsKey(puuid)) {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("3"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(time.getTime()+duration)); // 这里是解除时间,没写完
                    evilPlayers.put(damager.getUniqueId().toString(), evilInfo);
                    saveYaml.set("evilPlayers",evilPlayers);
                    saveSaveFile();
                } else {
                    List<String> evilInfo = evilPlayers.get(puuid);
                    if (Integer.parseInt(evilInfo.get(0)) > 3){ evilInfo.set(0, "3"); }
                }
            }
        }
    }

    public void saveSaveFile() {
        try { saveYaml.save(saveFile); } catch (IOException e){}
    }
}