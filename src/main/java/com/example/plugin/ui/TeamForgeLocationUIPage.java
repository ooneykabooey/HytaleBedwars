package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.entityinstances.BedwarsTeam;
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

public class TeamForgeLocationUIPage extends InteractiveCustomUIPage<TeamForgeLocationUIPage.ForgeData> {

    private BedwarsMap thisMap;
    private BedwarsTeam thisTeam;

    public static class ForgeData {
        public String button;
        public String inputXText = "0";
        public String inputYText = "0";
        public String inputZText = "0";

        public static final BuilderCodec<ForgeData> CODEC =
                BuilderCodec.builder(ForgeData.class, ForgeData::new)
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

    public TeamForgeLocationUIPage(PlayerRef playerRef, BedwarsMap map, BedwarsTeam team) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ForgeData.CODEC);
        thisMap = map;
        thisTeam = team;
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/TeamBedLocation.ui");

        // Binding "Submit" to send the data
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#Submit",
                EventData.of("Button", "Submit")
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
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull ForgeData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        assert player != null;

        if (data.button == null) return; // safety check

        switch (data.button) {

            case "Submit" -> {
                try {
                    // Gather double values.
                    double x = data.inputXText != null && !data.inputXText.isEmpty() ? Double.parseDouble(data.inputXText) : 0;
                    double y = data.inputYText != null && !data.inputYText.isEmpty() ? Double.parseDouble(data.inputYText) : 0;
                    double z = data.inputZText != null && !data.inputZText.isEmpty() ? Double.parseDouble(data.inputZText) : 0;

                    // Message the player the coords they entered.
                    BedwarsMessenger.coordinateEntry(player, x, y, z, "bed location of the " + thisTeam.getId() + " team");

                    // Add the data to the BedwarsTeam.
                    thisTeam.setForgeLocation(new Vector3d(x, y, z));
                    // TODO: Somehow add a BedwarsItemTimer to the BedwarsItemTimerManager.
                    // TODO: Maybe eliminate static lists of players and such and only attach it to BedwarsMap, just for thread safety.

                    // If the numbers aren't numbers.
                } catch (NumberFormatException e) {
                    BedwarsMessenger.invalidDoubleEntry(player);
                }

            }

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
                thisMap = null;
                thisTeam = null;
            }

            case "Next" -> {
                player.getPageManager().openCustomPage(ref, store, new TeamColorSelectUIPage(playerRef, thisMap)); // TODO: MAKE SURE TO ADD TO THE NEW UI'S CONSTRUCTOR: BedwarsMap map, COPY OVER thisMap.
            }


        }
    }
}
