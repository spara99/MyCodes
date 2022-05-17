import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Clase que permite ejecutar el modo3 (Ejecucion de comandos) extiende de Modo2, que a su vez extiende de Partida, donde tenemos las herramientas para ejecutar los tres modos 
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */

public class Modo3 extends Modo2{

//----------------------------------------ATRIBUTOS------------------------------------------------------------------------------------------------------------------
		
	    static PrintStream Delivery;
	    
		static boolean DeliveryNoActivo=false; //Cuando esta a false significa que no se ha creado un delivery aun o se ha cerrado correctamente, cuando esta a true significa que esta abierto un Delivery
		
		
//--------------------------------------------CONSTRUCTOR MODO 3 - EJECUCION DE COMANDOS---------------------------------------------------------------------------------

/**
 * Constructor del Modo 3		
 * @param ficheroC Fichero .txt con los comandos a ejecutar 
 * @param out variable tipo printstream el cual define si se imprime los resultados en consola o en un fichero de salida (esto esta previamente arreglado en MiniMus)
 * @throws FileNotFoundException manejo de ficheros, no se encuentra
 * @throws IOException manejo de ficheros
 */
		public Modo3 (String ficheroC, PrintStream out) throws FileNotFoundException, IOException {
			super();
			modo3(ficheroC,out);
		}
	
		
/**
 * Invocacion del metodo que corre el Modo 3
 * @param ficheroC Fichero .txt con los comandos a ejecutar 
 * @param out variable tipo printstream el cual define si se imprime los resultados en consola o en un fichero de salida (esto esta previamente arreglado en MiniMus)
 * @throws IOException manejo de ficheros
 */
		private void modo3(String ficheroC, PrintStream out) throws IOException {
			int lineas, lineasRes;
			DeliveryNoActivo = true;
			String[] aux;
			String[] lineasComandos = leerFicheroModo3(ficheroC);
			lineas = lineasComandos.length;
			lineasRes = lineas;
			
			while(lineasRes > 0) {
				aux = lineasComandos[lineas - lineasRes].split(" "); //Coge una linea y las separa por sus parametros
						
				switch(aux[0]) {
				case "NewPlayer":
					ejecutarComando(newPlayer(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "DeletePlayer":
					ejecutarComando(deletePlayer(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "NewCouple":
					ejecutarComando(newCouple(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "DeleteCouple":
					ejecutarComando(deleteCouple(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "DumpPlayers":
					ejecutarComando(dumpPlayers(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "ResetPlayers":
					ejecutarComando(resetPlayers(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "LoadPlayers":
					ejecutarComando(loadPlayers(aux) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "GenerateRandomDelivery":
					ejecutarComando(generateRandomDelivery(aux, out) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "PlayGame":
					ejecutarComando(playGame(aux, out) , lineasComandos[lineas - lineasRes]);
					break;
					
				case "PlayHand":
					playHand(aux);
					break;
					
				case "ResolvePares":
					resolvePares(aux);
					break;
				
				case "ResolveJuego":
					resolveJuego(aux);
					break;
					
				case "ResolveGrande":
					resolveGrande(aux);
					break;
					
				case "ResolveChica":
					resolveChica(aux);
					break;
					
				case "StartDelivery":
					ejecutarComando(StartDelivery(aux), lineasComandos[lineas - lineasRes]);
					break;
					
				case "D":
					ejecutarComando(D(aux, out), lineasComandos[lineas - lineasRes]);
					break;
					
				case "EndDelivery":
					ejecutarComando(EndDelivery(), lineasComandos[lineas - lineasRes]);
					break;
				
				default:
					break;
		
				}
				lineasRes--;
			}
		}
		
//------------------------------------------ COMANDOS BLOQUE 3 -----------------------------------------------
		
/**
 * Anade un nuevo jugador a la lista de jugadores
 * @param aux tenemos: aux[0]=NewPlayer, aux[1]=id y aux[2]=nombre
 * @return false al no lograr anadirlo, true en caso contrario
 */
		private boolean newPlayer(String[] aux) {
			
			String nombre="";
			int idNewPlayer = Integer.parseInt(aux[1]); //Pasamos el id del jugador de String a Entero 
			for(int i=2; i<aux.length ;i++) nombre = nombre + aux[i] + " ";
			nombre = nombre.substring(0,(nombre.length()-1)); //borramos el " " espacio de mas que se crea en el bucle anterior
			try {
				
				for (int j=0; j<jugadores.size();j++) {
					if (jugadores.get(j).getID() == idNewPlayer) {
						return false;
					}
				}
				jugadores.add(new Jugador(idNewPlayer, nombre));
				return true;
				
			} catch(ArrayIndexOutOfBoundsException e) {  // Si en el momento de ejecutar el comando, la lista "jugadores" esta vacia, entra en este catch y crea el nuevo jugador
				jugadores.add(new Jugador(idNewPlayer, nombre));
				return true;
			}
		}
	 
/**
 * Borra un jugador existente de la lista de jugadores 
 * @param aux tenemos: aux[0]=deletePlayer, aux[1]=id 
 * @return false si ningun jugador coincidia en el ID a eliminar, true en caso contrario
 */
		private boolean deletePlayer(String[] aux) {
			try {
				for (int j=0; j<jugadores.size();j++) {
					if (jugadores.get(j).getID() == Integer.parseInt(aux[1])) {
						jugadores.remove(j);
						return true;
					}
				}
				return false;
				
			} catch(ArrayIndexOutOfBoundsException e) {  // Si en el momento de ejecutar el comando, la lista "jugadores" esta vacia, entra en este catch y e imprimira un FAIL
				return false;
			}
		}

/**
 * Anade una nueva pareja a la lista de parejas
 * @param aux tenemos: aux[0]=NewCouple, aux[1]=id, aux[2]=id_j1, aux[3]=id_j2 y aux[4]=nombre
 * @return false si no logra crear la pareja, true en caso contrario
 */
		private boolean newCouple(String[] aux) {
			String nombrePareja="";
			int idCouple = Integer.parseInt(aux[1]);
			int id1 = Integer.parseInt(aux[2]);
			int id2 = Integer.parseInt(aux[3]);
			for(int i=4; i<aux.length ;i++) nombrePareja = nombrePareja + aux[i] + " ";
			nombrePareja = nombrePareja.substring(0,(nombrePareja.length()-1));
			
			try {
				
				for (int j=0; j<parejas.size();j++) {
					if (parejas.get(j).getIdpareja() == idCouple) {
						return false;
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) {
			}
			try {
				
			for (int j=0;j<jugadores.size();j++) {
				if (jugadores.get(j).getID() == id1 && id1!=id2) {
					for (int k=0;k<jugadores.size();k++) {
						if (jugadores.get(k).getID() == id2) {
							parejas.add(new Pareja(idCouple, nombrePareja, jugadores.get(j), jugadores.get(k)));
							return true;
						}
						
					} return false;
				}
			} return false;
				
			} catch(ArrayIndexOutOfBoundsException e) {  // Si en el momento de ejecutar el comando, la lista "jugadores" esta vacia, entra en este catch y crea el nuevo jugador
				return false;
			}
		}

/**
 * Elimina un pareja de la lista de parejas		
 * @param aux tenemos: aux[0]=DeleteCouple, aux[1]=id
 * @return false si no logra borrar la pareja, true en caso contrario
 */
		private boolean deleteCouple(String[] aux) {
			try {
				for (int j=0; j<parejas.size();j++) {
					if (parejas.get(j).getIdpareja() == Integer.parseInt(aux[1])) {
						parejas.remove(j);
						return true;
					}
				}
				return false;
				
			} catch(ArrayIndexOutOfBoundsException e) {  // Si en el momento de ejecutar el comando, la lista "jugadores" esta vacia, entra en este catch y e imprimira un FAIL
				return false;
			}
		}
		
/**
 * Almacena todos los jugadores y parejas creadas anteriormente en un fichero
 * @param aux tenemos: aux[0]=DumpPlayers y aux[1]=Fichero_jugadores
 * @return false si no logra volcar la informacion en el fichero, true en caso contrario
 * @throws FileNotFoundException manejo de ficheros, no se encuentran
 */
		private boolean dumpPlayers(String[] aux) throws FileNotFoundException {
			String ficheroR = aux[1];
			PrintWriter ficherof =new PrintWriter(new FileOutputStream(ficheroR)); //nos permite poner ficherof.println y asi poder escribir en un fichero y no en consola
				
				Collections.sort(jugadores);	//ordena los jugadores por su id 	
				for (int i=0;i<jugadores.size();i++) {
					ficherof.println("J "+jugadores.get(i).getID()+" "+jugadores.get(i).getNombreu());	
				}
				Collections.sort(parejas); //ordena las parejas por su id
				for (int i=0;i<parejas.size();i++) {
				ficherof.println("P "+parejas.get(i).getIdpareja()+" "+parejas.get(i).getID1()+" "+parejas.get(i).getID2()+" "+parejas.get(i).getNombrepareja());	
				}
				
				ficherof.close(); //cerramos el fichero de salida
				return true;
		}

/**
 * Borra toda la informacion de jugadores y parejas generadas en esta sesion 		
 * @param aux tenemos: aux[0]=ResetPlayers
 * @return siempre true, luego de limpiar las listas de parejas y jugadores
 */
		private boolean resetPlayers(String[] aux) {
			parejas.clear();
			jugadores.clear();
			return true;
		}
		
/**
 * Carga en memoria la informacion de jugadores y parejas contenidas en el fichero_jugadores
 * @param aux tenemos aux[0]=LoadPlayers y aux[1]=fichero_jugadores
 * @return false si no existe el fichero, true en caso de exista el fichero y lo lea
 * @throws IOException manejo de ficheros
 */
		private boolean loadPlayers(String[] aux) throws IOException {
			try {
			leerJugadores(aux[1]);
			return true;
			}
			catch(FileNotFoundException e) {
				return false;
			}
		}
		
/**
 * Se genera un fichero con un total de n jugadas obtenidas aleatoriamnete 
 * @param aux tenemos: aux[0]=n_jugadas, aux[1]=id_mano, aux[2]=fichero_jugadas
 * @param out tipo PrintStream, nos servira para que luego de cambiar la salida de escritura para el fichero_jugadas, vuelva a la salida con la que cargamos el modo3
 * @return false si no puede escribir el fichero especificado, true en caso contrario
 * @throws IOException manejo de ficheros
 */
		private boolean generateRandomDelivery(String[] aux,  PrintStream out) throws IOException { 
			
			leerJugadoresModo1y2(""); //Al no pasarle un fichero, creara dos parejas por defaults
			PrintStream outAux = new PrintStream(new FileOutputStream(aux[3]));
			System.setOut(outAux); //Se cambia el System.out por el fichero solicitado para imprimir
			setManoIni(Integer.parseInt(aux[2]));
			generarJugadaAleatoria(Integer.parseInt(aux[1])); 
			System.setOut(out); //Volvemos a cambiar el System.out para seguir imprimiendo por la salida con la que cargamos el modo 3	
			return true;
		}
		
/**
 * Se juega una partida con fichero_jugadas. Estas se jugaran si existen jugadores y parejas creadas segun el formato o crearemos unas por defecto	
 * @param aux tiene: aux[0]=fichero_jugadas y aux[1]=fichero_partida (fichero de salida)
 * @param out tipo PrintStream, nos servira para que luego de cambiar la salida de escritura para el fichero_jugadas, vuelva a la salida con la que cargamos el modo3
 * @return false si no existen algunos de los ficheros que se pasaron por aux, true en caso contrario 
 * @throws IOException manejo de ficheros
 */
		private boolean playGame(String[] aux, PrintStream out) throws IOException {
			try {
				
				PrintStream outAux1 = new PrintStream(new FileOutputStream(aux[2]));
				System.setOut(outAux1); //Se cambia el System.out por el fichero solicitado para imprimir
				
				if (parejas.size() < 2) {
					pareja1 = new Pareja(1, "Pareja A", new Jugador(1, "Jugador 1A"), new Jugador(2, "Jugador 2A"));
					pareja2 = new Pareja(2, "Pareja B", new Jugador(3, "Jugador 1B"), new Jugador(4, "Jugador 2B"));
					} else {
						pareja1 = parejas.get(0);
						pareja2 = parejas.get(1);
					}
				leerFicheroCartasModo2(aux[1]);
				modo2();
				Baraja.vaciar();
				
				System.setOut(out); //Volvemos a cambiar el System.out para seguir imprimiendo por la salida con la que cargamos el modo 3
				return true;
				
				} catch(FileNotFoundException e) {
					System.setOut(out); //Volvemos a cambiar el System.out para seguir imprimiendo por la salida con la que cargamos el modo 3
					return false;
				}
		
		}

/**
 * Se resuelven los cuatro lances para la jugada pasada por el fichero 		
 * @param aux tiene: aux[0]=jugada
 * @throws IOException manejo de ficheros
 */	
		private void playHand(String[] aux) throws IOException { 
			String jugada = leerJugadaModo3(aux);
			Baraja.repartir(pareja1,pareja2);
			
			//Imprimir la linea de los resultados de los lances
			System.out.print("PlayHand "+jugada+": ");
			resolverLances(", ");
			System.out.println();
			return;
		}
	
/**
 * Resuelve el lanzamiento pares para una jugada	
 * @param aux tiene: aux[0]=ResolvePares y aux[1]=Fichero_Jugada
 * @throws IOException manejo de ficheros
 */
		private void resolvePares(String[] aux) throws IOException {
			String jugada = leerJugadaModo3(aux);
			Baraja.repartir(pareja1,pareja2);
			
			//Imprimir la linea de los resultados de los lances
			Pares();
			System.out.print("ResolvePares "+jugada+": ");
			System.out.print(pareja1.jugador1.getPiedras(2)+" "+pareja2.jugador1.getPiedras(2)+" "+pareja1.jugador2.getPiedras(2)+" "+pareja2.jugador2.getPiedras(2));
			System.out.print(" - "+(pareja1.jugador1.getPiedras(2)+pareja1.jugador2.getPiedras(2))+" "+(pareja2.jugador1.getPiedras(2)+pareja2.jugador2.getPiedras(2)));
			System.out.println();

			return;
		}
		
/**
 * Resuelve el lanzamiento juego para una jugada		
 * @param aux tiene: aux[0]=ResolveJuego y aux[1]=Fichero_Jugada
 * @throws IOException manejo de ficheros
 */
		private void resolveJuego(String[] aux) throws IOException {
			String jugada = leerJugadaModo3(aux);
			Baraja.repartir(pareja1,pareja2);
			
			//Imprimir la linea de los resultados de los lances
			Juego();
			System.out.print("ResolveJuego "+jugada+": ");
			System.out.print(pareja1.jugador1.getPiedras(3)+" "+pareja2.jugador1.getPiedras(3)+" "+pareja1.jugador2.getPiedras(3)+" "+pareja2.jugador2.getPiedras(3));
			System.out.print(" - "+(pareja1.jugador1.getPiedras(3)+pareja1.jugador2.getPiedras(3))+" "+(pareja2.jugador1.getPiedras(3)+pareja2.jugador2.getPiedras(3)));
			System.out.println();

			return;
		} 
		
/**
 * Resuelve el lanzamiento grande para una jugada		
 * @param aux tiene: aux[0]=ResolveGrande y aux[1]=Fichero_Jugada
 * @throws IOException manejo de ficheros
 */		
		private void resolveGrande(String[] aux) throws IOException {
			String jugada = leerJugadaModo3(aux);
			Baraja.repartir(pareja1,pareja2);
			
			//Imprimir la linea de los resultados de los lances
			Grande();
			System.out.print("ResolveGrande "+jugada+": ");
			System.out.print(pareja1.jugador1.getPiedras(0)+" "+pareja2.jugador1.getPiedras(0)+" "+pareja1.jugador2.getPiedras(0)+" "+pareja2.jugador2.getPiedras(0));
			System.out.print(" - "+pareja1.whoIsGrande().getPiedras(0)+" "+pareja2.whoIsGrande().getPiedras(0));
			System.out.println();
			return;
		}
		
/**
 * Resuelve el lanzamiento chica para una jugada		
 * @param aux tiene: aux[0]=ResolveChica y aux[1]=Fichero_Jugada
 * @throws IOException manejo de ficheros
 */
		private void resolveChica(String[] aux) throws IOException {
			String jugada = leerJugadaModo3(aux);
			Baraja.repartir(pareja1,pareja2);
			
			//Imprimir la linea de los resultados de los lances
			Chica();
			System.out.print("ResolveChica "+jugada+": ");
			System.out.print(pareja1.jugador1.getPiedras(1)+" "+pareja2.jugador1.getPiedras(1)+" "+pareja1.jugador2.getPiedras(1)+" "+pareja2.jugador2.getPiedras(1));
			System.out.print(" - "+pareja1.whoIsChica().getPiedras(1)+" "+pareja2.whoIsChica().getPiedras(1));
			System.out.println();
			return;
		}

/**
 * Abre un fichero_jugadas si no esta abierto ya uno 		
 * @param aux tiene: aux[0]=StartDelivery y aux[1]=fichero_jugadas
 * @return true si se creo el fichero, false si ya existe otro fichero creado en ese momento 
 */
		private boolean StartDelivery(String[] aux) {
			try {
				if (DeliveryNoActivo) {
				Delivery = new PrintStream(new FileOutputStream(aux[1]));
				DeliveryNoActivo = false;
				return true;
				} else return false;
			} catch(Exception e) {
				return false;
			}
		}
		
/**
 * Escribe en el fichero abierto en el comando StartDelivery, una jugada pasada por un fichero de entrada		
 * @param aux tiene: aux[0]=D y aux[1]=jugada
 * @param out tipo PrintStream, nos servira para que luego de cambiar la salida de escritura para el fichero_jugadas, vuelva a la salida con la que cargamos el modo3
 * @return true si el fichero existia anteriormente y se escribio correctamente todo, false en caso contrario 
 */
		private boolean D(String[] aux, PrintStream out) {
			try {
				if (DeliveryNoActivo == false) {
					String jugada = leerJugadaModo3(aux);
					
					for(int i=0;i<jugada.length();i=i+17) {
						if (jugada.charAt(i) != '*' && jugada.charAt(i) != '-') return false;
					}
					System.setOut(Delivery);
					System.out.println(jugada);
					System.setOut(out); //Volvemos a cambiar el System.out para seguir imprimiendo por la salida con la que cargamos el modo 3	
					return true;
				}else return false;
			} catch(Exception e) {
				
				return false;
			}
		}

/**
 * Cierra el fichero y no permite que se escriba mas nada en el mismo	
 * @return true si logro cerrar el fichero, false si no habia un fichero abierto
 */
		private boolean EndDelivery() {
			if (DeliveryNoActivo) return false;
			DeliveryNoActivo = true;
			return true;
		}
		
		

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
/**
 * Metodo para saber si un comando del modo3 se ha ejecutado correctamente o no 		
 * @param exito boolean si es true, "OK, si es false 
 * @param comando String que contiene el nombre del comando que se esta ejecutando 
 */			
		private void ejecutarComando(boolean exito, String comando) {
			if(exito == true) System.out.println(comando+": OK.");
			else System.out.println(comando+": FAIL.");
		}

/**
 * Permite leer los ficheros_jugadas que se necesiten para el modo 3 		
 * @param aux tiene: cadena completa del fichero.Ejemplo: PlayHand Jugada.txt
 * @return jugada que seria aux sin la primera palabra clave. Ejemplo: Jugada.txt
 * @throws IOException manejo de ficheros
 */
		private String leerJugadaModo3(String[] aux) throws IOException{
			String jugada="";
			for(int i=1; i<aux.length;i++) {
				jugada = jugada + aux[i] + " ";
			}
			jugada = jugada.substring(0,jugada.length()-1);
			
			leerJugadoresModo1y2("");
			leerCartas(jugada);
			return jugada;
		}

/**
 * Lee fichero de comandos para el modo3
 * @param ficheroC Fichero .txt con los comandos a ejecutar 
 * @return Un String[] donde cada posicion tiene guardada una linea del fichero de comandos
 * @throws FileNotFoundException manejo de ficheros, no se encuentra
 * @throws IOException manejo de ficheros
 */
		private String[] leerFicheroModo3(String ficheroC) throws FileNotFoundException, IOException {
			
			String ficheroCompleto="";

			ficheroCompleto=lectorFicheros(ficheroC);
	        return ficheroCompleto.split("\n"); 

		}
		
}
