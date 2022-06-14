
class Position {
    public final int lineNumber;
    public final int linePosition;
    public final char charAt;

    public Position(int lineNumber, int linePosition, char charAt) {
        this.lineNumber = lineNumber;
        this.linePosition = linePosition;
        this.charAt = charAt;
    }

    public Position(Position pos) {
        this.lineNumber = pos.lineNumber;
        this.linePosition = pos.linePosition;
        this.charAt = pos.charAt;
    }

    @Override
    public String toString() {
        return "Position [charAt=" + charAt + ", lineNumber=" + lineNumber + ", linePosition=" + linePosition + "]";
    }
}