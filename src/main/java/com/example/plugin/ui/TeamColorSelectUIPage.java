package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.example.plugin.utils.TeamColor;
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
        public String teamColor;

        public static final BuilderCodec<TeamSelectedData> CODEC  =
                BuilderCodec.builder(TeamSelectedData.class, TeamSelectedData::new)
                        .addField(
                                new KeyedCodec<>("TeamColor", Codec.STRING),
                                (obj, val) -> obj.teamColor = val,
                                obj -> obj.teamColor
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

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterBlackTeam", EventData.of("TeamColor", "BLACK"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterBlueTeam", EventData.of("TeamColor", "BLUE"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterCyanTeam", EventData.of("TeamColor", "CYAN"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterGreenTeam", EventData.of("TeamColor", "GREEN"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterOrangeTeam", EventData.of("TeamColor", "ORANGE"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterPinkTeam", EventData.of("TeamColor", "PINK"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterPurpleTeam", EventData.of("TeamColor", "PURPLE"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterRedTeam", EventData.of("TeamColor", "RED"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterWhiteTeam", EventData.of("TeamColor", "WHITE"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RegisterYellowTeam", EventData.of("TeamColor", "YELLOW"), false);
    }

    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TeamColorSelectUIPage.TeamSelectedData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;

        if (data.teamColor == null) return;

        TeamColor color;
        try {
            color = TeamColor.valueOf(data.teamColor);
        } catch(IllegalArgumentException e) {
            return;
        }

        thisTeam = new BedwarsTeam(
                color.getDisplayName(),
                new Vector3d(),
                new Vector3d()
        );

        player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap, thisTeam));

    }
}
