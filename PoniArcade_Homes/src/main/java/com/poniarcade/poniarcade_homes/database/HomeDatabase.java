package com.poniarcade.poniarcade_homes.database;

import com.poniarcade.core.structs.Reloadable;
import com.poniarcade.poniarcade_homes.Home;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Optional;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
public interface HomeDatabase extends Reloadable {
    List<Home> getAllHomes(OfflinePlayer player);
    Optional<Home> getHome(OfflinePlayer player, String name);
    void setHome(OfflinePlayer player, Home home);
    void deleteHome(OfflinePlayer player, Home home);
}
