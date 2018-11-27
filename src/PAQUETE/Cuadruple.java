package PAQUETE;

public class Cuadruple {

	int Posicion; 
	String COJ; //Comp Operador o Jump
	String Par1;
	String Par2;
	String RS;
	
	
	public int getPosicion() {
		return Posicion;
	}

	public String getCOJ() {
		return COJ;
	}

	public String getPar1() {
		return Par1;
	}

	public String getPar2() {
		return Par2;
	}

	public String getRS() {
		return RS;
	}
	
	public void setRS(String s)
	{
		RS=s;
	}

	Cuadruple(){
		Posicion=1;
		COJ="";
		Par1="";
		Par2="";
		RS="";
	}
	
	Cuadruple(int pos,String thing, String p1, String p2, String algo){
		Posicion=pos;
		COJ=thing;
		Par1=p1;
		Par2=p2;
		RS=algo;
	}
	
	public String toString()
	{
		return (Posicion+" "+COJ+"\t"+Par1+"\t"+Par2+"\t"+RS);
	}
	

		
}


