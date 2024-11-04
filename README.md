PoniArcade Minecraft
====================

This is the main repository for all the custom plugins previously used at PoniArcade Minecraft.

This repository does not include all the plugins, and does not include the Git commit history of the plugins.

`master` is currently built against Spigot 1.19.

Components
----------

### PoniArcade_Core
Contains commands that every server needed - think along the lines of Essentials.

### PoniArcade_Database
Maintains a connection to a PostgreSQL database, and allows other plugins to use the database.

### PoniArcade_ClassesNG
Provides a "class" system, which allows users to join various "classes" which grant different abilities and other cool features.

### PoniArcade_Homes
Provides a "homes" system, for users to set different "homes" that they can then teleport back to at a later time.

### PoniArcade_Messaging
Provides various messaging features, such as nicknames, private messaging, and the ability for admins to "spy" on private messages.

### PoniArcade_PunishmentsNG
Provides features for administrators to punish rule-breaking users, such as mutes, freezes, kicks, and bans. Also logs history of punishments to a database.

### PoniArcade_Statistics
Provides a simple statistics manager, tracking various player actions such as times joined, minutes online, and blocks placed.

### PoniArcade_Warps
Provides commands for administrators to set various global "warps," which players can teleport back to.

Contributors
--------------------

A majority of the current code was lovingly written by AppleDash, but it is derived from lots of hard work by past PoniArcade and PonyMC developers, including the following (in no particular order):

* ioncann0ns
* ShortCircuit908
* Feld0
* BaronvonBubble
* DashKetchum
* alyalyaly123
* omnicons
* Lavoaster


Development
------------

### Building

* To compile an artifact (the JAR files containing the compiled plugins), run `mvn package` in a plugin's directory.
  The JAR will be placed in `server/plugins` in this repo.

* To compile artifacts for all currently used plugins, run `mvn package` in the repo root.
  There is a "super POM" file for the entire repo that compiles all of them!

### Local test server

A directory structure exists so that Maven can conveniently update the plugin
JAR files in your local Minecraft server. Run `mvn package` to update the JARs.

There is a `server/` directory in the root of the repo, which will act as a local test server and by default have all PoniArcade plugins installed.

Run the `start.sh` script in that folder to automatically download the right version of Spigot and launch the test server.

### Dependencies

Install these plugins into your local server's `plugins` directory:

* [LibsDisguises](http://ci.md-5.net/job/LibsDisguises/)
    * Requires [ProtocolLib](http://assets.comphenix.net/job/ProtocolLib/)
* WorldEdit
* WorldGuard
* VanishNoPacket
* SaneEconomy

### Database
See `db/README.md`. 

### Basic Code Style
* `variableNames`
* `methodNames`
* `ClassNames`
* `CONSTANT_NAMES`
* Use 4 spaces instead of tabs
* Braces start on the same line as the function: `if (true) {`
* Use `this` to access class member variables and methods
* Prefer long variable names over non-descriptive ones

There is a shell script included in this repository named `style.sh`, which can be used to format the code according to these guidelines. (`astyle` is required, only works on *nix.)
