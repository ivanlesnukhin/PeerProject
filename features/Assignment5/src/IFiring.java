import robocode.*;

public interface IFiring {
	
	public void setTurnGunRightRadians(double angle);
	
	public void setFire(double BulletPower);

	public void onScannedRobot(ScannedRobotEvent e);
	
	public void setTurnRadarRightRadians(double angle);

}