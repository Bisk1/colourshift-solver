package colourshift.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public enum Colour {

    GREY(ImmutableSet.of(), new Color(128, 128, 128)),
    RED(ImmutableSet.of(Component.RED), new Color(255, 0, 0)),
    GREEN(ImmutableSet.of(Component.GREEN), new Color(0, 255, 0)),
    BLUE(ImmutableSet.of(Component.BLUE), new Color(0, 0, 255)),
    YELLOW(ImmutableSet.of(Component.RED, Component.GREEN), new Color(255, 255, 0)),
    MAGENTA(ImmutableSet.of(Component.RED, Component.BLUE), new Color(255, 0, 255)),
    CYAN(ImmutableSet.of(Component.GREEN, Component.BLUE), new Color(0, 255, 255)),
    WHITE(ImmutableSet.of(Component.RED, Component.GREEN, Component.BLUE), new Color(255, 255, 255));

    private static class Holder {
        static Map<Colour, Set<Component>> coloursToComponents = Maps.newHashMap();
        static Map<Set<Component>, Colour> componentsToColours = Maps.newHashMap();
    }

    private Color awtColor;

    Colour(ImmutableSet<Component> components, Color awtColor) {
        Holder.coloursToComponents.put(this, components);
        Holder.componentsToColours.put(components, this);
        this.awtColor = awtColor;
    }

    public enum Component {
        RED,
        GREEN,
        BLUE
    }

    public Set<Component> getComponents() {
        return Holder.coloursToComponents.get(this);
    }

    public static Colour fromComponents(Set<Component> components) {
        return Holder.componentsToColours.get(components);
    }

    public Colour plus(Colour other) {
        Set<Component> componentsSum = Sets.union(getComponents(), other.getComponents());
        return fromComponents(componentsSum);
    }

    public boolean isSubcolour(Colour other) {
        return other.getComponents().containsAll(getComponents());
    }

    public static Colour mix(Collection<Colour> colours) {
        return colours
                .stream()
                .reduce(Colour.GREY, Colour::plus);
    }

    public Color getAwtColor() {
        return awtColor;
    }

}
