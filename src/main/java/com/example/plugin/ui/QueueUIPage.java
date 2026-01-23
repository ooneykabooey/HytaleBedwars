package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
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


/// @author yasha

public class QueueUIPage extends InteractiveCustomUIPage<QueueUIPage.QueueData> {

    private BedwarsMap thisMap;

    public static class QueueData {
        public String button;
        public String inputXText = "0";
        public String inputYText = "0";
        public String inputZText = "0";

        public static final BuilderCodec<QueueData> CODEC =
                BuilderCodec.builder(QueueData.class, QueueData::new)
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
                                new KeyedCodec<>("@InputZ", Codec.STRING),
                                (obj, val) -> obj.inputZText = val,
                                obj -> obj.inputZText
                        )
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .build();

    }

    public QueueUIPage(PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, QueueData.CODEC);
        thisMap = map;
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/Queue.ui");

        // Binding "Submit" to send the data
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#Submit",
                EventData.of("Button", "Coords")
                        .append("@InputX", "#InputX.Value")
                        .append("@InputY", "#InputY.Value")
                        .append("@InputZ", "#InputZ.Value"),
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
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull QueueData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;

         if (data.button == null) return; // safety check

        switch (data.button) {

            case "Coords" -> {
                try {
                    // Gather double values.
                    double x = data.inputXText != null && !data.inputXText.isEmpty() ? Double.parseDouble(data.inputXText) : 0;
                    double y = data.inputYText != null && !data.inputYText.isEmpty() ? Double.parseDouble(data.inputYText) : 0;
                    double z = data.inputZText != null && !data.inputZText.isEmpty() ? Double.parseDouble(data.inputZText) : 0;

                    // Message the player the coords they entered.
                    BedwarsMessenger.coordinateEntry(player, x, y, z, "queue");

                    // Add the data to the BedwarsMap.
                    thisMap.setQueueSpawn(new Vector3d(x, y, z));

                    // If the numbers aren't numbers.
                } catch (NumberFormatException e) {
                    BedwarsMessenger.invalidDoubleEntry(player);
                }

            }

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
            }

            case "Next" -> {
                player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap)); // TODO: MAKE SURE TO ADD TO THE NEW UI'S CONSTRUCTOR: BedwarsMap map, COPY OVER thisMap.
            }


        }

    }
}
