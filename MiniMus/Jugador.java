import java.util.ArrayList;
//import java.util.Collections;

/**
 * Esta clase define los jugadores que conformaran las parejas de juego, indicando su nombre, id, cartas, mano, etc...
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */

public class Jugador implements Comparable<Jugador> {

//----------------------------------------------------------------------------------------------------------------------
	//Atributos clase Jugador
	
	private int id; 

	private String nombreu; 
	
	private int puntos_jugador; 

	public boolean mano; 
	
	private ArrayList<Carta> cartas = new ArrayList<Carta>();
	
	private int valorescartas[]= {0,0,0,0,0,0,0,0};
	
	private int[] piedras = {0,0,0,0}; //Cada posicion tiene la cantidad de piedras que ha ganado el jugador en cada lance respectivamente los lances: grande, chica, pares y Juego
	
	private boolean TieneJuego;
//-----------------------------------------------------------------------------------------------------------------------
	
/**
 * Constructor para crear objetos del tipo jugador
 * @param id numero de identificacion del jugador
 * @param nombreu nombre de usuario del jugador
 */
	public Jugador (int id, String nombreu) { //Lector_jugadores, se usa ahi 
		this.id = id;
		this.nombreu = nombreu;
	}
		
/**
 * Constructor vacio de Jugador para poder crear clones de objetos de tipo jugador en Partida, especificamnete en el metodo leerJugadores
 */
	Jugador() { 
		
	}

	
//------------------------------- GETTERS y/o SETTERS ------------------------------------------------------------------
	
/**
 * Getter de Id del jugador
 * @return Id de un jugador
 */
	public int getID(){ 
		return this.id;
	}
	
/**
 * Getter de nombre del jugador
 * @return nombre del jugador
 */
	public String getNombreu(){ 
		return this.nombreu;
	}
	
/**
 * Getter puntos del jugador
 * @return puntos del jugador (segun sus cartas)
 */
	public int getPuntos_Jugador(){ 
		return this.puntos_jugador;
	}
	
 /**
  * Getter para ver si un jugador es mano 
  * @return false: NO es mano, true: SI es mano
  */
	public boolean getMano(){ 
		return this.mano;
	}

/**
 * Setter que permite cambiar por conveniencia el true o false de la mano de un jugador
 * @param b Boolean que representara el estado de mano de un jugador
 */
	public void setManoAux(boolean b) {
		this.mano = b;
	}

/**
 * Getter de cartas de un jugador, 1/4
 * @param i Parametro i, significa (0) carta 1/4, (1) carta 2/4, (2) carta 3/4 y (3) carta 4/4
 * @return la carta deseada de un jugador 
 */
	public Carta getCarta(int i) {
		return cartas.get(i);
	}
	
/**
 * Getter para ver el valor de una carta i, de un jugador deseado
 * @param i Parametro i, significa (0) carta 1/4, (1) carta 2/4, (2) carta 3/4 y (3) carta 4/4
 * @return El valor de la carta deseada de un jugador
 */
	public int getValoresCartas(int i) {
		return valorescartas[i];
	}
	
/**
 * Getter para ver si un jugador tiene Juego (lance)
 * @return True: Si tiene juego y false: No tiene juego 
 */
	public boolean getTieneJuego() {
		return this.TieneJuego;
	}
	
/**
 * Setter que permite asignar a cada jugador sus cuatro respectivas cartas 
 * @param carta1 del jugador
 * @param carta2 del jugador
 * @param carta3 del jugador
 * @param carta4 del jugador
 */
	public void setCartasAux(Carta carta1, Carta carta2, Carta carta3, Carta carta4) {

		for(int i = cartas.size()-1;i>=0;i--) cartas.remove(i); // Esta instruccion remueve las cartas de una jugada anterior (si es la primera jugada, no se ejecuta)
		this.cartas.add (carta1);
		this.cartas.add (carta2);
		this.cartas.add (carta3);
		this.cartas.add (carta4);
		this.calculaSumapuntos();
		this.asignarValores();
		this.resolverPares();
		this.TieneJuego = resolverLanceJuego();
	}

/**	
 * true si el jugador es mano, al ser auxiliar nos ayuda en partida
 */
	public void setJugadorManoAux(){
		this.mano = true;
	}

/**
 * Getter que permite saber las piedras de un jugador, [grande, chica, pares, juego]
 * @param i Parametro que permite elegir que piedras queremos obtener, entre grande, chica, pares o juego 
 * @return El valor de las piedras en la posicion i
 */
	public int getPiedras(int i) {
		return piedras[i];
	}

	
// ---------------------------- FUNCIONES DE LA CLASE ------------------------------------------------------------
	
/**
* Permite sumarle piedras a un jugador en el lance que se desee (dirigido por el parametro p)
* @param p Parametro que permite elegir que piedras queremos obtener, entre grande, chica, pares o juego 
* @param i Entero que le sumamos a las piedras que ya teniamos antes
*/
	public void sumaPiedras(int p, int i) {
			piedras[p] = piedras[p] + i;
		}
		
/**
 * Calcula los puntos totales de un jugador dependiendo de sus cartas
 */
	private void calculaSumapuntos() {

		int calcula = 0;
        for(int i=0;i<cartas.size();i++) calcula = calcula + cartas.get(i).getPuntos();
        this.puntos_jugador = calcula;
		  
	}
	
/**
 * Esta instruccion es para asegurar que no hayan asignaciones de valores al array anterior (para volver a calcularlo cada vez que al jugador 1 se le asignan otras cartas)
 */
	private void asignarValores() {
		for(int j=0; j<valorescartas.length;j++) valorescartas[j]= 0; 
		for(int i=0;i<cartas.size();i++){
			switch(cartas.get(i).getDenominacion()) { 
				case 'R':
				case '3':
					valorescartas[0]++;
					break;
					
				case 'C':
					valorescartas[1]++;
					break;
					
				case 'S':
					valorescartas[2]++;
					break;
						
				case '7':
					valorescartas[3]++;
					break;
					
				case '6':
					valorescartas[4]++;
					break;
					
				case '5':
					valorescartas[5]++;
					break;
						
				case '4':
					valorescartas[6]++;
					break;
			    	
				case '1':
			    case '2':
			    	valorescartas[7]++;
			    	break;
			    	
			    default:
			    	break;

			}
		}
	}

/**
 * Resuelve las piedras del lance Pares	
 */
	private void resolverPares() {
		this.piedras[2]= 0;
		for(int i=0;i<8;i++) {
			if (valorescartas[i]>1) {
				if(valorescartas[i] == 2) {
					if (this.piedras[2] == 1) {
						this.piedras[2] = 3;
						return;
					}
					else {	
						this.piedras[2] = 1;
					}
				}
				if(valorescartas[i] == 3) {
					this.piedras[2] =  2;
					return;
				}
				if(valorescartas[i] == 4) {
					this.piedras[2] =  3;
					return;
				}
			}
		}
		return;
	}
	
/**
 * Nos dice si un jugador tiene o no juego y a la vez le suma las piedras correspondientes, si tiene 31=3 piedras, demas juego= 2 piedras
 * @return True: si tiene juego, false: no tiene juego 
 */
	private boolean resolverLanceJuego() {
		if (puntos_jugador >= 31){
			if (puntos_jugador == 31) piedras[3] = 3;
			else piedras[3] = 2;
			return true;
		}
		return false;
	}

/**
 * Elimina las piedras de una jugada de los jugadores para poder mostrar en modo 3 en cada jugada cuantas piedras ha obtenido un jugador en una mano 
 */
	public void reiniciarPiedras() {
		this.piedras[0] = 0;
		this.piedras[1] = 0;
		this.piedras[2] = 0;
		this.piedras[3] = 0;
		
	}
	
/**
 * Sobreescritura del metodo compareTo para ordenar los Id de jugadores de menor a mayor	
 */
	@Override
	public int compareTo(Jugador o) {
		return this.id - o.id;
	}
}