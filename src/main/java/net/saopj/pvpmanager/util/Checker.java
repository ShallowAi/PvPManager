package net.saopj.pvpmanager.util;

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

public class Checker implements Listener {

    YamlConfiguration saveConfig;
    File saveFile;

    public Checker() {
        this.saveFile = Main.getInstance().getSaveFile();
        this.saveConfig = YamlConfiguration.loadConfiguration(saveFile);
    }

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Damageable victim = (Damageable)e.getEntity();
        if(victim.getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) e.getDamager();
            if(e.getFinalDamage() >= victim.getHealth()) {
                Map<String, List<String>> evilPlayers = (Map<String, List<String>>) saveConfig.get("evilPlayer");
                if (!evilPlayers.containsKey(damager.getName())) {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("3"); // 这里是罪行等级
                    evilInfo.add(""); // 这里是解除时间,没写完
                    evilPlayers.put(damager.getUniqueId().toString(),evilInfo);
                    saveConfig.set("evilPlayers",evilPlayers);
                    saveSaveFile();
                }
            }
        }
    }

    public void saveSaveFile() {
        try { saveConfig.save(saveFile); } catch (IOException e){}
    }
}
