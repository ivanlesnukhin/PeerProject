import robocode.*;

import java.awt.*;
import java.awt.geom.Point2D;
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
	public IFiring firingMethod;
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

		if (GF) {
			String movementMethod = "";
			if(WaveSurfing) {
				movementMethod = "WaveSurfing";
			} else if (Random) {
				movementMethod = "Random";
			}
			firingMethod = new GuessFactor(this, lastEnemyVelocity, lateralDirection, movementMethod);
		}
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
        firingMethod.onScannedRobot(e);
    }
}


