CREATE TABLE homes
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    player_id INT(8) unsigned NOT NULL,
    server_id INT(3) unsigned NOT NULL,
    data TEXT NOT NULL
);
CREATE TABLE mail
(
    id INT(11) unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    player_id INT(8) NOT NULL,
    player_name VARCHAR(18) NOT NULL,
    recipient_id INT(8) NOT NULL,
    message TEXT,
    is_read TINYINT(1) DEFAULT '0' NOT NULL,
    sent DATE NOT NULL
);
CREATE TABLE nicknames
(
    player_id INT(8) PRIMARY KEY NOT NULL,
    nickname VARCHAR(20),
    last_change DATETIME
);
CREATE TABLE players
(
    id INT(8) unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(18) NOT NULL,
    chat_filter TINYINT(1) DEFAULT '1' NOT NULL,
    forum_id VARCHAR(255),
    registration_code VARCHAR(10),
    uuid VARCHAR(36) NOT NULL,
    last_address VARCHAR(25)
);
CREATE TABLE servers
(
    id INT(3) unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL
);
CREATE TABLE statistics
(
    player_id INT(8) unsigned PRIMARY KEY NOT NULL,
    points INT(10) DEFAULT '0' NOT NULL,
    first_join DATETIME NOT NULL,
    last_join DATETIME NOT NULL,
    time_online INT(16) DEFAULT '0' NOT NULL,
    blocks_broken INT(9) DEFAULT '0' NOT NULL,
    blocks_placed INT(9) DEFAULT '0' NOT NULL,
    distance_travelled INT(10) DEFAULT '0' NOT NULL,
    mobs_killed INT(10) DEFAULT '0' NOT NULL,
    deaths INT(10) DEFAULT '0' NOT NULL,
    hours_per_day DOUBLE DEFAULT '0' NOT NULL
);

CREATE UNIQUE INDEX player_id ON homes (player_id, server_id);
CREATE UNIQUE INDEX nicknames ON nicknames (nickname);
CREATE UNIQUE INDEX name ON players (name);
CREATE UNIQUE INDEX uuid ON players (uuid);
CREATE UNIQUE INDEX id ON punishments (id);
CREATE INDEX player_id ON statistics (player_id);
