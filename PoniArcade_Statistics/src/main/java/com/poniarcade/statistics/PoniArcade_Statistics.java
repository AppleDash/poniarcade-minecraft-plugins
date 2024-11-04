package com.poniarcade.statistics;

import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.statistics.listeners.StatisticsListener;
import com.poniarcade.statistics.statistics.StatisticsManager;

/**
 * Created by appledash on 7/7/17.
 * Blackjack is best pony.
 */
public class PoniArcade_Statistics extends PoniArcadePlugin {
    private StatisticsManager statisticsManager;

    @Override
    public void onEnable() {
        this.statisticsManager = new StatisticsManager();
        PoniArcade_Core.getInstance().addReloadable(this.statisticsManager);
        this.getServer().getPluginManager().registerEvents(new StatisticsListener(this), this);
    }

    @Override
    public void onDisable() {
        this.statisticsManager.syncPendingPlayers();
    }

    public StatisticsManager getStatisticsManager() {
        return this.statisticsManager;
    }
}
