package com.example.plugin.ui;

import com.example.plugin.entityinstances.BedwarsMap;
import com.example.plugin.messenger.BedwarsMessenger;
import com.example.plugin.utils.GAMEMODE;
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
import com.hypixel.hytale.server.core.ui.DropdownEntryInfo;
import com.hypixel.hytale.server.core.ui.LocalizableString;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.*;


/// @author yasha

public class GamemodeUIPage extends InteractiveCustomUIPage<GamemodeUIPage.TeamSizeData> {

    private BedwarsMap thisMap;

    // Converted to Object[] Array, the list of gamemodes and their names are now in BedwarsMap.possibleGamemodes map.
    private Object[] gamemodes = BedwarsMap.possibleGamemodes.keySet().toArray();


    /** How this works
     *  Arrays.stream grabs from an Object[].
     *  Which is given by the possibleGamemodes map keys, converted to a set, converted to a map.
     * @param s in the .map statement is a new dropdown entry that has the key, and the value being the map of it off possibleGamemodes.
     * If this sounds confusing, I agree. I spent about 2 hours on this.
     */
    List<DropdownEntryInfo> entries = Arrays.stream((gamemodes))
            .map(s -> new DropdownEntryInfo(
                    LocalizableString.fromString(String.valueOf(s)),
                    BedwarsMap.possibleGamemodes.get(String.valueOf(s)).name()
            ))
            .toList();

    public static class TeamSizeData {
        public String button; // UI routes here
        public String gameValue;

        public static final BuilderCodec<TeamSizeData> CODEC =
                BuilderCodec.builder(TeamSizeData.class, TeamSizeData::new)
                        .addField(
                                new KeyedCodec<>("Button", Codec.STRING),
                                (obj, val) -> obj.button = val,
                                obj -> obj.button
                        )
                        .addField(
                                new KeyedCodec<>("@TeamValue", Codec.STRING),
                                (obj, val) -> obj.gameValue = val,
                                obj -> obj.gameValue
                        )
                        .build();

    }

    public GamemodeUIPage(PlayerRef playerRef, BedwarsMap map) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, TeamSizeData.CODEC);
        thisMap = map;
    }

    @Override
    public void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store) {

        cmd.append("Pages/Gamemode.ui");

        cmd.set("#TeamDropdown.Entries", entries);

        cmd.set("#TeamDropdown.Value", GAMEMODE.ONES.name());

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

        assert player != null : "Player is somehow null, in TeamSizeUIPage.handleDataEvent().";


        switch (data.button) {

            case "Cancel" -> {
                player.getPageManager().setPage(ref, store, Page.None);
                thisMap = null;
            }

            case "TeamDropdown" -> {
                GAMEMODE selected = GAMEMODE.valueOf(data.gameValue);

                BedwarsMessenger.selectedGamemode(player, selected);

                //TODO Send the selected gamemode
                thisMap.setGamemode(selected);


            }

            case "Next" -> {
                if (thisMap.getGamemode() == null) { // Do not allow player to pass without entering a valid value.
                    BedwarsMessenger.gamemodeNotSelected(player);
                } else {
                    player.getPageManager().openCustomPage(ref, store, new QueueUIPage(playerRef, thisMap));
                }
            }
        }
    }
}
