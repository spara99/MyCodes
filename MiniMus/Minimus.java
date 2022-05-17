import java.io.IOException;
import java.io.*;

/**
 * Clase MiniMus donde se ejecutaran los comandos para elegir el modo y los ficheros de entrada y salida 
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */
 
public class Minimus {

	static String ficheroC, ficheroJ; //FicheroC: Fichero que lleva las cartas
												//FicheroJ: Fichero que lleva los jugadores y parejas
												
	
	static PrintStream out; //Variable del tipo PrintStream, el cual permite cmabiar la salida de impresion 

//----------------------------------------------------------------------------------------------------------------------------------------	
	
/**
 * Main del programa, donde se invocaran los modos mediante unos argumentos pasados por fichero paa poder escoger los modos
 * @param args nos indicara que modo vamos a ejecutar 
 * @throws IOException si no pasan ningun parametro 
 */
	public static void main(String[] args) throws IOException{
		
		char Modo;
		try {
			Modo = args[0].charAt(1);
		} catch(ArrayIndexOutOfBoundsException e){
			Modo='-';
		}
		
		switch(Modo) {
//-----------------------------------MODO3----------------------------------------------------------------------------------------------		
		case 'c':
			leerArgumentos(args,2);
			
			new Modo3(ficheroC, out);
			break;
			
//-----------------------------------MODO2----------------------------------------------------------------------------------------------- 											
		case 'j':  
			
			leerArgumentos(args,2);
			
			new Modo2(ficheroC,ficheroJ); 
			break;
			
//-----------------------------------MODO1--------------------------------------------------------------------------------------------------		
		default:   
			
			leerArgumentos(args,0);
			new Modo1(ficheroJ);
			break;
		}
		
	
		
	}

// ----------------------------------- LECTURA DE ARGUMENTOS ----------------------------------------------------------------------------
	
	 
/**
 * Metodo que permite identificar los ficheros de entrada por parametro 
 * @param args separa por espacios los comandos introducidos, ejemplo: args[0]=-c, args[1]=ficherojugadores.txt
 * @param i Entero que permite avanzar en args, modo 2 y 3: i=2, modo 1: i=0
 */
	static private void leerArgumentos(String[] args, int i) {
		String ficheroR="";
		try { //Si hay un fichero de entrada o salida hace esto, si no va al catch 
		
			if(args[0].charAt(1) == 'j' || args[0].charAt(1) == 'c') ficheroC=(args[1]); 
			
			if(args[0+i].charAt(1)=='p') { 
				ficheroJ=args[1+i];
				if(args[2+i].charAt(1)=='o') ficheroR=args[3+i];
			}
			
			else if(args[0+i].charAt(1)=='o') {
				ficheroR=args[1+i];
			
			
			}
				
		}
		catch(ArrayIndexOutOfBoundsException e) { //La unica forma de entrar aca es que estemos en modo 1 
		}
			
		try {
			out = new PrintStream(new FileOutputStream(ficheroR));
			System.setOut(out); //Salida en el fichero seleccionado 
		} 
		catch(Exception e) {
			out = System.out; //Salida por consola
		}
	}
	
}
			
	


