package me.natanfoleto.adcommands.listeners;

import me.natanfoleto.adcommands.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommand implements Listener {
    @EventHandler
    public void onPlayerCommandExecute(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPermission("adc.bypass")) {
            List<String> commands = Main.getInstance().getCommands();

            switch (Main.getInstance().getConfig().getString("allowordeny")) {
                case "allow": {
                    boolean cmdFound = false;
                    String command = e.getMessage();

                    for (String cmd : commands) {
                        if (cmd.startsWith(command)) {
                            cmdFound = true;
                            break;
                        }
                    }

                    if (!cmdFound) {
                        e.setCancelled(true);

                        if (Main.getInstance().getConfig().getBoolean("showmessage")) {
                            p.sendMessage(Main.getInstance().getMessages().getString("commandnotfound").replace("%command%", command));
                        }

                        break;
                    }

                    break;
                }
                case "deny": {
                    String command = e.getMessage();

                    for (String cmd : commands) {
                        if (cmd.startsWith(command)) {
                            e.setCancelled(true);

                            if (Main.getInstance().getConfig().getBoolean("showmessage")) {
                                p.sendMessage(Main.getInstance().getMessages().getString("commandnotfound").replace("%command%", command));
                            }

                            break;
                        }
                    }

                    break;
                }
                default: {
                    e.setCancelled(true);

                    p.sendMessage("§6[ADCommands] §7Use 'allow' ou 'deny' na propriedade <allowordeny> na config do plugin." );
                    break;
                }
            }
        }
    }
}
