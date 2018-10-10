package PAQUETE;

public class Statement {

	Tokens id=ParserClass.variable;
	Tokens operador;
	Expresion expr;
	String 	Clasificacion;
	
	
	Statement(){
		id=ParserClass.variable;
		operador=ParserClass.variable;
		expr=new Expresion();
	}
	
	//NUEVA MODIFICACION
	Statement(Tokens iden, Tokens op, Expresion e, String Clasif){
		id=iden;
		operador=op;
		expr=e;
		Clasificacion=Clasif;
	}
	
	//NUEVO
	public String getClasificacion(){
		return Clasificacion;
	}
	
	public Tokens getId() {
		return id;
	}

	public Tokens getOperador() {
		return operador;
	}

	public Expresion getExpr() {
		return expr;
	}
	
	public String toString()
	{
		return (id.getToken()+operador.getToken()+expr.toString());
	}
	
	public boolean isStatement()
	{
		if(id.getToken().equals(""))
			return false;
		return true;
	}
		
}
