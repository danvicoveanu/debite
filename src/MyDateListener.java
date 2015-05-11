

import javax.swing.text.JTextComponent;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;


public class MyDateListener implements DateListener
{
	public Frame fr;
	
	public MyDateListener(Frame f)
	{
		fr = f;	
	}	
	
	
	public void dateChanged(DateEvent e)
	{
		
		//JOptionPane.showMessageDialog(new JFrame(), e.getSelectedDate().getTime().toString());
		String s =  e.getSelectedDate().getTime().toString();	
		//Tools.m(s);
		String[] v = s.split(" ");		
		String[] luni = "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" ");
		String[] lunile = "01 02 03 04 05 06 07 08 09 10 11 12".split(" ");
		
		int j = 0; for(int i = 0; i < 12; i++) { if(v[1].equals(luni[i])) { j = i; break; } } //identificam indexul lunii curente
	
		//caut controlul focusat
		//for (Component c : fr.getContentPane().getComponents())
		//{ JOptionPane.showMessageDialog(new JFrame(), Integer.toString(c.getLocation().x)); }
		
		//JOptionPane.showMessageDialog(null, Integer.toString(findPrevFocus().getLocation().x));
		
		//if(fr.getFocusOwner().getLocation().x == 87)
		//{
			
			//fr.getContentPane().getComponent(11).requestFocusInWindow();
			//x = 11;
			//JOptionPane.showMessageDialog(new JFrame(), "din");
     	//}
		//else if(fr.getFocusOwner().getLocation().x == 171)
		//{
	    	//x = 12;
	    	//fr.getContentPane().getComponent(12).requestFocusInWindow();
	    	//JOptionPane.showMessageDialog(new JFrame(), "dout");
		//}
		
		//JOptionPane.showMessageDialog(new JFrame(), "ooo");
		//KeyboardFocusManager.getCurrentKeyboardFocusManager().getpre().requestFocusInWindow();
		
		((JTextComponent) fr.getContentPane().getComponent(fr.index)).setText(v[5] + "-" + lunile[j] + "-" + v[2]);
		//((JTextComponent) fr.getContentPane().getComponent(fr.index)).setBackground(Color.WHITE);
		
		//JOptionPane.showMessageDialog(new JFrame(), s + "\n" + v[5] + "-" + lunile[j] + "-" + v[2]);		
		
		
	}

}
