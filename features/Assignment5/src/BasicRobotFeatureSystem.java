import robocode.*;

import java.awt.*;
import java.io.FileReader;
import java.util.Properties;

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
			GF = false,
			DBL_Bullet_Power = false,
			BBY = false,
			RBY = false,
			GWY = false;
    

	public BasicRobotFeatureSystem() {

		//////--------    Reads properties from file and converts into booleans
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
		/////-------------


		/////------------- Sets color
		if (BBY)
			setColors(Color.BLUE, Color.BLACK, Color.YELLOW);
		else if (RBY)
			setColors(Color.RED, Color.BLACK, Color.WHITE);
		else if (GWY)
			setColors(Color.GREEN, Color.WHITE, Color.YELLOW);
		/////-------------


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

		if (DBL_Bullet_Power)
			BULLET_POWER = 1.9;
	}
	
	
	boolean stringToBoolean (String input) {
		if(input == "false") 
			return false;
		else 
			return true;
	}

    public void run() {

    }

    public void onScannedRobot(ScannedRobotEvent e) {
    	movementMeth.onScannedRobot(e);
        firingMethod.onScannedRobot(e);
    }
}


