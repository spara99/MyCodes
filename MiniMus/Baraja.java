import java.util.ArrayList;
import java.util.Collections;
/**
 * Esta clase define la baraja como el conjunto de las 40 cartas de la baraja espanola utilizada en el minimus
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020
 */
public abstract class Baraja {

//Atributo de Baraja
	
	static public ArrayList<Carta> listaCartas = new ArrayList<Carta>();
	
	

//-------------------------BORRA CUALQUIER OTRO MAZO EXISTENTE PARA QUE SOLO PUEDA HABER UNO CREADO-------------------------------------------------

/**
 * Metodo  que crea una baraja de 40 cartas espanolas.
 * Antes de crear una baraja, elimina cualquiera que pueda exister antes.
 * La baraja se alamcena en el ArrayList, listaCartas.
 */
	static public void crear() {
    
    listaCartas.clear();
    char c;
    char den='B';
    String representacion;
    int cont2 = 0;
	
	for (int j=1; j<=12; j++){
		  
        if (j==11){

           if(cont2==0) {den='C'; cont2++; j=1;}
           else if(cont2==1){ den='E'; cont2++; j=1;}
           else if(cont2==2) {den='O'; cont2++; j=1;}

        }
      
		if (j==8){
			c='S';
		}
		else if (j==9){
			c='C';
		}
		else if (j==10){
			c='R';
			if(cont2==3) j=12;
		}
        else{
        String aux = String.valueOf(j);
        c=aux.charAt(0);
    }
        StringBuilder sb = new StringBuilder();
        sb.append(c);
        sb.append(den);
        representacion=sb.toString();
       
		listaCartas.add(new Carta(representacion));
	
	}
		
	}
	

	
//--------------------------------------BARAJA LAS CARTAS ALEATORIAMENTE--------------------------------------------------------		

/**
 * El metodo crear, crea una baraja ordenada, de tal forma que barajar lo que hara es mezclar de forma aleatoria la baraja
 */
	static public void barajar() { 
		
		
		
		if (listaCartas.size()!=0) {
		Collections.shuffle(listaCartas);
		
		}
		
		
	}
	
//---------------------------------REPARTIR CARTAS-------------------------------------------------------------------------------------------------------------

/**
 * Metodo que, como su nombre indica, sirve para repartir de la baraja a los jugadores sus cuatro cartas correspondientes
 * @param pareja1 Pasamos la pareja1 el cual nos servira para asignarle a los jugadores que la conforman las cartas
 * @param pareja2 Pasamos la pareja2 el cual nos servira para asignarle a los jugadores que la conforman las cartas
 */
	static public void repartir(Pareja pareja1, Pareja pareja2) {
		pareja1.reiniciarPiedras(); //Pone a 0 las piedras de la pareja1
		pareja2.reiniciarPiedras(); //Pone a 0 las piedras de la pareja2
		
		pareja1.jugador1.setCartasAux(listaCartas.get(0), listaCartas.get(1), listaCartas.get(2), listaCartas.get(3));
		pareja1.jugador2.setCartasAux(listaCartas.get(8), listaCartas.get(9), listaCartas.get(10), listaCartas.get(11));
		pareja2.jugador1.setCartasAux(listaCartas.get(4), listaCartas.get(5), listaCartas.get(6), listaCartas.get(7));
		pareja2.jugador2.setCartasAux(listaCartas.get(12), listaCartas.get(13), listaCartas.get(14), listaCartas.get(15));
		
		pareja1.parejaAux(); //Entre parejas se ve que puntacion y lances tienen 
		pareja2.parejaAux();
		
		for(int i=0; i<16; i++) { //remueve las cartas de la baraja al repartirlas
			listaCartas.remove(0);
		}
	}
	
		

//--------------------------------AGREGAR CARTAS----------------------------------------------------------------------------------------------------------

/**
 * Agrega una carta en especifico a la baraja
 * @param carta Una unica carta con su respectiva representacion 
 */
	static public void agregar(Carta carta) {
		listaCartas.add(carta);
	}
	
//--------------------------------VACIA LA BARAJA------------------------------------------

/**
 * Borra cualquier baraja existente 
 */
	static public void vaciar() {
		listaCartas.clear();
	}
	
}