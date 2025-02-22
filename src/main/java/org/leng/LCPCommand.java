package org.leng;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.UUID;

public class LCPCommand implements CommandExecutor {
    private final LengContainerPreview plugin;
    private final LCPDataManager dataManager;

    public LCPCommand(LengContainerPreview plugin, LCPDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用此命令！");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId(); // 使用 UUID

        if (!player.hasPermission("LCP.preview")) {
            player.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        if (args.length == 0) {
            // 显示帮助信息
            player.sendMessage(ChatColor.GOLD + "===== LengContainerPreview 帮助 =====");
            player.sendMessage(ChatColor.YELLOW + "/lcp on - 开启容器预览");
            player.sendMessage(ChatColor.YELLOW + "/lcp off - 关闭容器预览");
            player.sendMessage(ChatColor.YELLOW + "/lcp - 显示帮助信息");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                dataManager.setPreviewEnabled(playerId, true);
                player.sendMessage(ChatColor.GREEN + "容器预览已开启！");
                break;
            case "off":
                dataManager.setPreviewEnabled(playerId, false);
                player.sendMessage(ChatColor.RED + "容器预览已关闭！");
                break;
            default:
                player.sendMessage(ChatColor.RED + "未知命令！使用 /lcp 查看帮助。");
                break;
        }

        return true;
    }
}