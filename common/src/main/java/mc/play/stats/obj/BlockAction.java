package mc.play.stats.obj;

public enum BlockAction {
    PLACE("place"),
    BREAK("break");

    private final String action;

    BlockAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
