package com.poniarcade.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
public final class ColorHelper {
    private ColorHelper() { }

    public static Builder messageTo(CommandSender sender) {
        return ColorHelper.to(sender);
    }

    public static Builder to(CommandSender sender) {
        return new Builder(sender, ChatColor.WHITE, "");
    }

    public static Builder empty() {
        return new Builder(ChatColor.WHITE, "");
    }

    public static Builder put(String s) {
        return new Builder(ChatColor.WHITE, s);
    }

    public static Builder put(ChatColor color, String fmt, Object... args) {
        return new Builder(color, fmt, args);
    }

    public static Builder aqua(String fmt, Object... args) {
        return ColorHelper.put(ChatColor.AQUA, fmt, args);
    }

    public static Builder gold(String fmt, Object... args) {
        return ColorHelper.put(ChatColor.GOLD, fmt, args);
    }

    public static Builder red(String fmt, Object... args) {
        return ColorHelper.put(ChatColor.RED, fmt, args);
    }

    public static Builder blue(String fmt, Object... args) {
        return ColorHelper.put(ChatColor.BLUE, fmt, args);
    }

    public static Builder yellow(String fmt, Object... args) {
        return ColorHelper.put(ChatColor.YELLOW, fmt, args);
    }

    public static final class Builder {
        private CommandSender target;
        private String buffer;
        private ChatColor curColor;
        private ChatColor prevColor;

        private Builder(CommandSender target, ChatColor color, String fmt, Object... args) {
            this(color, fmt, args);
            this.target = target;
        }

        private Builder(ChatColor curColor, String fmt, Object... args) {
            this.curColor = curColor;
            this.buffer = "";
            this.put(curColor, fmt, args);
        }

        public Builder put(ChatColor color, String fmt, Object... args) {
            this.prevColor = this.curColor;
            this.curColor = color;
            if (args.length != 0) {
                this.buffer += color + String.format(fmt, args);
            } else { // Don't attempt to format if we have no args to format with.
                this.buffer += color + fmt;
            }
            return this;
        }

        public Builder gold(String fmt, Object... args) {
            return this.put(ChatColor.GOLD, fmt, args);
        }

        public Builder aqua(String fmt, Object... args) {
            return this.put(ChatColor.AQUA, fmt, args);
        }

        public Builder yellow(String fmt, Object... args) {
            return this.put(ChatColor.YELLOW, fmt, args);
        }

        public Builder red(String fmt, Object... args) {
            return this.put(ChatColor.RED, fmt, args);
        }

        public Builder blue(String fmt, Object... args) {
            return this.put(ChatColor.BLUE, fmt, args);
        }

        public Builder newLine() {
            this.buffer += "\n" + this.curColor;
            return this;
        }

        public Builder append(Builder builder) {
            this.buffer += builder.toString() + this.curColor;
            return this;
        }

        public Builder rewind() {
            this.buffer += this.prevColor;
            this.curColor = this.prevColor;
            return this;
        }

        public Builder reset() {
            return this.put(ChatColor.RESET, "");
        }

        public Builder reset(String fmt, Object... args) {
            return this.put(ChatColor.RESET, fmt, args);
        }

        public int length() {
            return this.buffer.length();
        }

        public void sendTo(CommandSender sender) {
            sender.sendMessage(this.toString());
        }

        public void send() {
            if (this.target == null) {
                throw new IllegalArgumentException("Cannot send() a ColorHelper that's not got a target!");
            }

            this.sendTo(this.target);
        }

        @Override
        public String toString() {
            return this.buffer;
        }
    }
}
