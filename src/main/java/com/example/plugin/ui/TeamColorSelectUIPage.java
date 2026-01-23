package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsTeam;
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

/// @author ooney


public class TeamColorSelectUIPage extends InteractiveCustomUIPage<TeamColorSelectUIPage.TeamSelectedData> {

    private BedwarsMap thisMap;
    private BedwarsTeam thisTeam;

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

                /// There's probably a way to loop through the TeamColor enum's values, and see if the button's ID matches the teamcolor, then creates a bedwarsteam with that color's ID.
                case "K" ->{
                    thisTeam = new BedwarsTeam("Black", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "B" ->{
                    thisTeam = new BedwarsTeam("Blue", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "C" ->{
                    thisTeam = new BedwarsTeam("Cyan", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "G" ->{
                    thisTeam = new BedwarsTeam("Green", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "O" ->{
                    thisTeam = new BedwarsTeam("Orange", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "PI" ->{
                    thisTeam = new BedwarsTeam("Pink", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "PU" ->{
                    thisTeam = new BedwarsTeam("Purple", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "R" ->{
                    thisTeam = new BedwarsTeam("Red", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "W" ->{
                    thisTeam = new BedwarsTeam("White", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
                case "Y" ->{
                    thisTeam = new BedwarsTeam("Yellow", new Vector3d(), new Vector3d());
                    player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));                }
        }
    }
}
