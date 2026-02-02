package com.example.plugin.ui;

import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.events.UIContext;
import au.ellie.hyui.html.TemplateProcessor;
import com.example.plugin.Bedwars;
import com.example.plugin.shop.BedwarsShopItem;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class HyUIShopPage {

    public void open(PlayerRef playerRef, Store<EntityStore> store) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return;

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        List<BedwarsShopItem> items = Bedwars.getInstance().getShopManager().getItems();
        ItemContainer inv = player.getInventory().getCombinedEverything();

        // Prepare shop item data
        var shopItems = items.stream().map(item -> {
            long haveCount = 0;
            if (inv != null) {
                for (short s = 0; s < inv.getCapacity(); s++) {
                    ItemStack stack = inv.getItemStack(s);
                    if (stack != null && !stack.isEmpty() && stack.getItemId().equals(item.getCost().getItemId())) {
                        haveCount += stack.getQuantity();
                    }
                }
            }

            return new ShopItemModel(
                    items.indexOf(item),
                    item.getReward().getItemId(),
                    item.getReward().getQuantity(),
                    item.getCost().getItemId(),
                    item.getCost().getQuantity(),
                    haveCount
            );
        }).toList();

        // Define the template with pure HYUIML
        TemplateProcessor template = new TemplateProcessor()
                .setVariable("shopItems", shopItems)
                .registerComponent("shopItemCard", """
                        <div style="anchor-width: 64; anchor-height: 100; layout-mode: top;">
                        <div class="item" style="anchor-height: 64; anchor-width: 128; layout-mode: left;">
                        <div class="cost-section" style="anchor-height: 64; anchor-width: 64;">
                                    <span class="item-slot" data-hyui-item-id="{{$costId}}" data-hyui-show-quality-background="true"></span>
                                    <div style="layout-mode: top;">
                                    <p class="input-quantity" style="anchor-height: 44;">{{$costQty}}</p>
                                    <p class="have-label" style="flex-weight: 1;">Have: {{$haveCount}}</p>
                                    </div>
                        </div>
                        <div class="reward-section" style="anchor-width: 64;">
                                    <span class="item-slot" data-hyui-item-id="{{$rewardId}}" data-hyui-show-quality-background="true"></span>
                                    <p class="reward-quantity">{{$rewardQty}}</p>
                        </div>
                        </div>
                        <button id="buy-btn-{{$index}}" class="shop-card-btn" style="anchor-width: 128; anchor-height 64;">Buy</button>
                        </div>
                        """);

        String html = template.process("""
<div class="page-overlay" style="anchor-width: 1600; anchor-height: 800;">
  <div class="decorated-container" data-hyui-title="Bedwars Item Shop"
  style="anchor-width: 1600; anchor-height: 800;">

    <!-- Scrollable shop grid -->
    <div class="container" style="anchor-height: 400; anchor-width: 400;">
    <div style="layout-mode: topscrolling; anchor-height: 300; anchor-width: 420;">
      {{#each shopItems}}
        {{@shopItemCard:index={{$index}},rewardId={{$rewardId}},rewardQty={{$rewardQty}},
                            costId={{$costId}},costQty={{$costQty}},haveCount={{$haveCount}}}}
      {{/each}}
      </div>
    </div>

    <!-- Close button -->
    <button class="secondary-button" id="close-btn" style="anchor-width: 160; anchor-height: 60; flex-weight: 1;">Close</button>
  </div>
</div>
""");


        // Build the page
        PageBuilder builder = PageBuilder.pageForPlayer(playerRef)
                .withLifetime(CustomPageLifetime.CanDismissOrCloseThroughInteraction)
                .fromHtml(html);

        // Add buy button listeners
        for (int i = 0; i < items.size(); i++) {
            int index = i;
            builder.addEventListener("buy-btn-" + index, CustomUIEventBindingType.Activating, (data, ctx) -> {
                handlePurchase(ctx, player, items.get(index), playerRef);
            });
        }

        // Add close button
        builder.addEventListener("close-btn", CustomUIEventBindingType.Activating, (data, ctx) -> {
            ctx.getPage().ifPresent(page -> page.close());
        });

        builder.open(store);
    }

    private void handlePurchase(UIContext ctx, Player player, BedwarsShopItem item, PlayerRef playerRef) {
        if (item.tryPurchase(player)) {
            Ref<EntityStore> ref = playerRef.getReference();
            if (ref != null) {
                open(playerRef, ref.getStore());
            }
        }
    }

    // Record for template data
    public record ShopItemModel(
            int index,
            String rewardId,
            int rewardQty,
            String costId,
            int costQty,
            long haveCount
    ) {}
}
