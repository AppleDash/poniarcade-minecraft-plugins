package com.poniarcade.core.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by appledash on 7/27/16.
 * Blackjack is still best pony.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandlesCommand {
    String subCommand() default "";
    Class[] params() default {};
    String permission() default "";
    int priority() default 0;
}
