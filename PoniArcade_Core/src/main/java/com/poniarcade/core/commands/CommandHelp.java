package com.poniarcade.core.commands;

import java.lang.annotation.*;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(CommandHelp.CommandHelps.class)
public @interface CommandHelp {
    String usage() default "";
    String help();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface CommandHelps {
        CommandHelp[] value();
    }
}
