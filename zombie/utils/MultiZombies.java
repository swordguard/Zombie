package zombie.utils;

import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import sun.audio.*;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JTable;

import zombie.entities.Creature;
import zombie.entities.Zombie;

public class MultiZombies implements Runnable{
	//JTable table = null;
	Zombie zombie = null;
	ImageIcon zombieicon = null;
	ImageIcon overlappedIcon = null;
	ImageIcon zomtureIcon = null;
	ImageIcon tunnelingIcon = null;
	String path = null;
	String coveredpath = null;
	ActionControl ac = null;
	int lastX = -1;
	int lastY = -1;
	String iconfile="zombieIMG.jpg";
	String zomturefile = "zomture.jpg";
	String overlapped = "overlappedzombie.jpg";
	String tunneling = "tunnelingzombie.jpg";
	String eatingPlant = "eatingPlant.wav";
	boolean bite = false;

	public MultiZombies(ActionControl ac) {

		//this.table  = ac.getTable();

		this.ac = ac;
		//use the current zombie
		if(ac.aNewZombie != null){
			this.zombie = ac.aNewZombie;
			this.lastX = this.zombie.getX();
			this.lastY = this.zombie.getY();
			this.zombieicon = new ImageIcon(iconfile);
			this.overlappedIcon = new ImageIcon(overlapped);
			this.zomtureIcon = new ImageIcon(zomturefile);
			this.tunnelingIcon = new ImageIcon(tunneling);
		}else{
			System.out.println("Warning: New zombie in MultiZombies initialization failed.");
		}
		path = ac.getActionStandard();
		coveredpath = new String();
	}


	@Override
	public void run() {
		//System.out.println("begin running....");
		ac.increAliveThreads();
		//ActionControl.zombieList.add(zombie);

		int c = Integer.parseInt(MainZombieFrame.countlbl.getText());
		int d = ac.getAliveThreads();
		if(c < d ){
			MainZombieFrame.countlbl.setText(String.valueOf(d));
		}

		for(int i = 0; i < path.length(); i++){
			//record last position of current zombie
			this.lastX = this.zombie.getX();
			this.lastY = this.zombie.getY();
			//move down
			if(path.charAt(i) == 'D' || path.charAt(i) == 'd'){
				this.zombie.moveDown();

			}

			//move up
			if(path.charAt(i) == 'U' || path.charAt(i) == 'u'){
				this.zombie.moveUp();
			}

			//move left
			if(path.charAt(i) == 'L' || path.charAt(i) == 'l'){
				this.zombie.moveLeft();
			}

			//move right
			if(path.charAt(i) == 'R' || path.charAt(i) == 'r'){
				this.zombie.moveRight();
			}
			//System.out.println(lastY + " "+ zombie.getY());

			coveredpath += path.charAt(i);
			//if(coveredpath.length() == path.length())System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			this.checkBiteAndUpdateTable();
			//System.out.println("["+Thread.currentThread().getName() + "]: I'm moving from x=" + lastX
			//	+", y="+lastY+" to x="+zombie.getX()+", y="+zombie.getY());
			//moveInTable(this.lastX, this.lastY, this.zombie);
		}//end of for
		System.out.println("["+Thread.currentThread().getName() + "]: I reach the end.  " + zombie.toString());
		//once it reaches the end, set its alive status to false.   add to the reachEndZombieList

		ActionControl.reachEndZombieList.add(this.zombie);
		//System.out.println("["+Thread.currentThread().getName() + "]: coveredpath ========" +coveredpath);
		this.zombie.setAlive(false);
		//ac.decreAliveThreads();
		//all zombies ends, then draw the table
		int num = ActionControl.reachEndZombieList.size();
		if(d == num){
			Iterator<Zombie> it = ActionControl.reachEndZombieList.iterator();
			for(int i = 0;i<num;i++){
				MainZombieFrame.table.setValueAt(zombieicon, ActionControl.reachEndZombieList.get(i).getY(), ActionControl.reachEndZombieList.get(i).getX());
			}
		}
		//System.out.println(ac.getAliveThreads() +" thread are remaining.  " + ActionControl.reachEndZombieList.size());

	}



	/**
	 * see if a zombie bites something, if it bites, a new zombie thread will start
	 */
	private void checkBiteAndUpdateTable(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//check the creature list
		synchronized(ActionControl.creatureList){
			//1. if meet with a creature, bite one creature
			//if(ActionControl.creatureList.size() > 0){
			if(MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()) != null
					&& MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()).toString().contains("creature")){
				Iterator<Creature> creListIte =  ActionControl.creatureList.iterator();
				while(creListIte.hasNext()){
					Creature creature = (Creature) creListIte.next();
					//synchronized(MainZombieFrame.table){
					//meeting with a creature
					if(this.zombie.getX() == creature.getX() && this.zombie.getY() == creature.getY()){
						//bite = true;
						ac.aNewZombie = (Zombie) zombie.bite();
						//System.out.println("A new zombie is born: " + "(" + ac.aNewZombie.getInitX()+ ", "+ ac.aNewZombie.getInitY() + ")");
						//ActionControl.zombieList.add(ac.aNewZombie);
						creListIte.remove();
						//ac.increAliveThreads();
						//if(lastX != -1 && lastY != -1 && MainZombieFrame.table != null){
						//synchronized(MainZombieFrame.table){
						MainZombieFrame.table.setValueAt(zomtureIcon, zombie.getY(), zombie.getX());
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						SoundsUtils.playSound(eatingPlant);
						MainZombieFrame.table.setValueAt(zombieicon, zombie.getY(), zombie.getX());
						//System.out.println("["+Thread.currentThread().getName() + "]: " + zombie.toString());
						MultiZombies mz = new MultiZombies(this.ac);
						Thread t = new Thread(mz);
						t.start();

						break;
					}
				}//end while
			}//end if
			//2. meeting with zombie
			else
				if(MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()) != null
				&& MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()).toString().contains("zombie")){
					//System.out.println("["+Thread.currentThread().getName() + "]: covered path = " + coveredpath);
					//both zombie are dead, set the overlapped icon, otherwise do nothing
					if(coveredpath== path || coveredpath.equals(path)){
						//System.out.println("coveredpath ========================================== " + coveredpath);
						if(checkThisZombieIsDead(zombie.getX(), zombie.getY())){
							//System.out.println("["+Thread.currentThread().getName() + "]: "+path +" " + coveredpath);
							MainZombieFrame.table.setValueAt(overlappedIcon, zombie.getY(), zombie.getX());
							System.out.println("["+Thread.currentThread().getName() + "]: ("+ zombie.getX()+","
									+ zombie.getY()+") "+"======== at least two dead zombies are here ========");
						}
					}//else System.out.println("coveredpath = " + coveredpath);
				}else
					//3. if meet with nothing
				{
					//no bite, no zombie, set value in new cell
					MainZombieFrame.table.setValueAt(zombieicon, zombie.getY(), zombie.getX());
				}	

		}//end synchronized
		clearLastCell(lastX, lastY);
		/*synchronized(MainZombieFrame.table){
			//2. if meet with another zombie
			if(MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()) != null
				&&	MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()).toString().contains("zombie")){
				//System.out.println("["+Thread.currentThread().getName() + "]: covered path = " + coveredpath);
				//both zombie are dead, set the overlapped icon, otherwise do nothing
				if(coveredpath== path || coveredpath.equals(path)){
					//System.out.println("coveredpath ========================================== " + coveredpath);
					if(checkThisZombieIsDead(zombie.getX(), zombie.getY())){
						//System.out.println("["+Thread.currentThread().getName() + "]: "+path +" " + coveredpath);
						MainZombieFrame.table.setValueAt(overlappedIcon, zombie.getY(), zombie.getX());
						System.out.println("["+Thread.currentThread().getName() + "]: ("+ zombie.getX()+","
						+ zombie.getY()+") "+"======== at least two dead zombies are here ========");
					}
				}//else System.out.println("coveredpath = " + coveredpath);
			}else
			//3. if meet with nothing
			{
				//no bite, no zombie, set value in new cell
				MainZombieFrame.table.setValueAt(zombieicon, zombie.getY(), zombie.getX());
			}	
		}*/

		//clearLastCell(lastX, lastY);
	}



	/**
	 * check in the ActionControl.reachEndZombieList to see if this zombie I'm meeting is dead or not
	 * @return
	 */
	public boolean checkThisZombieIsDead(int x, int y){

		synchronized(ActionControl.reachEndZombieList){
			Iterator<Zombie> it = ActionControl.reachEndZombieList.iterator();

			while(it.hasNext()){
				Zombie z = it.next();
				if(x == z.getX() && y == z.getY()){
					//System.out.println("found dead zombie int ("+x+","+y+")");
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * do what a zombie should do when moving one cell forward
	 * 1. meet a creature
	 * 2. meet a dead zombie
	 * 3. meet nothing
	 * @param lastX
	 * @param lastY
	 */
	private void checkNewCell(){
		if(ActionControl.creatureList.size() > 0){
			Iterator<Creature> creListIte =  ActionControl.creatureList.iterator();
			while(creListIte.hasNext()){
				Creature creature = (Creature) creListIte.next();
				//synchronized(MainZombieFrame.table){
				//1. if meet with a creature, bite one creature
				if(this.zombie.getX() == creature.getX() && this.zombie.getY() == creature.getY()){
					//bite = true;
					ac.aNewZombie = (Zombie) zombie.bite();
					//System.out.println("A new zombie is born: " + "(" + ac.aNewZombie.getInitX()+ ", "+ ac.aNewZombie.getInitY() + ")");
					ActionControl.zombieList.add(ac.aNewZombie);
					creListIte.remove();
					ac.increAliveThreads();
					//if(lastX != -1 && lastY != -1 && MainZombieFrame.table != null){
					//synchronized(MainZombieFrame.table){
					MainZombieFrame.table.setValueAt(zomtureIcon, zombie.getY(), zombie.getX());
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SoundsUtils.playSound(eatingPlant);
					MainZombieFrame.table.setValueAt(zombieicon, zombie.getY(), zombie.getX());
					//System.out.println("["+Thread.currentThread().getName() + "]: " + zombie.toString());
					MultiZombies mz = new MultiZombies(this.ac);
					Thread t = new Thread(mz);
					t.start();

					break;
				}else
					//2. if meet with another zombie
					if(MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()) != null
					&& MainZombieFrame.table.getValueAt(zombie.getY(), zombie.getX()).toString().contains("zombie")){
						//System.out.println("["+Thread.currentThread().getName() + "]: covered path = " + coveredpath);
						//both zombie are dead, set the overlapped icon, otherwise do nothing
						if(coveredpath== path || coveredpath.equals(path)){
							System.out.println("coveredpath ======= " + coveredpath);
							if(checkThisZombieIsDead(zombie.getX(), zombie.getY())){
								//System.out.println("["+Thread.currentThread().getName() + "]: "+path +" " + coveredpath);
								MainZombieFrame.table.setValueAt(overlappedIcon, zombie.getY(), zombie.getX());
								System.out.println("["+Thread.currentThread().getName() + "]: ("+ zombie.getX()+","
										+ zombie.getY()+") "+"======== at least two zombies are dead ========");
							}
						}//else System.out.println("coveredpath = " + coveredpath);
					}else
						//3. if meet with nothing
					{
						//no bite, no zombie, set value in new cell
						MainZombieFrame.table.setValueAt(zombieicon, zombie.getY(), zombie.getX());
					}	


			}
		}

	}



	/**
	 * do clear job to last cell when a zombie left last position
	 * 1. if a zombie in last cell is alive, it might clear the cell
	 * 1.1 facing wall, do nothing, otherwise clear last cell
	 * 2. if a zombie in last cell is not alive, do nothing to it, just move on
	 * @param lastX
	 * @param lastY
	 */
	private void clearLastCell(int lastX, int lastY){
		//End: clear its tail cell, set last cell null, depending conditions
		if(!checkThisZombieIsDead(lastX, lastY)){
			//deal with the situation that zombie reaches the wall
			//if(!zombie.isTunnel()){
			if(coveredpath.charAt(coveredpath.length()-1) == 'd' || coveredpath.charAt(coveredpath.length()-1) == 'D'){
				//facing the bottom wall
				if(lastY == zombie.getN() - 1){
					System.out.println("["+Thread.currentThread().getName() + "]: facing the bottom wall at "+zombie.getX());
					if(zombie.isTunnel())MainZombieFrame.table.setValueAt(null, lastY, lastX);
					//MainZombieFrame.table.setValueAt(tunnelingIcon, zombie.getY(), zombie.getX());
				}else{
					MainZombieFrame.table.setValueAt(null, lastY, lastX);
				}
			}else
				//facing the top wall
				if(coveredpath.charAt(coveredpath.length()-1) == 'u' || coveredpath.charAt(coveredpath.length()-1) == 'U'){
					if(lastY == 0){
						//System.out.println("["+Thread.currentThread().getName() + "]: facing the top wall at "+zombie.getX());
						if(zombie.isTunnel())MainZombieFrame.table.setValueAt(null, lastY, lastX);
					}else{
						MainZombieFrame.table.setValueAt(null, lastY, lastX);
					}
				}else
					//facing the right wall
					if(coveredpath.charAt(coveredpath.length()-1) == 'r' || coveredpath.charAt(coveredpath.length()-1) == 'R'){
						if(lastX == zombie.getN() - 1){
							//System.out.println("["+Thread.currentThread().getName() + "]: facing the right wall at "+zombie.getY());
							if(zombie.isTunnel())MainZombieFrame.table.setValueAt(null, lastY, lastX);
						}else{
							MainZombieFrame.table.setValueAt(null, lastY, lastX);
						}
					}else
						//facing the left wall
						if(coveredpath.charAt(coveredpath.length()-1) == 'l' || coveredpath.charAt(coveredpath.length()-1) == 'L'){
							if(lastX == 0){
								//System.out.println("["+Thread.currentThread().getName() + "]: facing the left wall at "+zombie.getY());
								if(zombie.isTunnel())MainZombieFrame.table.setValueAt(null, lastY, lastX);
							}else{
								MainZombieFrame.table.setValueAt(null, lastY, lastX);
							}
						}
			//MainZombieFrame.table.setValueAt(null, zombie.getY(), zombie.getX());
		}
	}

}
