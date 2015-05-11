

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Frame extends JFrame
{
	public int index; //memoreaza indexul JTextFiled-ului focusat
	public Component comp;
	private static final long serialVersionUID = 1L;

	public Frame()
    {	    
	    this.addWindowListener(new WindowAdapter()
	    {
	       @Override
	       public void windowClosing(WindowEvent e)
	       {   	   
	    	   e.getWindow().remove(comp);
	    	   comp = null;
	    	   System.gc(); //optimizare memorie RAM
	       }
	   });
     }
    
    //
    void setsize(int w, int h)
    {
    	this.setSize(w, h);
    }    
    
    //
    void setlocationRelativeTo(Object o) { this.setLocationRelativeTo(null); }
    
    //    
    void setvisible(boolean b)
    {
    	this.setVisible(b);
    }
    
    //
    void settitle(String t)
    {
    	this.setTitle(t);
    }

    //
    void setlayout()
    {
    	this.setLayout(null);
    }
    
    //
    void addd(Component c)
    {
    	 this.add(c);    	 
    	 comp = c; // adaug sp lui viz; sp = Jframe; si il transmit lui viz prin Jmain cind apelez metoda lui viz    	 
    }
    
   
}
