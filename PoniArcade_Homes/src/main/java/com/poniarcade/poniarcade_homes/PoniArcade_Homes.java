package com.poniarcade.poniarcade_homes;

import com.poniarcade.core.PoniArcadePlugin;
import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.handlers.CommandHandler;
import com.poniarcade.poniarcade_homes.commands.BuyHomeCommand;
import com.poniarcade.poniarcade_homes.commands.DeleteHomeCommand;
import com.poniarcade.poniarcade_homes.commands.HomeCommand;
import com.poniarcade.poniarcade_homes.commands.SetHomeCommand;
import com.poniarcade.poniarcade_homes.database.HomeDatabase;
import com.poniarcade.poniarcade_homes.database.HomeDatabasePostgresql;
import org.appledash.saneeconomy.ISaneEconomy;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by appledash on 7/15/17.
 * Blackjack is best pony.
 */
public class PoniArcade_Homes extends PoniArcadePlugin {
    public static final int DEFAULT_HOME_COUNT = 3;
    private HomeDatabase database;
    private ISaneEconomy saneEconomy;

    @Override
    public void onEnable() {
        this.saneEconomy = (ISaneEconomy) this.getServer().getPluginManager().getPlugin("SaneEconomy");
        this.database = new HomeDatabasePostgresql();
        PoniArcade_Core.getInstance().addReloadable(this.database);
        CommandHandler.addCommands(
            this,
            new BuyHomeCommand(this),
            new DeleteHomeCommand(this),
            new HomeCommand(this),
            new SetHomeCommand(this)
        );
    }

    public HomeDatabase getHomeDatabase() {
        return this.database;
    }

    public ISaneEconomy getSaneEconomy() {
        return this.saneEconomy;
    }

    public static int getNumberOfAllowedHomes(Permissible player) {
        Pattern pattern = Pattern.compile("poniarcade\\.homes\\.home\\.([0-9]+)");

        for (PermissionAttachmentInfo pai : player.getEffectivePermissions()) {
            Matcher m = pattern.matcher(pai.getPermission());
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
        }

        return PoniArcade_Homes.DEFAULT_HOME_COUNT;
    }
}
