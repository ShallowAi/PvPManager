package net.saopj.pvpmanager.CommandHander;

import net.saopj.pvpmanager.CommandHander.cmds.giveRedName;
import net.saopj.pvpmanager.File.MyFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHander implements CommandExecutor {

    MyFile saveFile;
    MyFile config;

    public CommandHander(MyFile saveFile, MyFile config) {
        this.saveFile = saveFile;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0] == "giveRedname") {
            new giveRedName(Bukkit.getPlayer(args[1]), saveFile, config);
        }
        return false;
    }
}
