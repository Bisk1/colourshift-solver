package colourshift.model.border;

import colourshift.model.Colour;

import java.io.Serializable;
import java.util.Optional;

public class BorderRequirement implements Serializable {
    private BorderStatus borderStatus;
    private Colour colour;

    private BorderRequirement(BorderStatus borderStatus, Colour colour) {
        this.borderStatus = borderStatus;
        this.colour = colour;
    }

    public static BorderRequirement unknown() {
        return new BorderRequirement(BorderStatus.UNKNOWN, null);
    }

    public static BorderRequirement indifferent() {
        return new BorderRequirement(BorderStatus.INDIFFERENT, null);
    }

    public static BorderRequirement cannotSend() {
        return new BorderRequirement(BorderStatus.CANNOT_SEND, null);
    }

    public static BorderRequirement mustSend(Colour colour) {
        return new BorderRequirement(BorderStatus.MUST_SEND, colour);
    }

    public static BorderRequirement canReceive(Colour colour) {
        return new BorderRequirement(BorderStatus.CAN_RECEIVE, colour);
    }

    public BorderStatus getBorderStatus() {
        return borderStatus;
    }

    public Optional<Colour> getColour() {
        return Optional.ofNullable(colour);
    }

    public int getStrength() {
        return borderStatus.getStrength();
    }

    public boolean strongerThan(BorderRequirement other) {
        return getStrength() > other.getStrength();
    }

}
