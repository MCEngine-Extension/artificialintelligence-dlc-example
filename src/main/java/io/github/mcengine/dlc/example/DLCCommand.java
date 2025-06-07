package io.github.mcengine.dlc.example.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles /exampledlc command logic.
 */
public class DLCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§aExampleDLC command executed!");
        return true;
    }
}
