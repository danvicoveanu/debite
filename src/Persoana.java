

public class Persoana
{

	public int ID, currentrowi;//i de la intern
	public String CNP;
	public String Nume;
	public String Prenume;
	
	private String[] ob;
	
	private Tools ti; //ti variabila interna de tip Tools
	
	//constructor cu 2 parametri :t, cr
	Persoana(Tools t, int cr) { ob = null; ti = t; currentrowi = cr; } // currentrowi = currentrow din clasa JMain
	
	//
	public void Get() { ob = ti.Get(currentrowi, "persoana"); ID = ti.ID; CNP = ob[0]; Nume = ob[1]; Prenume = ob[2]; }

	public void Set() { }
	
}
