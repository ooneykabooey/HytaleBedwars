package com.example.plugin.ui;

import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.Vector3d;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
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



    public static class QueueData {
        public String button;
        public String inputXText = "0";
        public String inputYText = "0";
        public String inputZText = "0";
        public Vector3d position;

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

    public QueueUIPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, QueueData.CODEC);
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

         if (data.button == null) return; // safety check

        switch (data.button) {

            case "Coords" -> {
                try {
                    double x = data.inputXText != null && !data.inputXText.isEmpty() ? Double.parseDouble(data.inputXText) : 0;
                    double y = data.inputYText != null && !data.inputYText.isEmpty() ? Double.parseDouble(data.inputYText) : 0;
                    double z = data.inputZText != null && !data.inputZText.isEmpty() ? Double.parseDouble(data.inputZText) : 0;

                    data.position = new Vector3d(x, y, z);

                    player.sendMessage(Message.raw(
                            "You entered the coordinates: " + x + ", " + y + " and " + z + " for the queue!"
                    ));

                    //TODO Send Vector3d to Queue system
                } catch (NumberFormatException e) {
                    player.sendMessage(Message.raw("Please only enter valid numbers! Examples: 1, 23, 29.3"));
                }

            }

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
            }

            case "Next" -> {
               // player.getPageManager(ref, store, PAGE);
            }


        }

    }
}
