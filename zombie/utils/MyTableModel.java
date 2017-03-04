package zombie.utils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	
	private int colCount = 0;
	//private JLabel[][] tableColData = null;
	private ImageIcon[][] tableColData = null;
	//private Vector<Object> rowData = null;
	
	//public MyTableModel(JLabel[][] tableColData) {
		public MyTableModel(ImageIcon[][] tableColData) {

		this.tableColData = tableColData;
		//this.rowData = rowData;
		this.colCount = tableColData.length;
	}
	
	@Override
	public  int getColumnCount() {
		// TODO Auto-generated method stub
		return colCount;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return colCount;
	}

	@Override
	public synchronized Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		/*Iterator<Vector<Object>> it = tableColData.iterator();
		Vector<Object> v = null;
		int i = 0;
		while(it.hasNext()){
			v = it.next();
			if(i++ == arg0)break;
		}
		return v.get(arg1);*/
		return tableColData[arg0][arg1];
	}

	@Override
	public synchronized void setValueAt(Object obj,  int row,int col){
		/*Iterator<Vector<Object>> it = tableColData.iterator();
		Vector<Object> v = null;
		int i = 0;
		while(it.hasNext()){
			v = it.next();
			if(i++ == row)break;
		}
		v.set(col, obj);*/
		//tableColData[row][col] = (JLabel)obj;
		tableColData[row][col] = (ImageIcon)obj;
		fireTableCellUpdated(row, col);
	}
	
	@Override
	public Class getColumnClass(int col){
		/*Iterator<Vector<Object>> it = tableColData.iterator();
		Vector<Object> v = null;
		int r = 0;
		int s = 0;
		Object v2 = null;
		while(it.hasNext()){
			
			v = it.next();
			Iterator<Object> ite = v.iterator();
			while(ite.hasNext()){
				v2 = (Object) ite.next();
				s++;
				if(!v2.equals(""))break;
			}
			r++;
			break;
		}
		return getValueAt(r, s).getClass();
*/
		/*int N = getColumnCount();
		for(int i = 0; i < N; i++){
			for(int j = 0;j < N; j++){
				System.out.print(i+","+j + " ");
				if(tableColData[i][j] != null){
					System.out.println(tableColData[i][j] + " " + getValueAt(i, j).getClass());
					return getValueAt(i, j).getClass();
				}
			}
		}*/
		
		return ImageIcon.class;
	}
}
