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
import com.hypixel.hytale.server.core.ui.DropdownEntryInfo;
import com.hypixel.hytale.server.core.ui.LocalizableString;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;


/// @author yasha

public class TeamSizeUIPage extends InteractiveCustomUIPage<TeamSizeUIPage.TeamSizeData> {

    // Makes the enum readable lol
    private static String formatGamemodeName(GAMEMODE mode) {
        return switch (mode) {
            case ONES -> "Solos (1v1s)";
            case TWOS -> "Doubles (2v2s)";
            case THREES -> "Trios (3v3s)";
            case FOURS -> "Squads (4v4s)";
            case FOURAFOUR -> "Squad v Squad (4v4)";
        };
    }

    // blah blah blah convert to data or sum. Imma be so fr chat gpt had to help out with this one :pray:
    List<DropdownEntryInfo> entries = Arrays.stream(GAMEMODE.values())
            .map(mode -> new DropdownEntryInfo(
                    LocalizableString.fromString(formatGamemodeName(mode)),
                    mode.name()
            ))
            .toList();

    public static class TeamSizeData {
        public String button; // UI routes here
        public String teamValue;

        public static final BuilderCodec<TeamSizeData> CODEC =
                BuilderCodec.builder(TeamSizeData.class, TeamSizeData::new)
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .addField(
                                new KeyedCodec<>("@TeamValue", Codec.STRING),
                                (obj, val) -> obj.teamValue = val,
                                obj -> obj.teamValue
                        )
                        .build();

    }

    public TeamSizeUIPage(PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, TeamSizeData.CODEC);
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/TeamSize.ui");

        cmd.set("#TeamDropdown.Entries", entries);

        cmd.set("#TeamDropdown.Value", GAMEMODE.TWOS.name());

        evt.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#TeamDropdown",
                EventData.of("Button", "TeamDropdown")
                        .append("@TeamValue", "#TeamDropdown.Value"),
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
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TeamSizeData data) {

        Player player = store.getComponent(ref, Player.getComponentType());


        switch (data.button) {

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
            }

            case "TeamDropdown" -> {
                GAMEMODE selected = GAMEMODE.valueOf(data.teamValue);

                player.sendMessage(
                        Message.raw("Selected gamemode: " + selected.name())
                );

                //TODO Send the selected gamemode
            }

            case "Next" -> {
                player.getPageManager().openCustomPage(ref, store, new QueueUIPage(playerRef));
            }
        }
    }
}
