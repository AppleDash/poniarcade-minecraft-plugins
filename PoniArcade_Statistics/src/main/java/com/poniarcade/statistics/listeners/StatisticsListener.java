package com.poniarcade.statistics.listeners;

import com.poniarcade.statistics.PoniArcade_Statistics;
import com.poniarcade.statistics.statistics.PlayerStatistics;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by appledash on 7/7/17.
 * Blackjack is best pony.
 */
public class StatisticsListener implements Listener {
    private final PoniArcade_Statistics plugin;

    public StatisticsListener(PoniArcade_Statistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        PlayerStatistics statistics = this.plugin.getStatisticsManager().getStatistics(evt.getPlayer());
        statistics.setTimesJoined(statistics.getTimesJoined() + 1);
        this.plugin.getStatisticsManager().putStatistics(evt.getPlayer(), statistics);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        this.plugin.getStatisticsManager().saveStatistics(evt.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        PlayerStatistics statistics = this.plugin.getStatisticsManager().getStatistics(evt.getPlayer());
        statistics.setBlocksBroken(statistics.getBlocksBroken() + 1);
        this.plugin.getStatisticsManager().putStatistics(evt.getPlayer(), statistics);
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent evt) {
        PlayerStatistics statistics = this.plugin.getStatisticsManager().getStatistics(evt.getPlayer());
        statistics.setBlocksPlaced(statistics.getBlocksPlaced() + 1);
        this.plugin.getStatisticsManager().putStatistics(evt.getPlayer(), statistics);
    }

    @EventHandler
    public void onEntityDeath(EntityDamageByEntityEvent evt) {
        if (evt.getDamager() instanceof Player && evt.getEntity() instanceof Creature) {
            PlayerStatistics statistics = this.plugin.getStatisticsManager().getStatistics((OfflinePlayer) evt.getDamager());
            statistics.setMobsKilled(statistics.getMobsKilled() + 1);
            this.plugin.getStatisticsManager().putStatistics((OfflinePlayer) evt.getDamager(), statistics);
        }
    }
}
