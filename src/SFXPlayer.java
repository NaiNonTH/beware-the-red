import java.net.URL;
import java.util.Objects;
import javax.sound.sampled.*;

public class SFXPlayer {
    URL url;
    Clip clip;

    public SFXPlayer(String urlString) {
        this.url = getClass().getResource(urlString);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(url));
            AudioFormat audioFormat = audioInputStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);

            clip = (Clip) AudioSystem.getLine(info);

            clip.open(audioInputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
        clip.setFramePosition(0);
    }
}
