package colourshift.model.border;

public enum BorderStatus {
    UNKNOWN(0),
    CANNOT_SEND(1),
    RECEIVE(2),
    MANDATORY(3),
    INDIFFERENT(3);

    /**
     * Strength is used to compare BorderStatus when deciding about updating,
     * when border has also already stronger (more informative) status set
     * than a new one (or equally strong) received in update, the update should be ignored.
     */
    private int strength;

    BorderStatus(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }
}
