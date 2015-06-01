package competition;

import gridhandler.GridHandler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import whobjects.Grid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;

public class CompetitionManager {

	/** 5 minutes */
	private final static long DEFAULT_AVAILABLE_TIME = 1000 * 60 * 5;

	private static CompetitionManager instance;

	private Timer timer;
	private boolean competRunning = false;
	private boolean scheduled = false;
	private Date scheduleTime = null;

	private Grid competGrid;

	public static CompetitionManager getInstance() {
		if (instance == null) {
			synchronized (CompetitionManager.class) {
				if (instance == null) {
					instance = new CompetitionManager();
				}
			}
		}
		return instance;
	}

	private CompetitionManager() {
		timer = new Timer("compet scheduler");

	}

	public WHMessage schedule(long timestamp) {
		return schedule(timestamp, DEFAULT_AVAILABLE_TIME);
	}

	/**
	 * Schedule a new competition, at the given time, available for the
	 * specified time.
	 * 
	 * @param timestamp
	 * @param available
	 * @return true if successful.
	 */
	public WHMessage schedule(final long timestamp, final long available) {
		if (timestamp < System.currentTimeMillis()) {
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Cannot schedule in the past!");
		}
		if (competRunning) {
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Cannot schedule while another competition is running!");
		}
		if (scheduled) {
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Cannot schedule while another is already scheduled in "
							+ getTime());
		}

		// the competition is scheduled in future
		scheduleTime = new Date(timestamp);
		scheduled = true;
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// start the competition
				competGrid = GridHandler.getInstance().getGrid();
				competRunning = true;

				// programm the self-destruct of the competition
				// (after the available time)
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						scheduled = false;
						competRunning = false;
						scheduleTime = null;
						competGrid = null;
					}
				}, available);
			}
		}, scheduleTime);
		return new WHMessage(WHMessageHeader.SCHEDULE_COMPET_ACK,
				"Competition scheduled in " + getTime());
	}

	private String getTime() {
		String next = null;
		long time = (scheduleTime.getTime() - System.currentTimeMillis()) / 1000;
		if (time > 60) {
			next = "approx. " + time / 60 + " minutes ";
		} else {
			next = "" + time + " seconds ";
		}
		next += "(" + scheduleTime.toString() + ")";
		return next;
	}

	public WHMessage getGrid() {
		if (!competRunning) {
			if (scheduled) {
				return new WHMessage(WHMessageHeader.SERVER_ERROR_500,
						"Next competition scheduled in " + getTime());
			} else {
				return new WHMessage(WHMessageHeader.SERVER_ERROR_500,
						"No competition running nor scheduled right now!");
			}
		} else {
			return new WHMessage(WHMessageHeader.GRID_REPLY,
					new WHGridReplyMessage(0, competGrid));
		}
	}

	public static void main(String args[]) {
		CompetitionManager manager = CompetitionManager.getInstance();

		// not scheduled competition
		System.out.println("no grid  : " + manager.getGrid());

		// no schedule in the past.
		System.out.println("no sched:  "
				+ manager.schedule(System.currentTimeMillis() - 1000));

		// schedule in 2sec, during 3 sec.
		System.out.println("scheduled: "
				+ manager.schedule(System.currentTimeMillis() + 2100, 3000));

		// no schedule with other schedule
		System.out.println("no sched:  "
				+ manager.schedule(System.currentTimeMillis() + 1000));

		// its scheduled... but not running!
		System.out.println("scheduled: " + manager.getGrid());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// its scheduled... but not running!
		System.out.println("scheduled: " + manager.getGrid());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// okay
		System.out.println("grid comp: " + manager.getGrid());
		System.out.println("grid comp: " + manager.getGrid());

		// should serialize & deserialize well !
		System.out.println(WHMessage.validateMessage(manager.getGrid()
				.toString()));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// not scheduled competition
		System.out.println("no grid  : " + manager.getGrid());

		// schedule is again possible, as previous one has finished !
		System.out.println("again sche:"
				+ manager.schedule(System.currentTimeMillis() + 1000));

	}

}
