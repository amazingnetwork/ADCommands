package me.natanfoleto.adcommands.commands;

import me.natanfoleto.adcommands.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if (!sender.hasPermission("adc.use")) {
            sender.sendMessage(Main.getInstance().getMessages().getString("nopermission"));

            return false;
        }

        if (args.length == 0) {
            Main.getInstance().getMessages().getStringList("messagehelp").forEach(sender::sendMessage);

            return false;
        }

        switch (args[0]) {
            case "list": {
                List<String> commands = Main.getInstance().getCommands();

                if (commands.isEmpty()) {
                    sender.sendMessage(Main.getInstance().getMessages().getString("emptylist"));
                    break;
                }

                StringBuilder list = new StringBuilder("");
                AtomicInteger i = new AtomicInteger(0);

                commands.forEach(command -> {
                    int size = i.getAndIncrement();

                    list
                            .append(command)
                            .append(size + 1 == commands.size() ? "." : ", ");
                });

                sender.sendMessage(list.toString());

                break;
            }
            case "add": {
                if (args.length != 2) {
                    sender.sendMessage(Main.getInstance().getMessages().getString("missingarguments"));
                    break;
                }

                if (!args[1].startsWith("/")) {
                    sender.sendMessage("§6[ADCommands] §7O comando precisa começar com /");
                    break;
                }

                Main.getInstance().addCommand(args[1]);
                sender.sendMessage(Main.getInstance().getMessages().getString("added"));

                break;
            }
            case "remove": {
                if (args.length != 2) {
                    sender.sendMessage(Main.getInstance().getMessages().getString("missingarguments"));
                    break;
                }

                boolean remove = Main.getInstance().removeCommand(args[1]);

                if (!remove) {
                    sender.sendMessage(Main.getInstance().getMessages().getString("commandnotfound"));
                }

                sender.sendMessage(Main.getInstance().getMessages().getString("removed"));
                break;
            }
            case "removeall": {
                Main.getInstance().removeAllCommand();

                sender.sendMessage(Main.getInstance().getMessages().getString("removedall"));
                break;
            }
            case "reload": {
                Main.getInstance().loadConfig();

                sender.sendMessage(Main.getInstance().getMessages().getString("pluginreloaded"));
                break;
            }
            default: {
                Main.getInstance().getMessages().getStringList("messagehelp").forEach(sender::sendMessage);
                break;
            }
        }

        return true;
    }
}
