package net.saopj.pvpmanager.EventChcker;

import net.saopj.pvpmanager.File.MyFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;

import net.milkbowl.vault.permission.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventChecker implements Listener {

    long duration_red; // 红名类罪行通缉持续时间
    long duration_orange; // 橙名类罪行通缉持续时间
    String redNameGroupName; // 红名玩家权限组名称
    String orangeNameGroupName; // 橙名玩家权限组名称

    Map<String, List<String>> criminals;
    FileConfiguration config;
    MyFile saveFile;
    Permission permManger;
    Location prison_in;

    public EventChecker(MyFile config, MyFile saveFile) {
        this.config = config;
        this.duration_red = config.getLong("duration-red",2400000L); // 单位是毫秒
        this.duration_orange = config.getLong("duration-orange", 1200000L);
        this.criminals = (Map<String, List<String>>) saveFile.get("Criminals");
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
                if (criminals.containsKey(duuid)) {
                    List<String> evilInfo = criminals.get(duuid);
                    evilInfo.set(0, "2");
                    evilInfo.set(1, String.valueOf(duration_red));
                    evilInfo.add("0"); // 0为不在监狱，1为在监狱
                    criminals.replace(duuid, evilInfo);
                    damager.sendMessage("因为你击杀了玩家，所以你的通缉等级变更为红名");
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("2"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(duration_red)); // 这里是解除时间
                    evilInfo.add("0"); // 0为不在监狱，1为在监狱
                    criminals.put(duuid, evilInfo);
                    damager.sendMessage("因为你击杀了玩家，所以你的通缉等级变更为红名");
                }
                permManger.playerAddGroup(damager, redNameGroupName);

                if (criminals.containsKey(vuuid) && criminals.get(vuuid).get(0) == "2") {
                    victim.teleport(prison_in);
                    victim.sendMessage("因为你是红名玩家，所以被玩家击杀后在黑铁宫监禁至通缉时间结束");
                }
            } else {
                if (criminals.containsKey(duuid)) {
                    List<String> evilInfo = criminals.get(duuid);
                    if (evilInfo.get(0) == "2"){
                        evilInfo.set(1, String.valueOf(duration_red)); // 刷新通缉时间
                    } else {
                        evilInfo.set(1, String.valueOf(duration_orange));
                    }
                    criminals.replace(duuid, evilInfo);
                } else {
                    List<String> evilInfo = new ArrayList<String>();
                    evilInfo.add("1"); // 这里是罪行等级
                    evilInfo.add(String.valueOf(duration_orange)); // 这里是解除时间,没写完
                    criminals.put(duuid, evilInfo);
                    permManger.playerAddGroup(damager ,orangeNameGroupName);
                }
            }
            saveFile.set("Criminals", criminals);
            saveFile.mySave();
        }
    }
}