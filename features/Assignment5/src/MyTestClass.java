import robocode.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class MyTestClass {
	private AdvancedRobot robot = new AdvancedRobot();
	private IFiring firing = new LinearTargeting(robot);
	
 
	
	@Test
	void testSetTurnGunRightRadians() {
		double gunAngle = 2.4;
		double beforeAngle = robot.getHeadingRadians();
		
		firing.setTurnGunRightRadians(gunAngle);
		double currentAngle = robot.getHeadingRadians();
		
		assertNotSame(beforeAngle, currentAngle);
		assertSame(currentAngle, beforeAngle+gunAngle);
	}
	
	@Test
	void testSetTurnRadarRightRadians() {
		double radarAngle = 2.4;
		double beforeAngle = robot.getRadarHeadingRadians();
		
		firing.setTurnGunRightRadians(radarAngle);
		double currentAngle = robot.getRadarHeadingRadians();
		
		assertNotSame(beforeAngle, currentAngle);
		assertSame(currentAngle, beforeAngle+radarAngle);
	}
}