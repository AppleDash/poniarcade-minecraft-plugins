package com.poniarcade.statistics.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by appledash on 7/7/17.
 * Blackjack is best pony.
 */
public class PlayerStatistics {
    private int timesJoined;
    private int minutesOnline;
    private int blocksBroken;
    private int blocksPlaced;
    private int mobsKilled;

    public PlayerStatistics(int timesJoined, int minutesOnline, int blocksBroken, int blocksPlaced, int mobsKilled) {
        this.timesJoined = timesJoined;
        this.minutesOnline = minutesOnline;
        this.blocksBroken = blocksBroken;
        this.blocksPlaced = blocksPlaced;
        this.mobsKilled = mobsKilled;
    }

    public PlayerStatistics() {
        this(0, 0, 0, 0, 0);
    }

    public PlayerStatistics(PlayerStatistics copyFrom) {
        this(copyFrom.timesJoined, copyFrom.minutesOnline, copyFrom.blocksBroken, copyFrom.blocksPlaced, copyFrom.mobsKilled);
    }

    public int getTimesJoined() {
        return this.timesJoined;
    }

    public int getMinutesOnline() {
        return this.minutesOnline;
    }

    public int getBlocksBroken() {
        return this.blocksBroken;
    }

    public int getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public int getMobsKilled() {
        return this.mobsKilled;
    }

    public void setTimesJoined(int timesJoined) {
        this.timesJoined = timesJoined;
    }

    public void setMinutesOnline(int minutesOnline) {
        this.minutesOnline = minutesOnline;
    }

    public void setBlocksBroken(int blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public void setMobsKilled(int mobsKilled) {
        this.mobsKilled = mobsKilled;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public PlayerStatistics clone() {
        return new PlayerStatistics(this.timesJoined, this.minutesOnline, this.blocksBroken, this.blocksPlaced, this.mobsKilled);
    }

    public static PlayerStatistics fromDatabase(ResultSet rs) throws SQLException {
        return new PlayerStatistics(rs.getInt("times_joined"), rs.getInt("minutes_online"), rs.getInt("blocks_broken"), rs.getInt("blocks_placed"), rs.getInt("mobs_killed"));
    }
}
