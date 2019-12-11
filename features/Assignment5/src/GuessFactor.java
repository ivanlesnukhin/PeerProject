import java.awt.geom.Point2D;

import robocode .*;
import robocode.util.Utils;

class GuessFactor implements IFiring {
	
	private AdvancedRobot robot;
	private final double BULLET_POWER = 1;
	private double lastEnemyVelocity;
	private double lateralDirection;
	private String movementMethod;
	
	
	public GuessFactor (AdvancedRobot robot, double lastEnemyVelocity, double lateralDirection, String movementMethod) {
		this.robot = robot;
		this.lastEnemyVelocity = lastEnemyVelocity;
		this.lateralDirection = lateralDirection;
		this.movementMethod = movementMethod;
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(robot);
		wave.gunLocation = new Point2D.Double(robot.getX(), robot.getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity, movementMethod);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		setFire(wave.bulletPower);
		if (robot.getEnergy() >= BULLET_POWER) {
			robot.addCustomEvent(wave);
		}
		setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - robot.getRadarHeadingRadians()) * 2);
	}


	@Override
	public void setFire(double BulletPower) {
		robot.setFire(BulletPower);
	}
	
	public void setTurnGunRightRadians(double angle) {
		robot.setTurnGunRightRadians(angle);
	}
	
	public void setTurnRadarRightRadians(double angle) {
		robot.setTurnRadarRightRadians(angle);
	}
}

class GFTWave extends Condition {
	static Point2D targetLocation;

	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;

	private static final int DISTANCE_INDEXES = 5;
	private static final int VELOCITY_INDEXES = 5;
	private static final int BINS = 25;
	private static final int MIDDLE_BIN = (BINS - 1) / 2;
	private static final double MAX_ESCAPE_ANGLE = 0.7;
	private static final double BIN_WIDTH = MAX_ESCAPE_ANGLE / (double)MIDDLE_BIN;
	
	private static int[][][][] statBuffers = new int[DISTANCE_INDEXES][VELOCITY_INDEXES][VELOCITY_INDEXES][BINS];

	private int[] buffer;
	private AdvancedRobot robot;
	private double distanceTraveled;
	
	GFTWave(AdvancedRobot _robot) {
		this.robot = _robot;
	}
	
	public boolean test() {
		advance();
		if (hasArrived()) {
			buffer[currentBin()]++;
			robot.removeCustomEvent(this);
		}
		return false;
	}

	double mostVisitedBearingOffset() {
		return (lateralDirection * BIN_WIDTH) * (mostVisitedBin() - MIDDLE_BIN);
	}
	
	void setSegmentations(double distance, double velocity, double lastVelocity, String movementMethod) {
		int distanceIndex = 0;
		if (movementMethod != null) {
			if (movementMethod == "WaveSurfing")
				distanceIndex = Math.min(DISTANCE_INDEXES-1, (int)(distance / (900 / DISTANCE_INDEXES)));
			else if (movementMethod == "Random")
				distanceIndex = (int)(distance / (1000 / DISTANCE_INDEXES));
			
			int velocityIndex = (int)Math.abs(velocity / 2);
			int lastVelocityIndex = (int)Math.abs(lastVelocity / 2);
			buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
		}
	}

	private void advance() {
		distanceTraveled += GFTUtils.bulletVelocity(bulletPower);
	}

	private boolean hasArrived() {
		return distanceTraveled > gunLocation.distance(targetLocation) - 18;
	}
	
	private int currentBin() {
		int bin = (int)Math.round(((Utils.normalRelativeAngle(GFTUtils.absoluteBearing(gunLocation, targetLocation) - bearing)) /
				(lateralDirection * BIN_WIDTH)) + MIDDLE_BIN);
		return GFTUtils.minMax(bin, 0, BINS - 1);
	}
	
	private int mostVisitedBin() {
		int mostVisited = MIDDLE_BIN;
		for (int i = 0; i < BINS; i++) {
			if (buffer[i] > buffer[mostVisited]) {
				mostVisited = i;
			}
		}
		return mostVisited;
	}	
}