import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, String> soundFiles; // To map sound names to file paths
    private Clip themeClip;                 // Clip to store background theme music

    public SoundManager() {
        soundFiles = new HashMap<>();
        loadSounds(); // Automatically load sounds when initializing
    }

    // Method to load sound files into the manager
    private void loadSounds() {
        try {
            loadSound("start", "resources/sounds/start_sound.wav");
            loadSound("move", "resources/sounds/move_sound.wav");
            loadSound("unlock", "resources/sounds/unlock_sound.wav");
            loadSound("look", "resources/sounds/look_sound.wav");
            loadSound("win", "resources/sounds/win_sound.wav"); // Extra sound for winning
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    // Load a sound file into the manager
    public void loadSound(String soundName, String filePath) {
        soundFiles.put(soundName, filePath);
        System.out.println("Loaded sound: " + soundName + " from " + filePath); // For debugging
    }

    // Play sound using the sound name
    public void playSound(String soundName) {
        String filePath = soundFiles.get(soundName);
        if (filePath != null) {
            try {
                File soundFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                System.out.println("Playing sound: " + soundName);
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                System.err.println("Error playing sound: " + soundName + " - " + e.getMessage());
            }
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }

    // Start the background theme music
    public void startTheme(String themeFile) {
        try {
            File theme = new File(themeFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(theme);
            themeClip = AudioSystem.getClip();
            themeClip.open(audioStream);
            themeClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the theme music
            themeClip.start();
            System.out.println("Starting theme: " + themeFile);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Error playing theme: " + themeFile + " - " + e.getMessage());
        }
    }

    // Method to lower the theme volume
    public void lowerThemeVolume() {
        if (themeClip != null && themeClip.isOpen()) {
            FloatControl volumeControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
            // Reduce volume by 10 decibels
            float currentVolume = volumeControl.getValue();
            float newVolume = Math.max(currentVolume - 100.0f, volumeControl.getMinimum());
            volumeControl.setValue(newVolume);
            System.out.println("Lowered theme volume by 10 dB.");
        }
    }

    // Close sound manager and release resources
    public void close() {
        if (themeClip != null && themeClip.isOpen()) {
            themeClip.stop();
            themeClip.close();
        }
        System.out.println("Closing SoundManager and releasing resources.");
    }
}
