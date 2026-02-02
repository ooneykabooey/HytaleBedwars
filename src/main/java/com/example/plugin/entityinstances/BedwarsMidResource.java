package com.example.plugin.entityinstances;


import com.example.plugin.entityinstances.BedwarsItemTimer;
import com.hypixel.hytale.math.vector.Vector3d;

///  @author ooney
///  This is an item spawner FOR MID RESOURCES, contains the item timer, and an ID for what resource it is, as well as any getter/setter methods..
public class BedwarsMidResource {

    private Vector3d resourceLocation;
    private BedwarsItemTimer spawner;
    private String ID; // FIRE, VOID
    private int duration;

    public BedwarsMidResource(Vector3d resourceLocation, String ID, int duration) {
        // TODO: Need the data from the resource type dropdown from the resource UI.

        this.resourceLocation = resourceLocation;
        this.ID = ID;
        this.duration = duration;

    }

    public Vector3d getResourceLocation() {
        return resourceLocation;
    }

    public String getID() {
        return ID;
    }

    public int getDuration() {
        return duration;
    }


}
