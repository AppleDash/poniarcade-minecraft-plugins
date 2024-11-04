package com.poniarcade.classesng.classes.type.master;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.ability.type.AbilityFlight;
import com.poniarcade.classesng.classes.power.type.SaddlePowerRandomClass;

import java.util.Optional;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class ClassDraconequus extends Class {
    public ClassDraconequus() {
        super(
            ClassType.MASTER,
            "Draconequus",
            ImmutableList.of(
                new AbilityFlight(0.11f, 0.5f)
            ),
            "You can steal another class' abilities for an hour using your saddle."
        );
        this.setSaddlePower(new SaddlePowerRandomClass());
	    this.setPrice(675_000.0D);
    }

    @Override
    public Optional<String> getExtraDescription() {
        return Optional.of("Now you can trial Alicorn without paying for it!");
    }
}
