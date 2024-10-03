public class EntityWhite extends Entity {
    private int type = Entity.WHITE;

    public EntityWhite(int windowWidth, int x, int y) {
        super(windowWidth, x, y);
    }

    @Override
    public int getType() {
        return type;
    }
}
