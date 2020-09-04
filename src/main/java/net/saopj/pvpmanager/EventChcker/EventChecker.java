package net.saopj.pvpmanager.EventChcker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;

import net.milkbowl.vault.permission.Permission;

import net.saopj.pvpmanager.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventChecker implements Listener {

    long duration_red; // 红名类罪行通缉持续时间
    long duration_orange; // 橙名类罪行通缉持续时间
    String redNameGroupName; // 红名玩家权限组名称
    String orangeNameGroupName; // 橙名玩家权限组名称

    Map<String, List<String>> evilPlayers;
    YamlConfiguration saveYaml;
    YamlConfiguration config;
    File saveFile;
    Main ins;
    Permission permManger;
    Location prison_in;

    public EventChecker() {
        this.ins = Main.getInstance();
        this.saveFile = ins.getSaveFile();
        this.saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        this.config = (YamlConfiguration) ins.getConfig();
        this.duration_red = config.getLong("duration-red",2400000L); // 单位是毫秒
        this.duration_orange = config.getLong("duration-orange", 1200000L);
        this.evilPlayers = (Map<String, List<String>>) saveYaml.get("evilPlayer");
        this.permManger = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.redNameGroupName = config.getString("red-name-group-name","danger");
        this.orangeNameGroupName = config.getString("orange-name-group-name","warn");
        this.prison_in = new Location(Bukkit.getServer().getWorld(config.getString("prison_world", "world")), config.getIntegerList("prison_in_loc").get(0), config.getIntegerList("prison_in_loc").get(1), config.getIntegerList("prison_in_loc").get(2));
    }

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            Player victim = (Player)e.getEntity();
            String vuuid = victim.getUniqueId().toString();
            Player damager = (Player) e.getDamager();
            String duuid = damager.getUniqueId().toString();
            // 判断是否是致命一击
            if(e.getFinalDamage() >= victim.getHealth()) {
                if (evilPlayers.containsKey(duuid)) {
                    List<String> evilInfo = evilPlayers.get(duuid);
                    evilInfo.set(0, "2");
                    evilInfo.set(1, String.valueOf(duration_red));
                    evilInfo.add("0"); // 0为不在监狱，1为在监狱
                    evilPlayers.replace(duuid, evilInfo);
                    damager.sendMessage("你的名字已经被鲜血染红！");
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("2"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(duration_red)); // 这里是解除时间
                    evilInfo.add("0"); // 0为不在监狱，1为在监狱
                    evilPlayers.put(duuid, evilInfo);
                    damager.sendMessage("你的罪行使你的通缉时间刷新");
                }
                permManger.playerAddGroup(damager, redNameGroupName);

                if (evilPlayers.containsKey(vuuid) && evilPlayers.get(vuuid).get(0) == "2") {
                    victim.teleport(prison_in);
                    victim.sendMessage("由于你是红名玩家，所以被玩家击杀后在黑铁宫监禁一段时间");
                }
            } else {

                if (evilPlayers.containsKey(duuid)) {
                    List<String> evilInfo = evilPlayers.get(duuid);
                    if (evilInfo.get(0) == "2"){
                        evilInfo.set(1, String.valueOf(duration_red)); // 刷新通缉时间
                    } else {
                        evilInfo.set(1, String.valueOf(duration_orange));
                        permManger.playerAddGroup(damager, orangeNameGroupName);
                    }
                    evilPlayers.replace(duuid, evilInfo);
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("1"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(duration_orange)); // 这里是解除时间,没写完
                    evilPlayers.put(duuid, evilInfo);
                    permManger.playerAddGroup(damager ,orangeNameGroupName);
                }
            }
            saveYaml.set("evilPlayers",evilPlayers);
            saveSaveFile();
        }
    }

    // 保存插件存档文件
    public void saveSaveFile() {
        try { saveYaml.save(saveFile); } catch (IOException e){}
    }
}