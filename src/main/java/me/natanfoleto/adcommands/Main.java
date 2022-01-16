package me.natanfoleto.adcommands;
import me.natanfoleto.adcommands.commands.AdCommand;
import me.natanfoleto.adcommands.listeners.PlayerCommand;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Main extends JavaPlugin {
    private List<String> commands;

    File configFile;
    File messagesFile;
    File adcommandsFile;

    FileConfiguration config;
    FileConfiguration messages;
    FileConfiguration adcommands;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§6[ADCommands] §7carregado com sucesso!");

        registerEvents();
        registerCommands();

        configFile = new File(getDataFolder(), "config.yml");
        messagesFile = new File(getDataFolder(), "messages.yml");
        adcommandsFile = new File(getDataFolder(), "adcommands.yml");

        createFiles();
        loadConfig();
    }

    private void createFiles() {
        if (!configFile.exists())
            saveResource("config.yml", false);
        if (!messagesFile.exists())
            saveResource("messages.yml", false);
        if (!adcommandsFile.exists()) {
            saveResource("adcommands.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(this.configFile);
        messages = YamlConfiguration.loadConfiguration(this.messagesFile);
        adcommands = YamlConfiguration.loadConfiguration(this.adcommandsFile);
    }

    public void loadConfig() {
        try {
            config.load(configFile);
            messages.load(messagesFile);
            commands = adcommands.getStringList("ADCCommands");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCommand(String commandName) {
        try {
            commands.add(commandName);
            adcommands.set("ADCCommands", commands);
            adcommands.save(adcommandsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean removeCommand(String commandName) {
        try {
            Iterator<String> commandsIterator = commands.iterator();
            boolean removed = false;

            while(commandsIterator.hasNext()) {
                String command = commandsIterator.next();

                if (command.equalsIgnoreCase(commandName)) {
                    commandsIterator.remove();
                    removed = true;

                    adcommands.set("ADCCommands", commands);
                    adcommands.save(adcommandsFile);
                }
            }

            return removed;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeAllCommand() {
        try {
            commands.clear();
            adcommands.set("ADCCommands", commands);
            adcommands.save(adcommandsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerCommand(), this);
    }

    public void registerCommands() {
        getCommand("adcommands").setExecutor(new AdCommand());
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public List<String> getCommands() {
        return commands;
    }

    public FileConfiguration getMessages() {
        return messages;
    }
}
