package net.saopj.pvpmanager.util;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;

public class Checker implements Listener {

    // 实体被另外一个实体攻击事件
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Damageable victim = (Damageable)e.getEntity();
        if(victim.getType() == EntityType.PLAYER && e.getDamager().getType() == EntityType.PLAYER) {
            if(e.getFinalDamage() >= victim.getHealth()) {

            }
        }
    }
}
