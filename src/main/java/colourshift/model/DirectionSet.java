package colourshift.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

public class DirectionSet implements Iterable<Direction>, Serializable {

	private Set<Direction> set;
	
	public DirectionSet(Direction... directions) {
		this.set = Sets.newHashSet(directions);		
	}
	
	public DirectionSet(Set<Direction> set) {
		this.set = set;
	}

	@Override
	public Iterator<Direction> iterator() {
		return set.iterator();
	}
	
	public Stream<Direction> stream() {
		return set.stream();
	}
	
	public Set<Direction> toSet() {
		return set;
	}
	
	public DirectionSet minus(Direction direction) {
		Set<Direction> difference = new HashSet<>(set);
		difference.remove(direction);
		return new DirectionSet(difference);
	}
	
	public boolean contains(Direction direction) {
		return set.contains(direction);
	}

	public Direction getAny() {
		return set.iterator().next();
	}
}
