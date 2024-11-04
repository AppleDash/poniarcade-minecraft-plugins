package com.poniarcade.core.commands.type;

import com.poniarcade.core.commands.BaseCommand;
import com.poniarcade.core.commands.Command;
import com.poniarcade.core.commands.CommandHelp;
import com.poniarcade.core.commands.HandlesCommand;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

import static com.poniarcade.core.utils.ColorHelper.messageTo;

@Command(name = "Weather", description = "Sets the world's weather.", basePermission = "core.weather")
public class WeatherCommand extends BaseCommand {
    public WeatherCommand(Plugin owning_plugin) {
        super(owning_plugin);
    }

    @CommandHelp(help = "Check the current weather type in your current world.")
    @HandlesCommand
    public void handleWeatherCheck(Player sender) {
        this.handleWeatherCheck(sender, sender.getWorld().getName());
    }

    //@HandlesCommand(params = { String.class })
    public void handleWeatherCheck(CommandSender sender, String worldName) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            messageTo(sender).red("No world named ").gold(worldName).aqua(" exists!").send();
            return;
        }

        messageTo(sender).aqua("The current weather type in ").gold(world.getName()).aqua(" is ").gold(this.getCurrentWeatherType(world).toString()).aqua(".").send();
    }

    @CommandHelp(usage = "<weather type>", help = "Set the current weather type in your current world.")
    @HandlesCommand(params = String.class)
    public void handleWeather(Player sender, String weatherType) {
        Optional<WeatherType> weather = this.parseWeatherType(weatherType);

        if (weather.isEmpty()) {
            messageTo(sender).red("There is no such weather type as ").gold(weatherType).red(".").send();
            return;
        }

        switch (weather.get()) {
            case CLEAR -> {
                sender.getWorld().setStorm(false);
                sender.getWorld().setThundering(false);
            }
            case DOWNFALL -> sender.getWorld().setStorm(true);
        }

        messageTo(sender).aqua("Weather for your current world set to ").gold(weatherType).aqua(".").send();
    }

    private WeatherType getCurrentWeatherType(World world) {
        if (world.hasStorm() || world.isThundering()) {
            return WeatherType.DOWNFALL;
        }

        return WeatherType.CLEAR;
    }

    private Optional<WeatherType> parseWeatherType(String typeName) {
        return switch (typeName.toLowerCase()) {
            case "clear", "off", "sun", "sunny" -> Optional.of(WeatherType.CLEAR);
            case "storm", "rain", "rainy", "raining", "stormy" -> Optional.of(WeatherType.DOWNFALL);
            default -> Optional.empty();
        };

    }
}
