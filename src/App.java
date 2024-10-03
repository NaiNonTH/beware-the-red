import javax.swing.JFrame;
import java.awt.BorderLayout;

public class App extends JFrame {
    final static int windowWidth = 720;
    final static int windowHeight = 720;

    Scene game = new Scene(windowWidth, windowHeight);

    public App() {
        setSize(windowWidth, windowHeight);
        setTitle("Beware the Red!");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        setLayout(new BorderLayout());

        add(game, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}