package net.saopj.pvpmanager.File;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

// 暂定为MyFile
public class MyFile extends YamlConfiguration {

    File f;
    YamlConfiguration yc;

    public MyFile(File f) {
        this.f = f;
        this.yc = YamlConfiguration.loadConfiguration(f);
    }

    public MyFile(File df, String fn) {
        this.f = new File(df, fn);
        this.yc = YamlConfiguration.loadConfiguration(f);
    }

    // 暂定为mySave
    public boolean mySave() {
        try {
            yc.save(f);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
