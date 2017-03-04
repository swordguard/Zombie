package zombie.utils;
/**
 * 25/04/2015
 * @author Jay Sun
 * boolean type variable tunnel indicates whether zombie can tunnel through the wall
 * line1 means, the first line of a text file, N, the width of the square stage
 * line2 means the initial coordinate of a zombie, x and y
 * line3 means the initial coordinates of creatures, x and ys
 * line4 means the order that tells a zombie how to move on the stage
 * creatureList list, initialized by ActionStart through SetInitialization.txt file, decreases by one every time a zombie moves to a creature¡¯s position and bites it;
 * zombieList list, initialized by ActionStart through SetInitialization.txt file, increases by one every time a zombie moves to a creature¡¯s position and bites it;
 * ongoingZombies list, initialized by ActionStart through SetInitialization.txt file, increases by one every time a zombie moves to a creature¡¯s position and bites it, decreases by one if a zombie finishes its preset path.
 */

import java.io.*;
import java.util.*;

import javax.swing.JTable;

//import swing.MainZombieFrame;
import zombie.entities.Creature;
import zombie.entities.Zombie;

public class ActionControl{
	
	
	boolean tunnel = false;

	//String action = null;
	String actionStandard = "DULRdulr";
	String line1 = null;
	String line2 = null;
	String line3 = null;
	String line4 = null;
	private JTable table = null;
	//Zombie zombie = null;
	Zombie aNewZombie = null;
	static List<Zombie> zombieList = new ArrayList<Zombie>();
	static List<Zombie> reachEndZombieList = Collections.synchronizedList(new ArrayList<Zombie>());
	static List<Creature> creatureList = Collections.synchronizedList(new ArrayList<Creature>());
	//a counter for living zombies, if set back to 0, game is over
	static int aliveThreads =0;
	static boolean onceEnd = false;
	
	public boolean getOnceEnd(){
		return this.onceEnd;
	}
	
	public void setOnceEnd(boolean b){
		onceEnd = b;
	}
	
	public static int getAliveThreads() {
		return aliveThreads;
	}


	public  synchronized void increAliveThreads() {
		ActionControl.aliveThreads++;
	}

	
	public  synchronized void decreAliveThreads() {
		ActionControl.aliveThreads--;
	}

	public synchronized JTable getTable() {
		return table;
	}


	public void setTable(JTable table) {
		this.table = table;
	}


	public static List<Zombie> getZombieList() {
		return zombieList;
	}


	public static void setZombieList(List<Zombie> zombieList) {
		ActionControl.zombieList = zombieList;
	}


	public static List<Creature> getCreatureList() {
		return creatureList;
	}


	public static void setCreatureList(List<Creature> creatureList) {
		ActionControl.creatureList = creatureList;
	}


	public ActionControl(String line1, String line2, String line3, String line4) {
		super();
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.line4 = line4;
	}
	

	public String getLine1() {
		return line1;
	}


	public void setLine1(String line1) {
		this.line1 = line1;
	}


	public Zombie getZombie() {
		return aNewZombie;
	}




	public void setZombie(Zombie zombie) {
		this.aNewZombie = zombie;
	}




	public String getActionStandard() {
		return actionStandard;
	}



	public void setActionStandard(String actionStandard) {
		this.actionStandard = actionStandard;
	}



	public boolean isTunnel() {
		return tunnel;
	}



	public void setTunnel(boolean tunnel) {
		this.tunnel = tunnel;
	}


	public ActionControl(boolean tunnel){
		super();
		this.tunnel = tunnel;
	}
	
	/**
	 * retrieve from text file
	 * @param file
	 */
	public void readFile(String file){
		BufferedReader br = null;
		String line = null;
		File fin = null;
		File dir = new File(".");
		try {
			fin = new File(dir.getCanonicalPath() + File.separator + file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*System.out.println("Example format of input file is as follows, please follow it.");
		System.out.println("4");
		System.out.println("2 1");
		System.out.println("0 1,2 1,3 1");
		System.out.println("ULDDRR");
*/
		//check each line of text file, ensure they are in correct form as show in examples
		try {
			br = new BufferedReader(new FileReader(fin));
			System.out.println("****************************** Begining of your file **************************");
			//check first line
			if((line = br.readLine().trim()) != null){
				System.out.println(line);
				if(!isNumeric(line)){
					System.out.println("Something in the first line of " + file 
							+ " must be a positive number, greater than 4(included), but don't be too large!");
					System.exit(1);
				}
				if(line == "" || line.equals("") || line == null){
					System.out.println("The first line of " + file + " is missing, pleas provide with the right format.");
					System.exit(1);
					return;
				}
				if((long)Integer.parseInt(line) > Integer.MAX_VALUE){
					System.out.println("The number in the first line in of" + file 
							+ " must be  between 4 and 9(4 and 9 are included)!");
					System.exit(1);
					return;
				}
				if(Integer.parseInt(line) < 4){
					System.out.println("The number in the first line in  of" + file 
							+ " must be greater than 4(included)!");
					System.exit(1);
					return;
				}
				line1 = line;
				//N = Integer.parseInt(line1);
			}

			//check second line
			if((line = br.readLine().trim()) != null){
				System.out.println(line);
				if(line == "" || line.equals("") || line == null){
					System.out.println("The second line of " + file + " is missing, pleas provide with the right format.");
					System.exit(1);
					return;
				}
				if(isPosition(line, Integer.parseInt(line1))){
					line2 = line;
				}else{
					System.exit(1);
				}

			}

			//check third line
			if((line = br.readLine().trim()) != null){
				if(line == "" || line.equals("") || line == null){
					System.out.println("The third line of " + file + " is missing, pleas provide with the right format.");
					System.exit(1);
					return;
				}
				System.out.println(line);

				if(line.endsWith(",")){
					System.out.println("Something goes wrong with the coordinates format in the third line, "
							+ "please check it and try it again!");
					System.exit(1);
				}
				String[] tmp2 = line.split(",");

				for(int i = 0;i<tmp2.length;i++){
					if(tmp2[i].length() < 3){
						System.out.println("Something goes wrong with the coordinates format in the third line, "
								+ "please check it and try it again!");
						System.exit(1);
						return;
					}
				}
				for(int i = 0;i<tmp2.length;i++){
					if(!isThirdLineOK(tmp2[i], Integer.parseInt(line1))){
						System.exit(1);
						return;
					}
				}
				line3 = line;
			}

			//check the fourth line
			if((line = br.readLine().trim()) != null){
				if(line == "" || line.equals("") || line == null){
					System.out.println("The fourth line of " + file + " is missing, pleas provide with the right format.");
					System.exit(1);
					return;
				}
				System.out.println(line);
				System.out.println("****************************** End of first four lines of the file **************************");
				for(int j = 0;j<line.trim().length();j++){
					if(actionStandard.indexOf(line.charAt(j)) == -1){
						System.out.println("The fourth line must be any one of 'U,D,L,R,u,d,l,r' or any combinations of them"
								+ ", with no blank space in between!");
						System.exit(1);
						return;
					}
				}
				line4 = line;
				this.setActionStandard(line4);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * do initialization
	 */
	public void initialize(){
		int xposition = -1;
		int yposition = -1;
		//seperate line2 with one or more blank space
		String tmp[] = line2.split("\\ +");
		xposition = Integer.parseInt(tmp[0]);
		yposition = Integer.parseInt(tmp[1]);

		
		//ongoingZombies = new Vector<>();
		//ongoingZombies = new ConcurrentHashMap<String, Zombie>();
		
		if(xposition != -1 && yposition != -1){
			aNewZombie = new Zombie(xposition, yposition, Integer.parseInt(line1), tunnel, true);
			this.setZombie(aNewZombie);
			//add to zombie list
			zombieList.add(aNewZombie);
			//ongoingZombies.put(String.valueOf(xposition) + String.valueOf(yposition), zombie);
			//ongoingZombies.add(aNewZombie);
		}
		

		String tmp2[] = line3.split(",");
		//creatureList = new ArrayList<Creature>();
		//List<Creature> creatureList1 = new Vector<Creature>();
		
		//add to creature list
		for(int i = 0;i < tmp2.length;i++){
			String tmp21[] = tmp2[i].split("\\ +");
			creatureList.add(new Creature(Integer.parseInt(tmp21[0]),Integer.parseInt(tmp21[1])));

		}
		
		
	}
	
	
	/**
	 * do single thread job
	 */
	/*public void action(){
		//Zombie zombie = this.getZombie();
		do{
			
			for(int i = 0; i < line4.length(); i++){
				//System.out.println(zombie);
				//move down
				if(line4.charAt(i) == 'D' || line4.charAt(i) == 'd'){
					
					zombie.moveDown();
					if(creatureList.size() > 0){
						//zombieBite(zombie, zombieList, ongoingZombies, creatureList);
						zombieBite(zombie);
					}
				}

				//move up
				if(line4.charAt(i) == 'U' || line4.charAt(i) == 'u'){
					zombie.moveUp();
					if(creatureList.size() > 0)zombieBite(zombie);
						//zombieBite(zombie, zombieList, ongoingZombies, creatureList);
				}

				//move left
				if(line4.charAt(i) == 'L' || line4.charAt(i) == 'l'){
					zombie.moveLeft();
					if(creatureList.size() > 0)zombieBite(zombie);
					//zombieBite(zombie, zombieList, ongoingZombies, creatureList);
				}

				//move right
				if(line4.charAt(i) == 'R' || line4.charAt(i) == 'r'){
					zombie.moveRight();
					if(creatureList.size() > 0)zombieBite(zombie);
					//zombieBite(zombie, zombieList, ongoingZombies, creatureList);
				}

				
			}//end of for
			//the zombie finishes all the path, remove it from the ongoing list according to key to this zombie
			ongoingZombies.remove(zombie);

			//as long as the ongoing zombie list has some zombie in, do it
			if(ongoingZombies.size() > 1)
				zombie = ongoingZombies.get(ongoingZombies.size() - 1);
			else if(ongoingZombies.size() == 1)
				zombie = ongoingZombies.get(0);
			else
				break;
		}while(!ongoingZombies.isEmpty());
		System.out.println("zombies score: " + (zombieList.size() - 1));
		System.out.print("zombies positions: " );
		//print all zombies
		Iterator<Zombie> zombieite = zombieList.iterator();
		while(zombieite.hasNext()){
			Zombie z = zombieite.next();
			System.out.print(z.getX() + " " + z.getY());
			if(zombieite.hasNext())System.out.print(",");
		}
	}
	
	*/
	
	
	
	/**
	 * do multi-threads job, many zombies are moving simultaneously
	 */
	public void beginMulActioin(JTable table){
		//new MultiZombies(this.getZombie(), ongoingZombies, this.creatureList, this.getActionStandard());
		this.setTable(table);
		MultiZombies mz = new MultiZombies(this);
		Thread t = new Thread(mz);
		t.start();
	}
	
	
	/**
	 * check if a string is a number
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str){
		boolean isNum = true;
		for(int i = 0;i<str.length();i++){
			if(!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return isNum;
	}

	/**
	 * check the second line to ensure its correct format as example
	 * @param line
	 * @param N
	 * @return
	 */
	private boolean isPosition(String line, int N){
		boolean isPositionOK = true;


		String[] tmp = line.split("\\ +");

		if(tmp.length != 2){
			System.out.println("Make sure the above line must be a correct coordinate: "
					+ "two positive numbers with a single blank space in between!");
			return false;
		}

		if(!isNumeric(tmp[0]) || !isNumeric(tmp[1])){
			System.out.println("Make sure the above line must be a correct coordinate: "
					+ "two positive numbers with a single blank space in between!");
			return false;
		}
		if((long)Integer.parseInt(tmp[0].trim()) >= N || (long)Integer.parseInt(tmp[0].trim()) < 0){
			System.out.println("The x coordinate set in the above line must be 0 and "
					+ (N-1) + "(0 and "+ (N-1) + " are included)!");
			return false;
		}
		if((long)Integer.parseInt(tmp[1].trim()) >= N || (long)Integer.parseInt(tmp[1].trim()) < 0){
			System.out.println("They coordinate set in the above line must be 0 and"
					+ (N-1) + "(0 and "+ (N-1) + " are included)!");
			return false;
		}

		return isPositionOK;
	}

	/**
	 * check the third line to ensure its correct format as example
	 * @param line
	 * @param N
	 * @return
	 */
	private boolean isThirdLineOK(String line, int N){
		boolean isPositionOK = true;
		String[] tmp = line.split("\\ +");

		if(tmp.length != 2){
			System.out.println("Make sure the third line consists of one or more correct coordinates, "
					+ "each of which is connected by a comma!");
			return false;
		}

		if(!isNumeric(tmp[0]) || !isNumeric(tmp[1])){
			System.out.println("Make sure the third line consists of one or more correct coordinates, "
					+ "each of which is connected by a comma!");
			return false;
		}
		if((long)Integer.parseInt(tmp[0].trim()) >= N || (long)Integer.parseInt(tmp[0].trim()) < 0){
			System.out.println("The x coordinate set in the above line must be 0 and "
					+ (N-1) + "(0 and "+ (N-1) + " are included)!");
			return false;
		}
		if((long)Integer.parseInt(tmp[1].trim()) >= N || (long)Integer.parseInt(tmp[1].trim()) < 0){
			System.out.println("The y coordinate set in the above line must be 0 and "
					+ (N-1) + "(0 and "+ (N-1) + " are included)!");
			return false;
		}

		return isPositionOK;
	}

	/**
	 * check if one of the creatures is bitten, if bitten, a new zombie will born,and a creature will disappear
	 * @param zombie
	 * @param zombieList
	 * @param ongoingzombieList
	 * @param creatureList
	 */
	
}
