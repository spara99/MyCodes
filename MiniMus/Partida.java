import java.io.*;
import java.util.ArrayList;


/**
 * Clase Partida, donde ejecutamos los modos de juego y sus respectivos lances
 * @author Christian Sparacino y Rodrigos Sifontes
 * @version 09/06/2020
 */
 
public abstract class Partida {

//------------------------------------ ATRIBUTOS ----------------------------------------------------------------------------------
	
	//Nombre de las parejas
	protected Pareja pareja1, pareja2; 

	
	//Piedras de los lances
	protected static int piedras[] = {0,0}; // piedras[0] = piedras de la pareja1, piedras[1] = piedras de la pareja2. Este array sera la suma de todas las piedras que vayan ganando las parejas

	protected static ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
	
	protected static ArrayList<Pareja> parejas = new ArrayList<Pareja>();
	
		
//------------------------------------- METODOS DE LA CLASE ------------------------------------------------------------------------------------------------		

/**
 * Metodo para que la mano se vaya corriendo de jugador a jugador 	
 */
	protected void correrMano() {
        if (pareja1.jugador1.getMano() == true){
            pareja1.jugador1.setManoAux(false);
            pareja2.jugador1.setManoAux(true);
        } else if (pareja2.jugador1.getMano() == true){
            pareja2.jugador1.setManoAux(false);
            pareja1.jugador2.setManoAux(true);
        } else if (pareja1.jugador2.getMano() == true){
            pareja1.jugador2.setManoAux(false);
            pareja2.jugador2.setManoAux(true);
        } else if (pareja2.jugador2.getMano() == true){
            pareja2.jugador2.setManoAux(false);
            pareja1.jugador1.setManoAux(true);
        }
    }

/**
 * Metodo que permite poner en falso el atributo mano de todos los jugadores; a traves de setManoAux(false)
 */
	private void reiniciarMano() {
		pareja1.jugador1.setManoAux(false);
		pareja1.jugador2.setManoAux(false);
		pareja2.jugador1.setManoAux(false);
		pareja2.jugador2.setManoAux(false);
	}
	
/**
 * llamada a los constructores auxileares
 * @param manoIni segun este parametro, asignamos a un jugador de la pareja1 o pareja2, la mano 
 */
	protected void setManoIni(int manoIni) {   // Asigna la mano inicial de cualquier partida
		reiniciarMano();
		if (manoIni == 1) pareja1.jugador1.setManoAux(true);
		if (manoIni == 2) pareja2.jugador1.setManoAux(true);
		if (manoIni == 3) pareja1.jugador2.setManoAux(true);
		if (manoIni == 4) pareja2.jugador2.setManoAux(true);
		return;
	}
	
/**
 * Genera cartas y por lo tanto jugadas aleatorias
 * @param repeticiones entero que definira cuantas jugadas aleatorias se hacen 
 */
	protected void generarJugadaAleatoria(int repeticiones) {
		while(repeticiones>0) {
			Baraja.crear();
			Baraja.barajar();
			Baraja.repartir(pareja1, pareja2);
			System.out.println(generarStringDeJugada());
			Baraja.vaciar();
			repeticiones--;
		}
	}

/**
 * Adapta la informacion de una jugada ya generada, a formato string. Ejemplo: -(7O, 3C, 3E, 2E)-(RB, RE, 3O, 3C)*(6O, 4C, 3B, 1O)-(RE, RC, 6C, 7B)
 * @return la cadena con las jugadas representadas como el ejemplo de arriba
 */
	protected String generarStringDeJugada() { 
		
		String cadenaAux ="";
		
		if(pareja1.jugador1.getMano() == true) cadenaAux = (cadenaAux + "*(");
		else cadenaAux = (cadenaAux + "-(");
				
		for(int j = 0; j < 4; j++){
			cadenaAux= (cadenaAux+pareja1.jugador1.getCarta(j).getRepresentacion()+", ");
		}
		cadenaAux = cadenaAux.substring(0,(cadenaAux.length()-2));      // Esto es para eliminar el ultimo ", " que se nos genera
		
		if(pareja2.jugador1.getMano() == true) cadenaAux = (cadenaAux + ")*(");
		else cadenaAux = (cadenaAux + ")-(");
		
		for(int j = 0; j < 4; j++){
			cadenaAux= (cadenaAux+pareja2.jugador1.getCarta(j).getRepresentacion()+", ");
		}
		cadenaAux = cadenaAux.substring(0,(cadenaAux.length()-2));      // Esto es para eliminar el ultimo ", " que se nos genera
		
		if(pareja1.jugador2.getMano() == true) cadenaAux = (cadenaAux + ")*(");
		else cadenaAux = (cadenaAux + ")-(");
		
		for(int j = 0; j < 4; j++){
			cadenaAux= (cadenaAux+pareja1.jugador2.getCarta(j).getRepresentacion()+", ");
		}
		cadenaAux = cadenaAux.substring(0,(cadenaAux.length()-2));      // Esto es para eliminar el ultimo ", " que se nos genera
		
		if(pareja2.jugador2.getMano() == true) cadenaAux = (cadenaAux + ")*(");
		else cadenaAux = (cadenaAux + ")-(");
		
		for(int j = 0; j < 4; j++){
			cadenaAux= (cadenaAux+pareja2.jugador2.getCarta(j).getRepresentacion()+", ");
		}
		cadenaAux = cadenaAux.substring(0,(cadenaAux.length()-2))+")";      // Esto es para eliminar el ultimo ", " que se nos genera
		return cadenaAux;
	}
	
/**
 * Metodo que nos permite saber quien es mano en un momento determinado  
 * @return el objeto jugador que sea mano al momento de ser llamada la funcion	
 */
	protected Jugador whoIsMano() {
		if (pareja1.jugador1.getMano()) return pareja1.jugador1;
		if (pareja1.jugador2.getMano()) return pareja1.jugador2;
		if (pareja2.jugador1.getMano()) return pareja2.jugador1;
		return pareja2.jugador2; // Si ninguno de los anteriores ha dado positivo, asumimos que este lo es. (Se ha puesto de esta manera porque eclipse pone error de no haber un return general
	}

/**
 * Llama a las funciones de todos los lances y las ejecuta (modo 1 y 2) imprimiendo a la vez por pantalla los resultados de cada lance
 * @param formato funciona para imprimir una cadena de caracteres entre la impresion de cada lance con sus puntuaciones 
 */
	protected void resolverLances(String formato) {
		Grande();
		System.out.print("Grande "+pareja1.whoIsGrande().getPiedras(0)+" "+pareja2.whoIsGrande().getPiedras(0));	
		System.out.print(formato);
		Chica();
		System.out.print("Chica "+pareja1.whoIsChica().getPiedras(1)+" "+pareja2.whoIsChica().getPiedras(1));
		System.out.print(formato);
		Pares();
		System.out.print("Pares "+(pareja1.jugador1.getPiedras(2)+pareja1.jugador2.getPiedras(2))+" "+(pareja2.jugador1.getPiedras(2)+pareja2.jugador2.getPiedras(2)));
		System.out.print(formato);
		Juego();
		System.out.print("Juego "+(pareja1.jugador1.getPiedras(3)+pareja1.jugador2.getPiedras(3))+" "+(pareja2.jugador1.getPiedras(3)+pareja2.jugador2.getPiedras(3)));
	}
	
/**
 * 	Retorna la puntuacion de la suma de todas las piedras hasta el momento de su invocacion
 * @return retorna un string del formato "a b" siendo a la puntuacion de la PA y b la puntuacion de la PB
 */
	protected String puntuacionLive() {
		return (piedras[0]+" "+piedras[1]);
	}
	
	
//------------------------- METODOS LANCES PARA GANAR PIEDRAS --------------------------------------------------------------------------------------------------------		
	
/**
 * Resuelve las piedras para el lance Grande
 */
 	protected void Grande(){
		
 		for (int i=0;i<8;i++) {
			if (pareja1.whoIsGrande().getValoresCartas(i)>pareja2.whoIsGrande().getValoresCartas(i)) {
				pareja1.whoIsGrande().sumaPiedras(0,3);
				pareja2.whoIsGrande().sumaPiedras(0,0);
				piedras[0]= piedras[0] + pareja1.whoIsGrande().getPiedras(0);
				return;
			
			}else if(pareja1.whoIsGrande().getValoresCartas(i)<pareja2.whoIsGrande().getValoresCartas(i)) {
				pareja2.whoIsGrande().sumaPiedras(0,3);
				pareja1.whoIsGrande().sumaPiedras(0,0);
				piedras[1]= piedras[1] + pareja2.whoIsGrande().getPiedras(0);
				return;
			}
		}	
		pareja1.whoIsGrande().sumaPiedras(0,1);
		pareja2.whoIsGrande().sumaPiedras(0,1);
		
		piedras[0]= piedras[0] + pareja1.whoIsGrande().getPiedras(0);
		piedras[1]= piedras[1] + pareja2.whoIsGrande().getPiedras(0);
		return;
	}

/**
 * Resuelve las piedras para el lance Chica
 */
	protected void Chica(){
		for (int i=7;i>=0;i--) {
			if (pareja1.whoIsChica().getValoresCartas(i)>pareja2.whoIsChica().getValoresCartas(i)) {
				pareja1.whoIsChica().sumaPiedras(1,3);
				pareja2.whoIsChica().sumaPiedras(1,0);
				piedras[0]= piedras[0] + pareja1.whoIsChica().getPiedras(1);
				return;
			}else if(pareja1.whoIsChica().getValoresCartas(i)<pareja2.whoIsChica().getValoresCartas(i)) {
				pareja2.whoIsChica().sumaPiedras(1,3);
				pareja1.whoIsChica().sumaPiedras(1,0);
				piedras[1]= piedras[1] + pareja2.whoIsChica().getPiedras(1);
				return;
			}
		}		
		pareja1.whoIsChica().sumaPiedras(1,1);
		pareja2.whoIsChica().sumaPiedras(1,1);
		
		piedras[0]= piedras[0] + pareja1.whoIsChica().getPiedras(1);
		piedras[1]= piedras[1] + pareja2.whoIsChica().getPiedras(1);
		return;
	}	
	
/**
 * Resuelve las piedras para el lance Pares
 */
	protected void Pares() {
		piedras[0] = piedras[0] + pareja1.jugador1.getPiedras(2) + pareja1.jugador2.getPiedras(2);
		piedras[1] = piedras[1] + pareja2.jugador1.getPiedras(2) + pareja2.jugador2.getPiedras(2);
	}
	
/**
 * Resuleve las piedras para el lance Juego 
 */
	protected void Juego() {
		
		if (pareja1.getJuego(0) == pareja2.getJuego(0)) {                                                                  // Contemplamos que los mejores de cada pareja sean iguales
			if (pareja1.getJuego(1) == pareja2.getJuego(1)) {                                                              // Si los dos peores tambien son iguales
				if (pareja1.jugador1.getMano() == true || pareja1.jugador2.getMano() == true) {							   // Gana la pareja que tenga al jugador mano
					pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
				} else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
			} 
			
			else {
				if (pareja1.getJuego(1) >= 31 && pareja2.getJuego(1) >= 31) {                                              // Contemplamos el caso en el que los dos peores tengan tambien juego
					if (pareja1.getJuego(1) <= 32 || pareja2.getJuego(1) <= 32) {                                          // Y, por consecuencia, contemplamos el caso en que tenga alguno tenga 31 o 32
						if (pareja1.getJuego(1) < pareja2.getJuego(1) ) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
						else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
					} 
							
			}	
				
				
				else if(pareja1.getJuego(1) >=31 || pareja2.getJuego(2) >=31){ 
					if (pareja1.getJuego(1) > pareja2.getJuego(1)) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2); // En el caso de que los dos no tengan juego, bastaria con comparar quien es mayor
						else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
				} else { 
					if (pareja1.jugador1.getMano() == true || pareja1.jugador2.getMano() == true) {							   // Gana la pareja que tenga al jugador mano
					pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
				} else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
					
				}
				
			}
			
		} 
		
		else {                                                                                                           // Si no son iguales los mejores, entramos en este else
			if (pareja1.getJuego(0) >= 31 && pareja2.getJuego(0) >= 31) {                                                  // Contemplamos el caso de que ambos tengan juego
				if (pareja1.getJuego(0) <= 32 || pareja2.getJuego(0) <= 32) {                                              // Y, al igual que antes, si alguno es 31 o 32
					if (pareja1.getJuego(0) < pareja2.getJuego(0) ) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
					else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);;
					}
				
				else {
                    if (pareja1.getJuego(0) > pareja2.getJuego(0) ) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
                    else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
                }
			} 
			
			else {                                                                                                       // En el caso que solo uno o ninguno sea juego, simplemente con saber quien es mayor podemos saber quien gana
				if (pareja1.getJuego(0) > pareja2.getJuego(0)) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3,2);
				else pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3,2);
			}
		}
		
		if (pareja1.jugador1.getTieneJuego() == false && pareja2.jugador1.getTieneJuego() == false && pareja1.jugador2.getTieneJuego() == false && pareja2.jugador2.getTieneJuego() == false) { // En el caso de que ninguno de los jugadores tenga juego, se decide por quien tenga la mayor puntuacion pero solo sumaria una piedra el equipo ganador
			if (pareja1.retornaJugador(pareja1.getJuego(2)).getPiedras(3) > 0) pareja1.retornaJugador(pareja1.getJuego(2)).sumaPiedras(3, -1);                                                                                                                  // Por lo que nos valen perfectamente los algoritmos anteriores para determinar que pareja ganaria, solo tendriamos que restarle un 1 a su puntuacion
			if (pareja2.retornaJugador(pareja2.getJuego(2)).getPiedras(3) > 0) pareja2.retornaJugador(pareja2.getJuego(2)).sumaPiedras(3, -1);
		}			
		piedras[0] = piedras[0] + pareja1.jugador1.getPiedras(3) + pareja1.jugador2.getPiedras(3);
		piedras[1] = piedras[1] + pareja2.jugador1.getPiedras(3) + pareja2.jugador2.getPiedras(3);
	
		return;
	}
	
	
 // ------------------------------- LECTORES DE FICHEROS ---------------------------------------------------------------------------------------------------------
	
/**
 * Lee ficheros de texto completos y los retorna en un string
 * @param archivo . txt que queramos leer
 * @return en un String con la informacion volcada del archivo .txt 
 * @throws IOException manejo de ficheros
 */
	protected String lectorFicheros(String archivo) throws IOException {

		String cadena;
		String text="";
        FileReader f= new FileReader(archivo);
        BufferedReader b= new BufferedReader(f);
        
        while((cadena =b.readLine())!=null){ 
        	text = text + cadena + "\n";
        }
        
        b.close();
        return text;
	}
	
/**
 * Lee fichero de cartas (jugadas) para el modo 2
 * @param ficheroC fichero .txt que incluye las jugadas preestablecidas
 * @throws IOException manejo de ficheros
 */
	protected void leerFicheroCartasModo2(String ficheroC) throws IOException {
		
		ficheroC = lectorFicheros(ficheroC);
		leerCartas(ficheroC);
		return;
	}

/**
 * Lee un string con una o varias lineas de jugadas
 * @param lineasJugadas String anteriormente creado, donde tenemos la informacion de las jugadas pasadas por el fichero de entrada (ficheroC)
 */
	protected void leerCartas(String lineasJugadas) {
		
		int k=0;
		String [] jugadas = lineasJugadas.split("\n"); // Cada posicion del vector de string, es un string con una linea de una jugada
		for(int u=0; u<jugadas.length ; u++){
			while(k<60){
				for (int j=2;j<15;j= j+4) {
					if(jugadas[u].charAt((k+j)-2) =='*'  && u == 0) setManoIni((k/17)+1); // Asigna el jugador mano en vista del asterisco en la primera linea
					Baraja.agregar(new Carta(jugadas[u].substring( k+j, k+j+2 ) ) );
				}
				k = k + 17;
			}
			k=0;
		}
	}
	
/**
 * Lee ficheros de jugadores y parejas 
 * @param ficheroJ fichero .txt con informacion sobre datos de jugadores y parejas 
 * @return un entero que nos permite saber si se crearon 0 parejas, 1 o 2 y asi poder completarlas si asi es el caso en leerJugadoresModo1y2
 * @throws IOException manejo de ficheros
 */
	protected int leerJugadores(String ficheroJ) throws IOException {
		
		int auxC = 0; // Es un contador que nos permite saber cuando ya hemos creado las dos parejas del juego	
		try {
			String ficheroCompleto="";
			ArrayList<Jugador> jugadoresAux = new ArrayList<Jugador>(); 
			String[] cadena_div;
			int idPlayer = 0, idPareja = 0, id1 = 0, id2 = 0;
			
			
			
			ficheroCompleto=lectorFicheros(ficheroJ); //leemos el fichero y lo pasamos a un String (ficheroCompleto)
			String[] players = ficheroCompleto.split("\n"); //players es un array donde tendremos en cada posicion una linea del fichero 
			
				for(int i = 0; i < players.length; i++) { //recorre String[] players linea a linea por medio de i 
				
					if(players[i].charAt(0)=='J') { //verifica si el elemento del array String player comienza con una J (es un jugador)
						
						players[i]=players[i].substring(2,players[i].length()); //Quita la J y el espacio siguiente de la linea String. 0 es la J, 1 es el espacio
						cadena_div = players[i].split(" "); //separa lo que queda en player[i] por espacios y los mete en el array cadena_div
							
						idPlayer = Integer.parseInt(cadena_div[0]); // Convierte el ID de string a int
					
						players[i]="";		//Vaciamos el array players[i]	
					
							for(int k = 1;k< cadena_div.length;k++){ //k comienza en 1 para saltarse el id y llegar al nombre del jugador
								players[i] = players[i] + cadena_div[k] + " "; // Dejamos solamente el nombre en players i, removiendo ahora el ID. NOTA: el espacio " " es para que el nombre se copie con espacios en caso de ser mas de una palabra
								}
							
						players[i] = players[i].substring(0,(players[i].length()-1)); // Quitamos el " " espacio extra que deja el bucle for anterior 
							
			
						jugadores.add(new Jugador(idPlayer, players[i])); //agregamos al jugador con su id y nombre a la lista "jugadores"
						jugadoresAux.add(new Jugador(idPlayer, players[i])); // se usa mas adelante para la pareja, es lo mismo que arriba pero lo desecharemos al finalizar el metodo 
		
					idPlayer = 0;
					
				}  //FIN if verifica si la linea empieza por J (es un jugador)
				
				
				
				
				else if(players[i].charAt(0) == 'P') { //verifica si el elemento del array String player comienza con una P (es una pareja)
					players[i]=players[i].substring(2,players[i].length()); //Quita la P y el espacio siguiente de la linea String. 0 es la P, 1 es el espacio
					String[] couples = players[i].split(" ");	//separa lo que queda en player[i] por espacios y los mete en el array couples

					idPareja = Integer.parseInt(couples[0]); //convierte en entero el id de la pareja (couples[0])
					id1 = Integer.parseInt(couples[1]);		 //convierte en entero el id del jugador1 (couples[1])
					id2 = Integer.parseInt(couples[2]);		 //convierte en entero el id del jugador2 (couples[2])
					
					players[i]="";			
					
					for(int k = 3;k< couples.length;k++){	//k comienza en 3 para saltarse los 3 ids y llegar al nombre de la pareja
						players[i] = players[i] + couples[k] + " "; //Dejamos solamente el nombre de la pareja en players i, removiendo ahora el ID
					}
					players[i] = players[i].substring(0,(players[i].length()-1)); //Quitamos el espacio extra que deja el bucle for anterior
							
				
						Jugador jugadorAux = new Jugador(), jugadorAux2 = new Jugador(); //creamos dos jugadores sin atributos (constructor vacio de jugador)
						int f=0; // f es un contador q nos permite saber cuando ya hemos leido dos jugadores existentes previamente a la pareja y que coincidan de id
						
						for (int j=0; j < jugadoresAux.size() ; j++) { //jugadoresAux es la lista creada en el if anterior 
							if (jugadoresAux.get(j).getID() == id1) {  //vemos si el id del jugador corresponde con el que se encuentra en la pareja 
								jugadorAux = jugadoresAux.get(j);
								f++;
							}
							if (jugadoresAux.get(j).getID() == id2) { //vemos si el id del jugador corresponde con el que se encuentra en la pareja 
								jugadorAux2 = jugadoresAux.get(j);
								f++;
							}
								
						} //FIN FOR jugadoresAux
						
							if (f == 2) { 			//Si f=2 siginifica que se han agregado ambos jugadores a una pareja
								if(auxC == 1) {
								parejas.add(pareja2 = new Pareja(idPareja, players[i], jugadorAux, jugadorAux2)); //si auxC=1 significa que tenemos agregado al jugador1, procedemos a agregar el jugador2
								auxC++;
								}
								if (auxC == 0) {
								parejas.add(pareja1 = new Pareja(idPareja, players[i], jugadorAux, jugadorAux2)); //si auxC=0 significa que tenemos que agregar al jugador1
								auxC++;
								}
						
								for (int j=0; j < jugadoresAux.size() ; j++) {
									if (jugadoresAux.get(j) == jugadorAux) {
									jugadoresAux.remove(j);
									}
								}
								for (int j=0; j < jugadoresAux.size() ; j++) {
									if (jugadoresAux.get(j) == jugadorAux2) {
									jugadoresAux.remove(j);
									}
								}
						}
						f = 0;
						
					
					idPareja = 0;
					id1 = 0;
					id2 = 0;
										
				} //FIN else if pareja 
			} //FIN for que recorre el Strings players, que es donde tenemos toda la informacion del fichero
		} catch(NullPointerException e) {

		}
				return auxC;
		
		
}

/**
 * Lee fichero de jugadores y parejas para modo 1 y modo 2
 * @param ficheroJ fichero .txt con informacion sobre datos de jugadores y parejas 
 * @throws IOException manejo de ficheros
 */
	protected void leerJugadoresModo1y2(String ficheroJ) throws IOException {
		try { 
			int auxC = leerJugadores(ficheroJ); 
			if (auxC < 2) {
	            if (auxC == 1) {
	                pareja2 = new Pareja(444,"Pareja B",new Jugador(890,"Jugador 1B"),new Jugador(567,"Jugador 2B"));
	                auxC++;
	            } else if (auxC == 0) {
	                pareja1 = new Pareja(555,"Pareja A",new Jugador(333,"Jugador 1A"),new Jugador(999,"Jugador 2A"));
	                pareja2 = new Pareja(444,"Pareja B",new Jugador(890,"Jugador 1B"),new Jugador(567,"Jugador 2B"));
	            }
			}
		} 
		catch (FileNotFoundException e) {  
			pareja1 = new Pareja(1, "Pareja A", new Jugador(1, "Jugador 1A"), new Jugador(2, "Jugador 2A"));
			pareja2 = new Pareja(2, "Pareja B", new Jugador(3, "Jugador 1B"), new Jugador(4, "Jugador 2B"));
		}
		
	}
	
	
}
