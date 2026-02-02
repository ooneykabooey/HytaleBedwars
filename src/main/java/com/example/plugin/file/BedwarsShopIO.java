package com.example.plugin.file;

import com.example.plugin.shop.BedwarsShopItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.logger.HytaleLogger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/// author yasha

public class BedwarsShopIO {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    // Thread-safe list to store active shop items
    private final List<BedwarsShopItem> shopItems = new CopyOnWriteArrayList<>();
    private final Path configDir = Path.of("bedwars_shop");
    private final Path configPath = configDir.resolve("shop.json");

    public void load() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                LOGGER.atWarning().log("Failed to create shop directory", e);
                return;
            }
        }

        if (!Files.exists(configPath)) {
            createDefault();
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            List<ShopEntry> entries = GSON.fromJson(reader, new TypeToken<List<ShopEntry>>(){}.getType());
            
            List<BedwarsShopItem> loadedItems = new ArrayList<>();
            if (entries != null) {
                for (ShopEntry entry : entries) {
                    loadedItems.add(new BedwarsShopItem(
                        entry.costId, 
                        entry.costQuantity, 
                        entry.rewardId, 
                        entry.rewardQuantity
                    ));
                }
            }

            shopItems.clear();
            shopItems.addAll(loadedItems);
            LOGGER.atInfo().log("Loaded %d shop items.", shopItems.size());
            
        } catch (IOException e) {
            LOGGER.atWarning().log("Failed to load shop items", e);
        }
    }

    public void createDefault() {
        List<ShopEntry> defaults = new ArrayList<>();
        defaults.add(new ShopEntry("iron_ingot", 1, "block_cloth_wool_black", 4));
        defaults.add(new ShopEntry("iron_ingot", 10, "leather_chestplate", 1));

        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(defaults, writer);
            LOGGER.atInfo().log("Created default shop config at %s", configPath);
        } catch (IOException e) {
            LOGGER.atWarning().log("Failed to create default shop config", e);
        }
    }

    public List<BedwarsShopItem> getItems() {
        return Collections.unmodifiableList(shopItems);
    }

    // DTO class for JSON mapping
    private static class ShopEntry {
        String costId;
        int costQuantity;
        String rewardId;
        int rewardQuantity;

        ShopEntry() {} // For Gson
        ShopEntry(String costId, int costQuantity, String rewardId, int rewardQuantity) {
            this.costId = costId;
            this.costQuantity = costQuantity;
            this.rewardId = rewardId;
            this.rewardQuantity = rewardQuantity;
        }
    }
}