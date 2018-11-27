package PAQUETE;


public class VarDeclaration {
	Tokens type,id;
	
	VarDeclaration(Tokens tipo, Tokens ident){
		type=tipo;
		id=ident;
	}

	public String getType() {
		return type.getToken();
	}

	public String getId() {
		return id.getToken();
	}

}
