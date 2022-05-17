/**
 * Esta clase define objetos de tipo carta, los cuales permitiran asiganarle valores segun las reglas del juego 
 * @author Christian Sparacino y Rodrigo Sifontes
 * @version 09/06/2020 
 */
public class Carta { //INICIO CLASE CARTA

//----------------------------------------------------------------------------------------------------------------
//Atributos clase carta
	
    private char denominacion; //1, 2, 3, 4, 5, 6 , 7, S, C, R 
    private char palo; //B, E, C, O
    private int puntos; //1, 4, 5, 6, 7, 10
    private String representacion;


//-------------------------------------------------------------------------------------------------------------
//Constructores clase carta
    
    /**
     * Constructor para crear una carta de las 40 posible combinaciones de la baraja
     * @param representacion Junta el palo y la denominacion en un solo String (5O, RE, 3B, 2C, etc)
     */
    public Carta (String representacion){
    	this.representacion=representacion;
        this.denominacion = representacion.charAt(0);
        this.palo = representacion.charAt(1);
        calculaPuntos();
        

    }

//-------------------------GETERS Y/O SETERS-------------------------------------------------------------------
   /**
    * Getter de representacion 
    * @return La representacion de una carta (denominacion y palo)
    */
    public String getRepresentacion() {  
    	return this.representacion;
    }
    
    /**
     * Getter de denominacion 
     * @return La denominacion de una carta
     */
    public char getDenominacion(){ 
        return this.denominacion;
    }
    
    /**
     * Getter de palo
     * @return El palo de una carta
     */
    public char getPalo(){ 
        return this.palo;
    }
    
    /**
     * Getter de puntos
     * @return Los puntos que vale una carta
     */
    public int getPuntos(){ 
        return this.puntos;
    }

//--------------------------------------------------------------------------------------------------------------------
   
    /**
     * Metodo que permite asignarle los puntos correspondientes que recibe una carta segun su denominacion 
     */
    private void calculaPuntos (){ 
    	switch(denominacion) { 
    	
    	case '1':
    	case '2':
    		puntos = 1;
    		break;
    	case '3':
    		puntos = 10;
    		break;
    	case '4':
    		puntos = 4;
    		break;
    	case '5':
    		puntos = 5;
    		break;
    	case '6':
    		puntos = 6;
    		break;
    	case '7':
    		puntos = 7;
    		break;
    	case 'S':
    	case 'C':
    	case 'R':
    		puntos = 10;
    		break;
    	
    	default:
    		break;
    	}
    	
    }

        
   
    }//FINAL CLASE CARTA