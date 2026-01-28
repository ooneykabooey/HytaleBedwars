package com.example.plugin.shop;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class BedwarsShopItem {

    private final ItemStack cost;
    private final ItemStack reward;

    public BedwarsShopItem(String costId, int costQuantity, String rewardId, int rewardQuantity) {
        this.cost = new ItemStack(costId, costQuantity);
        this.reward = new ItemStack(rewardId, rewardQuantity);
    }

    public boolean tryPurchase(Player player) {
        ItemContainer mainContainer = player.getInventory().getCombinedEverything();

        if (mainContainer.canRemoveItemStack(cost)) {
            mainContainer.removeItemStack(cost);
            mainContainer.addItemStack(reward);
            player.sendMessage(Message.raw("Purchased " + reward.getQuantity() + " " + reward.getItemId() + " for " + cost.getQuantity() + " " + cost.getItemId()));
            return true;
        } else {
            player.sendMessage(Message.raw("Not enough " + cost.getItemId() + "!"));
            return false;
        }
    }

    public ItemStack getCost() {
        return cost;
    }

    public ItemStack getReward() {
        return reward;
    }
}
