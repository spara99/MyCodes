import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Scanner;


public class udpcli {

    
    public static void main(String[] args) {
    	if (args.length == 2){


	        int puerto = Integer.parseInt(args[1]); //Puerto del servicio
	        String ip = args[0]; //Ip del servicio 
	        

	        try {

	        	//Pedimos por teclado el número a enviar:
	        	Scanner reader = new Scanner(System.in);
	        	System.out.println("Los números deben ir en una sola linea separados por espacios. Por ejemplo: 5 20 3 2");
	        	System.out.print("Introduzca número(s) a enviar (o únicamente el 0 en caso de querer salir): ");
	        	String number = reader.nextLine();

	        	//Si el numero ingresado es "0", cerramos cliente:
	        	if(number.equals("0")) {
	        		System.out.println("Programa finalizado.");
	        	}
	        	
	        	//Si no es "0" lo enviamos al servidor:
	        	else{
		            //Abrimos el socket
		            DatagramSocket socket = new DatagramSocket();
		            InetAddress address = InetAddress.getByName(ip);
		            //Temporizador de 10 segundo para esperar respuesta:
		            socket.setSoTimeout(10*1000);          

		            //Preparamos el Datagrama para enviar por UDP
		            DatagramPacket datagrama = new DatagramPacket(number.getBytes(), number.length(), address, puerto);
		            socket.send(datagrama);
		            System.out.println("Número(s) enviado(s).");
		            
		            //Espero la respuseta del servidor
		            System.out.println("Esperando respuesta del servidor...");
		            byte[] respuesta = new byte[64];
		            DatagramPacket datagramaR = new DatagramPacket(respuesta, respuesta.length);
		            socket.receive(datagramaR); //Espera bloqueante hasta que llega el mensaje
		            
		            String resultado = new String(respuesta);
		            System.out.println("El acumulador es: " +resultado );
		            
		            socket.close();
		        }

	        } //FIN TRY

	        catch (Exception e) {
	            System.out.println("No hay respuesta del servidor "+args[0]+":"+args[1]+" tras 10 segundos.");
	        }
	    } else System.out.println("La sintaxis correcta para iniciar el cliente: $ java udpcli IP_servidor puerto_destino");
    }

}