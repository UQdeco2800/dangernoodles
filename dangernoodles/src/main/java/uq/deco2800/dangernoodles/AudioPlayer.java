package uq.deco2800.dangernoodles;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.*;

/**
 * Audio Player for playing back sounds and/or music. 
 * @author Paul Haley
 */
public class AudioPlayer implements Runnable {
	private static final int BUFFER_SIZE = 4096;
	private static final int EOF = -1;

	// Audio file requested to be played
	private String audioFilepath = null;

	// Audio related variables for I/O and playback
	private AudioInputStream audioStream = null; // Input stream for file
	private AudioFormat audioFormat = null; // Audio format (file and finer details)
	private SourceDataLine sourceLine = null; // Source data line for playback
	private AudioInputStream inputStream = null; // Audio Input Stream related to the file to be playedback

	// Controls for the audio playback
	private volatile boolean loop = false; // Allows for a sound to have looped playback until unset, paused or stopped
	private volatile boolean stopped = false; // Stops the current playback entirely
	private volatile boolean isPaused = false; // Pauses the current playback until toggled
	private volatile boolean isPlaying = false; // True if a audio file is being played back

	// Controls for effects to be applied to audio playback (realtime).
	private volatile float amplification = 0.0f; // amplification (including decrease) of playback from base (decibels)
	// pan (left & right bias) of the audio being played back. -1 is left only, 0 centered, 1 right only
	private volatile float pan = 0.0f;

	// Logger for when things are going bad. Please report any issues if you had to use this.
	private static final Logger LOGGER = Logger.getLogger("Audio");

	/**
	 * Constructor for the audio player, takes the filepath of the file to be played. Does not start playing 
	 * 	immediately and required the run() method to be ran.
	 * @param filename File to be played, must be PCM file of format .wav (Microsoft) or .aiff (Apple).
	 */
	public AudioPlayer(String filepath) {
		this.audioFilepath = filepath;
	}

	/**
	 * This method handles the retrieving of the specified audio file from audioFilePath and then setting the 
	 * required file format details required for later playback.
	 * 
	 * @ensure Set file of audioFilePath has it audioFormat, inputStream and audioStream all setup as needed for 
	 * playback.
	 * @throws IOException If the specified file could not be found or a general IOException has occurred.
	 */
	public void setup() throws IOException {
		// A IOException is possible when obtaining the audio input stream...
		File file = new File(audioFilepath);
		
		@SuppressWarnings("deprecation")
		final URL url = file.toURL();
		
		try {
			inputStream = AudioSystem.getAudioInputStream(url);
			LOGGER.info("File successfully found: " + audioFilepath);
		} catch (UnsupportedAudioFileException e) {
			LOGGER.warning("Invalid file format. Expected PCM formatted .wav or .aiff." + failFile() + 
					e.getStackTrace());
			throw new IOException(e);
		}

		final AudioFormat baseFormat = inputStream.getFormat(); // Obtain audio format data
		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 
				16, baseFormat.getChannels(), 2 * baseFormat.getChannels(), baseFormat.getSampleRate(), false);
		// Obtains the audio stream if possible
		audioStream = AudioSystem.getAudioInputStream(audioFormat, inputStream);
	}

	/**
	 * Obtains the source data line that would be used for playback.
	 * 
	 * @throws LineUnavailableException Thrown if an audio line is not available. 
	 */
	private void obtainSourceLine() throws LineUnavailableException {
		// Obtaining dataline, specifying that a source data line and audio format.
		DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
		sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
	}
	
	/**
	 * Sets the source line gain (set the playback volume) if supported. 
	 */
	private void setSourceLineGain() {
		// Check if gain (amplification) is supported on the source line, sets it if so
		if (sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN))
			.setValue(amplification);
		}
		
		return;
	}
	
	/**
	 * Sets the source line pan (set the playback left and right channel bias) if supported.
	 */
	private void setSourceLinePan() {
		// Check if pan (left & right bias of sound) of the source line is supported, set it if so
		if (sourceLine.isControlSupported(FloatControl.Type.PAN)) {
			((FloatControl) sourceLine.getControl(FloatControl.Type.PAN)).setValue(pan);
		}
		
		return;
	}

	/**
	 * Attempts to plays the sound that has been set in the audioFilepath variable.
	 * 
	 * @require valid audioFilepath &&
	 * 		setup() was ran successfully &&
	 * 		obtainSourceLine() was ran successfully
	 * @ensure That audio playback will be attempted based off available system resources.
	 * @throws IOException Thrown if a IOException is raised.
	 * @throws LineUnavailableException Thrown if audio line resource was unavailable.
	 */
	private void playSound() throws IOException, LineUnavailableException {
		isPlaying = true; // Attempting to read and play file
		
		// Finds file, determines audio filetype and sets up the input stream if the file is valid
		setup();
		
		// Obtains the source line for playback.
		obtainSourceLine();
		
		// Preparing to play audio few things can go wrong here including failing to obtain a line
		// Playing the audio stream from file, onPlay
		sourceLine.open(audioFormat); // Attempting to open the audio line
		sourceLine.start(); // Begins streaming data (attempting to play audio).
		
		/* Attempts to play the file if EOF has not been reached or the 
		 * audio player has not been told to stop.
		 * 
		 * Sonar does complain about the nested if and while statements in the below code. It is needed though to 
		 * prevent the constant creation of passing of the audioBuffer for all the sounds being played; I tested this 
		 * found this out the hard way.
		 */
		final byte[] audioBuffer = new byte[BUFFER_SIZE]; // Buffer for playing audio byte stream
		int bytesRead = 0; // Number of bytes in the audioBuffer
		while (!(bytesRead == EOF || stopped)) {
			if (!isPaused) { // If the audio player is not pause, play audio
				isPlaying = true;
				
				// Attempts to read audio file being played back.
				bytesRead = audioStream.read(audioBuffer, 0, audioBuffer.length);
				
				// If not end of file, attempt playback.
				if (bytesRead != EOF) {
					// Sets volume and pan if available.
					setSourceLineGain();
					setSourceLinePan();

					// Attempts write to audio buffer to the source line to be sent to the mixer for playing.
					sourceLine.write(audioBuffer, 0, bytesRead);
				}
			} else { // Audio is paused, mark that it is not playing
				LOGGER.info("Playback is paused");
				isPlaying = false;
			}
		}

		// Plays remaining data, clean up/freeing resources
		sourceLine.drain();
		sourceLine.stop();
		sourceLine.close();
		audioStream.close();


		// finally closing the data stream from the file.
		inputStream.close();

		// Audio is claimed to always be playing until a stop is called or looping is cancelled.
		isPlaying = loop && !stopped ? true : false;
		
		// Audio playback is completely stopped.
	}

	/**
	 * Run method called when new thread is made for the audio player. Multiple threads are used to allow for audio 
	 * and other sound effects to be played concurrently.
	 * For sound effects (without looping enabled), the thread is terminated automatically when this method 
	 * returns.
	 */
	@Override
	public void run() {
		stopped = false;
		isPaused = false;

		// Play the audio once and loop if the audio is not stopped and is set to loop.
		do {
			try {
				playSound();
			} catch (IOException e) {
				LOGGER.warning("IOException has occured, playback stopped." + failFile() + e.getStackTrace() + e);
				stop();
			} catch (LineUnavailableException e) {
				LOGGER.warning("Audio Line was unavailable, playback stopped." + failFile() + e);
				stop();
			}
		} while (loop && !stopped);
	}

	/**
	 * @return Message about file that failed to work (including filepath).
	 */
	private String failFile() {
		return "\nFile attempted to be loaded " + "was:\n" + audioFilepath + "\n";
	}

	/**
	 * Pauses the audio playback.
	 */
	public void pause() {
		isPaused = true;
		return;
	}

	/**
	 * Resuming audio playback after being previously paused.
	 */
	public void resume() {
		isPaused = false;
		return;
	}

	/**
	 * This method stops audio playback e.g if audio was looped, this sets stopped to true which
	 * then enables the flow of control to exit from the run() method, thereby terminating
	 * the thread on which the current instance of AudioPlayer was running.
	 * 
	 * Stop also forces off looping and isPlaying.
	 */
	public void stop() {
		stopped = true;
		loop = false;
		isPlaying = false;
		return;
	}

	/**
	 * Check if playback is set to loop.
	 * @return true if the audio is set to be looping
	 */
	public boolean isLooping() {
		return this.loop;
	}

	/**
	 * Check if the playback is playing. Check the volume if this return true and you cannot hear anything.
	 * @return true if the audio file is currently playing (audible), false otherwise
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * Check the status of the playback being paused.
	 * @return true if playback is paused (not stopped), false otherwise
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * Check the file that is set for playing
	 * @return String of the filepath to the audio file setup to play
	 */
	public String getAudioFilepath() {
		return audioFilepath;
	}

	/**
	 * Returns the amplification of Audio Player (if supported) in decibels.
	 * @return Amplification set to Audio Player
	 */
	public float getAmplification() {
		return amplification;
	}

	/**
	 * Returns the pan (left and right channel basis) of the Audio Player if supported. -1 for full left channel, 0 
	 * for centered (default) and 1 for full right channel only.
	 * @return Pan of the Audio Player
	 */
	public float getPan() {
		return pan;
	}

	/**
	 * Set the filepath to the audio file to be played. Will also reconfigure the parts of the Audio Player so that 
	 * no pre-processing is required when a play request is called.
	 * @param filepath The filepath to the audio file to be played.
	 */
	public void setFile(String filepath) {
		this.audioFilepath = filepath;
		return;
	}

	/**
	 * Allows amplification (volume) of the playback to be controlled. This method allows for directly proportional 
	 * changes to the  amplification. For example:
	 * 	0 is effectively muted
	 *  1 is no additional gain, playback is as recorded 
	 * @require amplification is non-negative
	 * @ensure amplification is set to the value specified or a close approximation
	 * @param gain Relative gain to apply to playback. 
	 */
	public void setAmplification(float gain) {
		// If gain given is 0, sets to low value
		float newGain = gain <= 0.0f ? 0.0001f : gain;
		// Converting the gain given to decibels 
		setAmplificationDecibels((float)(20.0 * (Math.log(newGain) / Math.log(10.0))));
		return;
	}

	/**
	 * Allows amplification (volume) of playback, value given is in decibles.
	 * @param decibels decibels of gain to apply to playback
	 */
	public void setAmplificationDecibels(float decibels) {
		float safeGain = decibels < 6f ? decibels : 6; 
		this.amplification = safeGain;
		return;
	}

	/**
	 * Controls the channel bias of the audio playback by proportionally controlling the output of each channel. By 
	 * default, the audio playback is centered.
	 * @require -1 <= pan <= 1
	 * @ensure The given pan value is the new pan value for audio playback.
	 * @param pan -1 for full left channel, 0 is centered, 1 is full right channel.
	 */
	public void setPan(float pan) {
		this.pan = -1f <= pan && pan <= 1f ? pan : this.pan;
		return;
	}

	/**
	 * Set if the audio playback should loop when playing.
	 * @param loop True will cause audio playback to loop while the music is not paused or stopped, false will only 
	 * 	allow the file to be played once.
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
		return;
	}
}