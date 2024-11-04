package com.poniarcade.classesng.classes.power.type;

import com.poniarcade.classesng.PoniArcade_ClassesNG;
import com.poniarcade.classesng.classes.Class;
import com.poniarcade.classesng.classes.ClassManager;
import com.poniarcade.classesng.classes.ClassType;
import com.poniarcade.classesng.classes.PlayerClassData;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.ColorHelper;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

/**
 * Created by appledash on 8/12/16.
 * Blackjack is still best pony.
 */
public class SaddlePowerRandomClass extends SaddlePower {
    public SaddlePowerRandomClass() {
        super("Random Class", "Allows you to turn into a random class for an hour, during which time you will be stuck as that class.");
    }

    @Override
    public void activatePrimary(Player player) {
        ClassManager classMan = PoniArcade_ClassesNG.instance().getClassManager();
        PlayerClassData playerClassData = classMan.getClassData(player);
        List<Class> classList = classMan.getAllClasses(ClassType._ANY);
        Class selected = classList.get(new Random().nextInt(classList.size()));

        playerClassData.setSpoofedClass(selected, System.currentTimeMillis() + (60 * 60 * 1000)); // 1 hour

        player.sendMessage(ColorHelper.aqua("Poof! You are now ").append(selected.getInflectedColoredName()).aqua(" for the next hour!").toString());
    }
}
