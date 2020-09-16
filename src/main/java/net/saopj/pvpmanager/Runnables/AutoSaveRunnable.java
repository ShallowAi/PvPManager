package net.saopj.pvpmanager.Runnables;

import net.saopj.pvpmanager.File.MyFile;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveRunnable extends BukkitRunnable {

    public MyFile saveFile;

    public AutoSaveRunnable(MyFile saveFile) {
        this.saveFile = saveFile;
    }

    @Override
    public void run() {
        saveFile.mySave();
    }
}
