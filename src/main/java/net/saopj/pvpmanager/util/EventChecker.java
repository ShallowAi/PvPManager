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

public class EventChecker implements Listener {

    long duration_red; // 红名类罪行通缉持续时间
    long duration_orange; // 橙名类罪行通缉持续时间

    YamlConfiguration saveYaml;
    FileConfiguration config;
    File saveFile;
    Main ins;

    public EventChecker() {
        this.ins = Main.getInstance();
        this.saveFile = ins.getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        this.config = ins.getConfig();
        this.duration_red = config.getLong("duration-red",2400000L); // 单位是毫秒
        this.duration_orange = config.getLong("duration-orange", 1200000L);
    }

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Damageable victim = (Damageable)e.getEntity();
        Date time = new Date();
        if(victim.getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            String puuid = damager.getUniqueId().toString();
            Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
            if(e.getFinalDamage() >= victim.getHealth()) {
                if (evilPlayers.containsKey(puuid)) {
                    List<String> evilInfo = evilPlayers.get(puuid);
                    evilInfo.set(0, "2");
                    evilInfo.set(1, String.valueOf(time.getTime()+duration_red));
                    evilPlayers.replace(puuid, evilInfo);
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("2"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(time.getTime()+duration_red)); // 这里是解除时间,没写完
                    evilPlayers.put(damager.getUniqueId().toString(), evilInfo);
                }
            } else {
                if (evilPlayers.containsKey(puuid)) {
                    List<String> evilInfo = evilPlayers.get(puuid);
                    if (Integer.parseInt(evilInfo.get(0)) > 1){
                        evilInfo.set(1, String.valueOf(time.getTime()+duration_red));
                    } else {
                        evilInfo.set(0, "1");
                        evilInfo.set(1, String.valueOf(time.getTime()+duration_orange));
                    }
                    evilPlayers.replace(puuid, evilInfo);
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("1"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(time.getTime()+duration_orange)); // 这里是解除时间,没写完
                    evilPlayers.put(damager.getUniqueId().toString(), evilInfo);
                }
            }
            saveYaml.set("evilPlayers",evilPlayers);
            saveSaveFile();
        }
    }

    public void saveSaveFile() {
        try { saveYaml.save(saveFile); } catch (IOException e){}
    }
}