package uq.deco2800.dangernoodles.audio;

import uq.deco2800.dangernoodles.AudioPlayer;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Testing suite for the Audio Player class. Testing is limited by race conditions in the attempted playback of 
 * audio files. Evidence of this can be seen in the attemptingPlaybackValidFile() test.
 * 
 * @author Paul Haley
 */
public class AudioPlayerTest {
	private String longTrack = "resources/music/Bumpy_Ride_Rag.wav";
	private String filepath = "resources/sounds/chime1.wav";
	private AudioPlayer audio = new AudioPlayer(longTrack);
	
	/**
	 * Common tests for the default settings of an audio player.
	 * @param player The Audio Player that has been just created with no altered settings.
	 */
	private void creationCommonTests(AudioPlayer player) {
		assertFalse(player.isLooping());
		assertFalse(player.isPaused());
		assertFalse(player.isPlaying());
		assertTrue(player.getAudioFilepath() != null); // Audio player is safe by having file preset
		assertTrue(-0.05f <= player.getAmplification() && player.getAmplification() <= 0.05f);
		assertTrue(-0.05f <= player.getPan() && player.getPan() <= 0.05f);
	}
	
	/**
	 * Used to halt execution of test for period of time. Intended to be used for interaction with audio player 
	 * settings being changed and waiting for reaction. This can fail with a caught and ignored exception, this will 
	 * cause tests to fail. Additionally, there is no guarantee that the halt time is long enough for the AudioPlayer 
	 * to react.
	 */
	private void holdTesting() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// No action need, will likely cause test to fail though
		}
	}
	
	/**
	 * Test that the audio player has initialised as expected.
	 */
	@Test
	public void creation() {
		creationCommonTests(audio);
		assertTrue(audio.getAudioFilepath().equals(longTrack));
	}

	
	/**
	 * Sets filepath of file for the audio player to play, does not start the audio player.
	 */
	@Test
	public void settingFilepath() {
		// Setting a valid audio filepath (would not know till attempting to run file).
		audio.setFile(filepath);
		assertTrue(audio.getAudioFilepath().equals(filepath));
	}
	
	/**
	 * Tests the amplification methods keep values in range.
	 */
	@Test
	public void settingAmplificaiton() {
		// Normal case, recall a log scale is used, value is positive
		audio.setAmplification(10);
		assertTrue(audio.getAmplification() > 0);
		
		// 0 value (extreme case), this will produce a negative value in the logaritmic scale
		audio.setAmplification(0);
		assertTrue(audio.getAmplification() < -1);
		
		// Setting volume to actual 0 using direct method
		audio.setAmplificationDecibels(0);
		assertTrue(-0.05f <= audio.getAmplification() && audio.getAmplification() <= 0.05f);
	}
	
	/**
	 * Tests the pan methods to keep in range.
	 */
	@Test
	public void settingPan() {
		// Setting pan to typical value in range (negative)
		float testPan = -0.5f;
		audio.setPan(-0.5f);
		assertTrue(testPan - 0.05 <= audio.getPan() && audio.getPan() < testPan + 0.05);
		
		// Setting pan to typical value in range (positive)
		testPan = 0.5f;
		audio.setPan(testPan);
		assertTrue(testPan - 0.05 <= audio.getPan() && audio.getPan() < testPan + 0.05);
		
		// Setting pan to extreme valid value (right channel)
		final float rightChannelOnly = 1f;
		audio.setPan(rightChannelOnly);
		assertTrue(rightChannelOnly - 0.05f <= audio.getPan() && audio.getPan() <= rightChannelOnly);
		
		// Setting pan to extreme valid value (left channel)
		final float leftChannelOnly = -1f;
		audio.setPan(leftChannelOnly);
		assertTrue(leftChannelOnly <= audio.getPan() && audio.getPan() <= leftChannelOnly + 0.05f);
		
		// Setting pan to value outside of range, should not change value
		audio.setPan(rightChannelOnly + 0.5f);
		assertTrue(leftChannelOnly <= audio.getPan() && audio.getPan() <= leftChannelOnly + 0.05f);
		
		// Setting pan to value outside of range, should not change value
		audio.setPan(leftChannelOnly - 0.5f);
		assertTrue(leftChannelOnly <= audio.getPan() && audio.getPan() <= leftChannelOnly + 0.05f);
	}
	
	/**
	 * Testing pause and play functionality
	 */
	@Test
	public void settingPlayPause() {
		// Testing the pausing of audio
		audio.pause();
		assertTrue(audio.isPaused());
		
		// Testing if audio is unpaused on resume
		audio.resume();
		assertFalse(audio.isPaused());
	}
	
	/**
	 * Attempting audio playback and manipulation. The thread will be told to sleep a few times to allow for changes
	 * to be applied to the audioplayer's thread.
	 */
	 @Test
 	public void attemptingPlaybackValidFilepath() {
 		// Will loop audio player in a separate thread to ensure audioplayer is running for entire test
 		audio.setFile(filepath);
 		audio.setLoop(true);
 		Thread thread = new Thread(audio);
 		thread.start();
 		
 		// Checks that playback variables is set
 		assertFalse(audio.isPaused());
 		holdTesting();
 		assertTrue(audio.isPlaying());
 		
 		// End audio playback and tests the thread has died
 		audio.stop();
 		holdTesting();
 		assertFalse(audio.isPlaying());
 		assertFalse(thread.isAlive());
 	}
	
	/** 
	 * Test the setup method's handling of a invalid filepath and non-existing file.
	 */
	@Test(expected = IOException.class)
	public void attemptingSetupInvalidFilepath() throws IOException {
		AudioPlayer player = new AudioPlayer("Very_invalid_filepath.wav");
		player.setup();
	}
	
	/**
	 * Tests the setup method's handling of an existing file of incorrect file format.
	 */
	@Test(expected = IOException.class)
	public void attemptingSetupInvalidFiletype() throws IOException {
		AudioPlayer player = new AudioPlayer("resources/characters/dead.png");
		player.setup();
	}
	
	/**
	 * Audio player is made and attempts to playback a non-existent file.
	 * 
	 * This test could be upgraded to be reading a logger for the appropriate message.
	 */
	@Test
	public void attemptingPlaybackInvalidFilepathPlayer() {
		AudioPlayer player = new AudioPlayer("This is not a valid file");
		player.setLoop(true); // Testing the looping is switched off on failure
		player.run();
		assertFalse(player.isPlaying());
		assertFalse(player.isLooping());
	}
}
