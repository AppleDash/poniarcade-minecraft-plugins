PoniArcade_Warps
================

This is the plugin that is used to provide in-game teleportation to predefined locations.

## Commands
- `/warp` - List warps
- `/warp [player] <warp name>` - Teleport to the given warp, or teleport another player there
- `/setwarp <warp name> [posX] [posY] [posZ]` - Set a new warp, either at your current location or at the given coordinates.
- `/delwarp <warp name>` - Delete the given warp

## Permissions
- `poniarcade.warps.warp` - Use /warp
- `poniarcade.warps.warp.other` - Use /warp on others
- `poniarcade.warps.set` - Set new warps
- `poniarcade.warps.delete` - Delete existing warps

## Database
Uses the following PostgreSQL table, which must exist before the plugin is used:

```sql
CREATE TABLE warps
(
    id INTEGER PRIMARY KEY NOT NULL,
    server_id SMALLINT NOT NULL,
    name VARCHAR(32) NOT NULL,
    world UUID NOT NULL,
    position_x NUMERIC(11,3) NOT NULL,
    position_y NUMERIC(6,3) NOT NULL,
    position_z NUMERIC(11,3) NOT NULL,
    yaw NUMERIC(4,1) NOT NULL,
    pitch NUMERIC(3,1) NOT NULL
);
CREATE UNIQUE INDEX warps_server_id_name_uindex ON warps (server_id, name);
```