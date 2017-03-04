package zombie.utils;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import zombie.entities.Creature;
import zombie.utils.ActionControl;
import zombie.utils.ImageRenderer;
import zombie.utils.MyTableModel;

public class MainZombieFrame extends JFrame{
	// set the size of the frame
	private int frameWidth = 690;
	private int frameHeight = 750;
	private ActionControl ac = null;
	static String zombiefile = "zombieIMG.jpg";
	static String creaturefile = "creatureIMG.jpg";
	String bgdMusic  = "198_Mountains.wav";
	//final static String configfile = "SetInitialization.txt";
	//static JLabel imgIcon2DArr[][] = null;
	static ImageIcon imgIcon2DArr[][] = null;
	static JTable table = new JTable();
	final static JButton btn_start = new JButton("Start");
	static JLabel countlbl = new JLabel();
	//final static JButton btn_reset = new JButton("Reset");
	//private List<List<JLabel>> labelMatrix = null;

	public MainZombieFrame(ActionControl ac){
		//rowVector = new Vector<Vector<Object>>();
		this.ac = ac;
		initFrame();
	}
	
	
	
	
	
	private void initFrame(){
		
		this.setTitle("Welcome to Zombie World!");
		this.setSize(frameWidth, frameHeight);
		JPanel panel = new JPanel();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        
        int locationX = 0, locationY=0;
        locationX = (screenSize.width - frameSize.width) / 2 ;
        locationY = (screenSize.height - frameSize.height) / 2;
        
        setLocation(locationX, locationY);
     
		
		
		btn_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_start.setEnabled(false);
				//btn_reset.setEnabled(false);
				//System.out.println("starting");
				ac.beginMulActioin(table);
				//check thread's count
				//SoundsUtils.playSound(bgdMusic);
			}
		});
	/*	
		btn_reset.setEnabled(false);
		btn_reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_reset.setEnabled(false);
				btn_start.setEnabled(true);
				//initTableInFrame(frameWidth, frameHeight );
				ac.readFile(configfile);
				ac.initialize();
				initFrame();
			}
		});
		*/
		this.getContentPane().setLayout(new GridLayout());

		//JPanel panel = (JPanel) this.getContentPane();
		countlbl.setText(String.valueOf(ActionControl.getAliveThreads()));
		//countlbl.setText(String.valueOf(ActionControl.aliveThreads));
		panel.add(countlbl);
		panel.add(btn_start);
		//panel.add(btn_reset);
		initTableInFrame( frameSize.width, frameSize.height - btn_start.getHeight());
		//this.setTable(table);
		
		//System.out.println(" frameSize.width = " +  frameSize.width);
		//table.setValueAt(new ImageIcon(zombiefile), 0, 0);
		panel.add(table);
		panel.setOpaque(true);
		//panel.setBackground(new Color(200, 200 ,200));
		
		//this.add(panel);
		this.setContentPane(panel);
        setVisible(true);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	
	private void initTableInFrame( int width, int height){
		int N = ac.getZombie().getN();
		int y = ac.getZombie().getY();
		int x = ac.getZombie().getX();
		List<Creature> clist = ActionControl.getCreatureList();
		
		File fin1 = null;
		File fin2 = null;
		
		File dir = new File(".");
		try {
			fin1 = new File(dir.getCanonicalPath() + File.separator + zombiefile);
			fin2 = new File(dir.getCanonicalPath() + File.separator + creaturefile);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		//ImageIcon zIcon = new ImageIcon(fin1.toString());
		ImageIcon zIcon = new ImageIcon(fin1.toString());
		ImageIcon cIcon = new ImageIcon(fin2.toString());
		
		//imgIcon2DArr = new JLabel[N][N];
		imgIcon2DArr = new ImageIcon[N][N];
		//initialize 2D array of Icon, including zombies and creatures
		for(int i = 0; i < N; i++){
			for(int j = 0;j < N; j++){
				if(j == x && i == y){
					//imgIcon2DArr[i][j] = new JLabel(zIcon);
					imgIcon2DArr[i][j] = zIcon;
					//System.out.println(imgIcon2DArr[i][j]);
				}
			}
		}
		//initialize creatures' Icon
		Iterator<Creature> itc  = clist.iterator();
		while(itc.hasNext()){
			Creature creature = itc.next();
			//imgIcon2DArr[creature.getY()][creature.getX()] = new JLabel(cIcon);
			imgIcon2DArr[creature.getY()][creature.getX()] = cIcon;
			//System.out.println(imgIcon2DArr[creature.getY()][creature.getX()]);
		}
		
		MyTableModel mtm = new MyTableModel(imgIcon2DArr);
		table.setModel(mtm);
		//System.out.println("======"+mtm.getValueAt(y, x));
		int min = Math.min(width, height);
		
		int twidth = min / N * 95 /100;
		
		//TableCellRenderer tcr = new ImageRenderer();
		//table = new JTable(mtm);
		//table.setDefaultRenderer(Object.class, tcr);
		table.setSize(min, min);
		table.setEnabled(false);
		table.setRowHeight(twidth);
		table.setOpaque(true);
		//table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		TableColumnModel tcm = table.getColumnModel();
		for(int i = 0; i < N; i++){
			TableColumn tc = tcm.getColumn(i);
			tc.setPreferredWidth(twidth);
			//tc.setCellRenderer(new ImageRenderer());
		}
		
	}

	/*public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}*/
	
}


