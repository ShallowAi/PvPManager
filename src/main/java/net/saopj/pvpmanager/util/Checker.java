package net.saopj.pvpmanager.util;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;

public class Checker implements Listener {

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(isPVP(e)) {

        }
    }

    // 判断是否玩家对战
    public boolean isPVP(EntityDamageByEntityEvent e){
        return e.getEntity().getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER;
    }
}
