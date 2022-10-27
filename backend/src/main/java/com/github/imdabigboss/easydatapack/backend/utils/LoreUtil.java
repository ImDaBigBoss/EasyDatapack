package com.github.imdabigboss.easydatapack.backend.utils;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LoreUtil {
    public static final String NAMESPACE_KEY_PREFIX = "easyenchantment-";
    private static final String LORE_TAG_SPLITTER = "__x__";
    private static final Map<String, NamespacedKey> LORE_NAMESPACE_CACHE = new HashMap<>();

    public static void addLore(ItemStack item, String id, String text, int position) {
        List<String> lines = Arrays.asList(text.split(LORE_TAG_SPLITTER));

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        List<Component> lore = meta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lines = StringUtil.color(lines);

        StringBuilder loreTag = new StringBuilder();

        delLore(item, id);
        for (String line : lines) {
            position = addToLore(lore, position, line);

            if (loreTag.length() > 0) {
                loreTag.append(LORE_TAG_SPLITTER);
            }

            loreTag.append(line);
        }

        meta.lore(lore);
        item.setItemMeta(meta);

        PDCUtil.setData(item, getLoreNamespaceKey(id), loreTag.toString());
    }

    public static void delLore(ItemStack item, String id) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<Component> lore = meta.lore();
        if (lore == null) {
            return;
        }

        int index = getLoreIndex(item, id, 0);
        if (index < 0) {
            return;
        }

        int lastIndex = getLoreIndex(item, id, 1);
        int diff = lastIndex - index;

        for (int i = 0; i < (diff + 1); i++) {
            lore.remove(index);
        }

        meta.lore(lore);
        item.setItemMeta(meta);

        PDCUtil.removeData(item, getLoreNamespaceKey(id));
    }

    private static int addToLore(List<Component> lore, int position, String value) {
        if (position >= lore.size() || position < 0) {
            lore.add(Component.text(value));
        } else {
            lore.add(position, Component.text(value));
        }
        return position < 0 ? position : position + 1;
    }

    private static int getLoreIndex(ItemStack item, String id, int type) {
        String storedText = StringUtil.removeColors(PDCUtil.getData(item, getLoreNamespaceKey(id)));
        if (storedText.isEmpty() || storedText.isBlank()) {
            return -1;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return -1;
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            return -1;
        }

        String[] lines = storedText.split(LORE_TAG_SPLITTER);
        String lastText = null;
        int count = 0;

        if (type == 0) {
            for (String line : lines) {
                lastText = StringUtil.removeColors(line);
                if (!lastText.isEmpty()) {
                    break;
                }
                count--;
            }
        } else {
            for (int i = lines.length; i > 0; i--) {
                lastText = StringUtil.removeColors(lines[i - 1]);
                if (!lastText.isEmpty()) {
                    break;
                }
                count++;
            }
        }

        if (lastText == null) {
            return -1;
        }

        int index = lore.indexOf(ChatColor.GRAY + lastText) + count;

        if (index < 0) {
            PDCUtil.removeData(item, getLoreNamespaceKey(id));
        }
        return index;
    }

    private static NamespacedKey getLoreNamespaceKey(String id) {
        String lid = id.toLowerCase();
        return LORE_NAMESPACE_CACHE.computeIfAbsent(lid, key -> new NamespacedKey(EasyDatapackAPI.NAMESPACE_KEY, NAMESPACE_KEY_PREFIX + lid));
    }
}
