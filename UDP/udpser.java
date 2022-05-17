import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;


public class udpser {

   
    public static void main(String[] args) {
    	if (args.length == 1){
       
        int puerto = Integer.parseInt(args[0]);
        int acul=0;

        try {
           
            InetSocketAddress address = new InetSocketAddress(puerto);
            DatagramSocket socket = new DatagramSocket(address);
  
            while (true) {
               
                System.out.println("Preparado para recibir mensajes...");

                byte[] mensaje = new byte[64];
                DatagramPacket datagrama = new DatagramPacket(mensaje, mensaje.length);
                socket.receive(datagrama);  //Espera hasta que le llega un mensaje

                String mensaje_string = new String(mensaje);
                System.out.println("Números recibidos: " + mensaje_string);

                String [] numbers = mensaje_string.split(" ");

                for(int i=0; i<numbers.length; i++){

                    int number = (int)Double.parseDouble(numbers[i]);
                    if (number == 0) break; //Si se encuentra un 0 en la cadena se finaliza la observacion del mensaje 
                    System.out.print("Elemento "+i+"="+number+" --> ");
                    System.out.print("(Acul)"+acul+"+(Num)"+number+"=");
                    acul=acul+number;
                    System.out.println(acul);
                    
                }
               
                int puertoCliente = datagrama.getPort();
                InetAddress addressCliente = datagrama.getAddress();

                String acul_string = ""+acul;
                byte[] respuesta = acul_string.getBytes();
                DatagramPacket datagramaR = new DatagramPacket(respuesta, respuesta.length, addressCliente, puertoCliente);
                socket.send(datagramaR);
                 

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else System.out.println("La sintaxis correcta para iniciar el servidor: $ java udpser puerto");

    }

}