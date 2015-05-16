

import java.awt.*;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.FilenameFilter;
import java.io.IOException;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.RoundingMode;
//import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableModel;
//import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent;

public class Tools
{
	
	//bd
	Connection dbCon = null; Statement stmt = null, stmt1 = null; static Statement stmts = null; ResultSet rs = null, rs1 = null;
	static ResultSet rss;
	public int nr; public int ID; public static JLabel lblsumavalcalc, lblsumadb;
	String header[] = { "Nume prenume", "Valoare", "Valoare cotă", "CNP", "Data înc.", "Data sf." };
	static Frame fr = null; static LocalDate din = null, dout = null; Float valdebac = 0.0f; Float valdeb = 0.0f;     
	public String [][] ob = null; String dela, panala; DefaultTableModel tab;
	
	Tools(Frame f) //constr - conexiunea la bd
	{
	    fr = f;
	    tab = new DefaultTableModel();
	    tab.addColumn("Nume prenume"); tab.addColumn("Valoare"); tab.addColumn("Valoare cotă");
	    tab.addColumn("SumaI"); tab.addColumn("Data înc."); tab.addColumn("Data sf.");
	      
	    //facem conexiunea la DB
	    try
	    {
			Class.forName("com.mysql.jdbc.Driver"); // declar driverul SGBD
		} catch (ClassNotFoundException e) { JOptionPane.showMessageDialog(new JFrame(), "err mysql"); }
		
	    
	    //getting database connection to MySQL server
        try
        {
			dbCon = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/debitenew1", "root", "danvicoveanu");
			stmt = dbCon.createStatement(); stmt1 = dbCon.createStatement(); stmts = dbCon.createStatement();
		}
        catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
	}
	
	String[] GetP()
	{
		 String[] ob = null;
		 try
			{
			 	nr = CountPersoane(); ob = new String[nr];
			 	rs = stmt.executeQuery("select IdPers,nume,prenume from persoane");
		    	for(int i = 0; i<nr; i++) { rs.next(); ob[i] = rs.getString(1)+ "|" +rs.getString(2) + " " +rs.getString(3); } 	//selectia rs o introduc in ob[i] care este un vector de string
		    }
			catch (SQLException e)
			{ 
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}

		 return ob;
	}
	
	
	String[] GetDebitePers(int id)
	{
		 String[] ob = null;
		 try
			{
			 	nr = CountDebitePers(id); ob = new String[nr];
			 	rs = stmt.executeQuery("select IdDebite,valoareinit from debite where IdPers = " + Integer.toString(id));
		    	for(int i = 0; i<nr; i++)
		    	{ rs.next(); ob[i] = rs.getString(1) + "|" + rs.getString(2); } //selectia rs o introduc in ob[i] care este un vector de string
		    }
			catch (SQLException e)
			{ 
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}

		 return ob;
	}
	
	
	String[] GetC()
	{
		 String[] ob = null;
		 try
			{
			 	nr = CountCote(); ob = new String[nr];
			 	rs = stmt.executeQuery("select valcote from cote");
		    	for(int i = 0; i<nr; i++) { rs.next(); ob[i] = rs.getString(1); }
			}
			catch (SQLException e)
			{ 
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		 
		 return ob;	 
	}
	
	
	String[] GetS()
	{
		 String[] ob = null;
		 try
			{
			 	nr = CountSolduri(); ob = new String[nr];
			 	
			 	rs = stmt.executeQuery("select valoareinit,IdPers from debite");
			 	rs1 = stmt1.executeQuery("select IdPers,suma_achitata from plati");
			 	
		    	for(int i = 0; i<nr; i++) 
		    	{ 
		    		rs.next(); rs1.next();
		    		ob[i] = String.format("%.2f", Float.parseFloat(rs.getString(1))-Float.parseFloat(rs1.getString(2)));
		    	}
			}
			catch (SQLException e)
			{ 
				//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		 
		 return ob;	 
	}
	
	public String GetSold(String valinit, int iddeb)
	{
	    //m("'"+deb+"'");
		
		    try
			{
			 	rs1 = stmt1.executeQuery("select SoldDeb from debite where valoareinit like '" + valinit + "%' and IdDebite = " + Integer.toString(iddeb));
			 	if(rs1.next()) return Float.toString(rs1.getFloat(1));
			}
			catch (SQLException e)
			{ 
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		 
		 //m(Float.toString(s));
		 return "0.0"; 
	}
	
	//ma ajuta generand un id nou
	static int GetIdDebit(int idpers, int ind)
	{		
		try
		{
			rss = stmts.executeQuery("select IdDebite from debite where IdPers = " + Integer.toString(idpers));
	    	for(int i = 0; i<ind; i++) rss.next(); //navigam la ultimul index debitelor
	    	return rss.getInt(1);
		}
		catch (SQLException e)
		{ 
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage());			
		}
		
		return 0;
	}

	
	//
	int GetPersPlata(int ind)
	{		
		try
		{
			rs = stmt.executeQuery("select IdPers from plati where ID = " + Integer.toString(ind));
	    	rs.next(); return rs.getInt(1);    	
		}
		catch (SQLException e)
		{ 
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage());			
		}
		
		return 0;
	}
	
	
	//
	String[] Get(int cr, String tab)
	{
		
		ID = cr; String[] ob = null;
		
		if(tab == "persoana")
		{
			ob = new String[3];	//aduc vectorul de stringuri(nume, prenume, CNP) din baza
			
			try
			{
				//m(Integer.toString(cr));
				rs1 = stmt.executeQuery("select * from persoane where IdPers = " + Integer.toString(cr));
		    	
		    	rs1.next(); //citire
		    	
		    	ob[0] = rs1.getString(2);
		    	ob[1] = rs1.getString(3);
		    	ob[2] = rs1.getString(4);

			}
			catch (SQLException e)
			{ 
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
				// JOptionPane.showMessageDialog(new JFrame(), "Nu mai sunt persoane in DB x");
			}
		}
		
		else if(tab == "cota")
		{
			ob = new String[1];
			try
			{
				rs1 = stmt.executeQuery("select valcote from cote where IdCote = " + Integer.toString(cr));
		    	rs1.next(); //citire		    	
		    	ob[0] = rs1.getString(1);
			}
			catch (SQLException e)
			{ 
				//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
				 JOptionPane.showMessageDialog(new JFrame(), "Nu mai sunt cote in DB y");
			}
		
		}
		
		else if(tab == "debit")
		{
			ob = new String[6];
			try
			{
				//IdDebite, datain, dataout,suma, valoare, IdPers, IdCote
				rs1 = stmt.executeQuery("select IdDebite,datain,dataout,suma,valoareinit,IdPers,IdCote from debite where IdDebite = " + Integer.toString(cr));
		    	rs1.next(); //citire		    	
		    	ob[0] = rs1.getString(2);
		    	//m(rs1.getString(2));
		    	ob[1] = rs1.getString(3); ob[2] = rs1.getString(4);  ob[3] = rs1.getString(5); ob[4] = rs1.getString(6); ob[5] = rs1.getString(7);
			}
			catch (SQLException e)
			{ 
				//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
				 JOptionPane.showMessageDialog(new JFrame(), "Nu mai sunt debite in DB z");
			}
		}
		
		else if(tab == "plata")
		{
			ob = new String[2];
			try
			{
				rs1 = stmt.executeQuery("select ID, data_platii, suma_achitata from plati where ID = " + Integer.toString(cr));
		    	rs1.next(); //citire		    	
		    	ob[0] = rs1.getString(2); ob[1] = rs1.getString(3);
			}
			catch (SQLException e)
			{ 
				//JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
				 JOptionPane.showMessageDialog(new JFrame(), "Nu mai sunt debite in DB z");
			}
		}
		
		return ob;
	}
	
	
	//va returna o matrice de stringuri
	String[][] GetPersoane()
	{
		String[][] obj = null;
		
		try {
    	rs = stmt.executeQuery("select IdPers from persoane"); nr = 0; while (rs.next()) nr++; //numara randurile
    	obj = new String[nr][4];
    	
    	rs = stmt.executeQuery("select IdPers, CNP, nume, prenume from persoane");
    	
    	for(int i=0; i<nr; i++)
    	{
    		rs.next(); //aduce date din baza
    		obj[i][0] = String.valueOf(rs.getInt(1)); obj[i][1] = rs.getString(2); obj[i][2] = rs.getString(3); obj[i][3] = rs.getString(4);
    	}

    	//rs.close();
    	
	} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }			
	
	
	 //obj = { {"1", "...", "Misu", "Radu"}, {"2","...","Vicoveanu", "Dan"} };
	 
		return obj;
		
	}
	
	
	//va returna o matrice de stringuri
		String[][] GetCote()
		{
			String[][] obj = null;
			
			try {
	    	rs = stmt.executeQuery("select IdCote from cote"); nr = 0; while (rs.next()) nr++; //numara randurile
	    	obj = new String[nr][2];
	    	
	    	rs = stmt.executeQuery("select IdCote,valcote from cote");
	    	
	    	for(int i=0; i<nr; i++)
	    	{
	    		rs.next(); //aduce date din baza
	    		obj[i][0] = String.valueOf(rs.getInt(1)); obj[i][1] = String.valueOf(rs.getFloat(2));
	    	}
	    	   
	    	//rs.close();
	    	
		} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }			
		
		 
		return obj;
			
		}
		
		// Returnez matricea pentru  tabelul debite
		String[][] GetDebite()
		{
			String[][] obj = null;
			
			try
			{
	    	rs = stmt.executeQuery("select IdDebite from debite"); nr = 0; while (rs.next()) nr++; //numara randurile
	    	obj = new String[nr][7];
	    	
	    	rs = stmt.executeQuery("select IdDebite, datain, dataout, suma, valoare, idPers, idCote from debite");
	    	
	    	for(int i=0; i<nr; i++)
	    	{
	    		rs.next(); //aduce date din baza
	    		//obj[i][0] = String.valueOf(rs.getInt(1)); obj[i][1] = String.valueOf(rs.getFloat(2));
	    		obj[i][0] = String.valueOf(rs.getInt(1));
	    		obj[i][1] = rs.getString(2);//obj[i][3] = String.valueOf(rs.getFloat(3));
	    		obj[i][2] = rs.getString(3);
	    		obj[i][3] = String.valueOf(rs.getInt(4));
	    		obj[i][4] = String.valueOf(rs.getFloat(5)) ;
	    		obj[i][5] = String.valueOf(rs.getInt(6));
	    		obj[i][6] = String.valueOf(rs.getInt(7));
	    	}
	    	
	    	//rs.close();
	    	
		} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }			
		
		
		return obj;
	
		}
		
		
		// Returnez matricea pentru  tabelul debite
		String[][] GetPlati()
		{
			String[][] obj = null;
			
			try
			{
	    	rs = stmt.executeQuery("select IdDebite from plati"); nr = 0; while (rs.next()) nr++; //numara randurile
	    	obj = new String[nr][4];
	    	
	    	rs = stmt.executeQuery("select IdDebite, IdPers, data_platii, suma_achitata from plati");
	    	
	    	for(int i=0; i<nr; i++)
	    	{
	    		rs.next(); //aduce date din baza
	    		obj[i][0] = String.valueOf(rs.getInt(1));
	    		obj[i][1] = String.valueOf(rs.getInt(2));
	    		obj[i][2] = rs.getString(3);
	    		obj[i][3] = String.valueOf(rs.getFloat(4)) ;
	    	}
	    	
	    	
		} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }			
		
		
		return obj;
	
		}
		
		
		//
		boolean ArePersDebite(int id)
		{
			int len = 0;
			try
			{
				rs = stmt.executeQuery("select IdPers from debite where IdPers = " + Integer.toString(id));
				while(rs.next()) len++;
				if(len > 0) return true; else return false;
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
			return true;
		}
		
		//
		boolean CotaFolosita(int id)
		{
			int len = 0;
			try
			{
				rs = stmt.executeQuery("select IdCote from debite where IdCote = " + Integer.toString(id));
				while(rs.next()) len++;
				if(len > 0) return true; else return false;
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
			return true;
		}
		
		//mesaj
		public static void m(String s) { JOptionPane.showMessageDialog(new JFrame(), s); }
		
		//inserare persoana noua
		void InsertP(String cnp, String nume, String prenume)
		{
			//scoatem ;
			if(nume.contains(";")) nume.replace(";", "");
			if(prenume.contains(";")) prenume.replace(";", "");
			
			try
			{
				stmt.executeUpdate("insert into persoane values (" + Integer.toString(CountPersoane()+1) + ", '"+cnp+"', '"+nume+"', '"+prenume+"')");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}

		
		//ID, IdDebite, IdPers, data_platii, suma_achitata
		void InsertPlata(int iddebit, int idpers, String dataplatii, String sumaachitata)
		{
			//m(dataplatii);
			try
			{
				stmt.executeUpdate("insert into plati values (" + Integer.toString(CountPlati()+1) + ", " + Integer.toString(iddebit) + ", " + Integer.toString(idpers) + ", '" + dataplatii + "', '" + sumaachitata + "')");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}
		
		
		//inserare debit nou
		void InsertD(String datain, String dataout, String suma, String valoare, int idpers, int idcota)
		{			
			try
			{
				stmt.executeUpdate("insert into debite values (" + Integer.toString(CountDebite()+1) + ", '"+datain+"', '"+dataout+"', '"+suma+"', '" + valoare+"', '" + valoare + "', "+Integer.toString(idpers)+", "+Integer.toString(idcota)+", " + valoare + ")");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}
		
		
		//inserare cota noua
		void InsertC(String v)
		{			
			try
			{
				stmt.executeUpdate("insert into cote values (" + Integer.toString(CountCote()+1) + ", '"+ v +"')");
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			}
		}
		
		
		//inlociure persoana existenta
		boolean UpdateP(int cr, String cnp, String nume, String prenume)
		{
			//update persoane set IdPers = 2 where IdPers = 1;
			try
			{
				stmt.executeUpdate("update persoane set CNP = '"+cnp+"', nume = '"+nume+"', prenume = '"+prenume+"' where IdPers = " + Integer.toString(cr));
				return true;
			}
			catch (SQLException e)
			{
				m(e.getMessage());
				return false;
			}
		}
		
		
		//inlociure persoana existenta
		boolean UpdatePlata(int iddebit, String txtdataplata, String txtsumaachitata)
		{
			//update persoane set IdPers = 2 where IdPers = 1;
			try
			{
				stmt.executeUpdate("update plati set data_platii = '"+txtdataplata+"', suma_achitata = '"+txtsumaachitata+"' where IdPers = " + Integer.toString(iddebit));
				return true;
			}
			catch (SQLException e)
			{
				m(e.getMessage());
				return false;
			}
		}
		
		
		//inlocuire debit existent
		boolean UpdateD(int cr, String datain, String dataout, String suma, String val, int id, int idcota)
		{
			try
			{
				stmt.executeUpdate("update debite set datain = '"+datain+"', dataout = '"+dataout+"', suma = "+suma+", valoareinit = '"+val+"', IdPers="+ Integer.toString(id)+", IdCote="+ Integer.toString(idcota)+" where IdDebite = " + Integer.toString(cr));
				return true;
			}
			catch (SQLException e)
			{
				m(e.getMessage());
				return false;
			}
		}
		
		
		//inlocuire cota existenta
		boolean UpdateC(int cr, String cota)
		{
			try
			{
				stmt.executeUpdate("update cote set valcote = '"+cota+"' where IdCote = " + Integer.toString(cr));
				return true;
			}
			catch (SQLException e)
			{
				m(e.getMessage());
				return false;
			}
		}
		
		
		//sterg o persoana din BD meniu Persoane		
		boolean DeleteP(int cr)
		{
			if(!ArePersDebite(cr))
			{
				try
				{
					stmt.executeUpdate("delete from persoane where IdPers = " + cr); NumerotareP();
					return true;
				}
				catch(SQLException ex)
				{
					JOptionPane.showMessageDialog(new JFrame(), "err x");
					return false;
				}				
			}
			else
			{
				JOptionPane.showMessageDialog(new JFrame(), "Persoana are debite!");
				return false;
			}			
		}
		
		
		boolean DeleteD(int cr)
		{
			try
			{
				stmt.executeUpdate("delete from debite where IdDebite = " + cr); NumerotareD();
				return true;
			}
			catch(SQLException ex)
			{
				JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
				return false;
			}		
		}
		
		
		boolean DeleteC(int cr)
		{
			try
			{
				if(!CotaFolosita(cr))
				{
				    stmt.executeUpdate("delete from cote where IdCote = " + cr); NumerotareC();
				    return true;
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Cota este folosita !");
					return false;
				}
			}
			catch(SQLException ex)
			{
				JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
				return false;
			}		
		}
		
		
		boolean DeletePl(int cr)
		{
			try
			{
				stmt.executeUpdate("delete from plati where ID = " + cr); NumerotarePl();
			    return true;
			}
			catch(SQLException ex)
			{
				JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
				return false;
			}
		}
		
		
		//Functie renumerotare
		void NumerotareP() //ordinea: 
		{
			int i, len;
			
			try {								
				//stmt.executeQuery("SET GLOBAL foreign_key_checks = 0");
				//citim IdPers din persoane => lista	
				len = CountPersoane(); int[] idpers = new int[len];
				rs = stmt.executeQuery("SELECT IdPers from persoane");
            i = 0; while(rs.next()) { idpers[i] = rs.getInt("IdPers"); i++; }				

            for(int j=1; j<=len; j++)
				{
               //m(Integer.toString(j));
					//JOptionPane.showMessageDialog(new JFrame(), "j:"+Integer.toString(j));
					//verificam in tabelul persoane daca exista un IdPers cu valoarea idpers[j]
					rs = stmt.executeQuery("SELECT IdPers from persoane where IdPers = " + Integer.toString(j)); //caut randul cu id = 1
					
					try
					{            			
						rs.next(); rs.getInt("IdPers");
					   //JOptionPane.showMessageDialog(new JFrame(), "exista pers cu id:"+Integer.toString(j));
					}					
					catch(SQLException e) //daca nu gaseste un IdPers => j este disponibil
					{                  
                  stmt.executeUpdate("update persoane set IdPers = " + Integer.toString(j) + " where IdPers = " + Integer.toString(idpers[j-1]));
						stmt.executeUpdate("update debite set IdPers = " + Integer.toString(j) + " where IdPers = " + Integer.toString(idpers[j-1]));
					}
				}
					//stmt.executeQuery("SET GLOBAL foreign_key_checks = 1");		
			}
			catch (SQLException e)
			{
				JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
            //JOptionPane.showMessageDialog(new JFrame(), "err la numerotare");
			}
		    
					
		}
		
		void NumerotareD() //ordinea: 
		{
			@SuppressWarnings("unused")
			int i, len, x = 0;
			
			try {
								
				//stmt.executeQuery("SET GLOBAL foreign_key_checks = 0");
				//citim IdDebite din debite => lista
				rs = stmt.executeQuery("SELECT IdDebite from debite"); len = 0; while(rs.next()) len++;	
				int[] iddebite = new int[len+1];
				rs = stmt.executeQuery("SELECT * from debite"); i = 1;
				while(rs.next()) { iddebite[i] = rs.getInt("IdDebite"); i++; }
				
				for(int j=1; j<=len; j++)
				{
					rs = stmt.executeQuery("SELECT IdDebite from debite where IdDebite = " + Integer.toString(j)); //caut randul cu id = 1
					try 
					{ 
						rs.next(); x = rs.getInt("IdDebite");
					}
					
					catch(SQLException e) //daca nu gaseste un IdDebite => j este disponibil
					{
						//modificam peste tot: idpers[j] => j
						try
						{
							stmt.executeUpdate("update debite set IdDebite = " + Integer.toString(j) + " where IdDebite = " + Integer.toString(iddebite[j]));
						}
						catch(SQLException ex) 
						{						
							stmt.executeUpdate("update debite set IdDebite = " + Integer.toString(j) + " where IdDebite = " + Integer.toString(iddebite[j]));
						}
					}
				}
								
			}
			catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }		    
					
		}
		
		
		void NumerotareC() //ordinea: 
		{
			@SuppressWarnings("unused")
			int i, len, x = 0;
			
			try {
								
				//stmt.executeQuery("SET GLOBAL foreign_key_checks = 0");
				//citim IdCote din cote => lista
				rs = stmt.executeQuery("SELECT IdCote from cote"); len = 0; while(rs.next()) len++;	
				int[] idcote = new int[len+1];
				rs = stmt.executeQuery("SELECT * from cote"); i = 1;
				while(rs.next()) { idcote[i] = rs.getInt("IdCote"); i++; }
				
				for(int j=1; j<=len; j++)
				{
					rs = stmt.executeQuery("SELECT IdCote from cote where IdCote = " + Integer.toString(j)); //caut randul cu id = 1
					try 
					{ 
						rs.next(); x = rs.getInt("IdCote");
					}
					
					catch(SQLException e) //daca nu gaseste un IdCote => j este disponibil
					{
						//modificam peste tot: idcote[j] => j
						try
						{
							stmt.executeUpdate("update cote set IdCote = " + Integer.toString(j) + " where IdCote = " + Integer.toString(idcote[j]));
						}
						catch(SQLException ex) 
						{						
							stmt.executeUpdate("update cote set IdCote = " + Integer.toString(j) + " where IdCote = " + Integer.toString(idcote[j]));
						}
					}
				}
				
			}
			catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }		    
					
		}
		
		
		void NumerotarePl() //ordinea: 
		{
			@SuppressWarnings("unused")
			int i, len, x = 0;
			
			try {
								
				rs = stmt.executeQuery("SELECT ID from plati"); len = 0; while(rs.next()) len++;	
				int[] idplati = new int[len+1];
				rs = stmt.executeQuery("SELECT * from plati"); i = 1;
				while(rs.next()) { idplati[i] = rs.getInt("ID"); i++; }
				
				for(int j=1; j<=len; j++)
				{
					rs = stmt.executeQuery("SELECT ID from plati where ID = " + Integer.toString(j)); //caut randul cu id = 1
					try 
					{ 
						rs.next(); x = rs.getInt("ID");
					}
					
					catch(SQLException e) //daca nu gaseste un IdCote => j este disponibil
					{
						//modificam peste tot: idcote[j] => j
						try
						{
							stmt.executeUpdate("update plati set ID = " + Integer.toString(j) + " where ID = " + Integer.toString(idplati[j]));
						}
						catch(SQLException ex) 
						{						
							stmt.executeUpdate("update plati set ID = " + Integer.toString(j) + " where ID = " + Integer.toString(idplati[j]));
						}
					}
				}
				
				
			}
			catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }		    
					
		}
		
		
		//
		int CountPersoane()
		{
			int len = 0;
			try {
				rs = stmt.executeQuery("SELECT IdPers from persoane");
				while(rs.next()) len++;	//prima val a lui len este 1
			} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
			
			return len;
		}

		
		int CountCote()
		{
			int len = 0;
			try {
				rs = stmt.executeQuery("SELECT IdCote from cote");
				while(rs.next()) len++;	//prima val a lui len este 1
			} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
			return len;
		}
		
		int CountDebite() //toate
		{
			int len = 0;
			try {
				rs = stmt.executeQuery("SELECT IdDebite from debite");
				while(rs.next()) len++;	//prima val a lui len este 1
			} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
			return len;
		}
		
		int CountSolduri() //toate
		{
			int len = 0;
			
			try
			{
				int p = CountPlati(); int d = CountDebite();
				
				rs = stmt.executeQuery("select IdPers from debite");
			 	rs1 = stmt1.executeQuery("select IdPers from plati");
			 		
				//ma uit in tabele sa vad unde sunt mai multe randuri
				if(p >= d) 
				{
					while(rs1.next()) { rs.next(); if(rs1.getInt(1) == rs.getInt(1)) len++; } //am gasit un sold					
				}				
				else
				{					
					while(rs.next()) { rs1.next(); if(rs.getInt(1) == rs1.getInt(1)) len++; } //am gasit un sold					
				}
				
			}
			
			catch (SQLException e) { }
			
			return len;
		}
		
		int CountDebitePers(int idpers)
		{
			int len = 0;
			try {
				rs = stmt.executeQuery("SELECT IdDebite from debite where IdPers = " + Integer.toString(idpers));
				while(rs.next()) len++;	//prima val a lui len este 1
			} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
			return len;
		}
		
		int CountPlati()
		{
			int len = 0;
			try {
				rs = stmt.executeQuery("SELECT ID from plati");
				while(rs.next()) len++;	//prima val a lui len este 1
			} catch (SQLException e) { JOptionPane.showMessageDialog(new JFrame(), e.getMessage()); }
			return len;
		}
		
		//verific duplicitate id
		boolean VerificId(int id, String tab)
		{
			try
			{
	            rs = stmt.executeQuery("select * from " + tab + " where id_pers = '" + id + "'"); //returneaza un rand
	            rs.next(); //executa            
	            if( (int)rs.getInt(1) == id ) return true; else return false;
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(new JFrame(), "VerificId error.");
				return false;
			}
		}
		
		
		// functie generare ID - nu se mai foloseste
		int GenId(String tab)
		{
			try {
				for(int i = 1; i<2147483646; i++) if(!VerificId(i, tab)) return i;			
			} catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(), "GenId error.");
			}
			return 0;
		}
		

	//
    String[][] GetSelectie(String stxtnume, String stxtvalcalc, String stxtcota, String stxtsumai, String stxtdatain, String stxtdataout)
    {
    	String[][] obj = null;
    	
         try
         {
            rs = stmt.executeQuery("SELECT persoane.nume, persoane.prenume, debite.valoareinit, cote.valcote, debite.suma, debite.datain, debite.dataout FROM (persoane LEFT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN cote ON debite.IdCote=cote.IdCote");
            nr = 0; while(rs.next()) nr++; obj = new String[nr][6];
            
              //m("proba1");
              //JOptionPane.showMessageDialog(new JFrame(), nr);           
            //sql: Nume si prenume, val calc, cota, cnp, data in, data out
            //m(stxtdatain); m(stxtdataout);
           if(stxtnume.isEmpty() && stxtvalcalc.isEmpty() && stxtcota.isEmpty() && stxtsumai.isEmpty() && stxtdatain == "1900-01-01" && stxtdataout == "2500-01-01")
            	// if(stxtnume.isEmpty() && stxtvalcalc.isEmpty() && stxtcota.isEmpty() && stxtcnp.isEmpty() )
            	rs = stmt.executeQuery("SELECT persoane.nume, persoane.prenume, debite.valoareinit, cote.valcote, debite.suma, debite.datain, debite.dataout FROM (persoane LEFT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN cote ON debite.IdCote=cote.IdCote");
            else
            {
            	//m("tabel nou");
                //validare date      
            	// JOptionPane.showMessageDialog(new JFrame(), nr); 
                if(stxtdatain.contains("-") == false && stxtdatain.length()>3) stxtdatain = stxtdatain + "-01-01";
                if(stxtdataout.contains("-") == false && stxtdataout.length()>3) stxtdataout = stxtdataout + "-01-01";
               // m("s");
            	rs = stmt.executeQuery("SELECT persoane.nume, persoane.prenume, debite.valoareinit, cote.valcote, debite.suma, debite.datain, debite.dataout" +
            	" FROM (persoane LEFT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN cote ON debite.IdCote=cote.IdCote WHERE " +
            	"(persoane.nume like '" + stxtnume + "%' and debite.valoare like '" + stxtvalcalc + "%' and cote.valcote like '" + stxtcota +
            	"%' and debite.suma like '" + stxtsumai + "%' and debite.datain between '" + stxtdatain + "' and '" + stxtdataout + "') ORDER BY persoane.nume");
            }
            
            try
            {
               for(int i=0; i<nr; i++)
	    	   {
            	   rs.next(); //aduce date din baza
	    		   obj[i][0] = rs.getString(1) + " " + rs.getString(2); //numele si prenumele
	               obj[i][1] = rs.getString(3); //valoare
	               obj[i][2] = rs.getString(4); //val cota
	               obj[i][3] = rs.getString(5); //sumai
	               obj[i][4] = rs.getString(6); //datain
	               obj[i][5] = rs.getString(7); //dataout	               
	    	   }
            }
            catch(Exception ex) { }
         }
         catch(SQLException e) { m(e.getMessage()); }
         
         //fac suma debitelor+accesorii
         valdebac = 0.0f;  
         for(int i=0; i<nr; i++)
         {
        	 try { valdebac += Float.parseFloat(obj[i][1]); } catch(Exception e) {}
         }
         //lblsumavalcalc.setText("SUMA: " + Float.toString(valdebac));
         
         Locale.setDefault(Locale.US);
		 DecimalFormat df = new DecimalFormat(".00");
		 df.setRoundingMode(RoundingMode.UP);
         
         lblsumavalcalc.setText(String.format("<html><font color='GREEN'>DbAc: %s</font></html>", df.format(valdebac)));
         
         //fac suma debitelor
         valdeb = 0.0f;
         for(int i=0; i<nr; i++)
         {        	 
        	 try { valdeb += Float.parseFloat(obj[i][3]); } catch(Exception e) {}
         }
         //lblsumavalcalc.setText("SUMA: " + Float.toString(valdebac));
         
		 lblsumadb.setText(String.format("<html><font color='GREEN'>Db: %s</font></html>", df.format(valdeb))); 
         
         return obj;
    }            

      
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
        	if (!Character.isDigit(c) && c != '.') return false;
        }
        return true;
    }
    
    
    public static boolean ValidareNumerica(String valcalc, String cota, String sumai)
    {
    	if(valcalc.isEmpty() && cota.isEmpty() && sumai.isEmpty()) return true;
    	else
    	{
	    	if(!valcalc.isEmpty() && isNumeric(valcalc)) return true;
	    	if(!cota.isEmpty() && isNumeric(cota)) return true;
	    	if(!sumai.isEmpty() && isNumeric(sumai)) return true;
    	}
    	m("ValidareNumerica esuata!");
    	return false;
    }
    
    
    public static boolean ValidareDate(String stxtdatain, String stxtdataout)
    {
    	  JTextField cin = (JTextField) fr.getContentPane().getComponent(10);
    	  JTextField cout = (JTextField) fr.getContentPane().getComponent(12);
    		  
    	  if(stxtdatain == "1900-01-01" && stxtdataout == "2500-01-01")
    	  { cin.setBackground(Color.WHITE); cout.setBackground(Color.WHITE); return true; }
    	  
    	  if(stxtdatain.length() == 4 && stxtdataout.length() == 4)
    	  { cin.setBackground(Color.WHITE); cout.setBackground(Color.WHITE); return true; }
    	  
    	  if(stxtdatain.length() > 10) { cin.setBackground(Color.RED); return false; }
    	  else
    	  {
    		  cin.setBackground(Color.WHITE);
    	  
    		  if(stxtdataout.length() > 10) { cout.setBackground(Color.RED); return false; }
    		  else
    		  {
    			  cout.setBackground(Color.WHITE);
    			  
    			   try
    			   {    				  
    				   din = LocalDate.parse(stxtdatain, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
    				   cin.setBackground(Color.WHITE);
    				   
    				   try
    				   {
    					   dout = LocalDate.parse(stxtdataout, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
    					   cout.setBackground(Color.WHITE);    					   
    					   if(dout.isBefore(din))
    					   {
    						   m("dout < din");
    						   ((JTextComponent) fr.getContentPane().getComponent(10)).setText(""); //stxtdatain
    						   ((JTextComponent) fr.getContentPane().getComponent(10)).requestFocus();
    						   return false;
    						   }
    					   else return true;
    				   }
    			       catch (Exception e) { cout.setBackground(Color.RED); return false; }    				      
    			   } 
    		       catch (Exception e) { cin.setBackground(Color.RED); return false; }
    		  } 	  
    	  }
      }      
      
      
      public void tabel(JTable t, JScrollPane sp, String stxtnume, String stxtvalcalc, String stxtcota, String stxtsumai, String stxtdatain, String stxtdataout, Frame fr)
      {
    	  
    	  if(stxtdatain.isEmpty() && stxtdataout.isEmpty()) { stxtdatain = "1900-01-01"; stxtdataout = "2500-01-01"; } //min, max
    	  	  
    	  if(ValidareNumerica(stxtvalcalc, stxtcota, stxtsumai) == false) return;  	  
    	  
    	  
    	  if(ValidareDate(stxtdatain, stxtdataout) == false) return;  	  
    	
    	  //Tools.m(stxtsumai);
    	  
	      String [][] obj = GetSelectie(stxtnume, stxtvalcalc, stxtcota, stxtsumai, stxtdatain, stxtdataout);	
	      
	      	      
	      ob = obj; //pt export
	      dela = stxtdatain; panala = stxtdataout;
	      
	      nr = obj.length;	      
	      
	      tab.setNumRows(0); //sterg randurile din tabel
	      
	      
	      for(int i=0; i<nr; i++) tab.addRow(obj[i]); //iau un rand din matricea obj
		 
	      
	      //tab.addRow(new String[] { stxtnume, stxtnume} );
	      	      	      
	      
	      t = new JTable(tab);
	      //t.setFillsViewportHeight(true);
	      
		  
	      //jtab.setFillsViewportHeight(true);
          t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          //t.setPreferredScrollableViewportSize(new Dimension(550, 300));

         /* t.getSelectionModel().addListSelectionListener(
        	        new ListSelectionListener()
        	        {
        	        	
        	            public void valueChanged(ListSelectionEvent event)
        	            {
        	            	m("1");
        	            }
        	        }
        	);  
        	  */      
          
		  
		  sp = new JScrollPane(t);  //construim sp-ul pe baza lui tab	
		  
		  sp.setBounds(10, 80, 550, 300);
		  
		  fr.addd(sp);	  
		  
		 /* for(int i=0; i<nr; i++)
		  {
			  if(obj[i][0] == null || obj[i][0].isEmpty()) obj[i][0] = "x";
			  if(obj[i][1] == null || obj[i][1].isEmpty()) obj[i][1] = "x";
			  if(obj[i][2] == null || obj[i][2].isEmpty()) obj[i][2] = "x";
			  if(obj[i][3] == null || obj[i][3].isEmpty()) obj[i][3] = "x";
			  if(obj[i][4] == null || obj[i][4].isEmpty()) obj[i][4] = "x";
			  if(obj[i][5] == null || obj[i][5].isEmpty()) obj[i][5] = "x";
	      }
	      */		  
			  
          String s;
          
          for(int i=0; i<nr; i++)
   	      {
	    	  //s = t.getModel().getValueAt(i, 5).toString(); //extrag in s textul din celula de coordonate i,5 //
        	  s = obj[i][5];
	    	  //try { if(s.contains("2500")) {m(Integer.toString(i)); t.getModel().setValueAt(null, i, 5); } }
        	  try { if(s.contains("2500")) t.getModel().setValueAt("", i, 5); }//vad ca se oprea la i = 5;
	    	  catch(Exception e) { }
   	      }  

           
        t.setEnabled(false); // activeaza dezactiveaza editarea
        
         
      }
      
      
      //
      public void sold(String nume, String datain, String dataout)
      {
    	  //m(nume);
    	  if(datain.isEmpty() && dataout.isEmpty()) { datain = "1900-01-01"; dataout = "2500-01-01"; } //min, max
    	  if(datain.contains("-") == false && datain.length()>3) datain = datain + "-01-01";
          if(dataout.contains("-") == false && dataout.length()>3) dataout = dataout + "-01-01";
    	  if(ValidareDate(datain, dataout) == false) return;	  
    	  
    	  
    	try
    	{   
    		//SELECT persoane.IdPers, debite.IdDebite, persoane.nume, persoane.prenume, debite.datain, debite.dataout, plati.data_platii,
    		//debite.suma, debite.valoare, plati.suma_achitata
    		//FROM (persoane RIGHT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN plati ON debite.IdDebite=plati.IdDebite;
    		
    		/*SELECT persoane.IdPers, debite.IdDebite, persoane.nume, persoane.prenume, debite.datain, debite.dataout, plati.data_platii,
    		debite.suma, debite.valoare, plati.suma_achitata
    		FROM (persoane RIGHT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN plati ON debite.IdDebite=plati.IdDebite
    		where 
    		where persoane.nume like '"+
                 nume +"%' and debite.datain >= '"+datain+"' and debite.dataout <= '"+dataout+"' and plati.data_platii != '0000-00-00' ORDER BY persoane.nume"
    		*/
    		
    		
    		
    		
    		//SELECTIE DOAR PENTRU PLATI
    		/*SELECT debite.IdDebite, debite.IdPers,debite.datain, debite.dataout, debite.valoare, plati.data_platii, plati.suma_achitata,(debite.valoare - plati.suma_achitata) AS debit
              FROM debite RIGHT JOIN plati ON debite.IdDebite = plati.IdDebite
    		where 
    		 debite.datain >= '"+datain+"' and debite.dataout <= '"+dataout+"' ORDER BY debite.IdPers");
    		 
    		*/
    		 /*rs = stmt.executeQuery("SELECT persoane.IdPers, debite.IdDebite, persoane.nume, persoane.prenume, debite.datain, debite.dataout, plati.data_platii,"+
           		 " debite.suma, debite.valoare, plati.suma_achitata, (debite.valoare - plati.suma_achitata) AS debit FROM "+
           	  "(persoane RIGHT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN plati ON debite.IdDebite=plati.IdDebite where persoane.nume like '"+
                 nume +"%' and debite.datain >= '"+datain+"' and debite.dataout <= '"+dataout+"' ORDER BY persoane.nume");*/
    		 
    		rs = stmt.executeQuery("SELECT persoane.IdPers, debite.IdDebite, persoane.nume, persoane.prenume, debite.datain, debite.dataout, IFNULL(plati.data_platii, '0000-00-00') AS data_platii, "+
    	             "debite.suma, debite.valoareinit, IFNULL(plati.suma_achitata, '0') AS suma_achitata FROM (persoane RIGHT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN plati ON debite.IdDebite=plati.IdDebite "+
    	             "where persoane.nume like '"+ nume +"%' and debite.datain >= '"+datain+"' and debite.dataout <= '"+dataout+"' ORDER BY persoane.nume, debite.valoareinit, plati.ID");
    		
    		String iddebit = ""; nr = 0;
    		while(rs.next()) { if(nr==0) /*doar primul iddebit*/ iddebit = rs.getString(2); nr++; /*numar cate randuri are rs*/ }
    		ob = new String[nr+1][9]; //fac obiectul matrice cu nr rinduri + rind rosu(footer)  
               
    		//rs al doilea ma duce pe primul rind
             rs = stmt.executeQuery("SELECT persoane.IdPers, debite.IdDebite, persoane.nume, persoane.prenume, debite.datain, debite.dataout, IFNULL(plati.data_platii, '0000-00-00') AS data_platii,"+
             "debite.suma, debite.valoareinit-debite.suma AS accesorii, debite.valoareinit, IFNULL(plati.suma_achitata, '0') AS suma_achitata FROM (persoane RIGHT JOIN debite ON persoane.IdPers=debite.IdPers) LEFT JOIN plati ON debite.IdDebite=plati.IdDebite "+
             "where persoane.nume like '"+ nume +"%' and debite.datain >= '"+datain+"' and debite.dataout <= '"+dataout+"' ORDER BY persoane.nume, debite.valoareinit, plati.ID");
            
             Float sum = 0.0f;
             
             try
             {            	
            	
               for(int i=0; i<nr; i++)
 	    	   {
             	   rs.next(); //aduce date din baza
             	   
 	    		   ob[i][0] = rs.getString(3) + " " + rs.getString(4); //numele
 	               ob[i][1] = rs.getString(5); //data in
 	               ob[i][2] = rs.getString(6); //data out
 	               ob[i][3] = rs.getString(7); //data platii
 	               ob[i][4] = rs.getString(8); //sumai  
 	               ob[i][5] = String.format("%.2f", Float.parseFloat(rs.getString(9))); //accesorii 
 	               ob[i][6] = rs.getString(10); //valoareainit=de fapt este Db+Acesorii
 	               ob[i][7] = rs.getString(11); //suma achit              
 	               
 	              if(rs.getString(2).equals(iddebit)) { ob[i][8] = ""; sum += Float.parseFloat(ob[i][7]); }
	              else //s-a schimbat id-ul debitului
	              {
	            	  ob[i-1][8] = String.format("%.2f", Float.parseFloat(ob[i-1][6]) - sum);
	            	  sum = Float.parseFloat(ob[i][7]); iddebit = rs.getString(2); //memorez noul iddebit
	              }          	
 	    	   }
               
             }
             catch(Exception ex) { m(ex.getMessage()); }
             
             //Sume pe verticala
             Float v; v = 0.0f;
             //val achitata
             for(int i=0; i<nr; i++)
             {
            	 try { v += Float.parseFloat(ob[i][7]); } catch(Exception e) {}
             } 
             
             //ultimul rand (rosu)
             ob[nr][0] = "Sume:"; //numele
             ob[nr][1] = ""; //data in
             ob[nr][2] = ""; //data out
             ob[nr][3] = ""; //data platii
             ob[nr][4] = String.format("%.2f", valdeb); //sumai 
             
             ob[nr][7] = Float.toString(v); //suma achitata in matrice             
             //
             ob[nr-1][8] = String.format("%.2f", Float.parseFloat(ob[nr-1][6]) - sum); //calculez valoarea pentru linia anterioara
             
             //sumele cu rosu
             //v = Float.parseFloat(ob[0][6]);
             //String valdeb = ob[0][6]; //prima de sus//valinit
             //for(int i=1; i<nr; i++) //urmatoarele linii
            //{
            //   if(!ob[i][6].equals(valdeb)) //daca valdeb are valoare noua o introducem din nou in v si o compar din nou cu ob[i][5] care este valoarea initila
             
             //if(!ob[i][6].equls(valdeb) && !(!ob[i][0].equals(IDDebite)
            //   try { v += Float.parseFloat(ob[i][6]); valdeb = ob[i][6]; } catch(Exception e) {}
            //}
             
             ob[nr][6] = Float.toString(valdebac); //valoareainit de fapt este Db+accessorii
             
             ob[nr][5] = String.format("%.2f", Float.parseFloat(ob[nr][6])-Float.parseFloat(ob[nr][4])); //accesorii
             
             ob[nr][8] = String.format("%.2f", valdebac - Float.parseFloat(ob[nr][7])); //debit total ramas
    	}
    	catch(Exception e) { }
    	 
    	 //5=6-4
      }
      
      
      
      public boolean ValidareAdaugarePersoane(String CNP) throws SQLException
      {
        rs = stmt.executeQuery("select CNP from persoane where CNP = '" + CNP + "'"); //returneaza un rand
        rs.next(); //executa	            
        if(rs.getString(1).equals(CNP)) return false; else return true;
      }
      
      
      public static boolean ValidareAdaugareDebite(String sdin, String sdout, String ssuma)
      {
    	  if(sdin.isEmpty()) return false;
    	  else
    	  {
    		  if(sdin.length() == 4) sdin = sdin + "-01-01";
    		  if(sdout.isEmpty()) sdout = sdout + "2500-01-01";
    		  if(sdout.length() == 4) sdout = sdout + "-01-01";
    	  }
    	  if(ssuma.isEmpty()) ssuma = "0";
    	  if(isNumeric(ssuma) == false) return false;
    	  try
    	  {
    		  din = LocalDate.parse(sdin, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
    		  dout = LocalDate.parse(sdout, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));
    		  if(dout.isBefore(din) || dout.isEqual(din)) return false;
    	  }
    	  catch(Exception e)
    	  {
    		 m(e.getMessage()); return false;
    	  }    	      	  
    	  return true;
      }
      
      //
      //caut controlul focusat
    	//int i = 0;
  		//for (Component c : fr.getContentPane().getComponents())
  		//{ JOptionPane.showMessageDialog(new JFrame(), Integer.toString(i)); i++; } 
      
      
      public static void ExportCsv(String [][] o, String delaa, String panalaa)
      {
    	
    	  JFileChooser d = new JFileChooser();
    	  d.setDialogTitle("Export CSV");
    	  d.setCurrentDirectory(new File("."));
    	  d.setSelectedFile(new File("Debite.csv"));
    	  d.setFileSelectionMode(JFileChooser.FILES_ONLY);    	  
    	  d.setFileFilter(new FileNameExtensionFilter("CSV files", "csv", "CSV"));
    	  int r = d.showSaveDialog(fr);
    	  
    	    if(r == JFileChooser.APPROVE_OPTION)
    	    {
    	    	
    	        final int n = o.length; //m(Integer.toString(n));
    	        //int nc = jtab.getColumnCount(); m(Integer.toString(nc));
    	        String s = "";
    	        
    	        try {
    	        	File file = new File(d.getSelectedFile().getAbsolutePath()); file.delete();    	     
    	    		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
    	     
    	    		out.append("Debite; DE LA;"+delaa+";PANA LA;"+panalaa+";;\r\n"); //titlu
    	    		out.append("Nume si prenume;Val calc;Cota;CNP;Data in;Data out;\r\n");
    	    		
    	    		for(int i=0; i<n; i++)
			    	{
			    		for(int j=0; j<6; j++)
			    		{
			    		   s = o[i][j];
			    		   if(s == null) s = ""; else if(s.contains(";")) s = s.replace(";", "");
			    		   if(s.contains(".")) s = s.replace(".", ",");
			    		   s += ";";
			    		   String ss = new String(s.getBytes(), "UTF8");
			    		   out.append(ss);
			    		  // s = "";
			    		}			    		
			    		out.append("\r\n");
			    	}
    	     
    	    		out.flush();
    	    		out.close();
    	     
    	    	    } 
    	    	   catch (UnsupportedEncodingException e) 
    	    	   {
    	    		m(e.getMessage());
    	    	   } 
    	    	   catch (IOException e) 
    	    	   {
    	    		m(e.getMessage());
    	    	   }
    	    	   catch (Exception e)
    	    	   {
    	    		m(e.getMessage());
    	    	   } 
    	        
    	    	
    	    /*
    	    	
    	    	//
    	    	//Writer writer = new FileWriter("c:\\data\\output.txt", true);  //appends to file
    	    	//Writer writer = new FileWriter("c:\\data\\output.txt", false); //overwrites file
    	    	
    	    	String s = "";
    	    	int nr = tab.getRowCount();
    	    	//m(Integer.toString(nr));    	    	
    	    	
				try
				{
					OutputStream os = new FileOutputStream(d.getSelectedFile().getAbsolutePath());
    	    		Writer w = new OutputStreamWriter(os, "UTF-8");
    	    		
    	    		w.write("Nume si prenume;Val calc;Cota;CNP;Data in;Data out;");
					//validare si salvare
    	    		
			    	for(int i=0; i<nr; i++)
			    	{
			    		for(int j=0; j<6; j++)
			    		{
			    		   s = tab.getModel().getValueAt(i, j).toString();
			    		   if(s == null) s = ""; else if(s.contains(";")) s.replace(";", "");
			    		   s += ";";
			    		}
			    		s += "\r\n"; //linie noua
			    	}
			    	
			    	w.write(s);
			    	w.close();
			    	os.close();
				}
				catch (IOException e) { m(e.getMessage()); }
				*/
    	    }
    	  
      }
      
      
  }

