import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.lang.Exception;

import javax.swing.*;

class Scene extends JPanel implements Runnable {
    ArrayList<Entity> entities = new ArrayList<Entity>();

    int windowWidth,
        windowHeight;
    
    int mouseX;
    int mouseY;

    int wave = 0;
    int score = 0;
    boolean gameStarted = false;
    boolean gameOver = false;

    SFXPlayer shootPlayer = new SFXPlayer("assets/shoot.wav");
    SFXPlayer deadPlayer = new SFXPlayer("assets/dead.wav");

    Font normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
    Font largeFont = new Font(Font.SANS_SERIF, Font.BOLD, 36);
    
    public Scene(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        setSize(windowWidth, windowHeight);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        
        setFocusable(true);
        addKeyListener(new PlayerKeyboardShoot());
        addMouseListener(new PlayerMouseShoot());
        
        addMouseMotionListener(new UpdateMousePosition());
        
        addStarterEntity();
    }

    public void addStarterEntity() {
        entities.add(new EntityWhite(windowWidth, getWidth() / 2 - Entity.calculateEntitySize(windowWidth) / 2, getHeight() / 2 - Entity.calculateEntitySize(windowWidth) / 2 - 48));
    }

    public void spawnEntities() {
        int entityAmount = Math.min((int) Math.ceil(Math.random() * (wave / 5)) + 1, 12);
        int randomX,
            randomY;

        for (int i = 0; i < entityAmount; ++i) {
            randomX = (int) Math.floor(Math.random() * (windowWidth - Entity.calculateEntitySize(windowWidth)));
            randomY = (int) Math.floor(Math.random() * (windowHeight - 32 - Entity.calculateEntitySize(windowWidth)));
            int randomType = Entity.randomizeEntityType(wave);

            switch (randomType) {
                case Entity.WHITE:
                    entities.add(0, new EntityWhite(windowWidth, randomX, randomY));
                    break;
                case Entity.RED:
                    entities.add(new EntityRed(windowWidth, randomX, randomY));
                    break;
                case Entity.DISGUISED:
                    entities.add(new EntityDisguised(windowWidth, randomX, randomY));
                    break;
            }
        }

        if (onlyBadEntitiesLeft()) {
            randomX = (int) Math.floor(Math.random() * (windowWidth - Entity.calculateEntitySize(windowWidth)));
            randomY = (int) Math.floor(Math.random() * (windowHeight - 32 - Entity.calculateEntitySize(windowWidth)));
            
            entities.add(0, new EntityWhite(windowWidth, randomX, randomY));
        }
    }

    public boolean onlyBadEntitiesLeft() {
        for (Entity entity : entities) {
            if (entity.getType() == Entity.WHITE)
                return false;
        }

        return true;
    }
    
    public void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, windowWidth, windowHeight);
    }

    public void drawEntities(Graphics g) throws Exception {
        for (Entity entity : entities) {
            switch (entity.getType()) {
                case Entity.WHITE:
                    g.setColor(Color.WHITE);
                    break;
                case Entity.RED:
                    g.setColor(Color.RED);
                    break;
                case Entity.DISGUISED:
                    if (((Revealable) entity).isRevealed())
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.WHITE);
                    break;
                default:
                    throw new Exception("Invalid Entity Type — not in the range from 0 to 2. Type received: " + entity.getType());
            }
            
            g.fillOval(entity.x, entity.y, entity.size, entity.size);
        }
    }

    String beginString = "Click the circle to begin!";
    String gameOverString = "GAME OVER!!";
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics metrics = g.getFontMetrics();
        
        drawBackground(g);

        if (gameOver) {
            g.setColor(Color.WHITE);
            setFont(normalFont);
            g.drawString(gameOverString, getWidth() / 2 - metrics.stringWidth(gameOverString) / 2, getHeight() / 2);
        }
        else {
            g.setColor(Color.GRAY);
            setFont(largeFont);
            g.drawString(wave + "", getWidth() / 2 - metrics.stringWidth(wave + ""), getHeight() / 2);

            setFont(normalFont);

            if (!gameStarted) {
                g.setColor(Color.WHITE);
                g.drawString(beginString, getWidth() / 2 - metrics.stringWidth(beginString) / 2, getHeight() / 2 + metrics.getHeight() + 24);
            }

            try {
                drawEntities(g);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        String scoreString = "Score: " + score;
        
        g.setColor(Color.WHITE);
        g.drawString(scoreString, getWidth() - 16 - metrics.stringWidth(scoreString), metrics.getHeight());
    }
    
    @Override
    public void run() {
        
    }

    public boolean mouseIsHovering(Entity entity) {
        int entityRadius = entity.size / 2;

        int entityCenterX = entity.x + entityRadius;
        int entityCenterY = entity.y + entityRadius;

        int xDifference = Math.abs(entityCenterX - mouseX);
        int yDifference = Math.abs(entityCenterY - mouseY);

        int mouseDistanceFromCircle = (int) Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));

        return mouseDistanceFromCircle <= entityRadius;
    }

    private void shootAndCheck() {
        if (gameOver) return;

        boolean entityFound = false;

        for (Entity entity : entities) {
            if (mouseIsHovering(entity)) {
                gameStarted = true;

                if (entity.getType() != Entity.WHITE) {
                    setGameOver();
                    return;
                }

                entityFound = true;

                ++score;
                shootPlayer.play();
                entities.remove(entity);

                if (entities.isEmpty() || onlyBadEntitiesLeft()) {
                    ++wave;
                    entities.clear();
                    spawnEntities();
                }

                repaint();
                break;
            }
        }

        if (!entityFound && gameStarted)
            setGameOver();
    }

    public void setGameOver() {
        gameOver = true;
        deadPlayer.play();
        repaint();
    }
    
    class PlayerKeyboardShoot implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE)
                shootAndCheck();
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}
    }

    class PlayerMouseShoot implements MouseListener {
        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1)
                shootAndCheck();
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    class UpdateMousePosition implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            for (Entity entity : entities) {
                if (entity instanceof Revealable && mouseIsHovering(entity)) {
                    ((Revealable) entity).reveal();
                    repaint();
                }
            }
        }
    }
}
