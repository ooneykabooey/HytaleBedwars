package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;


/// @author yasha

public class SelectNumberOfMidForgesUIPage extends InteractiveCustomUIPage<SelectNumberOfMidForgesUIPage.ForgeData> {

    private BedwarsMap thisMap;
    private int numDiamondGens;
    private int numEmeraldGens;


    public static class ForgeData {
        public String button;
        public String inputXText = "0";
        public String inputYText = "0";

        public static final BuilderCodec<ForgeData> CODEC =
                BuilderCodec.builder(ForgeData.class, ForgeData::new)
                        .addField(
                                new KeyedCodec<>("@InputX", Codec.STRING),
                                ( obj, val) -> obj.inputXText = val,
                                obj -> obj.inputXText
                        )
                        .addField(
                                new KeyedCodec<>("@InputY", Codec.STRING),
                                (obj, val) -> obj.inputYText = val,
                                obj -> obj.inputYText
                        )
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .build();

    }

    public SelectNumberOfMidForgesUIPage(PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ForgeData.CODEC);
        thisMap = map;

    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/SelectNumberOfMidForges.ui");

        // Binding "Submit" to send the data
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#Submit",
                EventData.of("Button", "Submit")
                        .append("@InputX", "#InputX.Value")
                        .append("@InputY", "#InputY.Value"),
                false
        );

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ButtonsCancel",
                EventData.of("Button", "Cancel"),
                false
        );

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#ButtonsNext",
                EventData.of("Button", "Next"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ForgeData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;

        if (data.button == null) return; // safety check

        switch (data.button) {

            case "Submit" -> {
                try {
                    // Gather double values.
                    int x = data.inputXText != null && !data.inputXText.isEmpty() ? Integer.parseInt(data.inputXText) : 0;
                    int y = data.inputYText != null && !data.inputYText.isEmpty() ? Integer.parseInt(data.inputYText) : 0;

                    // Message the player the number of forges they are to register.
                    BedwarsMessenger.midForgeEntry(player, x, y);
                    numDiamondGens = x;
                    numEmeraldGens = y;

                    // If the numbers aren't numbers.
                } catch (NumberFormatException e) {
                    BedwarsMessenger.invalidDoubleEntry(player);
                }

            }

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
                thisMap = null;
            }

            case "Next" -> {
                //TODO: reroute back to this same forge if count <= registered forges. carry count over in the constructor.
                // TODO: message player if they have entered less than 2 emerald forges and/or less than 4 diamond forges.
                player.getPageManager().openCustomPage(ref, store, new DiamondForgeLocationUIPage(playerRef, thisMap, numDiamondGens, numEmeraldGens, 0));
            }


        }
    }
}
