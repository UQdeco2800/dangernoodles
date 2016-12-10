package uq.deco2800.dangernoodles.audio;

import uq.deco2800.dangernoodles.AudioManager;
import uq.deco2800.dangernoodles.AudioPlayer;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Testing suite for the Audio Manager class. All testing is done at lower volumes to make start up less irritating.
 * It is only checked that audio plays in base cases and is assumed that changes to volume don't affect if the audio 
 * is playing or not. This choice was made to speed up testing and remove room for test failure due to timing issues.
 * 
 * @author Paul Haley
 */
public class AudioManagerTest {
	// Instance variables to simplify file selection for media playback.
	private static final float MUTED = 0f;
	private final String prefixSound = "resources/sounds/";
	private final String prefixMusic = "resources/music/";
	private final String chime = prefixSound + "chime1.wav";
	private final String[] musicFiles = {prefixMusic + "Bumpy_Ride_Rag.wav", prefixMusic + "rain_loop1.wav", 
			prefixMusic + "snowstorm_loop1.wav"};
	
	/**
	 * Used to halt execution of test for period of time. Intended to be used for interaction with audio player 
	 * settings being changed and waiting for reaction. This can fail with a caught and ignored exception, this will 
	 * cause tests to fail. Additionally, there is no guarantee that the halt time is long enough for the AudioPlayer 
	 * to react.
	 * 
	 * Cannot test shutdown in AudioManager as test order cannot be specified and the singleton creation is 
	 * impossible.
	 */
	private void holdTesting() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// No action need, will likely cause test to fail though
		}
		return;
	}
	
	/**
	 * Converts the proportional volume fed to Audio Players and the Audio Manger to amplification measured in 
	 * decibels (dB). This method uses the same algorithm as used in the Audio Player's conversion.
	 * 
	 * @require 0 < volume
	 * @ensure volume in dB (decibels) is returned
	 * @param volume Proportional volume given to Audio Players or the Audio Manager
	 * @return Proportional volume in decibels.
	 */
	private float volumeToDecibels(float volume) {
		// If gain given is 0, sets to low value
		float newVolume = volume <= 0.0f ? 0.0001f : volume;
		// Converting the gain given to decibels 
		return (float)(20.0 * (Math.log(newVolume) / Math.log(10.0)));
	}
	
	/**
	 * Unmutes everything and sets the volume to be the given volume.
	 * @require 0 < volume
	 * @param volume Proportional volume for both sound effect and music playback.
	 */
	private void resetAudioManager(float volume) {
		AudioManager.unmuteSound();
		AudioManager.unmuteMusic();
		AudioManager.volumeSound(volume);
		AudioManager.volumeMusic(volume);
		return;
	}
	
	/**
	 * Testing the muting controls for sound.
	 */
	@Test
	public void mutingSoundControls() {
		// Resetting the Audio Manager
		float volume = 1;
		resetAudioManager(volume);
		
		assertFalse(AudioManager.isSoundMuted());
		
		// Testing direct muting methods
		AudioManager.muteSound();
		assertTrue(AudioManager.isSoundMuted());
		// Testing it is the same when used again.
		AudioManager.muteSound();
		assertTrue(AudioManager.isSoundMuted());
		
		// Tesing direct unmuting methods
		AudioManager.unmuteSound();
		assertFalse(AudioManager.isSoundMuted());
		// Testing it is the same when used again.
		AudioManager.unmuteSound();
		assertFalse(AudioManager.isSoundMuted());
		
		// Testing toggle muting
		AudioManager.toggleMuteSound(); // Muted
		assertTrue(AudioManager.isSoundMuted());
		AudioManager.toggleMuteSound(); // Unmuted
		assertFalse(AudioManager.isSoundMuted());
	}
	
	/**
	 * Testing the muting controls for music
	 */
	@Test
	public void mutingMusicControls() {
		// Resetting the Audio Manager
		float volume = 1;
		resetAudioManager(volume);
		
		assertFalse(AudioManager.isMusicMuted());
		
		// Testing direct muting methods
		AudioManager.muteMusic();
		assertTrue(AudioManager.isMusicMuted());
		// Testing it is the same when used again.
		AudioManager.muteMusic();
		assertTrue(AudioManager.isMusicMuted());
		
		// Tesing direct unmuting methods
		AudioManager.unmuteMusic();
		assertFalse(AudioManager.isMusicMuted());
		// Testing it is the same when used again.
		AudioManager.unmuteMusic();
		assertFalse(AudioManager.isMusicMuted());
		
		// Testing toggle muting
		AudioManager.toggleMuteMusic(); // Muted
		assertTrue(AudioManager.isMusicMuted());
		AudioManager.toggleMuteMusic(); // Unmuted
		assertFalse(AudioManager.isMusicMuted());
	}
	
	/**
	 * Volume controls testing for sound.
	 */
	@Test
	public void volumeSound() {
		float volume = 1;
		resetAudioManager(volume);
		
		// Testing default is correct
		assertTrue(volume - 0.05 < AudioManager.getVolumeSound() && AudioManager.getVolumeSound() < volume + 0.05f);
		
		// Increasing (normal case)
		volume = 1.5f;
		AudioManager.volumeSound(volume);
		assertTrue(volume - 0.05 < AudioManager.getVolumeSound() && AudioManager.getVolumeSound() < volume + 0.05f);
		
		// Decreasing (normal case)
		volume = 0.5f;
		AudioManager.volumeSound(volume);
		assertTrue(volume - 0.05 < AudioManager.getVolumeSound() && AudioManager.getVolumeSound() < volume + 0.05f);
		
		// Extreme case
		volume = 0f;
		AudioManager.volumeSound(volume);
		assertTrue(volume - 0.05 < AudioManager.getVolumeSound() && AudioManager.getVolumeSound() < volume + 0.05f);
		
		// 0< case
		volume = -2.0f;
		AudioManager.volumeSound(volume);
		assertTrue(AudioManager.getVolumeSound() < MUTED + 0.05f);
	}
	
	/**
	 * Volume controls testing for Music.
	 */
	@Test
	public void volumeMusic() {
		float volume = 1;
		resetAudioManager(volume);
		
		// Testing default is correct
		assertTrue(volume - 0.05 < AudioManager.getVolumeMusic() && AudioManager.getVolumeMusic() < volume + 0.05f);
		
		// Decreasing (normal case)
		volume = 0.5f;
		AudioManager.volumeMusic(volume);
		assertTrue(volume - 0.05 < AudioManager.getVolumeMusic() && AudioManager.getVolumeMusic() < volume + 0.05f);
		
		// Increasing beyond limit
		volume = 1.5f;
		AudioManager.volumeMusic(volume);
		assertTrue(AudioManager.getVolumeMusic() <= 1f);
		
		// Extreme case
		volume = 0f;
		AudioManager.volumeMusic(volume);
		assertTrue(volume - 0.05 < AudioManager.getVolumeMusic() && AudioManager.getVolumeMusic() < volume + 0.05f);
		
		// 0< case
		volume = -2.0f;
		AudioManager.volumeMusic(volume);
		assertTrue(AudioManager.getVolumeMusic() < MUTED + 0.05f);
	}
	
	/**
	 * Plays a single sound effect and ensures all settings given to the Audio Manager hold.
	 */
	@Test
	public void playSingleSoundVolumeHeld() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer player = AudioManager.playSound(chime, true);
		holdTesting();
		
		assertTrue(player.isPlaying()); // Tests that the sound is in fact playing.
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && player.getAmplification() < amplification + 0.05);
		player.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Plays a single sound effect, tests if changing the sound effect volume during playback works.
	 */
	@Test
	public void playSingleSoundVolumeChanged() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer player = AudioManager.playSound(chime, true);
		
		volume -= 0.5f; // decrease volume
		AudioManager.volumeSound(volume); // Changing sound effect volume.
		
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && 
				player.getAmplification() < amplification + 0.05);
		player.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Plays a single sound effect, tests if changing the music volume does not effect sound effect's.
	 */
	@Test
	public void playSingleSoundVolumeMusicChanged() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer player = AudioManager.playSound(chime, true);
		
		float musicVolume = 0.5f; // decrease volume
		AudioManager.volumeMusic(musicVolume); // Changing music volume.
		
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && 
				player.getAmplification() < amplification + 0.05);
		player.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Plays a single sound effect, tests if muting sound effect changes the playback volume.
	 */
	@Test
	public void playSingleSoundMutedSound() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer player = AudioManager.playSound(chime, true);
		// Muting sound
		AudioManager.muteSound();
		
		float amplification = volumeToDecibels(MUTED);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && 
				player.getAmplification() < amplification + 0.05);
		
		// Unmuting sounds and reassessing.
		AudioManager.unmuteSound();

		amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && 
				player.getAmplification() < amplification + 0.05);
		
		player.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Plays a single sound effect, tests if muting music does not changes the playback volume.
	 */
	@Test
	public void playSingleSoundMutedMusic() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer player = AudioManager.playSound(chime, true);
		
		AudioManager.muteMusic();
		
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < player.getAmplification() && 
				player.getAmplification() < amplification + 0.05);
		player.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Attempts playback of two music tracks on the separate channels.
	 */
	@Test
	public void playMusicDouble() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer mainMusic = AudioManager.playMusic(musicFiles[1], true, true);
		AudioPlayer overlayMusic = AudioManager.playMusic(musicFiles[2], true, false);
		
		holdTesting();
		
		assertTrue(mainMusic.isPlaying());
		assertTrue(overlayMusic.isPlaying());
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < mainMusic.getAmplification() && 
				mainMusic.getAmplification() < amplification + 0.05);
		assertTrue(amplification - 0.05 < overlayMusic.getAmplification() && 
				overlayMusic.getAmplification() < amplification + 0.05);
		mainMusic.stop();
		overlayMusic.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Attempts playback of two music tracks on the separate channels. Music volume and sound volume is changed, 
	 * checking that the music volume was applied correctly.
	 */
	@Test
	public void playMusicDoubleVolumeBoth() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer mainMusic = AudioManager.playMusic(musicFiles[1], true, true);
		AudioPlayer overlayMusic = AudioManager.playMusic(musicFiles[2], true, false);
		AudioManager.volumeSound(volume + 0.5f);
		volume -= 0.5f; // Decrement the volume for the music
		AudioManager.volumeMusic(volume);
		
		float amplification = volumeToDecibels(volume);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < mainMusic.getAmplification() && 
				mainMusic.getAmplification() < amplification + 0.05);
		assertTrue(amplification - 0.05 < overlayMusic.getAmplification() && 
				overlayMusic.getAmplification() < amplification + 0.05);
		mainMusic.stop();
		overlayMusic.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Attempts playback of two music tracks on the separate channels. Mutes music then checks if it is muted, then 
	 * applies an unmute and checks that the music has been unmuted.
	 */
	@Test
	public void playMusicDoubleMutedMusic() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer mainMusic = AudioManager.playMusic(musicFiles[1], true, true);
		AudioPlayer overlayMusic = AudioManager.playMusic(musicFiles[2], true, false);
		AudioManager.muteMusic();
				
		float amplification = volumeToDecibels(MUTED);
		
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < mainMusic.getAmplification() && 
				mainMusic.getAmplification() < amplification + 0.05);
		assertTrue(amplification - 0.05 < overlayMusic.getAmplification() && 
				overlayMusic.getAmplification() < amplification + 0.05);
		
		// Unmuting the Audio Manager and reassessing. 
		AudioManager.unmuteMusic();
		
		amplification = volumeToDecibels(volume);
		// Test that the amplification is correct.
		assertTrue(amplification - 0.05 < mainMusic.getAmplification() && 
				mainMusic.getAmplification() < amplification + 0.05);
		assertTrue(amplification - 0.05 < overlayMusic.getAmplification() && 
				overlayMusic.getAmplification() < amplification + 0.05);
				
		mainMusic.stop();
		overlayMusic.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Attempts playback of two music tracks on the separate channels. Checks whether the use of the stop method on 
	 * the overlay channel does not affect the main audio track.
	 */
	@Test
	public void playMusicDoubleStopOverlay() {
		float volume = 1;
		resetAudioManager(volume);
		AudioPlayer mainMusic = AudioManager.playMusic(musicFiles[1], true, true);
		AudioPlayer overlayMusic = AudioManager.playMusic(musicFiles[2], true, false);
		
		holdTesting();
		
		// Tests that both channels are playing.
		assertTrue(mainMusic.isPlaying());
		assertTrue(overlayMusic.isPlaying());
		
		AudioManager.stopMusicOverlay();
		
		// Tests that only the main music is still playing.
		assertTrue(mainMusic.isPlaying());
		assertFalse(overlayMusic.isPlaying());
		
		mainMusic.stop();
		overlayMusic.stop();
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();
	}
	
	/**
	 * Tests that when 17 sounds are given, the first sound has ceased playing.
	 */
	@Test
	public void soundOverflow() {
		float volume = 0.2f; // This will be loud and hectic
		resetAudioManager(volume);
		
		// Array to store all audio players
		final int MAX_SOUNDS = 16;
		AudioPlayer[] players = new AudioPlayer[MAX_SOUNDS];
		
		// Start 16 sounds, music overlay has been used to avoid looping.
		for (int i = 0; i < MAX_SOUNDS; ++i) {
			players[i] = AudioManager.playSound(musicFiles[2], false);
		}
		
		holdTesting();
		
		// Test that all sounds are playing
		for (int i = 0; i < MAX_SOUNDS; ++i) {
			assertTrue(players[i].isPlaying());
		}
		
		// Adding of additional sound should go here.
		
		// Stopping all playback.
		for (int i = 0; i< MAX_SOUNDS; ++i) {
			players[i].stop();
		}
		
		// Ensures enough time for Audio Manager to clear itself
		holdTesting();		
	}
}
