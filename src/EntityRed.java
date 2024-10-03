public class EntityRed extends Entity {
    public int type = Entity.RED;

    public EntityRed(int windowWidth, int x, int y) {
        super(windowWidth, x, y);
    }
    
    @Override
    public int getType() {
        return type;
    }
}
