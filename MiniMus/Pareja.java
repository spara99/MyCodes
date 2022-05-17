
/**
 * Clase pareja para poder crear objetos de tipo pareja
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */
 
public class Pareja implements Comparable<Pareja> {
	
	
	//numero unico entre 0 y 999 que indentifica la pareja
	private int idpareja; 
	
	//numero unico entre 0 y 999 que indentifica el jugador 1 de la pareja
	private int id1;
	
	//numero unico entre 0 y 999 que indentifica el jugador 2 de la pareja
	private int id2;
	
	//nombre de la pareja 
	private String nombrePareja; 
	
	//puntos de la pareja 
	private int puntos_pareja; 
	
	//jugador 1 y jugador 2 de la pareja
	Jugador jugador1, jugador2;
	
	//vector usado en resolver el lance de juego, {puntuacion del mejor jugador de la pareja, puntuacion del otro jugador, 1 o 2 (con 1 cuando el mejor jugador fue el jugador1 o 2 cuando fue el jugador2)}
	private int juego[] = {0,0,0};                  

	
/**
 * Constructor que permite la creacion de una pareja con sus correspondientes parametros 
 * @param idpareja numero de identificacion de la pareja 
 * @param nombrePareja nombre de la pareja 
 * @param jugador1 jugador 1 de la pareja 
 * @param jugador2 jugador 2 de la pareja 
 */
	public Pareja (int idpareja, String nombrePareja, Jugador jugador1, Jugador jugador2) {
	
			this.idpareja = idpareja;
			this.nombrePareja = nombrePareja;
			this.jugador1 = jugador1;
			this.jugador2 = jugador2;
			this.id1 = jugador1.getID(); //id del jugador 1 de la pareja
			this.id2 = jugador2.getID(); //id del jugador 2 de la pareja
			calculaSumapareja ();
			juegoDePareja();
	
	}
	
//--------------------------------------------------------- METODOS PARA RESOLVER LOS LANCES POR PAREJA --------------------------------------------------

/**
 * Estan pensados para saber cual de los dos jugadores de cada pareja tienen mejores cartas para el lance grande 
 * @return el jugador de la pareja que tiene mejor lanze grande 
 */
	public Jugador whoIsGrande() {
		for (int i=0;i<8;i++) {
			if (jugador1.getValoresCartas(i)>jugador2.getValoresCartas(i)) {
				return jugador1;
			}
			
			if(jugador1.getValoresCartas(i)<jugador2.getValoresCartas(i)) {
	  			return jugador2;		}
		}
		return jugador1;
	}
  		
/**
 * Estan pensados para saber cual de los dos jugadores de cada pareja tienen mejores cartas para el lance chico
 * @return el jugador de la pareja que tiene mejor lanze chico
 */
	public Jugador whoIsChica(){
		for (int i=7;i>=0;i--) {
			if (jugador1.getValoresCartas(i)>jugador2.getValoresCartas(i)) {
				return jugador1;
				
			}if(jugador1.getValoresCartas(i)<jugador2.getValoresCartas(i)) {
				return jugador2;
			}
		}	
		return jugador1;
	}

/**
 * Coge el mejor valor para solventar el lance de juego de la pareja (tengan juego o no la pareja)
 * Recordar que juego es: {puntuacion del mejor jugador de la pareja, puntuacion del otro jugador, 1 o 2 (con 1 cuando el mejor jugador fue el jugador1 o 2 cuando fue el jugador2)}
 */
 	private void juegoDePareja() { 
		
		if (jugador1.getPuntos_Jugador() == jugador2.getPuntos_Jugador() ) { //Si ambos jugadores tienen la misma puntacion se pone cualquiera de los dos en el juego[2], en este caso el jugador1
			juego[0] = jugador1.getPuntos_Jugador();
			juego[1] = jugador2.getPuntos_Jugador();
			juego[2] = 1;
			return;
		}
		
		if (jugador1.getTieneJuego() == true && jugador2.getTieneJuego() == true ) { //Si ambos jugadores tienen juego (31,32,40,37,36,35,34 y 33)
			
			if (jugador1.getPuntos_Jugador() <= 32 || jugador2.getPuntos_Jugador() <= 32) { //Ambos jugadores tienen juego y ambos tienen 32 o 31
				
				if (jugador1.getPuntos_Jugador() < jugador2.getPuntos_Jugador() ) { //jugador1 tiene 31 y jugador2 tiene 32
					juego[0] = jugador1.getPuntos_Jugador();
					juego[1] = jugador2.getPuntos_Jugador();
					juego[2] = 1;
					return;
				} else { //jugador2 tiene 31 y jugador1 tiene 32
					juego[0] = jugador2.getPuntos_Jugador();
					juego[1] = jugador1.getPuntos_Jugador();
					juego[2] = 2;
					return;
				}
						
			} 
		}
		if ( jugador1.getPuntos_Jugador() > jugador2.getPuntos_Jugador() ) { //jugador1 y jugador2 tienen juego, aqui evaluamos los casos 40-33, por ende si jugador1>jugador2, jugador1 tiene mejor juego
			juego[0] = jugador1.getPuntos_Jugador();
			juego[1] = jugador2.getPuntos_Jugador();
			juego[2] = 1;
			return;
		} else { //El else del caso explicado anteriormente
			juego[0] = jugador2.getPuntos_Jugador();
			juego[1] = jugador1.getPuntos_Jugador();
			juego[2] = 2;
			return;
		}
		
	}

//------------------------------------------------------ GETTERS y/o SETTERS -----------------------------------------------------------
	
	//Geters y Setters de la clase Pareja, estos permiten obtener(getter) o modificar(setters) los diferentes atributos de la clase pareja
	
/**
 * Getter del ID de la pareja  
 * @return id de pareja
 */
	public int getIdpareja(){       
        return this.idpareja;
    }
     
/**
 * Getter del nombre de la pareja
 * @return el nombre de la pareja
 */
	public String getNombrepareja(){ 
        return this.nombrePareja;
    }
	
/**
 * Getter de los puntos de la pareja en conjunto 
 * @return los puntos de la pareja (puntos del jugador 1 sumados con los del jugador 2 de la pareja)
 */
	public int getPuntospareja(){ 
        return this.puntos_pareja;
    }
	
/**
 * calcula la suma de los puntos del jugador 1 y del jugador 2 para la pareja
 */
	public void calculaSumapareja() {
	int calcula;
	  
	calcula = jugador1.getPuntos_Jugador() + jugador2.getPuntos_Jugador();
	this.puntos_pareja=calcula;
	
}	


/**
 * Getter de el vector juego, donde encontramos quien de las dos parejas tiene mejor lance juego, 
 * @param i i=0, vemos la puntacion del jugador1. i=1, vemos la puntacion del jugador2. i=3, vemos 1, si el jugador1 tiene mejor juego y 2 en caso contrario
 * @return los puntos de la pareja selecionada por el indice 
 */
	public int getJuego(int i) {
		return juego[i];
	
	}

/**
 * Getter id del jugador1 de la pareja
 * @return el id en entero
 */
	public int getID1() {
		return this.id1;
	}

	
/**
 * Getter id del jugador2 de la pareja 
 * @return el id en entero
 */
	public int getID2() {
		return this.id2;
	}

/**
 * Getter de un jugador de la pareja 
 * @param i , si i=1, retorna al jugador1, en caso contraio al jugador2
 * @return jugador1 o jugador2 dependiendo del parametro i
 */
	public Jugador retornaJugador(int i) {
		if (i == 1) return jugador1;
		return jugador2;
	}

/**
 * Reinicia las piedras, tanto del jugador1 como las del jugador2 de la pareja 
 */
	public void reiniciarPiedras() {
		this.jugador1.reiniciarPiedras();
		this.jugador2.reiniciarPiedras();
	}


// --------------------------------------------------- Metodo Auxiliar -----------------------------------------

/**
 * Calcula los puntos de la pareja y dice que jugadores de la pareja tienen mejores lances
 */
	public void parejaAux() {
		this.calculaSumapareja ();
		this.juegoDePareja();
	}

/**
 * Sobreescritura de compareTo para ordenar las parejas de menor a mayor por su id de pareja (solo se utiliza en el modo 3 en comando DumpPlayers)
 */
	@Override
	public int compareTo(Pareja o) {
		return this.idpareja - o.idpareja;
	}

}


		

	  
	

