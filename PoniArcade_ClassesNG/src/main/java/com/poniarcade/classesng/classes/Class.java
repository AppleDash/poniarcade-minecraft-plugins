package com.poniarcade.classesng.classes;

import com.google.common.collect.ImmutableList;
import com.poniarcade.classesng.classes.ability.Ability;
import com.poniarcade.classesng.classes.power.SaddlePower;
import com.poniarcade.core.utils.ColorHelper;
import com.poniarcade.core.utils.ColorHelper.Builder;
import com.poniarcade.core.utils.Utils;
import org.bukkit.inventory.*;

import java.util.*;

/**
 * Created by appledash on 7/18/16.
 * Blackjack is still best pony.
 *
 * Represents a Class on the server, having various "ambient" abilities and a "saddle power".
 */
public class Class {
    private static final Set<String> REGISTERED_CLASSES = new HashSet<>();

    private final ClassType type;
    private final String name;
    private final Map<java.lang.Class<? extends Ability>, Ability> abilities;
    private final String description;
    private double price;
    private SaddlePower saddlePower;
    private final List<Recipe> recipes = new ArrayList<>();

    public Class(ClassType type, String name, List<Ability> abilities, String description) {
        // Proactively prevent some weird bugs that could arise due to not only having one instance of a Class with a given name.
        if (Class.REGISTERED_CLASSES.contains(name)) {
            throw new IllegalStateException("Can't register a duplicate class named " + name + "!");
        }

        Class.REGISTERED_CLASSES.add(name);

        this.type = type;
        this.name = name;
        this.description = description;
        this.abilities = new LinkedHashMap<>();

        abilities.forEach(ability -> this.abilities.put(ability.getRootAbilityClass(), ability));
    }

    /**
     * Get the name of this Class, with spaces removed.
     * @return Class name
     */
    public String getSanitizedName() {
        return this.name.replace(" ", "");
    }

    public Builder getInflectedColoredName() {
        boolean an = ("aeiou".indexOf(this.name.toLowerCase().charAt(0)) != -1) && !this.name.toLowerCase().startsWith("uni");
        String inflection = an ? "an " : "a ";
        return ColorHelper.aqua(inflection).gold(this.name);
    }

    public String getFriendlyName() {
        return this.name;
    }

    /**
     * Get an Ability this class may have by java.lang.Class.
     * @param type Ability this class may have
     * @return Ability if present, empty otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends Ability> Optional<T> getAbility(java.lang.Class<T> type) {
        return this.abilities.containsKey(type) ? Optional.of((T) this.abilities.get(type)) : Optional.empty();
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Get how much money this class costs to buy.
     * @return Amount of money, or 0 if it is free
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Set the cost of this class.
     * Any value above 0 will result in this class becoming a paid class.
     * @param price New price
     */
    protected void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get the ClassType of this class.
     * @return ClassType
     */
    public ClassType getType() {
        return this.type;
    }

    /**
     * Set this class' "Saddle Power" - an ability that will be performed when they use a saddle.
     * @param saddlePower Saddle power
     */
    protected void setSaddlePower(SaddlePower saddlePower) {
        if (this.saddlePower != null) {
            throw new IllegalStateException("Cannot set saddle power again!");
        }

        this.saddlePower = saddlePower;
    }

    /**
     * Get this class' "Saddle Power".
     * @return Saddle power, or empty if none
     */
    public Optional<SaddlePower> getSaddlePower() {
        return Optional.ofNullable(this.saddlePower);
    }

    /**
     * Get all of this class' abilities.
     * @return List of abilities.
     */
    public List<Ability> getAbilities() {
        return ImmutableList.copyOf(this.abilities.values());
    }

    /**
     * Add some recipes that only this Class can craft.
     * @param recipes ClassRecipes - can be Shaped or Shapeless
     */
    protected void addRecipes(ClassRecipe recipes) {
        this.recipes.addAll(ImmutableList.copyOf(recipes.values()));
    }

    /**
     * Check if a Recipe belongs to this class, by comparing the ingredients and result.
     * @param recipe Recipe
     * @return True if the Recipe belongs to this Class, false otherwise.
     */
    public boolean hasRecipe(Recipe recipe) {
        for (Recipe r : this.recipes) {
            if (Utils.areRecipesEqual(r, recipe)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the Recipes that this class has registered.
     * @return List of Recipes
     */
    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }

    public Optional<String> getExtraDescription() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Class theClass && theClass.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
