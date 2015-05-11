


public class Plata
{

	public int currentrowi;//i de la intern
	public String data_platii, suma_achitata;	
	
	private String[] ob;
	
	private Tools ti; //ti variabila interna de tip Tools
	
	//constructor cu 2 parametri :t, cr
	Plata(Tools t, int cr) { ob = null; ti = t; currentrowi = cr; } // currentrowi = currentrow din clasa JMain
	
	//
	public void Get() { ob = ti.Get(currentrowi, "plata"); data_platii = ob[0]; suma_achitata = ob[1]; }
	
}
