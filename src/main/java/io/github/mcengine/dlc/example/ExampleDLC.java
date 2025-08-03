package io.github.mcengine.dlc.example;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.artificialintelligence.extension.dlc.IMCEngineArtificialIntelligenceDLC;

import io.github.mcengine.dlc.example.DLCCommand;
import io.github.mcengine.dlc.example.DLCListener;
import io.github.mcengine.dlc.example.DLCTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Main class for the ExampleDLC.
 * <p>
 * Registers the /exampledlc command and event listeners.
 */
public class ExampleDLC implements IMCEngineArtificialIntelligenceDLC {

    /**
     * Initializes the ExampleDLC.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        MCEngineExtensionLogger logger = new MCEngineExtensionLogger(plugin, "DLC", "ExampleDLC");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new DLCListener(plugin), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /exampledlc command
            Command exampleDLCCommand = new Command("exampledlc") {
                /**
                 * Handles logic for /exampledlc command.
                 */
                private final DLCCommand handler = new DLCCommand();

                /**
                 * Provides tab-completion for /exampledlc.
                 */
                private final DLCTabCompleter completer = new DLCTabCompleter();

                /**
                 * Executes the /exampledlc command.
                 *
                 * @param sender The command sender.
                 * @param label  The command label.
                 * @param args   The command arguments.
                 * @return true if successful.
                 */
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                /**
                 * Handles tab-completion for the /exampledlc command.
                 *
                 * @param sender The command sender.
                 * @param alias  The alias used.
                 * @param args   The current arguments.
                 * @return A list of possible completions.
                 */
                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            exampleDLCCommand.setDescription("Example DLC command.");
            exampleDLCCommand.setUsage("/exampledlc");

            // Dynamically register the /exampledlc command
            commandMap.register(plugin.getName().toLowerCase(), exampleDLCCommand);

            logger.info("Enabled successfully.");
        } catch (Exception e) {
            logger.warning("Failed to initialize ExampleDLC: " + e.getMessage());
            e.printStackTrace();
        }

        // Check for updates
        MCEngineCoreApi.checkUpdate(plugin, logger.getLogger(),
            "github", "MCEngine-DLC", "example",
            plugin.getConfig().getString("github.token", "null"));
    }

    @Override
    public void onDisload(Plugin plugin) {
        // No specific unload logic
    }

    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("example-dlc");
    }
}
