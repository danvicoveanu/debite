


public class Cota
{

	public int ID, currentrowi; //i de la intern
	String valcota;
	
	
	private String[] ob;
	
	Tools ti; //ti variabila interna de tip Tools
	
	//constructor cu 2 parametri :t, cr
	Cota(Tools t, int cr) { ob = null; ti = t; currentrowi = cr; } // currentrowi = currentrow din clasa JMain
	
	//
	public void Get() { ob = ti.Get(currentrowi, "cota"); ID = ti.ID; valcota = ob[0]; }

	public void Set(String val) { ti.InsertC(val); }
	
}
