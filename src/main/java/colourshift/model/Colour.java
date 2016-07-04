package colourshift.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public enum Colour {
	
	GREY,
	RED,
	GREEN,
	BLUE,
	YELLOW,
	MAGENTA,
	CYAN,
	WHITE;
	
	
	private enum Component {
		RED,
		GREEN,
		BLUE
	}
	
	private static Map<Colour, Set<Component>> coloursToComponents;
	private static Map<Set<Component>, Colour> componentsToColours;
	static {
		coloursToComponents = new HashMap<Colour, Set<Component>>();
		coloursToComponents.put(GREY, ImmutableSet.of());
		coloursToComponents.put(RED, ImmutableSet.of(Component.RED));
		coloursToComponents.put(GREEN, ImmutableSet.of(Component.GREEN));
		coloursToComponents.put(BLUE, ImmutableSet.of(Component.BLUE));
		coloursToComponents.put(YELLOW, ImmutableSet.of(Component.RED, Component.GREEN));
		coloursToComponents.put(MAGENTA, ImmutableSet.of(Component.RED, Component.BLUE));
		coloursToComponents.put(CYAN, ImmutableSet.of(Component.GREEN, Component.BLUE));
		coloursToComponents.put(WHITE, ImmutableSet.of(Component.GREEN, Component.GREEN, Component.BLUE));
		
		for (Entry<Colour, Set<Component>> colourToComponents : coloursToComponents.entrySet()) {
			componentsToColours.put(colourToComponents.getValue(), colourToComponents.getKey());
		}
	}
	
	public Set<Component> getComponents() {
		return coloursToComponents.get(this);
	}
	
	private static Colour colourOfComponents(Set<Component> components) {
		return componentsToColours.get(components);
	}
	
	public Colour plus(Colour other) {
		Set<Component> componentsSum = Sets.union(getComponents(), other.getComponents());
		return colourOfComponents(componentsSum);
	}
	
	public boolean isSubcolour(Colour other) {
		return other.getComponents().containsAll(getComponents());
	}
	
	public static Colour mix(Collection<Colour> colours) {
		return colours
				.stream()
				.reduce(Colour.GREY,
						(colour1, colour2) -> colour1.plus(colour2));
	}
	
}
