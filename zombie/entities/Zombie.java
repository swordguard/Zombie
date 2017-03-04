package zombie.entities;

import zombie.utils.IZombie;

/**
 * 
 * @author Jay Sun
 * a zombie has three attributes: 
 * 1. position, expressed by x and y coordinate,
 * 2. tunnel through the wall or not, expressed by tunnel
 * 3. and width of square area, expressed by N
 * a zombie has four methods: moveUp, moveDown, moveLeft, moveRight
 */
public class Zombie implements IZombie{
	int initX;
	int initY;
	int x;
	int y;
	int N;
	boolean tunnel;
	boolean alive;
	
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	
	
	public Zombie(int x, int y, int n, boolean tunnel, boolean alive) {
		super();
		this.initX = x;
		this.initY = y;
		this.x = x;
		this.y = y;
		N = n;
		this.tunnel = tunnel;
		this.alive = alive;
	}
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	
	
	public int getInitX() {
		return initX;
	}
	public void setInitX(int initX) {
		this.initX = initX;
	}
	public int getInitY() {
		return initY;
	}
	public void setInitY(int initY) {
		this.initY = initY;
	}
	public Zombie() {
		// TODO Auto-generated constructor stub
	}
	public void moveUp(){
		if(y <= 0){
			//tunnel to the bottome
			if(tunnel){
				this.y = N - 1;
			}else{
				this.y = 0;
			}
		}else{
			this.y = y -1;
		}
	}
	public void moveDown(){
		if(y >= N - 1){
			//tunnel to the top
			if(tunnel){
				this.y = 0;
			}
		}else{
			this.y = y + 1;
		}
	}
	public void moveLeft(){
		if(x <= 0){
			//tunnel to the rightmost
			if(tunnel){
				this.x = N -1;
			}else{
				this.x = 0;
			}
		}else{
			this.x = x -1;
		}
	}
	public void moveRight(){
		if(x >= N - 1){
			//tunnel to the rightmost
			if(tunnel){
				this.x = 0;
			}
		}else{
			this.x = x + 1;
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isTunnel() {
		return tunnel;
	}
	public void setTunnel(boolean tunnel) {
		this.tunnel = tunnel;
	}
	@Override
	public String toString() {
		return "Zombie [initX=" + initX + ", initY=" + initY + ", x=" + x
				+ ", y=" + y + ", N=" + N + ", tunnel=" + tunnel + "]";
	}
	@Override
	public IZombie bite() {
		// TODO Auto-generated method stub
		return new Zombie(this.getX(), this.getY(), this.getN(), this.isTunnel(), true);
	}
	
}
