package PAQUETE;

public class While extends Statement{
	
	Tokens w;
	Expresion expr;
	String 	Clasificacion;
	
	
	While(Tokens wh, Expresion e, Statement state,String clasif){
		super(state.id,state.operador,state.expr,state.Clasificacion);
		this.w=wh;
		this.expr=e;
		this.Clasificacion=clasif;
		//this.st=state;
	}
	
	While(Tokens w, Expresion e, String clasif)
	{
		this.w=w;
		this.expr=e;
		this.Clasificacion=clasif;
	}
	
	//NUEVO
	public String getClasificacion(){
		return Clasificacion;
	}

	public String getWhile() {
		return w.getToken();
	}

	public Expresion getExpresion() {
		return expr;
	}

	public String toString(){
		return w.getToken()+"("+expr.toString()+")";
	}

}
