public class EntityDisguised extends Entity implements Revealable {
    public final int type = Entity.DISGUISED;
    private boolean revealed = false;

    public EntityDisguised(int windowWidth, int x, int y) {
        super(windowWidth, x, y);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public boolean isRevealed() {
        return revealed;
    }

    @Override
    public void reveal() {
        revealed = true;
    }
}
