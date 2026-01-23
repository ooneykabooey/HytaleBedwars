package com.example.plugin.ui;

import com.example.plugin.utils.GAMEMODE;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
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

public class WelcomeUIPage extends InteractiveCustomUIPage<WelcomeUIPage.WelcomeEventData> {

    public static class WelcomeEventData {
            public String button; // UI routes here
            public String teamValue;

            public static final BuilderCodec<WelcomeEventData> CODEC =
                    BuilderCodec.builder(WelcomeUIPage.WelcomeEventData.class, WelcomeUIPage.WelcomeEventData::new)
                            .addField(
                                    new KeyedCodec<>("Button", Codec.STRING),
                                    (obj, val) -> obj.button = val,
                                    obj -> obj.button
                            )
                            .build();
    }

    public WelcomeUIPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, WelcomeEventData.CODEC);
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/Welcome.ui");

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#WelcomeButtonsCancel",
                EventData.of("Button", "Cancel"),
                false
        );
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#WelcomeButtonsBegin",
                EventData.of("Button", "Begin"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull WelcomeEventData data) {

        Player player = store.getComponent(ref, Player.getComponentType());


        switch (data.button) {

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
            }

            case "Begin" -> {
                 player.getPageManager().openCustomPage(ref, store, new TeamSizeUIPage(playerRef));
            }
        }
    }
}
