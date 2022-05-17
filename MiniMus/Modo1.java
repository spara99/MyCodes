import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Clase que permite ejecutar el modo1 (Juego autonomo) extiende de partida donde tenemos las herramientas para ejecutar los tres modos 
 * @author Christian Sparacino y Rodirgo Sifontes
 * @version 09/06/2020
 */
public class Modo1 extends Partida {

/**
 * Constructor que permite ejecutar el modo1 desde el programa principal 
 * @param ficheroJ fichero .txt que incluye nombres e ids de jugadores y parejas 
 * @throws FileNotFoundException manejo de ficheros, no se encuentra
 * @throws IOException manejo de ficheros
 */
	public Modo1 (String ficheroJ) throws FileNotFoundException, IOException{
		
		super();  //super de Partida
		leerJugadoresModo1y2(ficheroJ); //llamamos a leerJugadoresModo1y2 para, en caso de que tengamos un fichero tipo ficheroJ, podamos sacar la informacion de alli
		modo1(); //usamos el metodo modo1 que implementa el Juego Autonomo
		
	}
	
//------------------------------- METODOS ---------------------------------------------------------------------------------------------------------------------------
	
/**
 * Metodo que implementa el modo Juego Autonomo. Lo llamamos en el constructor de este modo
 */
		private void modo1() {
			int numeroJugadas=0;
			System.out.println(pareja1.getNombrepareja()+": "+pareja1.jugador1.getNombreu()+" y "+pareja1.jugador2.getNombreu()+".");
			System.out.println(pareja2.getNombrepareja()+": "+pareja2.jugador1.getNombreu()+" y "+pareja2.jugador2.getNombreu()+".");
			setManoIni(1);
			System.out.println("Mano: "+pareja1.jugador1.getNombreu()); //Para este modo, en todos los casos, siempre es el jugador J1PA el que empieza siendo la mano
			
			while(piedras[0]<40 && piedras[1]<40) {	
				generarJugadaAleatoria(1);						
				resolverLances(" ");
				System.out.println(" - "+puntuacionLive());
				correrMano();
				numeroJugadas++;
			}
			
			//imprime la pareja ganadora y el numero de partidas que se tuvieron que jugar para que esto ocurriera.
			if(piedras[0]>piedras[1]) {
				System.out.println("Gana: "+pareja1.getNombrepareja()+". Número total de jugadas: "+numeroJugadas+".");
			}
			else System.out.println("Gana: "+pareja2.getNombrepareja()+". Número total de jugadas: "+numeroJugadas+".");
		}
	
	
}
