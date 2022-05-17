//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;


public class tcp1cli {

    
    public static void main(String[] args) {
    	if (args.length == 2){


	        int puerto = Integer.parseInt(args[1]); //Puerto del servicio
	        String ip = args[0]; //Ip del servicio 
	        //InetAddress address = InetAddress.getByName(ip);
	        DataInputStream in;
	        DataOutputStream out;
	        String number = "1";
	        

	        try {

	        	Socket soc = new Socket(ip, puerto);
	        	in = new DataInputStream(soc.getInputStream());
	        	out = new DataOutputStream(soc.getOutputStream());

	        	//Scanner para leer numeros:
	        	Scanner reader = new Scanner(System.in);
	        	

	        	//Si el numero ingresado es "0", cerramos cliente
	        	//Si no es "0" lo enviamos al servidor y pedimos número otra vez hasta que sea 0:
	        	while(true) 
	        	{
	        		//Pedimos por teclado el número a enviar:
	        		System.out.println("Los numeros deben ir en una sola linea separados por espacios. Por ejemplo: 5 20 3 2");
	        		//System.out.println("Nota: si se encuentra un 0 en la cadena se finaliza la observacion del mensaje en ese punto.");
	        		System.out.println("");
	        		System.out.print("Introduzca numero(s) a enviar (o unicamente el 0 en caso de querer salir): ");
	        		number = reader.nextLine();
	        		if(number.equals("0")==true || number.substring(0,1).equals("0")==true) break;

	        		//enviamos cadena de numeros como string:
	        		out.writeUTF(number);
	        		//esperamos resultado del acumulador:
	        		int resultado = in.readInt();

	        		System.out.println("Acumulador="+resultado);
	        		System.out.println("");
	        		System.out.println("------------------------------");
	        		System.out.println("");
	        	}

	        	soc.close();
	        	System.out.println("Programa finalizado.");
	        	
	        	


	        } //FIN TRY

	        catch (Exception e) {
	            System.out.println("No hay respuesta del servidor "+args[0]+":"+args[1]);
	        }
	    } else System.out.println("La sintaxis correcta para iniciar el cliente: $ java udpcli IP_servidor puerto_destino");
    }

}