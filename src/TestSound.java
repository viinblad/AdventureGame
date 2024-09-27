import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class TestSound {
    public static void main(String[] args) {
        try {
            // Use the correct path to your .wav file
            File soundFile = new File("resources/sounds/theme_mix.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            // Wait for the sound to finish playing
            Thread.sleep(clip.getMicrosecondLength() / 1000); // Play for the full length of the audio

            // Clean up
            clip.close();
            audioInputStream.close();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("The audio file is not supported. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error loading the audio file. " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line is unavailable. " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Playback interrupted. " + e.getMessage());
        }
    }
}
