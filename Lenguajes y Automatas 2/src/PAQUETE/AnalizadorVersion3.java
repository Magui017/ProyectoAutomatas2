package PAQUETE;
import java.util.Scanner;
import java.util.Vector;
 
public class AnalizadorVersion3 {
	
	Scanner leer = new Scanner(System.in);
	//ARREGLO DE PALABRAS RESERVADAS
	//String PR [] = new String []{"INICIO","FIN","if","while","int","boolean","false","true","println"};
	String PR [] = new String []{"if","while","int","boolean"};
	
	//VECTOR CON LOS SIMBOLOS (OPERADORES Y SIMBOLOS) VALIDOS
 	Vector <Character> caracteresValidos = new Vector <Character> ();
	
	//VECTOR DE OBJETOS TIPO TOKEN, GUARDA LOS TOKEN EN UN OBJETO QUE TIENE EL TOKEN Y EL TIPO AL QUE PERTENECE
	 Vector <Tokens> tabla = new Vector <Tokens> (20,1);
	
	int ap;
	String linea;
	char caracter;
	String token;
	
//	static int ContError;
	int renglon=1;
	int columna=0;
	
	//VARIABLE PARA SABER SI EL PROGRAMA NO TIENE NINGUN ERROR
	boolean Correcto = true;
	
	//CONSTRUCTOR DE LA CLASE INICIALIZA VARIABLES IMPORTANTES Y MANDA LLAMAR EL METODO PRINCIPAL
	public AnalizadorVersion3(String Programa){
		
		llenaVector();
		ap = 0;
	//	ContError = 0;
		token = "";
		linea = Programa;
		IdentificaToken();
	}
	
	//ESTE METODO LLENA EL VECTOR CON LOS TOKENS VALIDOS, ESTO SIRVE PARA DETECTAR LOS ERRORES DE TIPO "HOL.A" ECT.
	public void llenaVector(){
		caracteresValidos.add('=');
		caracteresValidos.add('(');
		caracteresValidos.add(')');
		caracteresValidos.add(';');
		caracteresValidos.add(' ');
		caracteresValidos.add('\t');
		caracteresValidos.add('\n');
		caracteresValidos.add('+');		
	}
	
	public void IdentificaToken(){
		
		//TOMA EL PRIMER CARACTER Y ENTRA A ANALIZARLO
		caracter = linea.charAt(ap);
		//CICLO QUE SE REPITE HASTA LLEGAR AL FINAL DEL TEXTAREA
		do{
			//BUSCA EL CASO AL QUE PERTENECE EL CARACTER
			switch(caracter){
			
			case '=':
			token+=caracter;
				SiguienteCaracter();
				
				if(caracter == '=')
				{
					token+=caracter;
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tOPREL,==\n"; 
					tabla.addElement(new Tokens(token, "OPREL")); //AGREGA A UN VECTOR DE OBJETOS TIPO TOKEN
					SiguienteCaracter();
				} 
				//else if(caracter == '-' || caracter == '*' || caracter == '/' || caracter == '+'){
				else if(caracter == '(' || caracter == ')' || caracter == '+' || caracter == ';' || Character.isDigit(caracter)){ 
					token+=caracter;
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
		//			ContError++;
					Correcto = false;
					SiguienteCaracter();
				}
				else{
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tOPREL,=\n"; 
					tabla.addElement(new Tokens(token, "OPREL")); //AGREGA A UN VECTOR DE OBJETOS TIPO TOKEN
				}
				
				token = "";
				break;
				
			case '+':
				token+=caracter; 	
				SiguienteCaracter();
				
				//SI ALGUNO DE ESTOS CARACTERES SALE DESPUÉS DEL + RESULTA COMO ERROR
				if(caracter == '=' || caracter == '(' || caracter == ')' || caracter == '+'){
					token+=caracter;
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
		//			ContError++;
					Correcto = false;
					SiguienteCaracter();
				}
				else{
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tOPARI,+\n";
					tabla.addElement(new Tokens(token, "OPARI"));
				}
				
				token = "";
				break;
				
				
            case ';':
                token+=caracter;
                Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tSigno de puntuación,;\n";
                tabla.addElement(new Tokens(token, "PuntoComa"));
                
                token ="";
                SiguienteCaracter();
                break;
                
			case ' ': //ESPACIO EN BLANCO
				token+=caracter;
				SiguienteCaracter();
				while(caracter == ' ' && ap!=-1){
					SiguienteCaracter();
				}
				
				System.out.println("\tEB");
				token = "";
				break;
				
			case 9: //TABULADOR
				token+=caracter;
				columna+=3;
				SiguienteCaracter();
				while(caracter == 9 && ap!=-1){
					SiguienteCaracter();
					columna+=3;
				}
				
				System.out.println("\tTABs");
				token = "";
				break;
				
			case '\n':
				token+=caracter;
				SiguienteCaracter();
				renglon++;
				columna=1;
				while(caracter == '\n' && ap!=-1){
					SiguienteCaracter();
					renglon++;
					columna=1;
				}
				
				System.out.println("\tENTER");
				token = "";
				break;
				
			case '(':
                    token+=caracter;
                    Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tParentesis Izq\n";
                    tabla.addElement(new Tokens(token, "Parentesis izquierdo"));
                    token ="";
                    
                    SiguienteCaracter();
                    break;
                    
            case ')':
                    token+=caracter;
                    Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tParentesis Der\n";
                    tabla.addElement(new Tokens(token, "Parentesis derecho"));
                    token ="";
                    
                    SiguienteCaracter();
                    break;
                
			default:
				
			//	identificadores 
				if(Character.isLetter(caracter))
				{
					token+=caracter;
					SiguienteCaracter();
				
					if(Character.isLetter(caracter) || Character.isDigit(caracter))
					{
						while(Character.isLetter(caracter)||Character.isDigit(caracter)){
							token+=caracter;
							SiguienteCaracter();
						}
						
					}

					if(!caracteresValidos.contains(caracter)){
						while(!caracteresValidos.contains(caracter)){
								token+=caracter;
								SiguienteCaracter();
						}
						if(columna-token.length()==0)
							Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
						else
							Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-1-token.length())+"\n";
					//	ContError++;
						Correcto = false;
						token="";
						break;
					}
					
					if(PalabraReservada()){
						Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tPalabra Reservada \n";
						tabla.addElement(new Tokens(token, "PR"));
					}
					
					else{
						Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tIdentificador \n";
						tabla.addElement(new Tokens(token, "ID"));
					}
					token="";
				}
				else{
					
					while(!caracteresValidos.contains(caracter))
					{
						token+=caracter;
						SiguienteCaracter();
					}
					Interfaz.TokenTipo = Interfaz.TokenTipo + token+"\tERROR"+"  R"+renglon+":C"+(columna-token.length())+"\n";
					Correcto = false;

					token="";
					
					break;

				}
			}
		}while(ap>-1 && ap<linea.length());
		asignaCode();
		for(int i=0;i<tabla.size();i++){
			System.out.println(tabla.elementAt(i).token+"	"+tabla.elementAt(i).getCode());
		}
	}
	public Vector<Tokens> getVectorTokens(){
		return tabla;
	}
	
	public boolean isCorrect()
	{
		return Correcto;
	}
	
	public void asignaCode(){
		for(int i=0;i<tabla.size();i++){
			for(int a=0;a<PR.length;a++){
				if(tabla.elementAt(i).token.equals(PR[a])){//aquí cambié
					tabla.elementAt(i).setCode(a);//Es ppalabra reservada
				}
			}
			for(int b=0;b<caracteresValidos.size();b++){
				if(tabla.elementAt(i).getToken().equals(caracteresValidos.elementAt(b)+""))
					tabla.elementAt(i).setCode(b+9);//Es un simbolo de carcteres validos
			}
			if(tabla.elementAt(i).getToken().equals("=="))
				tabla.elementAt(i).setCode(19); //Es ==
			/*if(tabla.elementAt(i).getTipo().equals("NUME"))
				tabla.elementAt(i).setCode(20); //Es numero */
			if(tabla.elementAt(i).getTipo().equals("ID"))
					tabla.elementAt(i).setCode(21); //Es identificado		
		}
	}
	
	
	//AUMENTA EL APUNTADOR EN UNO PARA LEER EL SIGUIENTE CARACTER
	//TAMBIÉN AUMENTA LA VARIABLE COLUMNA
	public void SiguienteCaracter(){
		ap++;
		columna++;
		if(ap>-1 && ap<linea.length())
			caracter = linea.charAt(ap);
		else{
			ap = -1;
			caracter = '\n';
		}
	}

	//REVISA SI SE ENCONTRO UNA PALABRA RESERVADA
	public boolean PalabraReservada(){
		int cont = 0;
		do{
			if(token.equals(PR[cont])) //PARA QUE LAS PALABRAS RESERVADAS ESTEN ESCRITAS TAL CUAL
				return true;
			
			cont++;
		}while(cont<PR.length);
		return false;
	}

}

//CLASE DE TOKENS PARA CREAR OBJETOS QUE TENGAN DOS ATRIBUTOS, TOKEN Y TIPO
class Tokens{
	
	String token;
	String tipo;
	int code;
	
	public Tokens(String t, String tp){
		token = t;
		tipo = tp;
	
	}
	public Tokens(String t, String tp, int n){
		token = t;
		tipo = tp;
		code=n;
	
	}
	
	public void setCode(int n){
		code=n;
	}
	public int getCode(){
		return code;
	}
	public String getToken(){
		return token;
	}
	public String getTipo(){
		return tipo;
	}
	public boolean esInicio(){
		boolean res=false;
		if (token=="INICIO")
			res=true;
		return res;
	}
	public boolean esFin(){
		boolean res=false;
		if (token=="FIN")
			res=true;
		return res;
	}
}
