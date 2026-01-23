## Things to consider

#### Spawnpoints

Players' spawns are not attached to their beds.

But their beds being there determine if they
are able to respawn back at their places or if they're eliminated.

For that, somehow develop a system where a player dying will tp them back to a certain coordinate set to their team.

Make sure that there's no spawn conflictions.

To standardize it, make a circle of 8 blocks encircling a single block as points of the map,
where each of the 8 blocks represent spawnpoints.

Give an op or console menu to change the type of map preset to use.

##### Types of bedwars maps to use

- 1vx8: The classic 8 bedwars free for all.
- 2v2s: Same thing. Make sure drops are slightly faster.
- 3vx4: Only four teams; therefore only 4 blocks surrounding mid. Make sure drops are faster.
- 4vx4: Same thing, just even faster drops.
- 4v4: Only two teams across from each other; same drops as 4vx4

#### Item Spawns

LET:
- Iron -> Essence of Life
- Gold -> Essence of Ice
- Diamond -> Essence of Fire
- Emerald -> Essence of the Void

There are set blocks to determine what will spawn on them.

##### Blocks
- Iron/Gold: Wood_Village_Wall_Full
- Diamond: Wood_Village_Wall_Yellow_Full
- Emerald: Wood_Village_Wall_White_Full


##### Rates

- Iron: 0.6 - 0.8 seconds
- Gold: 8 - 10 seconds
- Diamond: 45 -> 30 -> 15 seconds
- Emerald: 60 -> 45 -> 30 seconds

Iron and Gold rates are affected by team upgrades, and the ability to spawn diamonds/emeralds on them.

#### Team Upgrades

There are upgrades teams can buy.

##### Player upgrades

- Try to add upgrades to increase a user's defense, health, or regen.
- Try to add upgrades allowing the user to mine faster.
- Try to add upgrades allowing the user to deal more damage.


##### Island upgrades

- Iron, Gold, Emerald forge.
- Proximity alarm
- Spawn mobs.

# For UI Sequence - Backend

When the user starts the registry process, a BedwarsMap thisMap is created.

thisMap will take submitted data and will be passed on in the next page's constructor as part of its "thisMap."

it will keep taking data until the BedwarsMap is complete, if anything is missing, there will be checks ran and the player will be notified/stopped from making progress in their registry.

The BedwarsMapManager is a parallel universe that stores BedwarsMaps in a set. They are to be accessed from a server lobby to pick from randomly when entering a bedwars queue.

