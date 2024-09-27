import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, Clip> soundLibrary; // Bibliotek til at gemme lyde
    private Clip themeClip; // Til temamusik

    public SoundManager() {
        soundLibrary = new HashMap<>(); // Initialiser biblioteket
    }

    // Indlæs en lydfil i biblioteket
    public void loadSound(String name, String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundLibrary.put(name, clip); // Gem clip i biblioteket
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Fejl ved indlæsning af lyd: " + e.getMessage());
        }
    }

    // Start temamusik
    public void startTheme(String themeFilePath) {
        try {
            themeClip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(themeFilePath));
            themeClip.open(audioInputStream);
            themeClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop musikken kontinuerligt
            themeClip.start(); // Start temamusik
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Fejl ved indlæsning af temamusik: " + e.getMessage());
        }
    }

    // Sænk temamusik
    public void lowerThemeVolume() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop(); // Stop musikken midlertidigt
            themeClip.setFramePosition(0); // Start forfra, hvis vi vil spille den igen senere
        }
    }

    // Afspil en lyd baseret på dens navn
    public void playSound(String name) {
        Clip clip = soundLibrary.get(name);
        if (clip != null) {
            clip.setFramePosition(0); // Start forfra
            clip.start();
        } else {
            System.err.println("Lyd '" + name + "' findes ikke i biblioteket.");
        }
    }

    // Stop en lyd baseret på dens navn
    public void stopSound(String name) {
        Clip clip = soundLibrary.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Stop lyden
        } else {
            System.err.println("Lyd '" + name + "' findes ikke eller kører ikke.");
        }
    }

    // Luk lyde og ryd op
    public void close() {
        for (Clip clip : soundLibrary.values()) {
            clip.close(); // Luk hver clip
        }
        if (themeClip != null) {
            themeClip.close(); // Luk temamusikken
        }
        soundLibrary.clear(); // Ryd biblioteket
    }
}
