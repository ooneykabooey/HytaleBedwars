package com.example.plugin.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.EntityEffect;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;


/// @author yasha

public class TestUIPage extends InteractiveCustomUIPage<TestUIPage.GreetEventData> {

    public static class GreetEventData {
        public String playerName; // UI routes here

        public static final BuilderCodec<GreetEventData> CODEC =
                BuilderCodec.builder(GreetEventData.class, GreetEventData::new)
                        .append(
                                // "@PlayerName" = read from UI input (the @ prefix is important!)
                                new KeyedCodec<>("@PlayerName", Codec.STRING),
                                // Setter: put the value into obj.playerName
                                (obj, val) -> obj.playerName = val,
                                // Getter: read the value from obj.playerName
                                obj -> obj.playerName
                        )
                        .add()
                        .build();

    }

    public TestUIPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, GreetEventData.CODEC);
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {
        cmd.append("Pages/Welcome.ui");

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#WelcomeButtonsCancel", new EventData().append("@PlayerName", "#NameInput.Value"));
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull GreetEventData data) {

        Player player = store.getComponent(ref, Player.getComponentType());

        String name = data.playerName != null && !data.playerName.isEmpty()
                ? data.playerName
                : "Stranger";

        playerRef.sendMessage(Message.raw("Hello, " + name + "!"));

        player.getPageManager().setPage(ref, store, Page.None);
    }
}
