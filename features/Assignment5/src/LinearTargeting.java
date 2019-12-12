import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Properties; 

import java.awt.geom.Point2D;

import robocode.ScannedRobotEvent;


public class LinearTargeting implements IFiring {

	private AdvancedRobot robot;
	
	LinearTargeting(AdvancedRobot m_robot){
		robot = m_robot;
	}
	
	@Override
	public void setFire(double BulletPower) {
		robot.setFire(BulletPower);
	}
	@Override
	public void setTurnGunRightRadians(double angle) {
		robot.setTurnGunRightRadians(angle);
	}
	@Override
	public void setTurnRadarRightRadians(double angle) {
		robot.setTurnRadarRightRadians(angle);
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		setFire(Math.min(3.0,robot.getEnergy()));
		double bulletPower = Math.min(3.0,robot.getEnergy());
		double myX = robot.getX();
		double myY = robot.getY();
		double absoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyX = robot.getX() + e.getDistance() * Math.sin(absoluteBearing);
		double enemyY = robot.getY() + e.getDistance() * Math.cos(absoluteBearing);
		double enemyHeading = e.getHeadingRadians();
		double enemyVelocity = e.getVelocity();
		 
		 
		double deltaTime = 0;
		double battleFieldHeight = robot.getBattleFieldHeight(), 
		       battleFieldWidth = robot.getBattleFieldWidth();
		double predictedX = enemyX, predictedY = enemyY;
		while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
		      Point2D.Double.distance(myX, myY, predictedX, predictedY)){		
			predictedX += Math.sin(enemyHeading) * enemyVelocity;	
			predictedY += Math.cos(enemyHeading) * enemyVelocity;
			if(	predictedX < 18.0 
				|| predictedY < 18.0
				|| predictedX > battleFieldWidth - 18.0
				|| predictedY > battleFieldHeight - 18.0){
				predictedX = Math.min(Math.max(18.0, predictedX), 
		                    battleFieldWidth - 18.0);	
				predictedY = Math.min(Math.max(18.0, predictedY), 
		                    battleFieldHeight - 18.0);
				break;
			}
		}
		double theta = Utils.normalAbsoluteAngle(Math.atan2(
		    predictedX - robot.getX(), predictedY - robot.getY()));
		    
		    
		setTurnGunRightRadians(Utils.normalRelativeAngle(theta - robot.getGunHeadingRadians()));
		 
		setTurnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearing - robot.getRadarHeadingRadians()));
		
		robot.fire(bulletPower);
	}
}