//https://www.baeldung.com/java-socket-connection-read-timeout
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
//Las siguientes tres libs son para poder implementar temporizador:
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.io.*;


public class tcp2cli {

    
    public static void main(String[] args) throws Exception{
    	if (args.length == 2){


	        int puerto = Integer.parseInt(args[1]); //Puerto del servicio
	        String ip = args[0]; //Ip del servicio 
	        DataInputStream in;
	        DataOutputStream out;
	        String number = "1";

	        Socket soc = new Socket();
	        SocketAddress socketAddress = new InetSocketAddress(ip, puerto);
	        

	        try {

	        	//Temporizador 15 segundos para conectarse a servidor:
	        	soc.connect(socketAddress, 15000); 

	        	try{
		        	in = new DataInputStream(soc.getInputStream());
		        	out = new DataOutputStream(soc.getOutputStream());

		        	//Scanner para leer numeros:
		        	Scanner reader = new Scanner(System.in);
		        	

		        	//Si el numero ingresado es "0", cerramos cliente
		        	//Si no es "0" lo enviamos al servidor y pedimos número otra vez hasta que sea 0:
		            System.out.println("Los numeros deben ir en una sola linea separados por espacios.");
		        	while(true) 
		        	{
		        		//Pedimos por teclado el número a enviar:
		        		System.out.println("");
		        		System.out.print("Introduzca numero(s) a enviar (o unicamente el 0 en caso de querer salir): ");
		        		number = reader.nextLine();
		        		if(number.equals("0")==true || number.substring(0,1).equals("0")==true) break;

		        		//enviamos cadena de numeros como string:
		        		out.writeUTF(number);
		        		//esperamos resultado del acumulador:
		        		int resultado = in.readInt();
		        	   
		        		System.out.println("Acumulador="+resultado);
		        		System.out.println("------------------------------");
		        	}

		        	soc.close();
		        	System.out.println("Programa finalizado.");
		        	
	        	
	        	}//FIN TRY 2

	        	catch(Exception a){
	        		System.out.println("Error en el servidor.");
	        	}

	        } //FIN TRY 1

	       catch (Exception e) {
	       	System.out.println("No hay respuesta del servidor "+args[0]+" , "+args[1]+" tras 15 segundos.");
	       }


	    } else System.out.println("La sintaxis correcta para iniciar el cliente: $ java udpcli IP_servidor puerto_destino");
    }

}