


public class Debit
{

	public int ID, currentrowi;//i de la intern
	public String datain, dataout, suma, valoare, idpers, idcota;
	
	
	private String[] ob;
	private Tools ti; //ti variabila interna de tip Tools
	
	//constructor cu 2 parametri :t, cr
	Debit(Tools t, int cr) { ob = null; ti = t; currentrowi = cr; } // currentrowi = currentrow din clasa JMain
	
	//
	public void Get() { ob = ti.Get(currentrowi, "debit"); ID = ti.ID; datain = ob[0]; dataout = ob[1]; suma = ob[2]; valoare = ob[3]; idpers = ob[4]; idcota = ob[5];  }
	
}
