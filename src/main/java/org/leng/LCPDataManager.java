package org.leng;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LCPDataManager {
    private final Map<UUID, Inventory> containerCache = new HashMap<>(); // 容器缓存
    private final Map<UUID, Long> cacheTimestamps = new HashMap<>(); // 缓存时间戳
    private final Map<UUID, Boolean> previewStatus = new HashMap<>(); // 玩家预览状态

    public boolean isPreviewEnabled(UUID playerId) {
        return previewStatus.getOrDefault(playerId, true); // 默认开启
    }

    public void setPreviewEnabled(UUID playerId, boolean enabled) {
        previewStatus.put(playerId, enabled);
    }

    public void cacheContainer(UUID playerId, Inventory inventory) {
        containerCache.put(playerId, inventory);
        cacheTimestamps.put(playerId, System.currentTimeMillis());
    }

    public Inventory getCachedContainer(UUID playerId) {
        return containerCache.get(playerId);
    }

    public void clearExpiredCache() {
        long currentTime = System.currentTimeMillis();
        cacheTimestamps.entrySet().removeIf(entry -> currentTime - entry.getValue() > 60000); // 清理过期缓存
    }
}