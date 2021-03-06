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
	
	JScrollPane Prog;
	JScrollPane Res;
	JScrollPane RPar;
	JScrollPane ScrollSemantica;
	JScrollPane ScrollTablaSimbolos;
	
	JScrollPane Sccodigointer;
	
	JTextArea Programa;
	JTextArea Resultado;
	JTextArea ResultadoParser;
	JTextArea ResultadoSemantico;
	JTextArea ResultadoTablaSimbolos;
	
	JTextArea ResultadoCodigointer;	
	
	JLabel lbPrograma;
	JLabel lbResultado;
	JLabel lbParser;
	JLabel lbTotal;
	JLabel lbTablaSimbolos;
	JLabel lbSemantico;
	
	JLabel lbCodigointer;
	
	JButton Calcular;
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
	}
	
	public void Escuchas(){
		 Calcular.addActionListener(this);
		 Programa.addCaretListener(this);
		 Programa.getDocument().addDocumentListener(this);
	}
	
	public void Ventana(){
		setSize(1350,750);
		setLocationRelativeTo(null);
		setLayout(null);
		ancho= getWidth();
		alto= getHeight();
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		Programa = new JTextArea();
		Resultado = new JTextArea();
		ResultadoParser = new JTextArea();
		ResultadoSemantico= new JTextArea();
		ResultadoTablaSimbolos = new JTextArea();
		
		ResultadoCodigointer = new JTextArea();
			
		Prog = new JScrollPane(Programa);
		Res = new JScrollPane(Resultado);
		RPar = new JScrollPane(ResultadoParser);
		ScrollSemantica=new JScrollPane(ResultadoSemantico);
		ScrollTablaSimbolos=new JScrollPane(ResultadoTablaSimbolos);
		
		Sccodigointer = new JScrollPane(ResultadoCodigointer);
		
		TextLineNumber tln = new TextLineNumber(Programa);
		Prog.setRowHeaderView( tln );
		
		lbPrograma = new JLabel("Programa");
		lbResultado = new JLabel("Escaner");
		lbParser = new JLabel("Resultado");
		lbTotal = new JLabel ("Tokens = ");
		lbTablaSimbolos = new JLabel("Tabla de Simbolos");
		lbSemantico= new JLabel("Semántica");
		
		lbCodigointer=new JLabel("Codigo intermedio");
		
		Calcular = new JButton();
		FilaColumna = new JTextField("Fila: 0 \t Columna: 0");
		
		Prog.setSize			(((int)(ancho/5)),((int)(alto/1.5)));      //
		Res.setSize				(((int)(ancho/5)),((int)(alto/3)));
		RPar.setSize			(((int)(ancho/5)),((int)(alto/3)));
		lbPrograma.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbResultado.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbParser.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		ScrollSemantica.setSize			(((int)(ancho/5)),((int)(alto/1.5)));
		ScrollTablaSimbolos.setSize				(((int)(ancho/5)),((int)(alto/3)));
		lbSemantico.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		ResultadoTablaSimbolos.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		Calcular.setSize		(((int)(ancho/6)),((int)(alto/17.5)));
		
		ScrollSemantica.setLocation		(((int)(ancho/1.5)),((int)(alto/1.9)));
		ScrollTablaSimbolos.setLocation		(((int)(ancho/1.5)),((int)(alto/9)));
		Calcular.setLocation	(((int)(ancho/160)),((int)(alto/350)));		
		lbTotal.setLocation		(((int)(ancho/3)),((int)(alto/2.35)));
		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));   		
		lbTotal.setSize			(((int)(ancho/4)),((int)(alto/14)));
		FilaColumna.setSize		(((int)(ancho/3.0188)),((int)(alto/17.5)));
		lbPrograma.setLocation	(((int)(ancho/28)),((int)(alto/20)));           //
		lbResultado.setLocation	(((int)(ancho/4)),((int)(alto/20)));             //
		lbParser.setLocation	(((int)(ancho/4)),((int)(alto/2.16)));            //
		Prog.setLocation		(((int)(ancho/120)),((int)(alto/9)));             //
		Res.setLocation			(((int)(ancho/4)),((int)(alto/9)));             //
		lbTablaSimbolos.setLocation	(((int)(ancho/1.5)),((int)(alto/20)));
		lbSemantico.setLocation	(((int)(ancho/1.5)),((int)(alto/2.16)));
	
		RPar.setLocation		(((int)(ancho/4)),((int)(alto/1.9)));     //
		Calcular.setLocation	(((int)(ancho/160)),((int)(alto/350)));
		lbTotal.setLocation		(((int)(ancho/4)),((int)(alto/2.35)));    //
		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));
		FilaColumna.setEnabled(false);
		FilaColumna.setOpaque(false);
		FilaColumna.setDisabledTextColor(Color.black);
		FilaColumna.setBorder(null);
		
		ScrollSemantica.setSize			(((int)(ancho/5)),((int)(alto/3)));      //
		ScrollTablaSimbolos.setSize			(((int)(ancho/5)),((int)(alto/3)));   //
		ScrollTablaSimbolos.setLocation		(((int)(ancho/2)),((int)(alto/9)));   //
		ScrollSemantica.setLocation		(((int)(ancho/2)),((int)(alto/1.9)));     //
				
		lbSemantico.setSize		(((int)(ancho/3)),((int)(alto/14)));
		lbTablaSimbolos.setSize		(((int)(ancho/3)),((int)(alto/14)));
		lbTablaSimbolos.setLocation	(((int)(ancho/2)),((int)(alto/20))); //
		lbSemantico.setLocation	(((int)(ancho/2)),((int)(alto/2.16)));   //
		
		Sccodigointer.setLocation		(((int)(ancho/1.33)),((int)(alto/9)));
		Sccodigointer.setSize		(((int)(ancho/5)),((int)(alto/3)));
		
		lbCodigointer.setLocation		(((int)(ancho/1.33)),((int)(alto/20)));
		lbCodigointer.setSize		(((int)(ancho/3)),((int)(alto/14)));
		
		
		add(lbPrograma);
		add(lbResultado);
		add(lbParser);
		add(Prog);
		add(Calcular);
		add(Res);
		add(RPar);
		add(lbTotal);	
		add(FilaColumna);
		add(ScrollSemantica);
		add(ScrollTablaSimbolos);
		add(lbSemantico);
		add(lbTablaSimbolos);
		
		
		add(Sccodigointer);
		add(lbCodigointer);
		
		Resultado.setEnabled(false);
		Resultado.setOpaque(true);
		Resultado.setDisabledTextColor(Color.black);
		ResultadoParser.setEnabled(false);
		ResultadoParser.setOpaque(true);
		ResultadoParser.setDisabledTextColor(Color.black);
		ResultadoSemantico.setEnabled(false);
		ResultadoSemantico.setOpaque(true);
		ResultadoSemantico.setDisabledTextColor(Color.black);
		ResultadoTablaSimbolos.setEnabled(false);
		ResultadoTablaSimbolos.setOpaque(true);
		ResultadoTablaSimbolos.setDisabledTextColor(Color.BLACK);
		
		lbPrograma.setFont(new Font("Verdana", Font.BOLD, 16));
		lbResultado.setFont(new Font("Verdana", Font.BOLD, 16));
		lbParser.setFont(new Font("Verdana", Font.BOLD, 16));
		lbSemantico.setFont(new Font("Verdana", Font.BOLD, 16));
		lbTablaSimbolos.setFont(new Font("Verdana", Font.BOLD, 16));
		
		lbCodigointer.setFont(new Font("Verdana", Font.BOLD, 16));
		
		Calcular.setOpaque(true);
		Calcular.setContentAreaFilled(false);
		Calcular.setBorderPainted(true);
		Calcular.setText("Analizar");
		Calcular.setIcon((new ImageIcon(((new ImageIcon("analyze.png")).getImage().getScaledInstance
		(Calcular.getWidth(), Calcular.getHeight(), java.awt.Image.SCALE_SMOOTH)))));
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evento){	
		//BOTON DE ANALISIS
		if(evento.getSource() instanceof JButton){
			if (evento.getSource() == Calcular){
				Resultado.setText("");
				
				if(Programa.getText().length()<1){	
					ResultadoParser.setText("Programa correcto");
					ResultadoParser.setDisabledTextColor(Color.BLUE);
					return;
				}
				
				//INDICIA QUE EL PROGRAMA ACTUAL HA SIDO ESCANEADO
				Escaneado = true;
				
				TokenTipo = "Token\tTipo\n";
				TabladeSimbolos="ID\tTipo\n";
				ObjAnalizador =new AnalizadorVersion3(Programa.getText());
				Resultado.setText(TokenTipo);
				ResultadoTablaSimbolos.setText(TabladeSimbolos);
				
	
				
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
					ResultadoParser.setDisabledTextColor(Color.RED);
					ResultadoParser.setText("Programa Incorrecto");
					return;
				}
				ObjParser = new ParserClass(VectorTokens);
				
				ResultadoParser.setText("");
				
				if(!ObjParser.isCorrect()){
					ResultadoParser.setDisabledTextColor(Color.RED);
					ResultadoParser.setText("Syntax Error\n");
				}else
					ResultadoParser.setDisabledTextColor(Color.BLUE);
				
				ResultadoParser.setText(ResultadoParser.getText()+ObjParser.Resultado());
				//OCUPO VALIDAR ALGO AQUI (ES POR EL JTEXTAREA DE LA TABLA DE SIMBOLOS)
				ResultadoTablaSimbolos.setText(TabladeSimbolos);
				ResultadoSemantico.setText(ObjParser.ResultadoSemantico());
			}
		}
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
    }
    public void removeUpdate(DocumentEvent e) {
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
    }
    public void changedUpdate(DocumentEvent e) {
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
    }
	
	public static void main(String[] args) {
		new Interfaz();
	}
}