package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsTeam;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.GAMEMODE;
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
import java.util.List;

/// @author ooney, yasha

public class TeamSpawnUIPage extends InteractiveCustomUIPage<TeamSpawnUIPage.TeamSelectedData> {

    private BedwarsMap thisMap;
    private GAMEMODE gamemode;

    private List<TeamColor> getAllowedTeams() {
        if (gamemode == null) return List.of();
        return BedwarsMap.teamsPerGamemode.getOrDefault(gamemode, List.of());
    }

    private List<DropdownEntryInfo> buildTeamEntries() {
        return getAllowedTeams().stream()
                .map(color -> new DropdownEntryInfo(
                        LocalizableString.fromString(color.getDisplayName()),
                        color.name()
                ))
                .toList();
    }


    public static class TeamSelectedData {
        public String button;
        public String team;
        public String inputXText = "0";
        public String inputYText = "0";
        public String inputZText = "0";

        public static final BuilderCodec<TeamSelectedData> CODEC  =
                BuilderCodec.builder(TeamSelectedData.class, TeamSelectedData::new)
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .addField(
                                new KeyedCodec<>("@TeamValue", Codec.STRING),
                                (obj, val) -> obj.team = val,
                                obj -> obj.team
                        )
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
                        .build();
    }

    public TeamSpawnUIPage(@Nonnull PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, TeamSelectedData.CODEC);
        thisMap = map;
        this.gamemode = map.getGamemode();
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt, @Nonnull Store<EntityStore> store) {

        cmd.append("Pages/TeamSpawn.ui");

        // Populating the dropdown
        cmd.set("#TeamColorDropdown.Entries", buildTeamEntries());
        cmd.set("#TeamColorDropdown.Value", GAMEMODE.ONES.name());

        // Reading values from the dropdown
        evt.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#TeamColorDropdown",
                EventData.of("Button", "TeamColorDropdown")
                        .append("@TeamValue", "#TeamColorDropdown.Value"),
                false
        );

        // Binding "Save" to send the data
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#Save",
                EventData.of("Button", "Coords")
                        .append("@InputX", "#InputX.Value")
                        .append("@InputY", "#InputY.Value")
                        .append("@InputZ", "#InputZ.Value")
                        .append("@TeamValue", "#TeamColorDropdown.Value"),
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
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TeamSelectedData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;


        if (data.button == null) return;

            switch (data.button) {

                case "Cancel" -> {
                    player.getPageManager().setPage(ref, store, Page.None);
                }


                case "TeamColorDropdown" -> {
                    TeamColor selected = TeamColor.valueOf(data.team);

                    player.sendMessage(
                            Message.raw("Selected team: " + selected.getDisplayName())
                    );
                }

                case "Coords" -> {

                    TeamColor selected = TeamColor.valueOf(data.team);

                    if (data.team == null) {
                        player.sendMessage(Message.raw("Please select a team before saving!"));
                        return;
                    }



                    try {
                        // Gather double values.
                        double x = data.inputXText != null && !data.inputXText.isEmpty() ? Double.parseDouble(data.inputXText) : 0;
                        double y = data.inputYText != null && !data.inputYText.isEmpty() ? Double.parseDouble(data.inputYText) : 0;
                        double z = data.inputZText != null && !data.inputZText.isEmpty() ? Double.parseDouble(data.inputZText) : 0;

                        // Message the player the coords they entered.
                        BedwarsMessenger.coordinateEntry(player, x, y, z, "team spawn");
                        player.sendMessage(
                                Message.raw("For the " + selected.getDisplayName() + " team!")
                        );

                        // Add the data to the BedwarsMap.
                        thisMap.setQueueSpawn(new com.hypixel.hytale.math.vector.Vector3d(x, y, z));

                        TeamColor color = TeamColor.valueOf(data.team);
                        Vector3d spawn = new Vector3d(x, y, z);

                        /// It needs a forge location, so we use the spawn as a placeholder and update it in the next UI
                        BedwarsTeam team = new BedwarsTeam(color.name(), spawn, spawn);

                        // If the numbers aren't numbers.
                    } catch (NumberFormatException e) {
                        BedwarsMessenger.invalidDoubleEntry(player);
                    }



                }
                case "Next" -> {
                    // Plug in the next UI page here
                    // player.getPageManager().openCustomPage(ref, store, new TeamSpawnUIPage(playerRef, thisMap));
                }
        }
    }
}
