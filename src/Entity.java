public abstract class Entity {
    public static final int INVALID = -1;
    public static final int WHITE = 0;
    public static final int RED = 1;
    public static final int DISGUISED = 2;

    public int x;
    public int y;
    public int size;

    public Entity(int windowWidth, int x, int y) {
        size = calculateEntitySize(windowWidth);
        this.x = x;
        this.y = y;
    }

    public abstract int getType();

    public static int calculateEntitySize(int windowWidth) {
        return (int) Math.floor(windowWidth * 0.1);
    }

    private static double EWeight = 1;
    private static double whiteChance = -1;

    public static int randomizeEntityType(int wave) {
        if (EWeight <= 2.5) {
            whiteChance = 100 - wave / (5.0 / EWeight);
            EWeight += (100 - whiteChance) / 1000;
        }

        double randomNumber = Math.random() * 100;

        if (randomNumber > whiteChance)
            if (wave > 15 && Math.round(Math.random()) == 0)
                return Entity.DISGUISED;
            else
                return Entity.RED;
        else
            return Entity.WHITE;
    }
}
