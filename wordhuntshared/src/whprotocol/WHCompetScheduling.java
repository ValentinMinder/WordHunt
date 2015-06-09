package whprotocol;

import java.util.Date;

import whprotocol.WHProtocol.WHLangage;
import whprotocol.WHProtocol.WHPointsType;

public class WHCompetScheduling extends WHAuthMessage {

	public static long defaultDelayStart = 1000;
	/** Delay to launch a competition. Default = 1000. Negative value non valid. */
	private long delayStart = defaultDelayStart;

	public static long defaultAvailableWindowTime = 1000 * 60 * 5;
	/** how long the game can be joined. Default: 5 min */
	private long availableWindowTime = defaultAvailableWindowTime;
	public static long defaultAvailableGameTime = 1000 * 60 * 5;
	/** how long the game must be. Default: 2 min */
	private long availableGameTime = defaultAvailableGameTime;

	/** language of the competition. Default: french */
	private WHLangage langage = WHLangage.FRENCH;
	/** points type of the competition. Default: letter_freq */
	private WHPointsType pointsType = WHPointsType.LENGTH_LETTERFREQ;

	/**
	 * schedule a competition for immediate scheduling
	 * 
	 * @param authToken
	 */
	public WHCompetScheduling(String authToken) {
		super(0, authToken);
	}

	/**
	 * schedule a competition with the given delay, given that's in the positive
	 * range > 1000 ms (immediate scheduling otherwise)
	 * 
	 * @param authToken
	 * @param delay
	 */
	public WHCompetScheduling(String authToken, long delay) {
		super(0, authToken);
		if (delay > delayStart) {
			this.delayStart = delay;
		}
	}

	/**
	 * schedule a competition with the given delay, given that's in the positive
	 * range > 1000 ms (immediate scheduling otherwise), available for the
	 * window time
	 * 
	 * @param authToken
	 * @param delay
	 * @param window
	 */
	public WHCompetScheduling(String authToken, long delay, long window) {
		super(0, authToken);
		if (delay > delayStart) {
			this.delayStart = delay;
		}
		this.availableWindowTime = window;
	}

	/**
	 * schedule a competition at the given time, given that's in the future
	 * (immediate scheduling otherwise)
	 * 
	 * @param authToken
	 * @param time
	 */
	public WHCompetScheduling(String authToken, Date time) {
		super(0, authToken);
		if (time.getTime() > System.currentTimeMillis()) {
			this.delayStart = Math.max(delayStart,
					time.getTime() - System.currentTimeMillis());
		}
	}

	/**
	 * @return the delayStart
	 */
	public long getDelayStart() {
		return delayStart;
	}

	/**
	 * @param delayStart
	 *            the delayStart to set
	 */
	public void setDelayStart(long delayStart) {
		this.delayStart = delayStart;
	}

	/**
	 * @return the availableWindowTime
	 */
	public long getAvailableWindowTime() {
		return availableWindowTime;
	}

	/**
	 * @param availableWindowTime
	 *            the availableWindowTime to set
	 */
	public void setAvailableWindowTime(long availableWindowTime) {
		this.availableWindowTime = availableWindowTime;
	}

	/**
	 * @return the availableGameTime
	 */
	public long getAvailableGameTime() {
		return availableGameTime;
	}

	/**
	 * @param availableGameTime
	 *            the availableGameTime to set
	 */
	public void setAvailableGameTime(long availableGameTime) {
		this.availableGameTime = availableGameTime;
	}

	/**
	 * @return the langage
	 */
	public WHLangage getLangage() {
		return langage;
	}

	/**
	 * @param langage
	 *            the langage to set
	 */
	public void setLangage(WHLangage langage) {
		this.langage = langage;
	}

	/**
	 * @return the pointsType
	 */
	public WHPointsType getPointsType() {
		return pointsType;
	}

	/**
	 * @param pointsType
	 *            the pointsType to set
	 */
	public void setPointsType(WHPointsType pointsType) {
		this.pointsType = pointsType;
	}

}
