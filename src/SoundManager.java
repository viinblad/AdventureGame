import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, String> soundFiles; // To map sound names to file paths
    private Clip themeClip;                 // Clip to store background theme music

    private final float EFFECTS_VOLUME = 0.0f;  // Volume in decibels for sound effects
    private final float THEME_VOLUME = -20.0f;    // Lower volume for theme music

    public SoundManager() {
        soundFiles = new HashMap<>();
        loadSounds(); // Automatically load sounds when initializing
    }

    // Method to load sound files into the manager
    private void loadSounds() {
        try {
            loadSound("start", "resources/sounds/start_sound.wav"); // Sound Effect
            loadSound("move", "resources/sounds/move_sound.wav");   // Sound Effect
            loadSound("unlock", "resources/sounds/unlock_sound.wav"); // Sound Effect
            loadSound("look", "resources/sounds/look_sound.wav");   // Sound Effect
            loadSound("win", "resources/sounds/win_sound.wav");     // Sound Effect
            loadSound("theme", "resources/sounds/theme_music.wav"); // Theme music
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    // Load a sound file into the manager
    public void loadSound(String soundName, String filePath) {
        soundFiles.put(soundName, filePath);
        System.out.println("Loaded sound: " + soundName + " from " + filePath); // For debugging
    }

    public void playSoundEffect(String soundName) {
        String filePath = soundFiles.get(soundName);
        System.out.println("Requested sound: " + soundName + " with path: " + filePath); // Debug line
        if (filePath != null) {
            try {
                File soundFile = new File(filePath);
                System.out.println("Absolute path: " + soundFile.getAbsolutePath()); // Debug line
                if (!soundFile.exists()) {
                    System.err.println("Sound file does not exist: " + soundFile.getAbsolutePath());
                    return; // Exit if the file doesn't exist
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                setVolume(clip, EFFECTS_VOLUME);
                clip.start();
                System.out.println("Playing sound effect: " + soundName);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (IOException e) {
                System.err.println("I/O error while playing sound effect: " + soundName + " - " + e.getMessage());
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Unsupported audio file format for sound effect: " + soundName + " - " + e.getMessage());
            } catch (LineUnavailableException e) {
                System.err.println("Line unavailable while playing sound effect: " + soundName + " - " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error while playing sound effect: " + soundName + " - " + e.getMessage());
                e.printStackTrace(); // Detailed exception for any unexpected issues
            }
        } else {
            System.err.println("Sound effect not found: " + soundName);
        }
    }


    // Start the background theme music
    public void startTheme() {
        String themeFile = soundFiles.get("theme");
        if (themeFile != null) {
            try {
                File theme = new File(themeFile);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(theme);
                themeClip = AudioSystem.getClip();
                themeClip.open(audioStream);
                setVolume(themeClip, THEME_VOLUME);  // Setting theme music volume
                themeClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the theme music
                themeClip.start();
                System.out.println("Starting theme music.");
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                System.err.println("Error playing theme music: " + e.getMessage());
            }
        } else {
            System.err.println("Theme music file not found.");
        }
    }

    // Method to set volume for a given clip (volume in decibels)
    private void setVolume(Clip clip, float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);  // Set the volume (in decibels)
        } else {
            System.err.println("Volume control not supported.");
        }
    }

    // Stop and close theme music
    public void stopTheme() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
            themeClip.close();
            //System.out.println("Theme music stopped.");
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
