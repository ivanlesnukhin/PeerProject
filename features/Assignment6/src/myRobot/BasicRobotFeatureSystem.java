package myRobot;
import robocode.*;

import java.awt.*;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class BasicRobotFeatureSystem extends AdvancedRobot {
	IMovement movementMeth;

	public IFiring firingMethod;
    private static double BULLET_POWER = 1.0;
	private static double lateralDirection;
	private static double lastEnemyVelocity;
	
	public boolean WaveSurfing = false, 
			Wall_Smoothing = false,
			visibleEnemyWaves = false,
			Random = false,
			GuessFactor = false,
			//IMPLEMENTATION OF LT AND CT
			LinearTargeting = false,
			CircularTargeting = false,
			DBL_Bullet_Power = false,
			BBY = false,
			RBY = false,
			GWY = false;
    

	public BasicRobotFeatureSystem() {

		// Reads properties from file and converts into booleans		
		ConfigurationManager cm = ConfigurationManager.getInstance();
		
		WaveSurfing = cm.getProperty("WaveSurfing", true);
		Wall_Smoothing = cm.getProperty ("Wall_Smoothing", true);
		visibleEnemyWaves = cm.getProperty ("visibleEnemyWaves", true);
		Random = cm.getProperty ("Random", true);
		GuessFactor = cm.getProperty ("GuessFactor", true);
		//IMPLEMENTATION OF LT AND CT
		LinearTargeting = cm.getProperty("LinearTargeting", true);
		CircularTargeting = cm.getProperty("CircularTargeting", true);
		DBL_Bullet_Power = cm.getProperty ("DBL_Bullet_Power", true);
		BBY = cm.getProperty ("BBY", true);
		RBY = cm.getProperty ("RBY", true);
		GWY = cm.getProperty ("GWY", true);
		
		//ClassLoader getClass.getclassLoader.getResourceAStream()

		// Sets color
		// selects movement method
		if(WaveSurfing) {
			movementMeth = new WaveSurfing(this);
			System.out.print("WaveSurfing2: " + WaveSurfing + "\n");
		} else if(Random) {
			System.out.print("Random2: " +Random + "\n");
			movementMeth = new RandomMovement(this);
		}

		lateralDirection = 1;
		lastEnemyVelocity = 0;

		// Sets up guessfactor targeting
		if (GuessFactor) {
			String movementMethod = "";
			if(WaveSurfing) {
				movementMethod = "WaveSurfing";
			} else if (Random) {
				movementMethod = "Random";
			}
			firingMethod = new GuessFactor(this, lastEnemyVelocity, lateralDirection, movementMethod);
			//IMPLEMENTATION OF LT AND CT
		} else if(LinearTargeting) {
			firingMethod = new LinearTargeting(this);
		} else if (CircularTargeting) {
			firingMethod = new CircularTargeting(this); 
		} 
		// Sets bullet power
		if (DBL_Bullet_Power)
			BULLET_POWER = 1.9;
	}
	
	// Changes a string to a boolean if 
	boolean stringToBoolean (String input) {
		if(input == "false") 
			return false;
		else if (input == "true")
			return true;
		else
			System.exit(0);
		return false;
	}

    public void run() {
		if (BBY)
			setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
		else if (RBY)
			setColors(Color.RED, Color.BLACK, Color.WHITE);
		else if (GWY)
			setColors(Color.GREEN, Color.WHITE, Color.YELLOW);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		do {
			// basic mini-radar code
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		} while (true);
	}

    public void onScannedRobot(ScannedRobotEvent e) {
		System.out.print("Random3: " + Random + "\n");
		System.out.print("WaveSurfing3: " + WaveSurfing + "\n");
    	System.out.print(WaveSurfing + "\n");
    	if(CircularTargeting || LinearTargeting || GuessFactor)
    	{
    		firingMethod.onScannedRobot(e);
    	}
    	if(WaveSurfing || Random)
    	{
    		movementMeth.onScannedRobot(e);
    	}    	
        
    }
}


