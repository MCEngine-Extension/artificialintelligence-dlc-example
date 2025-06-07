package io.github.mcengine.dlc.example;

import io.github.mcengine.api.mcengine.dlc.IMCEngineDLC;
import io.github.mcengine.api.mcengine.dlc.MCEngineDLCLogger;
import io.github.mcengine.dlc.example.command.DLCCommand;
import io.github.mcengine.dlc.example.listener.DLCListener;
import io.github.mcengine.dlc.example.tabcompleter.DLCTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Example DLC for MCEngineArtificialIntelligence.
 */
public class ExampleDLC implements IMCEngineDLC {
    @Override
    public void onLoad(Plugin plugin) {
        MCEngineDLCLogger logger = new MCEngineDLCLogger(plugin, "Example-DLC");

        try {
            // Register listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new DLCListener(plugin), plugin);

            // Access Bukkit's CommandMap reflectively
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define and register the /exampledlc command
            Command exampleDLCCommand = new Command("exampledlc") {
                private final DLCCommand handler = new DLCCommand();
                private final DLCTabCompleter completer = new DLCTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            exampleDLCCommand.setDescription("Example DLC command.");
            exampleDLCCommand.setUsage("/exampledlc");

            commandMap.register(plugin.getName().toLowerCase(), exampleDLCCommand);

            logger.info("Enabled successfully.");
        } catch (Exception e) {
            logger.warning("Failed to initialize Example-DLC: " + e.getMessage());
            e.printStackTrace();
        }
        MCEngineApi.checkUpdate(this, "github", "MCEngine-DLC", "example", getConfig().getString("github.token", "null"));
    }
}
