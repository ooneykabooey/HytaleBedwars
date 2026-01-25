package com.example.plugin.ui;

import com.example.plugin.Bedwars;
import com.example.plugin.entityinstances.BedwarsMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
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

public class EndUIPage extends InteractiveCustomUIPage<EndUIPage.EndEventData> {
    public BedwarsMap thisMap;

    public static class EndEventData {
        public String button; // UI routes here

        public static final BuilderCodec<EndEventData> CODEC =
                BuilderCodec.builder(EndUIPage.EndEventData.class, EndUIPage.EndEventData::new)
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .build();
    }

    public EndUIPage(PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, EndEventData.CODEC);
        thisMap = map;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt, @Nonnull Store<EntityStore> store) {

        cmd.append("Pages/End.ui");

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#WelcomeButtonsBegin",
                EventData.of("Button", "Done"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull EndEventData data) {

        Player player = store.getComponent(ref, Player.getComponentType());


        switch (data.button) {

            case "Done" -> {
                Bedwars.registerMap(thisMap);
                player.getPageManager().setPage(ref, store, Page.None);
            }
        }
    }
}
