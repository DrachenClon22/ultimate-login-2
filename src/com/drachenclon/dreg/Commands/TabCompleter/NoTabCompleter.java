package com.drachenclon.dreg.Commands.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.Collections;
import java.util.List;

public class NoTabCompleter implements TabCompleter {
    public static final NoTabCompleter INSTANCE = new NoTabCompleter();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}