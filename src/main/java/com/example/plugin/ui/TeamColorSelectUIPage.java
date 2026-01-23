package com.example.plugin.ui;

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

/// @author ooney

public class TeamColorSelectUIPage extends InteractiveCustomUIPage<TeamColorSelectUIPage.TeamSelectedData> {

    private BedwarsMap thisMap;

    public static class TeamSelectedData {
        public String button;

        public static final BuilderCodec<TeamSelectedData> CODEC  =
                BuilderCodec.builder(TeamSelectedData.class, TeamSelectedData::new)
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .build();
    }

    public TeamColorSelectUIPage(@Nonnull PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, TeamSelectedData.CODEC);
        thisMap = map;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt, @Nonnull Store<EntityStore> store) {

        cmd.append("Pages/TeamColorSelect.ui");

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterBlackTeam", EventData.of("Button", "K"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterBlueTeam", EventData.of("Button", "B"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterCyanTeam", EventData.of("Button", "C"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterGreenTeam", EventData.of("Button", "G"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterOrangeTeam", EventData.of("Button", "O"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterPinkTeam", EventData.of("Button", "PI"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterPurpleTeam", EventData.of("Button", "PU"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterRedTeam", EventData.of("Button", "R"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterWhiteTeam", EventData.of("Button", "W"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterYellowTeam", EventData.of("Button", "Y"), false);
    }

    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TeamColorSelectUIPage.TeamSelectedData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;

        if (data.button == null) return;

            switch (data.button) {

                // TODO: No need for all of these to point to the same page.
                // TODO: Heavy polish, especially on how tf .ui files work.
                case "K" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "B" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "C" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "G" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "O" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "PI" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "PU" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "R" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "W" ->{player.getPageManager().setPage(ref, store, Page.None);}
                case "Y" ->{player.getPageManager().setPage(ref, store, Page.None);}
        }
    }
}
