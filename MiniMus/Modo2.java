import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Clase que permite ejecutar el modo2 (Juego con reparto de cartas preestablecido) extiende de partida donde tenemos las herramientas para ejecutar los tres modos 
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */

public class Modo2 extends Partida{

/**
 * Constructor vacio Modo2 para que Modo3 pueda extender de esta clase 
 */
	public Modo2() {
		
	}
	
/**
 * Constructor que permite ejecutar el modo2 desde el programa principal 
 * @param ficheroC fichero .txt que incluye las jugadas preestablecidas
 * @param ficheroJ fichero .txt que incluye nombres e ids de jugadores y parejas
 * @throws FileNotFoundException manejo de ficheros, no se encunetra
 * @throws IOException manejo de ficheros
 */
	public Modo2(String ficheroC, String ficheroJ) throws FileNotFoundException, IOException {
		super();
		leerJugadoresModo1y2(ficheroJ);
		leerFicheroCartasModo2(ficheroC);
		modo2();
		
		
	}
	
	
/**
 * Metodo que implementa el modo Juego con reparto de cartas preestablecido. Lo llamamos en el constructor de este modo
 */
	protected void modo2() {
		int jugadasRes,jugadas;
		piedras[0] = 0; //Antes de finalizar el modo, reiniciamos la piedras, esto influye en el modo 3 al utilizar playGame
		piedras[1] = 0;
					
		jugadas = Baraja.listaCartas.size()/16; // listaCartas contiene todas las cartas de todas las jugadas, en orden. Dividimos entre 16 para saber cuantas jugadas tenemos
		System.out.println(pareja1.getNombrepareja()+": "+pareja1.jugador1.getNombreu()+" y "+pareja1.jugador2.getNombreu()+".");
		System.out.println(pareja2.getNombrepareja()+": "+pareja2.jugador1.getNombreu()+" y "+pareja2.jugador2.getNombreu()+".");
		System.out.println("Mano: "+whoIsMano().getNombreu()+".");
					
		jugadasRes = jugadas;
					
		while(jugadasRes>0) {	
			Baraja.repartir(pareja1,pareja2);
			System.out.print(generarStringDeJugada()+"\n");
			resolverLances(" ");
			System.out.println(" - "+puntuacionLive());
			correrMano();
			jugadasRes--;
			if (piedras[0] >=40 || piedras[1] >=40) break; //Si una pareja consigue 40 puntos o mas, se acaba la partida
		}
		if(piedras[0] >=40 || piedras[1] >=40) {
			if (piedras[0] > piedras[1]) System.out.println("Gana: "+pareja1.getNombrepareja()+". Número total de jugadas: "+(jugadas-jugadasRes)+".");
			if (piedras[1] > piedras[0]) System.out.println("Gana: "+pareja2.getNombrepareja()+". Número total de jugadas: "+(jugadas-jugadasRes)+".");
		} else System.out.println("Partida incompleta. Número total de jugadas: "+(jugadas-jugadasRes)+".");
				
	}
			
		
		
	
}
