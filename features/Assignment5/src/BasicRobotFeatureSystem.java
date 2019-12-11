import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Properties; 

/**
 * A combination of the movement from BasicSurfer and the gun from 
 * GFTargetingBot. See:
 *   http://robowiki.net?BasicSurfer
 *   http://robowiki.net?GFTargetingBot
 *
 * Change log:
 *   1.01 - solved bug in distance-bin calculation when distance > 900
 *   1.02 - fixed wave mismatching caused by rounding errors
 */

public class BasicRobotFeatureSystem extends AdvancedRobot {
	IMovement movementMeth;
	//#if WaveSurfing
    public static int BINS = 47;
    public static double _surfStats[] = new double[BINS]; // we'll use 47 bins
    public Point2D.Double _myLocation;     // our bot's location
    public Point2D.Double _enemyLocation;  // enemy bot's location

    public ArrayList _enemyWaves;
    public ArrayList _surfDirections;
    public ArrayList _surfAbsBearings;
    //#endif
	
	//#if Random
//@	private static final double BULLET_POWER = 1.9;
    //#else
    private static double BULLET_POWER = 1.0;
	//#endif
    
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	
	public boolean WaveSurfing = false, 
			Wall_Smoothing = false,
			visibleEnemyWaves = false,
			Random = false,
			GF = false,
			DBL_Bullet_Power = false,
			BBY = false,
			RBY = false,
			GWY = false;

	//#if WaveSurfing
    // We must keep track of the enemy's energy level to detect EnergyDrop,
    // indicating a bullet is fired
    public static double _oppEnergy = 100.0;

    // This is a rectangle that represents an 800x600 battle field,
    // used for a simple, iterative WallSmoothing method (by Kawigi).
    // If you're not familiar with WallSmoothing, the wall stick indicates
    // the amount of space we try to always have on either end of the tank
    // (extending straight out the front or back) before touching a wall.
    public static Rectangle2D.Double _fieldRect
        = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);
    public static double WALL_STICK = 160;
    //#endif
    
    //#if Random
//@    private static GFTMovement movement;
//@    
//@	public BasicRobotFeatureSystem() {
//@		movement = new GFTMovement(this);	
//@	}
	//#endif
	
	
	boolean stringToBoolean (String input) {
		if(input == "false") 
			return false;
		else 
			return true;
	}

    public void run() {
    	try (FileReader reader = new FileReader("runtime.properties")) {
    		Properties properties = new Properties();
    		properties.load(reader);
    		
    		Wall_Smoothing = stringToBoolean(properties.getProperty("Wall_Smoothing"));
			visibleEnemyWaves = stringToBoolean(properties.getProperty("visibleEnemyWaves"));
			Random = stringToBoolean(properties.getProperty("Random"));
			GF = stringToBoolean(properties.getProperty("GF"));
			DBL_Bullet_Power = stringToBoolean(properties.getProperty("DBL_Bullet_Power"));
			BBY = stringToBoolean(properties.getProperty("BBY "));
			RBY = stringToBoolean(properties.getProperty("RBY"));
			GWY = stringToBoolean(properties.getProperty("GWY"));
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	
    	if (BBY)
    		setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
    	else if (RBY)
    		setColors(Color.RED, Color.BLACK, Color.WHITE);
    	else if (GWY)
    		setColors(Color.GREEN, Color.WHITE, Color.YELLOW);
    	
    	if(WaveSurfing) {
    		movementMeth = new WaveSurfing(this);
    	} else if(Random) {
    		movementMeth = new RandomMovement(this);
    	} 


		lateralDirection = 1;
		lastEnemyVelocity = 0;
    }

	    //#if DBL_Bullet_Power
//@		BULLET_POWER = 1.9;
		//#endif
		
		
		//#if Random
//@		setAdjustRadarForGunTurn(true);
//@		setAdjustGunForRobotTurn(true);
//@		do {
//@			turnRadarRightRadians(Double.POSITIVE_INFINITY); 
//@		} while (true);
//@	}
//@ 
//@	public void onScannedRobot(ScannedRobotEvent e) {
		//#endif
		
		//#if WaveSurfing

    public void onScannedRobot(ScannedRobotEvent e) {
    	
    	movementMeth.onScannedRobot(e);
        //#if GF
		double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		double enemyVelocity = e.getVelocity();
		if (enemyVelocity != 0) {
			lateralDirection = GFTUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
		}
		GFTWave wave = new GFTWave(this);
		wave.gunLocation = new Point2D.Double(getX(), getY());
		GFTWave.targetLocation = GFTUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
		wave.lateralDirection = lateralDirection;
		wave.bulletPower = BULLET_POWER;
		wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
		lastEnemyVelocity = enemyVelocity;
		wave.bearing = enemyAbsoluteBearing;
		setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
		setFire(wave.bulletPower);
		if (getEnergy() >= BULLET_POWER) {
			addCustomEvent(wave);
		}
		//#if Random
//@		        movement.onScannedRobot(e);
		//#endif
		setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);
		//#endif
		
    }

    //#if visibleEnemyWaves
   
    //#endif
    
}


//#if GF
class GFTWave extends Condition {
	static Point2D targetLocation;

	double bulletPower;
	Point2D gunLocation;
	double bearing;
	double lateralDirection;
//#if Random
//@	private static final double MAX_DISTANCE = 1000;
//#endif
//#if WaveSurfing
	private static final double MAX_DISTANCE = 900;
//#endif
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
	
	void setSegmentations(double distance, double velocity, double lastVelocity) {
		//#if WaveSurfing
		int distanceIndex = Math.min(DISTANCE_INDEXES-1, (int)(distance / (MAX_DISTANCE / DISTANCE_INDEXES)));
		//#endif
		//#if Random
//@		int distanceIndex = (int)(distance / (MAX_DISTANCE / DISTANCE_INDEXES));
		//#endif
		int velocityIndex = (int)Math.abs(velocity / 2);
		int lastVelocityIndex = (int)Math.abs(lastVelocity / 2);
		buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
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

class GFTUtils {
	static double bulletVelocity(double power) {
		return 20 - 3 * power;
	}
	
	static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}
	
	static double absoluteBearing(Point2D source, Point2D target) {
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}

	static int sign(double v) {
		return v < 0 ? -1 : 1;
	}
	
	static int minMax(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}
}
//#endif
//#if Random
//@class GFTMovement {
//@	private static final double BATTLE_FIELD_WIDTH = 800;
//@	private static final double BATTLE_FIELD_HEIGHT = 600;
//@	private static final double WALL_MARGIN = 18;
//@	private static final double MAX_TRIES = 125;
//@	private static final double REVERSE_TUNER = 0.421075;
//@	private static final double DEFAULT_EVASION = 1.2;
//@	private static final double WALL_BOUNCE_TUNER = 0.699484;
//@ 
//@	private AdvancedRobot robot;
//@	private Rectangle2D fieldRectangle = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN,
//@		BATTLE_FIELD_WIDTH - WALL_MARGIN * 2, BATTLE_FIELD_HEIGHT - WALL_MARGIN * 2);
//@	private double enemyFirePower = 3;
//@	private double direction = 0.4;
//@ 
//@	GFTMovement(AdvancedRobot _robot) {
//@		this.robot = _robot;
//@	}
//@ 
//@	public void onScannedRobot(ScannedRobotEvent e) {
//@		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
//@		double enemyDistance = e.getDistance();
//@		Point2D robotLocation = new Point2D.Double(robot.getX(), robot.getY());
//@		Point2D enemyLocation = GFTUtils.project(robotLocation, enemyAbsoluteBearing, enemyDistance);
//@		Point2D robotDestination;
//@		double tries = 0;
//@		while (!fieldRectangle.contains(robotDestination = GFTUtils.project(enemyLocation, enemyAbsoluteBearing + Math.PI + direction,
//@				enemyDistance * (DEFAULT_EVASION - tries / 100.0))) && tries < MAX_TRIES) {
//@			tries++;
//@		}
//@		if ((Math.random() < (GFTUtils.bulletVelocity(enemyFirePower) / REVERSE_TUNER) / enemyDistance ||
//@				tries > (enemyDistance / GFTUtils.bulletVelocity(enemyFirePower) / WALL_BOUNCE_TUNER))) {
//@			direction = -direction;
//@		}
//@		// Jamougha's cool way
//@		double angle = GFTUtils.absoluteBearing(robotLocation, robotDestination) - robot.getHeadingRadians();
//@		robot.setAhead(Math.cos(angle) * 100);
//@		robot.setTurnRightRadians(Math.tan(angle));
//@	}
//@}
//#endif
