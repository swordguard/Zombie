package zombie.utils;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



public class ImageRenderer extends DefaultTableCellRenderer {
	/*String imgfile;
	
	public ImageRenderer(String imgfile) {
		// TODO Auto-generated constructor stub
		//this.imgfile = imgfile;
	}*/
	
	
	//JLabel label = new JLabel();
	  @Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int row, int column) {
		  
		  /*
			File fin1 = null;
			File dir = new File(".");
			try {
				fin1 = new File(dir.getCanonicalPath() + File.separator + imgfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageIcon icon = new ImageIcon(fin1.toString());
	    //lbl.setText((String) value);
	    lbl.setIcon(icon);*/
	    
	   /* if (value!=null) {
	        label.setHorizontalAlignment(JLabel.CENTER);
	        //value is parameter which filled by byteOfImage
	        label.setIcon(new ImageIcon((byte[])value));
	        //label.setIcon(new ImageIcon(value));
	        }
	    
	    */
		  //System.out.println("======in renderer: "+value.toString());
		  if(value instanceof ImageIcon){
			  System.out.println("======in renderer: "+value.toString());
			  this.setIcon(new ImageIcon( value.toString()));
			  this.setText("ok");
			  //label.setOpaque(false); 
		  }
		 
		  
		return this;
		  /*if(column != -1 && row != -1){
	            return new JLabel(new ImageIcon( value.toString()));
	        }else{
	            return super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
	        }*/
	  }

}
