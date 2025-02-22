package org.leng;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class LCPListener implements Listener {
    private final LengContainerPreview plugin;
    private final LCPDataManager dataManager;

    public LCPListener(LengContainerPreview plugin, LCPDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("已启用 LengContainerPreview 容器预览功能！");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // 检查预览功能是否开启
        if (!dataManager.isPreviewEnabled(playerId)) {
            return;
        }

        // 获取玩家准心对准的方块
        Block targetBlock = getTargetBlock(player);
        if (targetBlock != null && targetBlock.getState() instanceof Container) {
            Container container = (Container) targetBlock.getState();
            Inventory inventory = container.getInventory();

            // 缓存容器数据
            dataManager.cacheContainer(playerId, inventory);

            // 发送容器数据
            sendContainerData(player, inventory);
        }
    }

    private Block getTargetBlock(Player player) {
        RayTraceResult rayTrace = player.rayTraceBlocks(5); // 5 格距离
        if (rayTrace != null) {
            return rayTrace.getHitBlock();
        }
        return null;
    }

    private void sendContainerData(Player player, Inventory container) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);

        try {
            // 发送容器大小
            out.writeInt(container.getSize());

            // 发送每个物品
            for (ItemStack item : container.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    out.writeUTF(item.getType().name()); // 物品类型
                    out.writeInt(item.getAmount());     // 物品数量
                } else {
                    out.writeUTF("AIR"); // 空物品
                    out.writeInt(0);
                }
            }

            // 发送数据
            player.sendPluginMessage(plugin, "lengcontainer:preview", outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}