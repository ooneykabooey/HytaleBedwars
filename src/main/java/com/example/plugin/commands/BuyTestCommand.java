package com.example.plugin.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class BuyTestCommand extends AbstractPlayerCommand {

    public BuyTestCommand() {
        super("buytest", "This command test the buy system");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        Inventory inventory = player.getInventory();
        
        // The main container holds the player's primary inventory slots
        ItemContainer mainContainer = inventory.getCombinedEverything();

        // Define the cost (e.g., 4 Iron Ingots)
        ItemStack cost = new ItemStack("Ingredient_Bar_Iron", 1);

        // Define the reward (e.g., 1 Wool Block)
        ItemStack reward = new ItemStack("Cloth_Block_Wool_White", 4);

        // Check if the player has enough of the cost item in their main container
        if (mainContainer.canRemoveItemStack(cost)) {
            // Remove the cost item
            mainContainer.removeItemStack(cost);
            
            // Add the reward item
            mainContainer.addItemStack(reward);
            
            player.sendMessage(Message.raw("Purchase successful! Exchanged " + cost.getQuantity() + " " + cost.getItemId() + " for " + reward.getQuantity() + " " + reward.getItemId() + "."));
        } else {
            player.sendMessage(Message.raw("You do not have enough " + cost.getItemId() + "!"));
        }
    }
}
