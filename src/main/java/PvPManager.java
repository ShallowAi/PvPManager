import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPManager extends JavaPlugin {

    public void onEnable() {
        Bukkit.getLogger().info("PVP 管理插件正在启动");
        saveDefaultConfig();
        reloadConfig();
    }

    public void onDisable() {
        
    }

}
