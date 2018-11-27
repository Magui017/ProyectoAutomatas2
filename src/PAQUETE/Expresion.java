package PAQUETE;

public class Expresion 
{
	Tokens iden=ParserClass.variable;
	Tokens op=ParserClass.variable; //se les asigna una variable con valor "" para no acarrear el error nullpointexception
	Tokens iden2=ParserClass.variable; 
	String 	Clasificacion;
	
	//crear una expresion vacia 
	Expresion()
	{
		iden=ParserClass.variable;
		//Clasificacion=ParserClass.Estring;
	}
	
	
	//MODIFICACIONES PARA LA CLASIFICACION DE LAS EXPRESIONES
	
	
	Expresion(Tokens id, String Clasif)
	{
		iden=id;
		Clasificacion=Clasif;
	}
	
	Expresion(Tokens id, Tokens Operador, Tokens id2, String Clasif)
	{
		iden=id;
		op=Operador;
		iden2=id2;
		Clasificacion=Clasif;
	}
	
	//NUEVA MODIFICACION
	public String getClasificacion(){
		return Clasificacion;
	}
	
	
	public String getId() {
		return iden.getToken();
	}

	public String getId2() {
		return iden2.getToken();
	}

	public String getOperador(){
		return op.getToken();
	}
	
	//Es expresion ? 
	public boolean isExpresion(){
		//si el identificador estas vacio es por que no es una expresion D:
		if(iden.getToken().equals(""))
			return false;
		return true; // y pss aqui si hay expresion :D
	}
	
	//para imprimir toda la expresion completa
	public String toString(){
		return (iden.getToken()+op.getToken()+iden2.getToken());
	}
	
	
}
