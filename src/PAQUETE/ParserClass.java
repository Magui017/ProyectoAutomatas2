package PAQUETE;

import java.util.Vector;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class ParserClass {
	
	int index;
	Vector <Tokens> tokens;
	Vector <Error> errors;
	
	Vector <VarDeclaration> VarDec; //VECTOR PARA GUARDAR DECLARACIONES DE VARIABLES
	Vector <Statement> States; // VECTOR PARA UARDAR STATEMENTS
	Vector <Cuadruple> Cuadruples;
	Vector <Cuadruple> Wails;
	//VECTORES PARA ALMACENAR LA INFORMACION DE LAS VARIABLES DECLARADAS
	
	Vector <String> ID;
	Vector <String> Tipo;
	
	static Tokens variable; 
	static String Estring;
	
	static int Cont=0;
	static int Jumps=0;
	static int saltos=0;
	
	static int inutil;
	static int basura;
	
	
	int contError;
	int lines;
	Tokens currentToken;
  	String res="";
  	String ressem="";
  	
  	boolean Correcto=true;
  	
  	//Constructor del parser. Inicializ variables  y llama al método que hace el trabajo
 	public ParserClass(Vector <Tokens> tokens){
 		this.tokens=tokens;
 		index=0;//inicia en el token de la posición 0
 		nextToken();
 		variable = new Tokens("","INVALIDO",-1);
 		errors= new Vector <Error>();
 		VarDec= new Vector <VarDeclaration>();
 		States= new Vector <Statement>();
 		Cuadruples= new Vector <Cuadruple>();
 		Wails= new Vector <Cuadruple>();
 		
 		Tipo = new Vector <String>();
 		ID = new Vector <String>();
 		
 		lines=0;
 		Program();
 	}
 	
 	//GRAMATICA
 	/*
 	Program -> VarDeclaration* Statement* <EOF>
 	VarDeclaration -> ("int | boolean") Identifier ";" 
 	Statement -> Identifier "=" Expresion";"
 	Statement -> "if" "(" Expresion ")" Statement 
 	Statement -> "while "(" Expresion ")" Statement
 	Expresion -> Identifier ("=="|"+") Identifier | Identifier 
 	Identifier -> letra (letra | digito )*
 	*/
 	
 	public void Program(){
 		
 		//IDENTIFICADOR, IF Y WHILE
 		if(currentToken.getCode()==21 || currentToken.getCode()==0 || currentToken.getCode()==1){}
 		else{
 			VarDeclaration();
 		}
 		
 		while(currentToken.getCode()==21||currentToken.getCode()==0||currentToken.getCode()==1)
 		{
 			if(Statements()){
 				nextToken();
 			}
 			else
 				errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Statement mal declarado"));
 		}
 		
 		//HAY CODIGO BASURA 
 		if(currentToken.getCode()==2|| currentToken.getCode()==3||currentToken.getCode()==12 
 				|| currentToken.getCode()==10 || currentToken.getCode()==11 || currentToken.getCode()==16||currentToken.getCode()==17||currentToken.getCode()==18    
 				|| currentToken.getCode()==19 || currentToken.getCode()==9 || currentToken.getCode()==11 )
 		{
 			System.out.println("");
 			errors.addElement(new Error(errors.size()+1,"Codigo Invalido","Hay codigo basura"));
 			Correcto=false;
 		}
 		
 		System.out.println("");
 		System.out.println("ARBOL DE DECLARACIÓN DE VARIABLES");
 		System.out.println("");
 		
 		for(int i=0;i<VarDec.size();i++){
 		    System.out.println(VarDec.elementAt(i).getType()+"    "+VarDec.elementAt(i).getId());
 		 }
 	
 		System.out.println("");
 		System.out.println("ARBOL DE ESTATUTOS");
 		System.out.println("");
 		
 		//SIEMPRE EL ULTIMO STATEMENT QUE AGREGO AL VECTOR SE IMPRIME PRIMERO, PERO TODOS LOS DEMAS SI SE IMPRIMEN EN EL ORDEN
 		//GOTTA CHECK THIS
 		if(errors.size()==0){
 			
 			/*for(int i=States.size()-1; i>=0; i--){
	 		    System.out.println(States.elementAt(i).toString());
	 		}*/
	 		
 			System.out.println("");
	 		for(int i=0; i<States.size(); i++){
	 		    System.out.println(States.elementAt(i).toString());
	 		 }
	 	
 		}
 		else
 			System.out.println("El progrma tiene errores, por lo tanto no se genera arbol sintactico de Statements");
 		
 		System.out.println("");
 		printRes();
 		System.out.println(res);
 		String Cuadruple="";
 		
 		if(Semantica())
 		{
 			ressem=ressem+"El programa es correcto\n No hay errores de Semántica";
 			CodigoIntermedio();
 			
 			Cuadruples.addElement(new Cuadruple((Cuadruples.size()+1),"","","",""));
 			
 			int posi;	
 			//CHECAR LOS JUMPS 
 			System.out.println("");
 			System.out.println("\tCÓDIGO INTERMEDIO");
 			System.out.println("\tCUADRUPLES-------");
 			System.out.println("\t-----------------");
 			for(int i=Cuadruples.size()-1;i>=0;i--)
 			{
 				if(Cuadruples.elementAt(i).COJ.equals("Jump"))
 				{
 					if(i>0 && Cuadruples.elementAt(i-1).COJ.equals("Jump"))
 					{
 						for(int j=0; j<Cuadruples.size();j++)
 						{
 							if(Cuadruples.elementAt(j).COJ.equals("JZ") && Cuadruples.elementAt(j).RS.equals("&"))
 							{
	 								if(j>0 && Cuadruples.elementAt(j-1).COJ.equals("CMP"))
	 								{
	 									Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j-1).Posicion);
	 									Cuadruples.elementAt(j).setRS(""+Cuadruples.elementAt(i+1).Posicion);	 		
	 									break;
	 								}
	 								else
	 								{
	 									Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j).Posicion);
	 									Cuadruples.elementAt(j).setRS(""+Cuadruples.elementAt(i+1).Posicion);
	 									break;
	 								}
 							}
 						} 						
 					}
 					else
 					{
 					
 						for(int j=i; j>0;j--)
 						{
 							if(Cuadruples.elementAt(j).COJ.equals("JZ") && Cuadruples.elementAt(j).RS.equals("&"))
 							{
 								if(Cuadruples.elementAt(j-1).COJ.equals("CMP"))
 								{
 									Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j-1).Posicion);
 									Cuadruples.elementAt(j).setRS(""+Cuadruples.elementAt(i+1).Posicion);
 									break;
 								}
 								else
 								{
 									Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j).Posicion);
 									Cuadruples.elementAt(j).setRS(""+Cuadruples.elementAt(i+1).Posicion);
 									break;
 								}
 							}
 						} 					 					
 					}

 				}
 			}
 			
 			
 			for(int i=0; i<Cuadruples.size(); i++)
 			{
 				//Cuadruple=Cuadruple+Cuadruples.elementAt(i).toString()+"\n"; 				
 			/*	if(Cuadruples.elementAt(i).COJ.equals("Jump"))
 				{
 					for(int j=i;j>0;j--)
 					{
 						if(Cuadruples.elementAt(j).RS.equals("&") && Cuadruples.elementAt(j).COJ.equals("JZ") && Cuadruples.elementAt(j-1).COJ.equals("CMP") )
 						{
 								Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j-1).Posicion); //quite -1
 								//System.out.println(Cuadruples.elementAt(j).Posicion); 
 								break;
 						}
 						else if(Cuadruples.elementAt(j).RS.equals("&") && Cuadruples.elementAt(j).COJ.equals("JZ") && !Cuadruples.elementAt(j-1).COJ.equals("CMP") )
 						{
 							Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j).Posicion);
							break;
 						}
 					}
 					
 				} */
 				
 				/*
 				else if(Cuadruples.elementAt(i).COJ.equals("JZ") && Cuadruples.elementAt(i).RS.equals("&"))
 				{	
 					//GOTTA CHECK THIS ASAP
 					for(int j=i;j<Cuadruples.size();j++)
 					{
 						if(Cuadruples.elementAt(j).COJ.equals("="))
 						{
 							for(int k=j;k<Cuadruples.size();k++)
 							{
 								if(Cuadruples.elementAt(k+1).COJ.equals("Jump"))
 								{
 									//System.out.println("ESTA");
 									saltos++;
 								}
 								else
 									break;
 							}
 							//System.out.println(Jumps);
 							Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j+1).Posicion);
 							break;
 						}
 						else
 						{
 							Cont++;
 						}
 					}
 					
 				}*/
 				// ----------------------------------------IF
 			
 				// ESTO SI FUNCIONA LE DEBES QUITAR EL COMENTARIO
  /* else if */if(Cuadruples.elementAt(i).COJ.equals("JZ") && Cuadruples.elementAt(i).RS.equals("#"))
 				{
 					for(int j=i;j<Cuadruples.size();j++)
 					{
 						if(Cuadruples.elementAt(j).COJ.equals("="))
 						{
 							for(int k=j;k<Cuadruples.size();k++)
 							{
 								if(Cuadruples.elementAt(k+1).COJ.equals("Jump"))
 								{
 									//System.out.println("ESTA");
 									//System.out.println("encontre 1 jump");
 									Jumps++;
 								}
 								else
 									break;
 							}
 							//System.out.println(Jumps);

 							Cuadruples.elementAt(i).setRS(""+Cuadruples.elementAt(j+1+Jumps).Posicion);
 							break;
 						}
 						else
 						{
 							//Cont++; ESTO NO LO OCUPO AL PARECER
 						}
 					}
 				}
  				Jumps=0;
 			
 				System.out.println(Cuadruples.elementAt(i).toString());
 			} 				
 		}
 		else{
 			ressem=ressem+"\nEl programa tiene errores de semántica";
 		}
 		
 		
 	}
 	
 	//----------------------ANALIZADOR SEMANTICO
 	
	public boolean Semantica()
	{
		boolean Semantica=true;
		String Var="";
		String Var2="";
		String Taip;
		String Aid;
	
		//Pregunto para saber si el analisis sintactico fue correcto
 		if(res=="El programa es correcto ")
 		{
 			//IMPRIMO LA TABLA DE SIMBOLOS
 			for(int i=0;i<VarDec.size();i++){
 				Interfaz.TabladeSimbolos=Interfaz.TabladeSimbolos+VarDec.elementAt(i).getId()+"\t"+VarDec.elementAt(i).getType()+"\n";
 			}
 			
 			//COMIENZO CON EL ANALISIS SEMANTICO
 			
 			//VER SI HAY VARIABLES CON EL MISMO NOMBRE REPETIDAS
 		//	System.out.println("TAMAÑO "+VarDec.size());
 			System.out.println("");
 			
 			for(int i=0; i<VarDec.size();i++)
 			{
 				Var=VarDec.elementAt(i).getId();
 				for(int j=i+1; j<VarDec.size();j++)
 				{
 					Var2=VarDec.elementAt(j).getId(); 					//CUANDO ASIGO AQUI LA POSICION J POR ALGUNA RAZON SE IMPRIME DOS VECES
 					if(Var.equals(Var2))
 					{
 						ressem="Error de Semántica -\n La variable "+Var+" esta declarada más de una vez";
 						return Semantica=false;
 					}
 				}
 			}
 			
 			//COMIENZO CON EL ANALISIS SEMANTICO
 			if(VarDec.isEmpty() && !States.isEmpty())
 			{
 				ressem="Error - El programa no tiene variables declaradas";
 				return false;
 			}
 			
 			//------------COMENZAR A CHECAR LOS ESTEITMENS
 			
 			//Variables Auxiliares 
 			Statement Actual;
 			String Clasif;
 			String ClasifExp;
 			
 			Expresion Xpre;
 			If perro;
 			While gato;
 			
 			String id;
 			String id2;
 			String id3;
 			
 			//FOR PARA RECORRER TODO EL VECTOR DE STATEMENTS 
 			//----------------------------CHECAR QUE LAS VARIABLES ESTEN DECLARADAS 
 			for(int i=0;i<States.size();i++)
 			{
 				// Clasificacion A = Asignacion
 				// Clasificacion B = if
 				// Clasificacion C = while
 				
 				Clasif = States.elementAt(i).getClasificacion();
 				//Actual = States.elementAt(i);
 			
 				//Asignación
 				if(Clasif.equals("A"))
 				{
 					id=States.elementAt(i).getId().getToken(); //Le asigno el identificador que va antes de la asignacion 

 					int j=0;
 					//CHECO SI EL ID ESTA DECLARADO
 					while(j<VarDec.size())
 					{
 						System.out.println(VarDec.elementAt(j).getId());
 						String Aux=VarDec.elementAt(j).getId();
 						
 						if(id.equals(Aux))
 						{
 							j=VarDec.size();
 						}
 						else if(j==VarDec.size()-1 && !id.equals(Aux))
 						{
 							ressem="Error de Semántica -\n La variable "+id+" NO esta declarada";
 							return Semantica = false;
 						}
 						else
 							j++;
 					}	

 					Xpre=States.elementAt(i).getExpr();
 					ClasifExp=Xpre.getClasificacion();

 					//DEPENDIENDO DEL TIPOD DE EXPRESION CHECO SI EXISTEN LOS ID EN EL VECTOR DE DECLARACIONES
 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();
	
 						id3=Xpre.getId2();

 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 						
 						int l=0;
 						while(l<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(l).getId();
	 						
	 						if(id3.equals(Aux))
	 						{
	 							l=VarDec.size();
	 						}
	 						else if(l==VarDec.size()-1 && !id3.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id3+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							l++;
 	 					} 				
  					}
 					else if(ClasifExp.equals("b"))
 					{
 						id2=Xpre.getId();
 						
 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 					}
 				}
 				//if
 				
 				else if(Clasif.equals("B"))
 				{

 					perro=(If)States.elementAt(i);
 					Xpre=perro.getExpresion();
 				
 					ClasifExp=Xpre.getClasificacion();

 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();

 						id3=Xpre.getId2();
 						
 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 						
 						int l=0;
 						while(l<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(l).getId();
	 						
	 						if(id3.equals(Aux))
	 						{
	 							l=VarDec.size();
	 						}
	 						else if(l==VarDec.size()-1 && !id3.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id3+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							l++;
 	 					} 				
  					}
 					else if(ClasifExp.equals("b"))
 					{
 						id2=Xpre.getId();
 						System.out.println(id2);
 						
 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 					}
 					
 				}
 				//while
 				else
 				{
 					gato=(While)States.elementAt(i);
 					Xpre=gato.getExpresion();
 				
 					ClasifExp=Xpre.getClasificacion();

 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();

 						id3=Xpre.getId2();
	
 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 						
 						int l=0;
 						while(l<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(l).getId();
	 						
	 						if(id3.equals(Aux))
	 						{

	 							l=VarDec.size();
	 						}
	 						else if(l==VarDec.size()-1 && !id3.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id3+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							l++;
 	 					} 				
  					}
 					else if(ClasifExp.equals("b"))
 					{
 						id2=Xpre.getId();
 						System.out.println(id2);
 						
 						int k=0;
 						while(k<VarDec.size())
 	 					{
	 						String Aux=VarDec.elementAt(k).getId();
	 						
	 						if(id2.equals(Aux))
	 						{
	 							System.out.println("Si esta declarada la variable: "+id2);
	 							k=VarDec.size();
	 						}
	 						else if(k==VarDec.size()-1 && !id2.equals(Aux))
	 						{
	 							ressem="Error de Semántica -\n La variable "+id2+" NO esta declarada";
	 							return Semantica = false;
	 						}
	 						else
	 							k++;
 	 					}
 					}
 				}

 			} 
 			
 			//CHECAR LA COMPATIBILIDAD DE TIPOS EN LAS EXPRESIONES
 			
 			//Variables auxjiliares
 			String Typo ="";
 			String Typo2="";
 			String Typo3="";
 			
 			String Op="";
 			
 			for(int i=0;i<States.size();i++)
 			{
 				
 				Clasif = States.elementAt(i).getClasificacion();
 			
 				//------------------------------------------------------------ASIGNACIÓN
 				if(Clasif.equals("A"))
 				{
 					id=States.elementAt(i).getId().getToken(); //Le asigno el identificador que va antes de la asignacion 
 					
 					//OCUPO OBTENER EL TIPO DE DATO DEL IDENTIFICADOR
 					
 					for(int j=0; j<VarDec.size();j++)
 		 			{
 		 				Var=VarDec.elementAt(j).getId();
 		 				if(Var.equals(id))
 		 				{
 		 					Typo=VarDec.elementAt(j).getType();
 		 				}
 		 			}
 					
 					Xpre=States.elementAt(i).getExpr();
 					ClasifExp=Xpre.getClasificacion();
 					
 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();
 						//CHECAR EL TIPO DEL PRIMER IDENTIFICADOR DE LA EXPRESION
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id2))
 	 		 				{
 	 		 					Typo2=VarDec.elementAt(j).getType();
 	 		 					
 	 		 				}
 	 		 			}
 						
 						//CHECAR EL TIPO DEL SEGUNDO IDENTIFICADOR DE LA EXPRESION
 						id3=Xpre.getId2();
 						
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id3))
 	 		 				{
 	 		 					Typo3=VarDec.elementAt(j).getType();

 	 		 				}
 	 		 			}

 						Op=Xpre.getOperador();

 					
 						//FUNCIONAN 
 						// BOOLEAN = BOOLEAN == BOOLEAN
 						// BOOLEAN = INT == INT 
 						// INT = INT + INT 
 						
 	
 						if(Typo.equals("boolean"))
 						{ 							
 							if(Typo2.equals("int")&&Typo3.equals("boolean")&&Op.equals("*"))
							{
								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
	 									" y una variable de tipo "+Typo3+" ("+id3+")");
								return Semantica=false;
							}
							else if(Typo2.equals("boolean")&&Typo3.equals("int")&&Op.equals("*"))
							{
								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
	 									" y una variable de tipo "+Typo3+" ("+id3+")");
								return Semantica=false;
							}	
							else if(Typo2.equals("boolean")&&Typo3.equals("boolean")&&Op.equals("*"))
							{
								ressem=("Error de Semantica \n No se puede realizar una suma entre dos variables\n de tipo boolean");
								return Semantica=false;
							}
							else if(Typo2.equals("int")&&Typo3.equals("int")&& Op.equals("*"))
							{
								ressem=("Error de Semantica \nNo se puede asignar una suma de dos variables tipo int \n a una variable tipo boolean ");	
								return Semantica=false;
							}
							else if(Typo2.equals("int")&&Typo3.equals("boolean")&&Op.equals("+"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
 	 									" y una variable de tipo "+Typo3+" ("+id3+")");
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&&Op.equals("+"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
 	 									" y una variable de tipo "+Typo3+" ("+id3+")");
 								return Semantica=false;
 							}	
 							else if(Typo2.equals("boolean")&&Typo3.equals("boolean")&&Op.equals("+"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma entre dos variables\n de tipo boolean");
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("int")&& Op.equals("+"))
 							{
 								ressem=("Error de Semantica \nNo se puede asignar una suma de dos variables tipo int \n a una variable tipo boolean ");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("boolean")&&Op.equals("-"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
 	 									" y una variable de tipo "+Typo3+" ("+id3+")");
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&&Op.equals("-"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma o comparación entre una variable de tipo "+Typo2+" ("+id2+") \n"+
 	 									" y una variable de tipo "+Typo3+" ("+id3+")");
 								return Semantica=false;
 							}	
 							else if(Typo2.equals("boolean")&&Typo3.equals("boolean")&&Op.equals("-"))
 							{
 								ressem=("Error de Semantica \n No se puede realizar una suma entre dos variables\n de tipo boolean");
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("int")&& Op.equals("-"))
 							{
 								ressem=("Error de Semantica \nNo se puede asignar una suma de dos variables tipo int \n a una variable tipo boolean ");	
 								return Semantica=false;
 							}
 
 							else if(Typo2.equals("int")&&Typo3.equals("boolean") && Op.equals("=="))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una comparacion entre uan variable \n de tipo int y una variable de tipo boolean");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&& Op.equals("=="))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una comparacion entre uan variable \n de tipo boolean y una variable de tipo int");	
 								return Semantica=false;
 							}	
 						}
 						// FUNCIONAN
 						// INT = INT + INT 
 						else if(Typo.equals("int"))
 						{
 							if((Typo2.equals("boolean") && Typo3.equals("boolean")) && Op.equals("*"))
 							{
 								ressem=("Error de Semantica \nNo se puede realizar una suma entre dos variables\n de tipo boolean");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&& Op.equals("*"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo boolean y una variable de tipo int");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("boolean")&& Op.equals("*"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo int y una variable de tipo boolean");	
 								return Semantica=false;
 							}
 							else if((Typo2.equals("boolean") && Typo3.equals("boolean")) && Op.equals("+"))
 							{
 								ressem=("Error de Semantica \nNo se puede realizar una suma entre dos variables\n de tipo boolean");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&& Op.equals("+"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo boolean y una variable de tipo int");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("boolean")&& Op.equals("+"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo int y una variable de tipo boolean");	
 								return Semantica=false;
 							}
 							else if((Typo2.equals("boolean") && Typo3.equals("boolean")) && Op.equals("-"))
 							{
 								ressem=("Error de Semantica \nNo se puede realizar una suma entre dos variables\n de tipo boolean");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&& Op.equals("-"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo boolean y una variable de tipo int");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("int")&&Typo3.equals("boolean")&& Op.equals("-"))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una suma entre una variable \n de tipo int y una variable de tipo boolean");	
 								return Semantica=false;
 							}

 							else if(Typo2.equals("int")&&Typo3.equals("boolean") && Op.equals("=="))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una comparacion entre uan variable \n de tipo int y una variable de tipo boolean");	
 								return Semantica=false;
 							}
 							else if(Typo2.equals("boolean")&&Typo3.equals("int")&& Op.equals("=="))
 							{
 								ressem=("Error de Semantica \nNo se puede hacer una comparacion entre uan variable \n de tipo boolean y una variable de tipo int");	
 								return Semantica=false;
 							}
 							
 							else if(Typo2.equals("int")&&Typo3.equals("int")&& Op.equals("=="))
 							{
 								ressem=("Error de Semantica \nNo se puede asignar un resultado boolean a una variable \n de tipo int");	
 								return Semantica=false;
 							}
 						}	
 					}
 					else if(ClasifExp.equals("b"))
 					{
 						//CHECAR EL TIPO DEL IDENTIFICADOR DE LA EXPRESION 
 						id2=Xpre.getId();
 					
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id2))
 	 		 				{
 	 		 					Typo2=VarDec.elementAt(j).getType();
 	 		 				}
 	 		 			}
 						
 						//CHECAR COMPATIBILIDAD DE TIPOS ENTRE EL ID Y EL ID2
 						if(!Typo.equals(Typo2))
 						{
 							ressem=("Error de Semantica \n Incompatibilidad de Tipos entre las variables "+id+" de tipo "+Typo+"\n"+
 									" y la variable "+id2+" de tipo "+Typo2);
 							return Semantica=false;
 						}
 					}
 				}
 				//---------------------------------------------------if
 				else if(Clasif.equals("B"))
 				{
 					perro=(If)States.elementAt(i);
 					Xpre=perro.getExpresion();
 				
 					ClasifExp=Xpre.getClasificacion();
 					
 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();
 						
 						id3=Xpre.getId2();
 						
 						Op=Xpre.getOperador();

 						//CHECAR EL TIPO DE DATO DEL PRIMER IDENTIFICADOR DE LA EXPRESION
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id2))
 	 		 				{
 	 		 					Typo2=VarDec.elementAt(j).getType();
 	 		 				}
 	 		 			}	
 						
 						//CHECAR EL TIPO DE DATO DEL SEGUNDO IDENTIFICADOR DE LA EXPRESION
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id3))
 	 		 				{
 	 		 					Typo3=VarDec.elementAt(j).getType();
 	 		 				
 	 		 				}
 	 		 			}	
 						
 						if(Op.equals("+")||Op.equals("-")||Op.equals("*"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro de una condición If";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 						if(Op.equals("*"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro de una condición If";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 						if(Op.equals("-"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro de una condición If";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 					}
 					else if(ClasifExp.equals("b"))
 					{
 						id=Xpre.getId();
 			
 						//Checar el tipo
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id))
 	 		 				{
 	 		 					Typo=VarDec.elementAt(j).getType();
 	 		 				}
 	 		 			}
 						
 						if(Typo.equals("int"))
 						{
 							ressem="Error de Semantica \n No puede realizarse la condición booleana con \nuna variable de tipo int ";
 							return Semantica = false;
 						}
 					}
 				}
 				//-------------------------------------------------------------------while
 				else
 				{
 					gato=(While)States.elementAt(i);
 					Xpre=gato.getExpresion();
 				
 					ClasifExp=Xpre.getClasificacion();

 					
 					if(ClasifExp.equals("a"))
 					{
 						id2=Xpre.getId();
 						id3=Xpre.getId2();	
 						Op=Xpre.getOperador();

 						
 						//CHECAR EL TIPO DE DATO DEL PRIMER IDENTIFICADOR DE LA EXPRESION
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id2))
 	 		 				{
 	 		 					Typo2=VarDec.elementAt(j).getType();
 	 		 					
 	 		 				}
 	 		 			}	
 			
 						//CHECAR EL TIPO DE DATO DEL SEGUNDO IDENTIFICADOR DE LA EXPRESION
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id3))
 	 		 				{
 	 		 					Typo3=VarDec.elementAt(j).getType();
 
 	 		 				}
 	 		 			}	

 						if(Op.equals("+")||Op.equals("-")||Op.equals("*"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro como parametro del ciclo While ";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 						if(Op.equals("*"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro como parametro del ciclo While ";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 						if(Op.equals("-"))
 						{
 							ressem="Error de Semantica \n No puede haber una suma dentro como parametro del ciclo While ";
 							return Semantica = false;
 						}
 						else if(Op.equals("=="))
 						{
 							if(Typo2.equals("int") && Typo3.equals("boolean"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 							else if(Typo2.equals("boolean") && Typo3.equals("int"))
 							{
 								ressem="Error de Semantica \n No se puede realizar una comparacion entre \n variables de tipo int y boolean ";
 	 							return Semantica = false;
 							}
 						}
 					}
 					else if(ClasifExp.equals("b"))
 					{
 						id=Xpre.getId();
 						
 						//Checar el tipo
 						for(int j=0; j<VarDec.size();j++)
 	 		 			{
 	 		 				Var=VarDec.elementAt(j).getId();
 	 		 				if(Var.equals(id))
 	 		 				{
 	 		 					Typo=VarDec.elementAt(j).getType();

 	 		 				}
 	 		 			}
 						
 						if(Typo.equals("int"))
 						{
 							ressem="Error de Semantica \n No puede realizarse un ciclo while con una \n variable int como condición";
 							return Semantica = false;
 						}
 					}
 				}
 			}
 		}
 		else	
 		{
 			ressem="El programa tiene errores de Sintaxis "
 					+"\n"+"por lo que no se realiza el Análisis Semántico";
 			Semantica=false;
 		}		
		return Semantica;
	}

	
	public void CodigoIntermedio()
	{
		String Tipo="";
		String Clasif;
		String ClasifExp;
		String AuxClas;
		Expresion Xpre;
		If perro;
		While gato;
		
		int m=1;
		int l=1;
		int Utilidad=0;	
		
		
		for(int i=0;i<States.size();i++)
		{
			Clasif = States.elementAt(i).getClasificacion(); //OBTENGO QUE TIPO DE STATEMENT ES
			String salto = "";

			switch (Clasif)
			{
				//NORMAL ----  Identifier = Expresion 
				//			   Expresion:  Identificador (==|+) Identificador | Identificador 
				case "A":
							Xpre=States.elementAt(i).getExpr();
							ClasifExp=Xpre.getClasificacion(); //QUE TIPO DE EXPRESION OBTENGO
				
							//ESTE CASO GENERA 2 LINEAS DE CUADRUPLES
							if(ClasifExp.equals("a"))
							{
								if(Xpre.getOperador().equals("=="))
								{
									//System.out.println((l)+" CMP\t"+Xpre.getId()+"\t"+Xpre.getId2()+"\t t"+(m));
									Cuadruples.add(new Cuadruple(l,"CMP",Xpre.getId(),Xpre.getId2(),"t"+m));
								}else if(Xpre.getOperador().equals("-"))
								{	//System.out.println((l)+" +\t"+Xpre.getId()+"\t"+Xpre.getId2()+"\t t"+(m));
									Cuadruples.add(new Cuadruple(l,"-",Xpre.getId(),Xpre.getId2(),"t"+m));
								}	
								else if(Xpre.getOperador().equals("*"))
								{	//System.out.println((l)+" +\t"+Xpre.getId()+"\t"+Xpre.getId2()+"\t t"+(m));
									Cuadruples.add(new Cuadruple(l,"*",Xpre.getId(),Xpre.getId2(),"t"+m));
								}
								else 
								{	//System.out.println((l)+" +\t"+Xpre.getId()+"\t"+Xpre.getId2()+"\t t"+(m));
									Cuadruples.add(new Cuadruple(l,"+",Xpre.getId(),Xpre.getId2(),"t"+m));
								}
								
								
								
								
								//System.out.println((++l)+" =\tt"+(m++)+"\t\t "+States.elementAt(i).getId().token);
								Cuadruples.add(new Cuadruple(++l,"=","t"+(m++),"",States.elementAt(i).getId().token));
								
								if(i>0){
									Utilidad=i-1;
									
									AuxClas=States.elementAt(Utilidad).getClasificacion();	
									
									System.out.println("                        "+AuxClas);
									while(AuxClas.equals("C")&&Utilidad>=0){
										
										if(Utilidad>=0)
										{
											//System.out.println("AQUI CREE UN JUMP LOLX2");
											Cuadruples.add(new Cuadruple(++l,"Jump","","","$"));
											
											--Utilidad;
											if(Utilidad!=-1)
												AuxClas=States.elementAt(Utilidad).getClasificacion();
											
											//System.out.println(AuxClas);
											//System.out.println(Utilidad);
										}
										else{
											Utilidad--;
										}
									}
								}			
								
								m++;
							}
							// b
							//ESTE CASO GENERA 1 LINEA DE CUADRUPLES
							else {
								//System.out.println((l)+" =\t"+Xpre.getId()+"\t\t"+States.elementAt(i).getId().token);	
								Cuadruples.add(new Cuadruple(l,"=",Xpre.getId(),"",States.elementAt(i).getId().token));

								if(i>0){
									Utilidad=i-1;
									
									AuxClas=States.elementAt(Utilidad).getClasificacion();	
									
									System.out.println("                        "+AuxClas);
									while(AuxClas.equals("C")&&Utilidad>=0){
										
										if(Utilidad>=0)
										{
										//	System.out.println("AQUI CREE UN JUMP LOLX2");
											Cuadruples.add(new Cuadruple(++l,"Jump","","","$"));
											
											--Utilidad;
											if(Utilidad!=-1)
												AuxClas=States.elementAt(Utilidad).getClasificacion();
											
										//	System.out.println(AuxClas);
											//System.out.println(Utilidad);
										}
										else{
											Utilidad--;
										}
									}
								}					
							}
							l++;
						//	salto.replace('#', (char)m);
							
					break;	
				case "B":
						perro=(If)States.elementAt(i);
						Xpre=perro.getExpresion();
						ClasifExp=Xpre.getClasificacion(); 

						//if(identificador==identificador)

						//ESTE CASO DE IF GENERA DOS CUADRUPLES
						if(ClasifExp.equals("a"))
						{
							Cuadruples.add(new Cuadruple(l,"CMP",Xpre.getId(),Xpre.getId2(),"t"+m));
							Cuadruples.add(new Cuadruple(++l,"JZ","t"+m,"","#"));
							m++;

						}
						//b
						//if(identificador)
						else
						{					
							Cuadruples.add(new Cuadruple(l,"JZ",Xpre.getId(),"","#"));
						}
						l++;				
					break; 
				//WHILE	
				case "C":
					
						gato=(While)States.elementAt(i);
						Xpre=gato.getExpresion();
						ClasifExp=Xpre.getClasificacion(); 
					
						//while(identificador==identificador)
						if(ClasifExp.equals("a"))
						{
							//while(a==b)
							Cuadruples.add(new Cuadruple(l,"CMP",Xpre.getId(),Xpre.getId2(),"t"+m));
							Cuadruples.add(new Cuadruple(++l,"JZ","t"+m,"","&"));
							m++;
						}
						//b
						//while(identificador)
						else
						{
							Cuadruples.add(new Cuadruple(l,"JZ",Xpre.getId(),"","&"));
						}
						l++;
						break;		
			}	
		} 		
	}

	
	//------------------------------ANALIZADOR SINTACTICO
	
 	public boolean VarDeclaration()
 	{
 		boolean correcto = false;
 		
 		//while(currentToken.getCode()==2 || currentToken.getCode()==3)
		while(tipo_dato())	
 		{
					VarDec.add(new VarDeclaration(tipoDato(),ID()));
					
					if(VarDec.lastElement().getType().equals("")||VarDec.lastElement().getId().equals(""))
						VarDec.remove(VarDec.lastElement());
					
					//pregunta por ;
					if(currentToken.getCode()==12)
					{
						Correcto=true;
						nextToken();
					}
					else
					{
						//VarDec.remove(VarDec.lastElement());
						Correcto=false;
						
						if(errors.size()==0)
						{
							errors.addElement(new Error(errors.size()+1,"Signo especial","Falta punto y coma"));
						}
						
						return false;
					}	
			}
 		
 		return correcto;
 	}
 	
 	public boolean Statements()
 	{
 		boolean correcto = false;
 		int caracter;
 		String Clasificacion;
 		caracter=currentToken.getCode();
 
 		Statement State= new Statement();
 		
 		switch(caracter)
 		{
 			//caso identificadores 
 			//indetifier=expresion
 			case 21:
 			{
 				Tokens iden= currentToken;
 				nextToken();
 				//si el token actual es un =
 				if(currentToken.getCode()==9)
 				{
 					//checar la expresion
 					Tokens op = currentToken;
 					nextToken(); //No estaba consumiendo el = 
 					Expresion expr= Expresion();
 					
 					if(expr.isExpresion()){
 						if(currentToken.getCode()==12) //preguntar por punto y coma
 						{	
 							//nextToken();
 							//-----------------------------ES UN STATEMENT DE TIPO:
 							//-----------------------------identifier = (Expresion)
 							Clasificacion="A";
 							State=new Statement(iden,op,expr,Clasificacion);
 							States.addElement(State);
 							//return correcto=true;
 							return correcto =true;
 						}	
 						else
 						{
 							errors.addElement(new Error(errors.size()+1,"","Falta Punto y Coma"));
 							return correcto=false;
 						}
 					}
 					else{
 	 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACIÓN mal declara 2"));
 	 	 				return false;
 					}
 				}
 				else
 				{
 					errors.addElement(new Error(errors.size()+1,"Statement 1","ASIGNACIÓN mal declara 1"));
 					return  false;
 				}
 				
 			}
 			//caso if
 			case 0:
 			{
 				Tokens condicion=currentToken;
 				nextToken();
 					//pregunta si sigue un (
 					if(currentToken.getCode()==10)
 					{
 						nextToken(); //Consumir el (
 						Expresion expr = Expresion(); //checar expresion						
 						
 						if(expr.isExpresion())
 						{
 							nextToken();
 							
 							//-----------------------------ES UN STATEMENT DE TIPO:
 							//-----------------------------if (Expresion)
 							Clasificacion="B";
 							States.add(new If(condicion,expr,Clasificacion));
 							
 							if(Statements())
 							{	
 								//States.add(new If(condicion,expr,State));
 	 							correcto=true;
 	 							return correcto;
 							}
 	 						else{
 	 							States.remove(States.lastElement());
 	 							System.out.println(currentToken.getCode());  
 								errors.addElement(new Error(errors.size()+1,"Statement 2","IF al declarado Caso 3"));
 								return correcto;
 							}
 			
 						}
 						else
 						{
 							System.out.println(currentToken.getCode());
 							errors.addElement(new Error(errors.size()+1,"Statement 2","IF al declarado Caso 2"));
 							return false;
 						}	
 					}
 					else
 					{
 						errors.addElement(new Error(errors.size()+1,"Statement 2","IF mal declarado Caso 1"));
 						return false;
 					} //tenia un break en lugar de esto
 			}
 			//caso while
 			case 1:
 			{
 				Tokens wail=currentToken;
 				nextToken();
 					//pregunta si sigue un (
 					if(currentToken.getCode()==10)
 					{
 						//checar expresion
 						nextToken(); //no estaba consumiendo el (
 						
 						Expresion expr = Expresion(); 						
 						//Antes preguntaba por Expresion() el método
 						if(expr.isExpresion())
 						{
 							nextToken();
 								
 							//-----------------------------ES UN STATEMENT DE TIPO:
 							//-----------------------------while (Expresion)
 							Clasificacion="C";
 							States.add(new While(wail,expr,Clasificacion));
 							
 							if(Statements())
 							{	
 								//States.add(new While(wail,expr,State));
 	 							correcto=true;
 	 							return correcto;
 							}
 	 						else
 							{
 	 							States.remove(States.lastElement());
 	 							System.out.println(currentToken.getCode());  
 								errors.addElement(new Error(errors.size()+1,"Statement 3","WHILE al declarado Caso 3"));
 								return correcto;
 							}
 			
 						}
 						else
 						{
 							System.out.println(currentToken.getCode());
 							errors.addElement(new Error(errors.size()+1,"Statement 3","WHILE al declarado Caso 2"));	
 							return false;
 						}	
 					}
 					else
 					{
 						errors.addElement(new Error(errors.size()+1,"Statement 3","WHILE mal declarado Caso 1"));
 						return false;
 					}
 			}
 			default:
 				return correcto;
 		} 				
 		//	return correcto;
		//return State.isStatement();
 	}

 	public Expresion Expresion() {
 		
 		//El caso base de expresion es un token vacio 
 		Expresion Expr=new Expresion(new Tokens("",""),"");
 		
 		String clasificacion;
 		
 		//pregunta por identificador
 		if(currentToken.getCode()==21){
 			
 			Tokens id=currentToken;
 			nextToken();
 			//pregunta por el == o un +
 			if(currentToken.getCode()==19||currentToken.getCode()==16||currentToken.getCode()==17||currentToken.getCode()==18){
 				
 				Tokens op = currentToken;
 				nextToken();
 				//pregunta por identificador 
 				if(currentToken.getCode()==21)
 				{
 					
 					//-----------------------------ES UNA EXPRESION DE TIPO:
 					//-----------------------------identifier =  Identifier (==|+) Identifier
 					clasificacion="a";
 					Expr=new Expresion(id,op,currentToken,clasificacion); //aqui creo la expresion de tipo id(+|==)id
 					nextToken();
 				}
 				else{
 					errors.addElement(new Error(errors.size()+1,"Expresion "+lines," Se espera un id"));
 				}
	 	    }
	 	    else{
	 	    	
	 	    	//-----------------------------ES UNA EXPRESION DE TIPO:
				//-----------------------------identifier
	 	    	clasificacion="b";
	 	     	Expr=new Expresion(id,clasificacion);
	 	     	System.out.println(Expr.toString());
	 	    }
	   }
 		else{
	 	    errors.addElement(new Error(errors.size()+1,"Expresion "+lines," Se espera un id"));
	  }
	  return Expr;
 	}
 	
 	//Actualiza l variable "currentToken" al siguiente token en el vector
 	public void nextToken(){
 		if(index<tokens.size()){//si el índice es menor que el tamaño del vector
 				currentToken=tokens.elementAt(index);
 				index++;
 		}
 		else{
 			currentToken= new Tokens("INVALIDO","INVALIDO",-1);
 		}
 	}
 	 	
 	public Tokens tipoDato(){
 		Tokens td=variable; //variable = new Tokens("INVALIDO","INVALIDO",-1);
 		if (tipo_dato())	
 		{
 			td=currentToken;
 			nextToken();
 		}
 		else
 			errors.addElement(new Error(errors.size()+1,"Expresion "+lines, "Se espera un tipo de dato"));
 		return td;
 	}	
 
 	public Tokens ID(){
 		Tokens id=variable;
 		if(currentToken.getCode()==21){
 			id=currentToken;
 			nextToken();
 		}
 		else
 			errors.addElement(new Error(errors.size()+1,"Declaracion de Variables "+lines, "Se espera un id"));				
 		return id;
 	}
 	
 	public boolean tipo_dato(){
   		if(currentToken.getCode()==2||currentToken.getCode()==3)
    		return true;
   		
   		return false;
  	}
 	
    public void printRes(){
    	if(errors.size()==0){
    		res="El programa es correcto ";
    	}else{
    		Correcto = false;
    		res="Se encontraron "+errors.size()+" errores en el programa"+"\n";
    		for(int i=0;i<errors.size();i++){
    			res+="Error "+errors.elementAt(i).getNum()+": "+errors.elementAt(i).getLocation()+","+errors.elementAt(i).getDesc()+"\n";
    		}
    	}
    }
    
    public boolean isCorrect(){
    	return Correcto;
    }
    
    public String Resultado(){
    	return res;
    }
    
    public String ResultadoSemantico()
    {
    	return ressem;
    }
    
}
class Error{
	int num;
	String location;
	String desc;
	
	public Error(int num, String location, String desc){
		this.num=num;
		this.location=location;
		this.desc=desc;
	}
	
	public int getNum(){
		return num;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getDesc(){
		return desc;
	}
} 