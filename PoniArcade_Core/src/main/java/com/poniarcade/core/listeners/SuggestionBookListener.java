package com.poniarcade.core.listeners;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.commands.type.SuggestCommand;
import com.poniarcade.core.structs.Suggestion;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
public class SuggestionBookListener implements Listener {
    private final PoniArcade_Core plugin;

    public SuggestionBookListener(PoniArcade_Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignBook(PlayerEditBookEvent evt) {
        if (!evt.isSigning()) {
            return;
        }

        BookMeta oldBookMeta = evt.getPreviousBookMeta();

        if (oldBookMeta.hasDisplayName() && oldBookMeta.getDisplayName().equals(SuggestCommand.SUGGESTION_BOOK_NAME)) {
            if (this.plugin.getSuggestionHandler().hasTooManyRecentSuggestions(evt.getPlayer().getUniqueId())) {
                evt.setCancelled(true);
                messageTo(evt.getPlayer()).red("You have submitted too many suggestions recently - wait an hour and try again!").send();
                return;
            }

            BookMeta bookMeta = evt.getNewBookMeta();
            this.plugin.getSuggestionHandler().saveSuggestion(new Suggestion(evt.getPlayer(), bookMeta));
            evt.setCancelled(true);
            evt.getPlayer().getInventory().setItem(evt.getSlot(), new ItemStack(Material.AIR, 0));
            messageTo(evt.getPlayer()).aqua("Your suggestion has been saved!").send();
        }
    }
}
