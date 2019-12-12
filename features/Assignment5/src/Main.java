import java.io.InputStream;
import java.util.Scanner;

import properties.PropertyManager; 

public class Main {
	public static void main(String[] args) {
		InputStream is = ClassLoader.getSystemResourceAsStream("runtime.properties");
		Scanner sc = new Scanner(is);
		ConfigurationManager cm = ConfigurationManager.getInstance();
		cm.getProperty("WaveSurfing",false);
		cm.getProperty("Random",false);
		cm.checkPropertyKindActive("GF");
		
		while(sc.hasNext()) {
			if (sc.next() == "Random=true") {
				System.out.println("Hello");
			}
		}
		/*if(Random) {
			System.out.println("Hello");
		}*/
		System.out.println("Not Hello");
		
	}

}
