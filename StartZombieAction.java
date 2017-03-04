import zombie.utils.ActionControl;
import zombie.utils.MainZombieFrame;


public class StartZombieAction {
	
	public static void main(String[] args) {

		boolean tunnel = false;
		final String configfile = "SetInitialization.txt";
		
		if(args.length > 0){
			if(args[0] == "true" || args[0].equals("true"))tunnel = true;
		}
		
		ActionControl ac = new ActionControl(tunnel);
		ac.readFile(configfile);
		ac.initialize();
		
		new MainZombieFrame(ac);
		
	}


}
