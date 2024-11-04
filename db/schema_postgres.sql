-- Player data
CREATE TABLE players
(
    uuid UUID PRIMARY KEY NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    last_seen TIMESTAMP NOT NULL,
    last_server_name VARCHAR(16) NOT NULL
);

-- Suggestions
CREATE SEQUENCE suggestions_id_seq;
CREATE TABLE suggestions
(
    id INTEGER DEFAULT nextval('suggestions_id_seq'::regclass) PRIMARY KEY NOT NULL,
    creator_uuid UUID NOT NULL,
    creator_name VARCHAR(16) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    title TEXT NOT NULL,
    pages TEXT NOT NULL,
    viewed BOOLEAN DEFAULT '0' NOT NULL
);

-- Class data

CREATE TABLE player_class_data
(
  player_uuid UUID PRIMARY KEY NOT NULL,
  class_name TEXT,
  next_class_change BIGINT DEFAULT 0 NOT NULL,
  spoofed_class TEXT,
  spoofed_class_expiry BIGINT DEFAULT 0 NOT NULL,
  purchased_classes TEXT
);

CREATE TABLE player_class_cooldowns
(
    player_uuid UUID PRIMARY KEY NOT NULL,
    cooldown INTEGER NOT NULL
);

CREATE UNIQUE INDEX player_class_data_player_uuid_uindex ON player_class_data (player_uuid);

-- Teleport Locations
CREATE SEQUENCE teleport_locations_id_seq;

CREATE TABLE teleport_locations
(
  id INTEGER DEFAULT nextval('teleport_locations_id_seq'::regclass) PRIMARY KEY NOT NULL,
  server_name VARCHAR(16) NOT NULL,
  name VARCHAR(32) NOT NULL,
  world UUID NOT NULL,
  position_x NUMERIC(11,3) NOT NULL,
  position_y NUMERIC(6,3) NOT NULL,
  position_z NUMERIC(11,3) NOT NULL,
  yaw NUMERIC(4,1) NOT NULL,
  pitch NUMERIC(3,1) NOT NULL,
  type VARCHAR(16) NOT NULL
);

CREATE UNIQUE INDEX teleport_locations_server_name_name_uindex ON teleport_locations (server_name, name);

-- Punishments
CREATE SEQUENCE punishments_id_seq;
CREATE TYPE punishment_action AS ENUM ('KICK', 'WARN', 'MUTE', 'FREEZE', 'BAN');
CREATE TYPE punishment_direction AS ENUM ('PUNISH', 'REVOKE_PUNISHMENT');

CREATE TABLE punishments
(
  id INTEGER DEFAULT nextval('punishments_id_seq'::regclass) PRIMARY KEY NOT NULL,
  player_uuid UUID NOT NULL,
  moderator_uuid UUID NOT NULL,
  time TIMESTAMP NOT NULL,
  action punishment_action NOT NULL,
  direction punishment_action NOT NULL,
  reason TEXT NOT NULL
);

-- Homes
CREATE SEQUENCE homes_id_seq;

CREATE TABLE homes
(
    id INTEGER DEFAULT nextval('homes_id_seq'::regclass) PRIMARY KEY NOT NULL,
    server_name VARCHAR(16) NOT NULL,
    owner_id UUID NOT NULL,
    name VARCHAR(32) NOT NULL,
    world UUID NOT NULL,
    position_x NUMERIC(11,3) NOT NULL,
    position_y NUMERIC(6,3) NOT NULL,
    position_z NUMERIC(11,3) NOT NULL,
    yaw NUMERIC(4,1) NOT NULL,
    pitch NUMERIC(3,1) NOT NULL
);

CREATE UNIQUE INDEX homes_server_name_owner_id_name_uindex ON homes (server_name, owner_id, name);

-- Statistics
CREATE TABLE player_statistics
(
    player_id UUID PRIMARY KEY NOT NULL,
    server_name VARCHAR(16) NOT NULL,
    times_joined INTEGER NOT NULL,
    minutes_online INTEGER NOT NULL,
    blocks_broken INTEGER NOT NULL,
    blocks_placed INTEGER NOT NULL,
    mobs_killed INTEGER NOT NULL
);

CREATE UNIQUE INDEX player_statistics_player_id_server_name_uindex ON player_statistics (player_id, server_name);

-- Nicknames
CREATE TABLE player_nicknames
(
    player_id UUID PRIMARY KEY NOT NULL,
    nickname VARCHAR(32) NOT NULL
);
