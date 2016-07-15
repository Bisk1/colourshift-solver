package colourshift.model.border;

import colourshift.model.Colour;
import colourshift.model.Direction;
import colourshift.model.DirectionSet;
import colourshift.model.blocks.Block;
import colourshift.model.blocks.BorderStatus;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BorderMap implements Serializable {
	private Map<Direction, BorderView> map;
	
	public static class Builder {
		private Map<Direction, Border> borderMap;
		
		public Builder() {
			this.borderMap = Maps.newHashMap();
		}

		public void setBorder(Direction direction, Border borderView) {
			borderMap.put(direction, borderView);
		}
		
		public Border getBorder(Direction direction) {
			return borderMap.get(direction);
		}
		
		public BorderMap build(Block block) {
			Map<Direction, BorderView> borderViewMap = borderMap.entrySet().stream()
			.collect(Collectors.toMap(
							e -> e.getKey(),
							e -> e.getValue().getView(block)
					));
			return new BorderMap(borderViewMap);
		}
	}
	
	private BorderMap(Map<Direction, BorderView> map) {
		this.map = map;
	}

	public void send(Direction toDirection, Colour colour) {
		if (map.containsKey(toDirection)) {
			map.get(toDirection).send(colour);
		}
	}

	public void changeBlock(Block newBlock) {
		for (BorderView borderView : map.values()) {
            borderView.changeBlock(newBlock);
        }
	}

	public Colour getIncomingColourMix(Direction direction) {
		return map.get(direction).getIncomingColour();
	}

    /**
     * For all directions that are in the direction set, get all the colours that are coming in
     * through them and mix the colours.
     *
     * @param directionSet
     * @return mixed colour
     */
	public Colour getIncomingColourMix(DirectionSet directionSet) {
		return map.entrySet().stream()
				.filter(directionToBorderView -> directionSet.contains(directionToBorderView.getKey()))
				.map(Map.Entry::getValue)
				.map(BorderView::getIncomingColour)
				.reduce(Colour.GREY, (colour1, colour2) -> colour1.plus(colour2));
	}

    public void reset(Block fromBlock) {
        for (BorderView borderView : map.values()) {
            borderView.reset();
        }
    }

	public int size() {
		return map.size();
	}

	public boolean contains(Direction direction) {
		return map.containsKey(direction);
	}

	public Optional<Block> getNeighbour(Direction direction) {
		return map.containsKey(direction) ? Optional.of(map.get(direction).getNeighbour()) : Optional.empty();
	}

	public void borderUnused(Direction direction) {
        if (map.containsKey(direction)) {
            map.get(direction).unused();
        }
	}

    public Set<Direction> findDirectionsWithStatus(BorderStatus borderStatus) {
        Set<Direction> foundDirections =
                map.entrySet()
                .stream()
                .filter(e -> e.getValue().getStatus() == borderStatus)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        // Special case for UNUSED: non-existing borders (edge) map to UNUSED
        if (borderStatus == BorderStatus.UNUSED) {
            Set<Direction> missingDirections = Sets.newHashSet(Direction.values());
            for (Direction direction : Direction.values()) {
                if (map.containsKey(direction)) {
                    missingDirections.remove(direction);
                }
            }
            foundDirections.addAll(missingDirections);
        }
        return foundDirections;
    }
}
