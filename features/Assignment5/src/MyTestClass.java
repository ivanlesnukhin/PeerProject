import robocode.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class MyTestClass {
	
	//Perform test for Linear Targeting (LT) feature
	private AdvancedRobot robotLT = new AdvancedRobot();
	private IFiring firing = new LinearTargeting(robotLT);
	
	/**
	 * Tests whether or not the robot changes its gun angle
	 */
	@Test
	void testSetTurnGunRightRadians() {
		double oldAngle = robotLT.getHeadingRadians();
		
		double deltaGunAngle = 2.4;
		firing.setTurnGunRightRadians(deltaGunAngle);
		double newAngle = robotLT.getHeadingRadians();
		
		assertNotSame(oldAngle, newAngle);
		assertSame(newAngle, oldAngle + deltaGunAngle);
	}
	
	/**
	 * Tests whether or not the robot changes its radar angle
	 */
	@Test
	void testSetTurnRadarRightRadians() {
		
		double oldAngle = robotLT.getRadarHeadingRadians();
		
		double deltaRadarAngle = 2.4;
		firing.setTurnRadarRightRadians(deltaRadarAngle);
		double newAngle = robotLT.getRadarHeadingRadians();
		
		assertNotSame(oldAngle, newAngle);
		assertSame(newAngle, oldAngle + deltaRadarAngle);
	}
	
	//
}