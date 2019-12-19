import robocode.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
//import static org.junit.jupiter.api.Assertions.*;
import java.awt.geom.Point2D;
//import org.junit.jupiter.api.Test;


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
		
	
		assertNotNull(robotLT); //-does the robot exist or not? This is a question!.. 
		assertNotSame(oldAngle, newAngle);
		assertSame(newAngle, oldAngle + deltaRadarAngle);
	}
	
	
	// RandomMovement Tests
	
	private AdvancedRobot robotRM = new AdvancedRobot();
	private AdvancedRobot enemyRM = new AdvancedRobot();
	
	private IMovement movement = new RandomMovement(robotRM);
	
	@Test
	void testOnScannedRobot() {
		Point2D oldRobotLocation = new Point2D.Double(robotRM.getX(), robotRM.getY());
		
		assertSame(oldRobotLocation, new Point2D.Double(robotRM.getX(), robotRM.getY()));
		
	}
	
}