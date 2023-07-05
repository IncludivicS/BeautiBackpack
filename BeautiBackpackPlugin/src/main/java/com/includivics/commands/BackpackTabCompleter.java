package com.includivics.commands;

import com.includivics.BeautiBackpackPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackpackTabCompleter implements org.bukkit.command.TabCompleter {

    private final BeautiBackpackPlugin main;

    public BackpackTabCompleter(BeautiBackpackPlugin main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("give");
            completions.add("help");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                completions.addAll(main.backpackConfig.getbackpacklist());
            }
        }
        // Filter the completions list to only include those that start with the current argument
        completions.removeIf(s -> !s.startsWith(args[args.length - 1]));

        return completions;
    }
}
