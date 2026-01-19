package com.example.plugin.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class GreetEventData {
    public String playerName; // UI routes here

    public static final BuilderCodec<GreetEventData> CODEC =
            BuilderCodec.builder(GreetEventData.class, GreetEventData::new)
                    .append(
                            new KeyedCodec<>("@PlayerName", Codec.STRING),
                            (obj, val) -> obj.playerName = val,
                            obj -> obj.playerName
                    )
                    .add()
                    .build();

}
