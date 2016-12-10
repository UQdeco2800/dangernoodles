package uq.deco2800.dangernoodles;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * This is the Audio Manager for all playback of sounds and music in the game. This manager should be used over the 
 * direct creation of Audio Players from the AudioPlayer.java class as to allow for more centralised control and 
 * increased performance. Control for played sounds and music are available from the returned AudioPlayer object at 
 * invocation. 
 * 
 * This class is a singleton via the use of an enum, this ensures that only the one instance can exist. 
 * 
 * @author Paul Haley
 */
public enum AudioManager {
	INSTANCE;
	
	/**
	 * Singleton, no constructor wanted.
	 */
	private AudioManager() {
	}
	
	// Instance variables
	// Constants about maximums of playback and indexes for music playing.
	private static final int MAX_SOUND = 16;
	private static final int MAX_MUSIC = 2;
	private static final int MAIN_MUSIC = 0;
	private static final int OVERLAY_MUSIC = 1;
	
	// Volumes and muted variables.
	private static final float MUTED = 0f;
	private static float volumeSound = 1;
	private static float volumeMusic = 1;
	private static boolean mutedSound = false;
	private static boolean mutedMusic = false;
	
	// Index and arrays for for holding references to AudioPlayers being used. 
	private static int soundIndex = 0;
	private static AudioPlayer[] playingSounds = new AudioPlayer[MAX_SOUND];
	private static AudioPlayer[] playingMusic = new AudioPlayer[MAX_MUSIC];
	
	// Array of time since playback commenced for sound effects, indexing matches playingSounds array.
	private static long[] playingSoundsTime = new long[MAX_SOUND];
		
	// Queues for thread pools for sound effect and music playback.
	private static final BlockingQueue<Runnable> soundQueue = new ArrayBlockingQueue<>(MAX_SOUND);
	private static final BlockingQueue<Runnable> musicQueue = new ArrayBlockingQueue<>(MAX_MUSIC);
	
	// Current number of active sound effects.
	private static int activeSounds = 0;
	
	// Fixed thread pool for sound effects playback.
	private static ExecutorService soundPool = new ThreadPoolExecutor(MAX_SOUND, MAX_SOUND, 0L, 
			TimeUnit.MILLISECONDS, soundQueue) {
	    /**
	     * Before Execute allows for an action to occur just as a new queued task begins. In this scenerio, it is just
	     * incrementing the active sounds count.
	     */
	    @Override
		protected void beforeExecute(Thread thread, Runnable runnable) {
			activeSounds++;
		}
		
		/**
         * After Execute allows for an action to occur just as a old task finishes. In this scenerio, it is just
         * decrementing the active sounds count.
         */
		@Override
	    protected void afterExecute(Runnable runnable, Throwable thrown) {
			activeSounds--;
		}
	};
	
	// Fixed thread pool for music playback.
	private static ExecutorService musicPool = new ThreadPoolExecutor(MAX_MUSIC, MAX_MUSIC, 0L, 
			TimeUnit.MILLISECONDS, musicQueue) {
		
	};
	
	// Logger for when things are going bad. Please report any issues if you had to use this.
	private static final Logger LOGGER = Logger.getLogger("Audio");
	
	/**
	 * This is intended for relatively short sound effect playback only, for music playback, see playMusic().
	 * If you want to playback your sound at a different volume, use the overloaded method with gain.
	 * 
	 * This method is used for the playback of all sound effects in game. This method should be used over directly 
	 * accessing AudioPlayer.java. When this method is called, an AudioPlayer is made with the specified sound and 
	 * looping setting and a reference to the object is returned. Upon calling, the sound is played immediately but 
	 * can be paused (as well as any other controls present in the AudioPlayer), this should be avoided though and 
	 * instead delay the call. If the AudioPlayer is set to loop, a reference must be held such that looping can be 
	 * ceased eventually. 
	 * 
	 * If there are any issues or requests for changes, please contact Paul Haley, \@paul-haley on Slack or I am also 
	 * be reached on our GitHub ticket #55
	 * 
	 * @param filepath Complete string filepath to the .wav or .aiff file to be played.
	 * @param loop Set true if looped playback is wanted, false otherwise. IF SET TRUE, HOLD INSTANCE OF AUDIOPLAYER 
	 * SO THAT IT CAN BE UNLOOPED LATER.
	 * @return AudioPlayer instance for this sound effect or null if the AudioManager is shutdown.
	 */
	public static AudioPlayer playSound(String filepath, boolean loop) {
		return AudioManager.playSound(filepath, loop, 1f);
	}
	
	/**
	 * This is intended for relatively short sound effect playback only, for music playback, see playMusic().
	 * 
	 * This method is used for the playback of all sound effects in game. This method should be used over directly 
	 * accessing AudioPlayer.java. When this method is called, an AudioPlayer is made with the specified sound and 
	 * looping setting and a reference to the object is returned. Upon calling, the sound is played immediately but 
	 * can be paused (as well as any other controls present in the AudioPlayer), this should be avoided though and 
	 * instead delay the call. If the AudioPlayer is set to loop, a reference must be held such that looping can be 
	 * ceased eventually. 
	 * 
	 * If there are any issues or requests for changes, please contact Paul Haley, \@paul-haley on Slack or I am also 
	 * be reached on our GitHub ticket #55
	 * 
	 * @require 0 <= gain
	 * 
	 * @param filepath Complete string filepath to the .wav or .aiff file to be played.
	 * @param loop Set true if looped playback is wanted, false otherwise. IF SET TRUE, HOLD INSTANCE OF AUDIOPLAYER 
	 * SO THAT IT CAN BE UNLOOPED LATER.
	 * @param gain 1 represents normal volume, 0 if effectively muted, volume is proportional.
	 * @return AudioPlayer instance for this sound effect or null if the AudioManager is shutdown.
	 */
	public static AudioPlayer playSound(String filepath, boolean loop, float gain) {
		// Checks if the Sound Pool is still operating, could have been shutdown
		if (soundPool.isShutdown()) {
			return null;
		}
		
		
		// Attempts to remove oldest element in sound queue if it is full.
		if (soundQueue.size() == MAX_SOUND) {
			soundQueue.remove(); // Removes the oldest sound queued to play.
			LOGGER.warning("Sound queue for execution is full!");
		}

		
		// Index of oldest sound, intialised for use later if the if block not entered.
		int oldestPlayerIndex = soundIndex;
		
		// If there is a maximum number of sounds playing, kill the oldest one.
		if (activeSounds >= MAX_SOUND) {
			LOGGER.info("Killing oldest sound effect, more than 16 active sounds.");
			oldestPlayerIndex = oldestSoundIndex();
			// Killing the oldest AudioPlayer
			playingSounds[oldestPlayerIndex].stop();
		}
		
		// Setting up new sound so timing and access to the player are saved
		playingSounds[oldestPlayerIndex] = new AudioPlayer(filepath);
		playingSounds[oldestPlayerIndex].setLoop(loop);
		playingSoundsTime[oldestPlayerIndex] = System.currentTimeMillis();
		AudioPlayer player = playingSounds[oldestPlayerIndex];
		player.setLoop(loop);
		
		// Setting the volume based on if sound is muted or not.
		// If a custom volume has been requested, mute overrides
		float volume = (0.95f < gain && gain < 1.05f) ? volumeSound : gain;
		volume = mutedSound ? MUTED : volume;
		player.setAmplification(volume);
		
		// Queuing audio player to play. Should begin playing immediately due to killing oldest.
		soundPool.execute(player);
		
		// Setting index for next execution
		soundIndex = (oldestPlayerIndex + 1) % MAX_SOUND;
		return player;
	}
	
	/**
	 * Use this method to play music or overlays (ambiance), sound effects should be played via playSound().
	 * 
	 * Playing music through this method will cause instaneous playback of the requested .wav or .aiff sound resource
	 * give. The music can be set to loop. Music playback as been purposely restricted to a main track and an overlay
	 * track, there is no technical limitations of either and is purely for usability. 
	 * 
	 * It is not as important as playing sounds to hold on to looped music as when the track is replaced, the previous
	 * one will be killed.
	 * 
	 * If there are any issues or requests for changes, please contact Paul Haley, \@paul-haley on Slack or I am also 
     * be reached on our GitHub ticket #55
     * 
     * @param filepath Complete string filepath to the .wav or .aiff file to be played.
     * @param loop Set true if looped playback is wanted, false otherwise.
	 * @param isMainTrack Set true for the music to be considered the main track, false otherwise. There is no 
	 * technical difference and is purely for usability. 
	 * @return AudioPlayer instance playing the music file or null if the AudioManager has been shutdown.
	 */
	public static AudioPlayer playMusic(String filepath, boolean loop, boolean isMainTrack) {
		// Checks if the Music Pool is still operating, could have been shutdown
		if (musicPool.isShutdown()) {
			return null;
		}
		
		// If music queue is full, stop the current music.
		if (musicQueue.size() == MAX_MUSIC) {
			stopMusicOverlay();
		}
		
		int trackIndex = isMainTrack ? MAIN_MUSIC : OVERLAY_MUSIC;
		// If there is an audio player playing music, stop it.
		if (playingMusic[trackIndex] != null) {
			playingMusic[trackIndex].stop();
		}
		
		// Setup to play new music file.
		playingMusic[trackIndex] = new AudioPlayer(filepath);
		AudioPlayer player = playingMusic[trackIndex];
		player.setLoop(loop);
		
		// Setting the volume based on if music is muted or not.
		float volume = mutedMusic ? MUTED : volumeMusic;
		player.setAmplification(volume);
		
		// Queuing audio player to play music, should commence immediately.
		musicPool.execute(player);
		
		return player;
	}
	
	/**
	 * Stops the currently playing music overlay. Main music track will continue playing.
	 */
	public static void stopMusicOverlay() {
		AudioPlayer overlay = playingMusic[OVERLAY_MUSIC];
		if (overlay != null) {
			overlay.stop();
		}
		return;
	}
	
	/**
	 * Internal method to change the volume on all currently playing sound effects.
	 * @require volume >= 0
	 * @param volume The volume level for sound effect playback. 0 is muted, 1 is original.
	 */
	private static void updateVolumeSound(float volume) {
		for (int i = 0; i < MAX_SOUND; ++i) {
			if (playingSounds[i] != null) {
				playingSounds[i].setAmplification(volume);
			}
		}
	}
	
	/**
     * Internal method to change the volume on all currently playing music.
     * @require volume >= 0
     * @param volume The volume level for music playback. 0 is muted, 1 is original.
     */
	private static void updateVolumeMusic(float volume) {
		for (int i = 0; i < MAX_MUSIC; ++i) {
			if (playingMusic[i] != null) {
				playingMusic[i].setAmplification(volume);
			}
		}
	}
	
	/**
	 * Gives the current sound effects volume, some audio may have used a custom value which override.
	 * @return Current sound effects volume.
	 */
	public static float getVolumeSound() {
		return volumeSound;
	}
	
	/**
	 * Gives the current music volume, some audio may have used a custom value which override.
	 * @return Current music volume.
	 */
	public static float getVolumeMusic() {
		return volumeMusic;
	}
	
	/**
	 * Set the sound effect playback volume. This will affect all currently playing sounds and future sounds.
	 * 
	 * @require 0 <= volume
	 * @param volume Volume for sound to be played back at. 0 is muted, 1 is original.
	 */
	public static void volumeSound(float volume) {
		// Checking volume value is in range and then setting.
		float adjustedVolume = volume;
		adjustedVolume = adjustedVolume < 0 ? 0 : adjustedVolume; // Ensure higher than minimum
		volumeSound = adjustedVolume;
		
		updateVolumeSound(volumeSound);
		return;
	}
	
	/**
	 * Set the music playback volume. This will affect all currently playing music and future music.
	 * 
	 * @require 0 <= volume
	 * @param volume Volume for music to be played back at. 0 is muted, 1 is original.
	 */
	public static void volumeMusic(float volume) {
		// Checking volume value is in range and then setting.
		float adjustedVolume = volume;
		adjustedVolume = adjustedVolume < 0 ? 0 : adjustedVolume; // Ensure higher than minimum
		adjustedVolume = adjustedVolume > 1 ? 1 : adjustedVolume; // Ensure lower than maximum
		volumeMusic = adjustedVolume;
		
		updateVolumeMusic(volumeMusic);
		return;
	}
	
	/**
	 * Check if sound effects are muted.
	 * @return Returns true if sound effects are muted.
	 */
	public static boolean isSoundMuted() {
		return mutedSound;
	}
	
	/**
	 * Check if music is muted.
	 * @return Returns true if music is muted.
	 */
	public static boolean isMusicMuted() {
		return mutedMusic;
	}
	
	/**
	 * Mutes all sound effects playing and in the future.
	 */
	public static void muteSound() {
		updateVolumeSound(MUTED);
		mutedSound = true;
		return;
	}
	
	/**
	 * Mutes all music playing and in the future.
	 */
	public static void muteMusic() {
		updateVolumeMusic(MUTED);
		mutedMusic = true;
		return;
	}

	/**
	 * Unmute all sound effects, volume will return to the last set level.
	 */
	public static void unmuteSound() {
		volumeSound(volumeSound);
		mutedSound = false;
		return;
	}
	
	/**
	 * Unmute all music, volume will return to the last set level.
	 */
	public static void unmuteMusic() {
		volumeMusic(volumeMusic);
		mutedMusic = false;
		return;
	}
	
	/**
	 * Toggles the muting of sound effects playback.
	 */
	public static void toggleMuteSound() {
		if (mutedSound) {
			unmuteSound();
		} else {
			muteSound();
		}
	}
	
	/**
	 * Toggles the muting of music playback.
	 */
	public static void toggleMuteMusic() {
		if (mutedMusic) {
			unmuteMusic();
		} else {
			muteMusic();
		}
	}
	
	/**
	 * Finds the oldest sound player in the sound thread pool and returns its index in the array.
	 * @ensure 0 <= return <= MAX_SOUND
	 * @return Array index of oldest sound player.
	 */
	private static int oldestSoundIndex() {
		long oldestPlayerTime = System.currentTimeMillis(); // Works downwards from current time.
		int oldestPlayerIndex = 0;
		
		// Iterates all playing sounds to find oldest one
		for (int i = 0; i < MAX_SOUND; ++i) {
			if (playingSoundsTime[i] < oldestPlayerTime) {
				oldestPlayerIndex = i;
				oldestPlayerTime = playingSoundsTime[i];
			}
		}
		return oldestPlayerIndex;
	}
	
	/**
	 * Immediately stop the AudioManager and close the thread pools running sound effects and music.
	 */
	public static void shutdown() {
		soundPool.shutdownNow();
		musicPool.shutdownNow();
		return;
	}
}
