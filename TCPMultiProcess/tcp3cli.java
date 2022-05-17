
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
//Las siguientes tres libs son para poder implementar temporizador:
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.io.*;
//Librerias para cliente UDP
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//Librerias nio:
import java.nio.*;
import java.nio.channels.*;


public class tcp3cli {

    
    public static void main(String[] args) throws Exception{

    //--------------------------- MODO UDP ---------------------------------------------

    	if (args.length == 3){ 

    		String ip = args[0]; //Ip del servicio 
	        int puerto = Integer.parseInt(args[1]); //Puerto del servicio

	        try {

	        	//Pedimos por teclado el número a enviar:
	        	Scanner reader = new Scanner(System.in);
	        	System.out.println(" ");
	        	System.out.println("--------MODO UDP--------");
	        	System.out.print("Introduzca numero a enviar (o unicamente el 0 en caso de querer salir): ");
	        	int number = reader.nextInt();

	        	//Si el numero ingresado es "0", cerramos cliente:
	        	if(number==0) 
	        	{
	        		System.out.println(" ");
	        		System.out.println("Programa finalizado.");
	        	}
	        	
	        	//Si no es "0" lo enviamos al servidor:
	        	else
	        	{
	        		//Abrimos el socket
		        	DatagramChannel socket = DatagramChannel.open();
		        	InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, puerto);
		                 
		            ByteBuffer bufS = ByteBuffer.allocate(1024); //Buffer Send
		            ByteBuffer bufR = ByteBuffer.allocate(1024); //Buffer Receive

		            bufS.clear();
		        	bufS.putInt(0,number);
		        	socket.send(bufS, inetSocketAddress);
		       
		        	//esperamos resultado del acumulador:
		        	bufR.clear();
		        	socket.receive(bufR);
		        	int resultado = bufR.getInt(0);  
		            
		            System.out.println("Acumulador="+resultado);
		        	System.out.println("------------------------------");
		            
		            socket.close();
		            System.out.println(" ");
		            System.out.println("Programa finalizado.");
		        }

	        } //FIN TRY UDP

	          catch (Exception e) 
	          {
	            System.out.println("No hay respuesta del servidor.");
	          }


} // FIN IF UDP




	//--------------------------- MODO TCP --------------------------------------------
		else if (args.length == 2){

			String ip = args[0]; //Ip del servicio 
	        int puerto = Integer.parseInt(args[1]); //Puerto del servicio
	        int number = 0;

	        try {

		        SocketChannel soc = SocketChannel.open();
		        //soc.configureBlocking(false); //SOLO ES NO BLOQUIANTE EN EL SERVIDOR!!
		        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, puerto);
		        soc.connect(inetSocketAddress);	        

			    //Scanner para leer numeros:
			    Scanner reader = new Scanner(System.in);
			        	
			    //Si el numero ingresado es "0", cerramos cliente
			    //Si no es "0" lo enviamos al servidor y pedimos número otra vez hasta que sea 0:
			    System.out.println(" ");
			    System.out.println("--------MODO TCP--------");

			    while(true) 
			    {
			        //Pedimos por teclado el número a enviar:
			        System.out.print("Introduzca numero a enviar (o unicamente el 0 en caso de querer salir): ");
			        number = reader.nextInt();
			        if(number==0) break;

			        //enviamos numero como int:
			        ByteBuffer bufS = ByteBuffer.allocate(1024); //Buffer Send
			        ByteBuffer bufR = ByteBuffer.allocate(1024); //Buffer Receive
			        
			        bufS.clear();
			        bufS.putInt(0,number);
			        soc.write(bufS);
			       
			        //esperamos resultado del acumulador:
			        bufR.clear();
			        soc.read(bufR); 
			        int resultado = bufR.getInt(0);  
			        	   
			        System.out.println("Acumulador="+resultado);
			        System.out.println("------------------------------");
			    }

			    soc.close();
			    System.out.println(" ");
			    System.out.println("Programa finalizado.");
		}//FIN TRY TCP

		catch (Exception e) 
	          {
	            System.out.println("No hay respuesta del servidor.");
	          }
		        	
	        	
	        }//FIN IF TCP

	       

	//--------------------------- SINTAXIS INCORRECTA --------------------------------------------

	   else System.out.println("La sintaxis correcta para iniciar el cliente: $ java udpcli IP_servidor puerto_destino [-u]");

}
}





