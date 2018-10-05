package PAQUETE;


import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Interfaz extends JFrame implements ActionListener, CaretListener, DocumentListener{
	

	private static final long serialVersionUID = 1L;
	
	JMenuBar Menu;
	JMenu MenuArchivo;
	JMenuItem elementoNuevo;
	JMenuItem elementoAbrir;
	JMenuItem elementoGuardar;
	JMenuItem elementoGuardarComo;
	JMenuItem elementoSalir;
	
	JScrollPane Prog;
	JScrollPane Res;
	JScrollPane RPar;
	
	//NUEVO
	JScrollPane Sem;
	JScrollPane TS;
	
	JTextArea Programa;
	JTextArea Resultado;
	JTextArea ResParser;
	
	//NUEVO
	JTextArea ResSemantico;
	JTextArea TablaSimbolos;
	
	JLabel lbPrograma;
	JLabel lbResultado;
	JLabel lbParser;
	JLabel lbTotal;
	
	//NUEVO
	JLabel lbTablaSimbolos;
	JLabel lbSemantico;

	JButton Calcular;

	boolean guarda = false;
	String NombreDoc;
	
	int ancho;
	int alto;
	
	public static String TokenTipo;
	public static String TabladeSimbolos;
	
	Vector <Tokens> VectorTokens = new Vector <Tokens> (20,1);
	
	//------------->NUEVO FILA Y COLUMNA CURSOR
	JTextField FilaColumna;
	int linea;
	int columna;
	
	AnalizadorVersion3	ObjAnalizador;
	ParserClass ObjParser;
	
	//------------->NUEVO IMPEDIR HACER PARSER SI NO SE HA HECHO EL SCANEO
	boolean Escaneado = false;
	
	public Interfaz(){
		super("Compilador");
		
		Ventana();
		Escuchas();
		
		//LISTENER QUE SE ACTIVA CUANDO SE CAMBIA DE TAMAÑO LA INTERFAZ PARA PROPORCIONAR EL TAMAÑO Y POSICIÓN DE LOS OBJETOS
		addComponentListener(new java.awt.event.ComponentAdapter() {
	         public void componentResized(java.awt.event.ComponentEvent evt) {
	     		ancho = getWidth();
	    		alto  = getHeight();
	    		
	    		Prog.setSize			(((int)(ancho/3.4)),((int)(alto/1.48)));
	    		Res.setSize				(((int)(ancho/2)),((int)(alto/3)));
	    		RPar.setSize			(((int)(ancho/2)),((int)(alto/3)));
	    		
	    		//NUEVO
	    		Sem.setSize			(((int)(ancho/3.4)),((int)(alto/3)));
	    		TS.setSize				(((int)(ancho/3.4)),((int)(alto/3)));
	    		
	    		
	    		lbPrograma.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		lbResultado.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		lbParser.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		
	    		//NUEVO 
	    		lbSemantico.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		TablaSimbolos.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		
	    		Calcular.setSize		(((int)(ancho/6.5)),((int)(alto/17.5)));
	    		
	    		lbTotal.setSize			(((int)(ancho/4)),((int)(alto/14)));
	    		FilaColumna.setSize		(((int)(ancho/3.0188)),((int)(alto/17.5)));
	    		
	    		lbPrograma.setLocation	(((int)(ancho/28)),((int)(alto/20)));
	    		lbResultado.setLocation	(((int)(ancho/3)),((int)(alto/20)));
	    		lbParser.setLocation	(((int)(ancho/3)),((int)(alto/2.16)));
	    		
	    		//NUEVO       CHECAR LAS POSICIONES
	    		lbTablaSimbolos.setLocation	(((int)(ancho/1.5)),((int)(alto/20)));
	    		lbSemantico.setLocation	(((int)(ancho/1.5)),((int)(alto/2.16)));
	    		 
	    		Prog.setLocation		(((int)(ancho/160)),((int)(alto/9)));
	    		Res.setLocation			(((int)(ancho/3)),((int)(alto/9)));  //arriba
	    		RPar.setLocation		(((int)(ancho/3)),((int)(alto/1.9))); //abajo
	    		
	    		//NUEVO      CHECAR POSICIONES
	    		
	    		Sem.setLocation		(((int)(ancho/1.5)),((int)(alto/1.9)));
	    		TS.setLocation		(((int)(ancho/1.5)),((int)(alto/9)));
	    
	    		Calcular.setLocation	(((int)(ancho/160)),((int)(alto/350)));		
	    		lbTotal.setLocation		(((int)(ancho/3)),((int)(alto/2.35)));
	  
	    		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));   		
	    		Calcular.setIcon((new ImageIcon(((new ImageIcon("analyze.png")).getImage()).getScaledInstance
	    				(Calcular.getWidth(), Calcular.getHeight(), java.awt.Image.SCALE_SMOOTH))));
	    	
	    		validate();
	         }
	     });
		
	}
	
	public void Escuchas(){
		 elementoNuevo.addActionListener(this);
		 elementoAbrir.addActionListener(this);
		 elementoGuardar.addActionListener(this);
		 elementoGuardarComo.addActionListener(this);
		 elementoSalir.addActionListener(this);
		 Calcular.addActionListener(this);
		 
		 Programa.addCaretListener(this);
		 
		 Programa.getDocument().addDocumentListener(this);
	}
	
	public void Ventana(){
		setSize(1200,700);
		setLocationRelativeTo(null);
		setLayout(null);
		ancho = getWidth();
		alto  = getHeight();
		this.setIconImage(new ImageIcon("OW.png").getImage());
		this.setResizable(false);
		
		//------------------------------------------------------------->CREAR LA BARRA DE MENU Y EL MENU ARCHIVO
		Menu = new JMenuBar();
		
		MenuArchivo = new JMenu( "Archivo" ); // crea el menú archivo
	    MenuArchivo.setMnemonic( 'A' ); // establece el nemónico a A
		
	    //Opción Nuevo
	    elementoNuevo = new JMenuItem( "Nuevo" );
	    elementoNuevo.setMnemonic( 'N' );
	    MenuArchivo.add( elementoNuevo );
	    
	    //Opción Abrir
	    elementoAbrir = new JMenuItem( "Abrir" ); // crea el elemento abrir
	    elementoAbrir.setMnemonic( 'A' );
	    MenuArchivo.add( elementoAbrir );
		
	    //Opción Guardar
		elementoGuardar = new JMenuItem( "Guardar" );
		elementoGuardar.setMnemonic( 'G' );
		MenuArchivo.add( elementoGuardar );
		
		//Opción Guardar Como
		elementoGuardarComo = new JMenuItem( "Guardar Como" );
		elementoGuardarComo.setMnemonic( 'C' );
		MenuArchivo.add( elementoGuardarComo );
		
		//Opción Salir
		elementoSalir = new JMenuItem( "Salir" );
		elementoSalir.setMnemonic( 'S' );
		MenuArchivo.add( elementoSalir );
	    
		Menu.add( MenuArchivo );
		setJMenuBar( Menu ); // agrega la barra de menús a la aplicación
		
		
		//------------------------------------------------------------->CREAR ELEMENTOS
		//CREAR OBJETO
		Programa = new JTextArea();
		Resultado = new JTextArea();
		ResParser = new JTextArea();
		
		//NUEVO
		ResSemantico= new JTextArea();
		TablaSimbolos = new JTextArea();
			
		Prog = new JScrollPane(Programa);
		Res = new JScrollPane(Resultado);
		RPar = new JScrollPane(ResParser);
		
		//NUEVO
		Sem=new JScrollPane(ResSemantico);
		TS=new JScrollPane(TablaSimbolos);
		
		TextLineNumber tln = new TextLineNumber(Programa);
		Prog.setRowHeaderView( tln );
		
		lbPrograma = new JLabel("Programa");
		lbResultado = new JLabel("Escaner");
		lbParser = new JLabel("Resultado");
		lbTotal = new JLabel ("Tokens = ");
		
		//NUEVO
		lbTablaSimbolos = new JLabel("Tabla de Simbolos");
		lbSemantico= new JLabel("Semántica");
		
		Calcular = new JButton();
		
		FilaColumna = new JTextField("Fila: 0 \t Columna: 0");
		
		//TAMAÑOS
		Prog.setSize			(((int)(ancho/1.1)),((int)(alto/1.48)));
		Res.setSize				(((int)(ancho/2.4)),((int)(alto/3)));
		//NUEVO RESULTADOS DEL PARSER
		RPar.setSize			(((int)(ancho/2.4)),((int)(alto/3)));
		
		lbPrograma.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbResultado.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbParser.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		
		Calcular.setSize		(((int)(ancho/6.5)),((int)(alto/17.5)));
		
		lbTotal.setSize			(((int)(ancho/4)),((int)(alto/14)));
		FilaColumna.setSize		(((int)(ancho/3.0188)),((int)(alto/17.5)));

		//POSICIONES
		lbPrograma.setLocation	(((int)(ancho/28)),((int)(alto/20)));
		lbResultado.setLocation	(((int)(ancho/1.8)),((int)(alto/20)));
		lbParser.setLocation	(((int)(ancho/1.8)),((int)(alto/2.16)));
		
		Prog.setLocation		(((int)(ancho/160)),((int)(alto/9)));
		Res.setLocation			(((int)(ancho/1.9)),((int)(alto/9)));
		
		//NUEVO RESULTADOS DEL PARSER
		RPar.setLocation		(((int)(ancho/1.9)),((int)(alto/1.9)));
		Calcular.setLocation	(((int)(ancho/160)),((int)(alto/350)));
		lbTotal.setLocation		(((int)(ancho/1.4)),((int)(alto/2.35)));
		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));
		
		FilaColumna.setEnabled(false);
		FilaColumna.setOpaque(false);
		FilaColumna.setDisabledTextColor(Color.black);
		FilaColumna.setBorder(null);
		
		
		//COSAS AGREGADAS POR MI 
		
		//scrollpanes
		Sem.setSize			(((int)(ancho/2.4)),((int)(alto/3)));
		TS.setSize			(((int)(ancho/2.4)),((int)(alto/3)));
		
		TS.setLocation		(((int)(ancho/0.5)),((int)(alto/9)));
		Sem.setLocation		(((int)(ancho/0.5)),((int)(alto/1.9)));
		
		
		//etiquetas
		lbSemantico.setSize		(((int)(ancho/2)),((int)(alto/14)));
		lbTablaSimbolos.setSize		(((int)(ancho/2)),((int)(alto/14)));
		
		lbTablaSimbolos.setLocation	(((int)(ancho/0.5)),((int)(alto/20)));
		lbSemantico.setLocation	(((int)(ancho/0.5)),((int)(alto/2.16)));

		//------------------------------------------------------------->AGREGAR LOS ELEMENTOS AL JFRAME
		add(lbPrograma);
		add(lbResultado);
		add(lbParser);
		
		add(Prog);
		add(Calcular);
		add(Res);
		add(RPar);
		
		add(lbTotal);	
		add(FilaColumna);
		
		//NUEVO
		//add(Sem);
		//add(TS);
		//add(lbSemantico);
		//add(lbTablaSimbolos);
		
		Resultado.setEnabled(false);
		Resultado.setOpaque(true);
		Resultado.setDisabledTextColor(Color.black);
		
		ResParser.setEnabled(false);
		ResParser.setOpaque(true);
		ResParser.setDisabledTextColor(Color.black);
		
		//NUEVO
		ResSemantico.setEnabled(false);
		ResSemantico.setOpaque(true);
		ResSemantico.setDisabledTextColor(Color.black);
		TablaSimbolos.setEnabled(false);
		TablaSimbolos.setOpaque(true);
		TablaSimbolos.setDisabledTextColor(Color.black);
		
		
		
		
		lbPrograma.setFont(new Font("Verdana", Font.BOLD, 16));
		lbResultado.setFont(new Font("Verdana", Font.BOLD, 16));
		lbParser.setFont(new Font("Verdana", Font.BOLD, 16));
		//NUEVO
		lbSemantico.setFont(new Font("Verdana", Font.BOLD, 16));
		lbTablaSimbolos.setFont(new Font("Verdana", Font.BOLD, 16));
		
		//Calcular.setOpaque(false);
		Calcular.setContentAreaFilled(false);
		//Calcular.setBorderPainted(false);
		Calcular.setIcon((new ImageIcon(((new ImageIcon("")).getImage()).getScaledInstance
				(Calcular.getWidth(), Calcular.getHeight(), java.awt.Image.SCALE_SMOOTH))));
			
		
		cerrar();
		//------------------------------------------------------------->PONER VISIBLE
		
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent evento){
		
		if(evento.getSource() instanceof JMenuItem){
			if (evento.getSource() == elementoNuevo ){
				Nuevo();
				return;
			}
			
			if (evento.getSource() == elementoAbrir ){
				Programa.setText(abrirArchivo());
				return;
			}
			
			if (evento.getSource() == elementoGuardar ){
				guardarArchivo();
				return;
			}

			if (evento.getSource() == elementoGuardarComo ){
				guardarComoArchivo();
				return;
			}
			
			if (evento.getSource() == elementoSalir && !guarda && !Programa.getText().equals(""))
	    	{
	    		int ax = JOptionPane.showConfirmDialog(null, "¿Desea guardar el archivo?");
	            if(ax == JOptionPane.YES_OPTION)
	                guardarArchivo();
	            else if(ax == JOptionPane.NO_OPTION)
	            	
	            	System.exit( 0 ); // sale de la aplicación
	    	}
			else
				System.exit( 0 ); //sale

		}
		
		//BOTON DE ANALISIS
		if(evento.getSource() instanceof JButton){
			if (evento.getSource() == Calcular){
				Resultado.setText("");
				
				if(Programa.getText().length()<1){
					/*JOptionPane.showMessageDialog(null,
			    	         "Agregue texto para escanear",
			    	             "Información",JOptionPane.INFORMATION_MESSAGE);
					*/
					ResParser.setText("Programa Correcto");
					ResParser.setDisabledTextColor(Color.BLUE);
					return;
				}
				
				//INDICIA QUE EL PROGRAMA ACTUAL HA SIDO ESCANEADO
				Escaneado = true;
				
				TokenTipo = "Token\tTipo\n";
				TabladeSimbolos="ID\tTipo\n";
				ObjAnalizador =new AnalizadorVersion3(Programa.getText());
				Resultado.setText(TokenTipo);
				TablaSimbolos.setText(TabladeSimbolos);
				VectorTokens=ObjAnalizador.getVectorTokens();
				lbTotal.setText("Tokens = "+VectorTokens.size());
			
				//A PARTIR DE AQUI SE VA CHECANDO EL PARSER
				//NO SE PERMITE USAR EL PARSER SI NO SE HA ESCANEADO
				if(!Escaneado){
					JOptionPane.showMessageDialog(null,
			    	         "El programa actual no ha sido Escaneado, Escanee para usar el Parser",
			    	             "Información",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				//NO SE PERMITE USAR EL PARSER SI HAY ERRORES EN EL ESCANEO
				if(!ObjAnalizador.isCorrect()){
					/*JOptionPane.showMessageDialog(null,
			    	         "El programa debe tener solo tokens validos para usar el Parser",
			    	             "Información",JOptionPane.INFORMATION_MESSAGE);*/
					ResParser.setDisabledTextColor(Color.RED);
					ResParser.setText("Programa Incorrecto");
					return;
				}
				ObjParser = new ParserClass(VectorTokens);
				
				ResParser.setText("");
				
				if(!ObjParser.isCorrect()){
					ResParser.setDisabledTextColor(Color.RED);
					ResParser.setText("Syntax Error\n");
				}else
					ResParser.setDisabledTextColor(Color.BLUE);
				
				ResParser.setText(ResParser.getText()+ObjParser.Resultado());
				//OCUPO VALIDAR ALGO AQUI (ES POR EL JTEXTAREA DE LA TABLA DE SIMBOLOS)
				TablaSimbolos.setText(TabladeSimbolos);
				ResSemantico.setText(ObjParser.ResultadoSemantico());
				
				
			}
			
		}
		
	}
	
	//---------------------------------------------------------------------->NUEVO
    public void Nuevo () {

    	if (! guarda && !Programa.getText().equals(""))
    	{
    		int ax = JOptionPane.showConfirmDialog(null, "¿Desea guardar el archivo?");
            if(ax == JOptionPane.YES_OPTION){
                guardarArchivo();
                Programa.setText("");
                Resultado.setText("");
                NombreDoc = null;
            }
            else if(ax == JOptionPane.NO_OPTION){
            	Programa.setText("");
            	Resultado.setText("");
            	NombreDoc = null;
            }
    		guarda=false;
    	}
    }
    
    
    //---------------------------------------------------------------------->ABRIR
    private String abrirArchivo() {
    	if (! guarda && !Programa.getText().equals(""))
    	{
    		int ax = JOptionPane.showConfirmDialog(null, "¿Desea guardar el archivo?");
            if(ax == JOptionPane.YES_OPTION)
            guardarArchivo();
            Programa.setText("");
            Resultado.setText("");
    	}
    	  String aux="", texto="";
    	  try
    	  {
    	   //Llamamos el metodo que permite cargar la ventana
    	   JFileChooser file=new JFileChooser();
    	   file.showOpenDialog(this);
    	   //Abrimos el archivo seleccionado
    	   File abre=file.getSelectedFile();
    	   
    	   NombreDoc=abre.getParent()+"/"+abre.getName(); //Aquí es donde toma la dirección del texto
    	   //System.out.println(abre.getName()+"  "+abre.getParent());//no es necesaria pero era para ver para qué sirve cada uno xD
    	   
    	   //Recorremos el archivo, lo leemos para plasmarlo en el area de texto
    	   if(abre!=null)
    	   {     
    	      FileReader archivos=new FileReader(abre);
    	      BufferedReader lee=new BufferedReader(archivos);
    	      while((aux=lee.readLine())!=null)
    	      {
    	         texto+= aux+ "\n";
    	      }
    	         lee.close();
    	    }    
    	   }
    	   catch(IOException ex)
    	   {
    	     JOptionPane.showMessageDialog(null,ex+"" +
    	           "\nNo se ha encontrado el archivo",
    	                 "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
    	    }
    	  return texto;//El texto se almacena en el JTextArea
    	}

    //---------------------------------------------------------------------->GUARDAR
	private void guardarArchivo() {

		//NombreDoc es una variable global de tipo String que contiene la dirección del archivo actual
		if(NombreDoc==null)//Para cuando le des guardar y no exista el archivo aún
			guardarComoArchivo();
		else{
			File doc=new File(NombreDoc);
			doc.delete();//Elimino el archivo actual
			File nuevoDoc=new File(NombreDoc);//Creo el nuevo archivo
			String texto = Programa.getText();//Tomo el texto que está en el JTextArea
			//System.out.println(source);

			try {
				FileWriter f2 = new FileWriter(nuevoDoc, false);//Hago que pueda escribir en el neuvo doc
				f2.write(texto);//Le escribo dicho texto en el nuevo documento
				f2.close();
				JOptionPane.showMessageDialog(null,
		    	         "El archivo se ha guardado Exitosamente",
		    	             "Información",JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
    }
	
	//---------------------------------------------------------------------->GUARDAR COMO
		private void guardarComoArchivo() {

	    	 try{
	    	  
	    	  JFileChooser file=new JFileChooser();
	    	  file.showSaveDialog(this);
	    	  
	    	  File guarda =file.getSelectedFile();
	    	  
	    	  if(guarda !=null){
	    	   //guardamos el archivo y le damos el formato directamente,
	    	    
	    	    FileWriter  save=new FileWriter(guarda);
	    	  
	    	    save.write(Programa.getText());
	    	    save.close();
	    	    JOptionPane.showMessageDialog(null,
	    	         "El archivo se ha guardado Exitosamente",
	    	             "Información",JOptionPane.INFORMATION_MESSAGE);
	    	    }
	    	  }
	    	  catch(IOException ex){
	    	   JOptionPane.showMessageDialog(null,
	    	        "Su archivo no se ha guardado",
	    	           "Advertencia",JOptionPane.WARNING_MESSAGE);
	    	  }
	    }
	
	//------------------------------------------------------------------->Validar el CIERRE con X
	public void cerrar()
	{
		
		try{
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					confirmarSalida();
				}
			});
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void confirmarSalida(){
		int valor=JOptionPane.showConfirmDialog(this, "¿Desea guardar antes de salir?", "Salir", JOptionPane.YES_NO_OPTION);
		
		if  (valor==JOptionPane.YES_OPTION){
			guardarArchivo();
			System.exit(0);
		}
		else  if (valor==JOptionPane.NO_OPTION)
				System.exit(0); 
	}
	
	//---------------->NUEVO METODO PARA INDICAR LA FILA Y COLUMNA DEL CURSOR
	public void caretUpdate(CaretEvent e) {
		  JTextArea editArea = (JTextArea)e.getSource();
		 
		  int linea = 1;
		  int columna = 1;
		 
		  try {
		    int caretpos = editArea.getCaretPosition();
		    linea= editArea.getLineOfOffset(caretpos);
		    columna = caretpos - editArea.getLineStartOffset(linea);
		    
		    
		    // Ya que las líneas las cuenta desde la 0
		    linea += 1;
		  } catch(Exception ex) { }
		 
		  // Actualizamos el estado
		  actualizarEstado(linea, columna);
	}
	
	private void actualizarEstado(int linea, int columna) {
		FilaColumna.setText("Fila: " + linea + "\t Columna: " + columna);
	}
	
	//----------------->NUEVO METODOS PARA NO DEJAR USAR EL PARSER SIN HABER ESCANEADO ANTES CUALQUIER CAMBIO
	public void insertUpdate(DocumentEvent e) {
		Escaneado = false;
		lbTotal.setText("Tokens = ");
	//	lbErrores.setText("Errores = ");
    }
    public void removeUpdate(DocumentEvent e) {
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
    	//lbErrores.setText("Errores = ");
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
    	//lbErrores.setText("Errores = ");
    }
	
	public static void main(String[] args) {
		new Interfaz();
	}

}