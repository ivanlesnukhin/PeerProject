import java.io.InputStream;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		InputStream is = ClassLoader.getSystemResourceAsStream("runtime.properties");
		Scanner sc = new Scanner(is);
		ConfigurationManager cm = ConfigurationManager.getInstance();
		/*cm.getProperty("WaveSurfing",true);
		cm.getProperty("Random",true);
		cm.checkPropertyKindActive("GF");*/
		
		BasicRobotFeatureSystem brfs = new BasicRobotFeatureSystem();
		brfs.run();
		
		/*while(sc.hasNext()) {
			String tmpString = sc.next().split("=")[0];
			boolean tmpBool = cm.getProperty(tmpString, false);
			System.out.println(tmpString + " is " + tmpBool + "\n");
		}*/
		/*if(Random) {
			System.out.println("Hello");
		}*/
		System.out.println("Not Hello");
		
	}

}
