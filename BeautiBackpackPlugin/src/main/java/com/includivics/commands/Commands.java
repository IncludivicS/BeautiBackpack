package com.includivics.commands;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.items.BackpackItem;
import com.includivics.utilities.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.includivics.BeautiBackpackPlugin.pluginPrefix;

public class Commands implements CommandExecutor {

    private final BeautiBackpackPlugin main;

    public Commands(BeautiBackpackPlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        switch (args[0]) {
            case "give" -> doGiveCommand(sender, args);
            case "reload" -> doReloadCommand(sender);
            case "help" -> sendHelpMessage(sender);
        }

        return true;
    }

    private void doGiveCommand(CommandSender sender, String[] args) {
        boolean senderIsPlayer = sender instanceof Player;
        if (senderIsPlayer && !sender.hasPermission("beautibackpack.give")) {
            sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&4You don't have permission to do that!"));
            return;
        }
        if (args.length == 3) {

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            BackpackItem backpack = main.backpackConfig.getBackpack(args[2]);
            ItemStack backpackItemStack = backpack.getBackpackItemStack();
            String senderName = sender.getName();

            backpackItemStack = main.nms.setTag(backpackItemStack, "UUID", UUID.randomUUID().toString());
            backpackItemStack = main.nms.setTag(backpackItemStack, "owner", senderName);

            if (targetPlayer != null) {
                targetPlayer.getInventory().addItem(backpackItemStack);
                targetPlayer.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&6You got " + args[2] + " backpack from " + senderName));
                sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&aYou just gave " + args[2] + "backpack to " + targetPlayer.getName()));
            }
        } else {
            sendHelpMessage(sender);
        }

    }

    private void doReloadCommand(CommandSender sender) {
        if (sender instanceof Player && sender.hasPermission("beautibackpack.reload")) {
            sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&4You don't have permission to do that!"));
            return;
        }
        sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&ePlugin successfully reloaded"));
        main.mainConfig.load();
        main.backpackConfig.load();
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&4Command Usage"));
        sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&a/beautibackpack reload"));
        sender.sendMessage(ColorUtil.translateColorCodes(pluginPrefix + "&a/beautibackpack give [player] [backpackname]"));
    }

}
