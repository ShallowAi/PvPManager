package net.saopj.pvpmanager.CommandHander.cmds;

import net.milkbowl.vault.permission.Permission;
import net.saopj.pvpmanager.File.MyFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class giveRedName {
    public giveRedName(Player p, MyFile saveFile, MyFile config){
        Map<String, List<String>> criminals = (Map<String, List<String>>) saveFile.get("Criminals");
        String puuid = p.getUniqueId().toString();
        String redNameGroupName = config.getString("red-name-group-name", "danger");
        Long duration_red = config.getLong("duration-red", 2400000L);
        Permission permManger = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        if (criminals.containsKey(puuid)) {
            List<String> evilInfo = criminals.get(puuid);
            evilInfo.set(0, "2");
            evilInfo.set(1, String.valueOf(duration_red));
            evilInfo.add("0"); // 0为不在监狱，1为在监狱
            criminals.replace(puuid, evilInfo);
            p.sendMessage("你的通缉等级变更为红名");
        } else {
            List<String> evilInfo = new ArrayList<String>();
            evilInfo.add("2"); // 这里是罪行等级
            evilInfo.add(String.valueOf(duration_red)); // 这里是解除时间
            evilInfo.add("0"); // 0为不在监狱，1为在监狱
            criminals.put(puuid, evilInfo);
            p.sendMessage("你的通缉等级变更为红名");
        }
        permManger.playerAddGroup(p, redNameGroupName);
        saveFile.set("Criminals", criminals);
        saveFile.mySave();
    }
}
