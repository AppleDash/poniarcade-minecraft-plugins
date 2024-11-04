package com.poniarcade.core.commands.type;

import com.poniarcade.core.PoniArcade_Core;
import com.poniarcade.core.structs.Suggestion;
import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.HandlesCommand;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
@Command(name = "Suggest", description = "Provide suggestions on server improvements to the staff.", basePermission = "poniarcade.core.suggest")
public class SuggestCommand extends BaseCommand {
    public static final String SUGGESTION_BOOK_NAME = "Suggestion Book";
    private final PoniArcade_Core plugin;

    public SuggestCommand(PoniArcade_Core plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @HandlesCommand
    public void handleSuggest(Player sender) {
        if (this.doesPlayerHaveSuggestionBook(sender)) {
            messageTo(sender).red("You already have a suggestion book. Try using that one!").send();
            return;
        }

        ItemStack book = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta meta =  book.getItemMeta();
        meta.setDisplayName(SuggestCommand.SUGGESTION_BOOK_NAME);
        book.setItemMeta(meta);
        sender.getInventory().addItem(book);
        messageTo(sender).aqua("I have given you a suggestion book. Fill it out and sign it to submit your suggestion!").send();
    }

    @HandlesCommand(subCommand = "view", permission = "poniarcade.core.suggest.view")
    public void handleSuggestList(Player sender) {
        ColorHelper.Builder b = messageTo(sender);

        List<Suggestion> suggestions = this.plugin.getSuggestionHandler().getSuggestions();


        if (suggestions.isEmpty()) {
            b.aqua("There are no pending suggestions.").send();
            return;
        }

        for (int i = 0; i < suggestions.size(); i++) {
            Suggestion suggestion = suggestions.get(i);
            b.aqua("#").gold("%d", suggestion.getId()).aqua(" from ").gold(suggestion.getCreatorName());

            if (i == suggestions.size() - 1) {
                b.aqua(".");
            } else {
                b.aqua(", ");
            }
        }

        b.send();
    }

    @HandlesCommand(subCommand = "read", params = Integer.class, permission = "poniarcade.core.suggest.view")
    public void handleSuggestRead(Player sender, int id) {
        Optional<Suggestion> suggestionOptional = this.plugin.getSuggestionHandler().getSuggestion(id);

        if (suggestionOptional.isEmpty()) {
            messageTo(sender).red("There is no suggestion with that ID.").send();
            return;
        }

        Suggestion suggestion = suggestionOptional.get();
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        book.setItemMeta(suggestion.toBookMeta((BookMeta) book.getItemMeta()));
        sender.getInventory().addItem(book);
        messageTo(sender).aqua("I have given you the book containing that suggestion.").send();
    }

    @HandlesCommand(subCommand = "archive", params = Integer.class, permission = "poniarcade.core.suggest.view")
    public void handleSuggestMark(Player sender, int id) {
        Optional<Suggestion> suggestionOptional = this.plugin.getSuggestionHandler().getSuggestion(id);

        if (suggestionOptional.isEmpty()) {
            messageTo(sender).red("There is no suggestion with that ID.").send();
            return;
        }

        this.plugin.getSuggestionHandler().removeSuggestion(suggestionOptional.get());
        messageTo(sender).aqua("That suggestion has been removed from the list of new suggestions.").send();
    }

    private boolean doesPlayerHaveSuggestionBook(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (Utils.isStackEmpty(itemStack))  {
                continue;
            }

            if (itemStack.getType() == Material.WRITABLE_BOOK) {
                if (itemStack.getItemMeta() != null) {
                    if (itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals(SuggestCommand.SUGGESTION_BOOK_NAME)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
