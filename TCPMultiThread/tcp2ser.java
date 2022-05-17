//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;


public class tcp2ser {

   
    public static void main(String[] args) {
    	if (args.length == 1){
       
        int puerto = Integer.parseInt(args[0]);


        try {
            
            ServerSocket soc = new ServerSocket(puerto);
            
            while (true) {
               
                System.out.println("Preparado para conectar...");
                System.out.println("");

                //Espero que el cliente se conecte:
                Socket soc_aux = soc.accept();
                System.out.println("Cliente conectado.");
                System.out.println("");
                proccesHijo PH = new proccesHijo(soc_aux);
                PH.start();

            }//Fin primer while

        } catch (Exception e) {
            e.printStackTrace();
        }
    } else System.out.println("La sintaxis correcta para iniciar el servidor: $ java udpser puerto");

    }

}

 class proccesHijo extends Thread {

    Socket soc_aux;

    //Constructor:
    public proccesHijo(Socket soc_aux){
        this.soc_aux = soc_aux;
    }

    public void run(){

                int acul=0;
                DataInputStream in;
                DataOutputStream out;

                while(true){

                    try{
                        in = new DataInputStream(soc_aux.getInputStream());
                        out = new DataOutputStream(soc_aux.getOutputStream());
                        //Leo el mensaje que envia (en UTF, String):
                        String mensaje_string = in.readUTF();
                        System.out.println("Numeros recibidos: " + mensaje_string);

                        String [] numbers = mensaje_string.split(" ");

                        for(int i=0; i<numbers.length; i++){

                            int number = (int)Double.parseDouble(numbers[i]);
                            System.out.print("(Acul)"+acul+"+(Num)"+number+"=");
                            acul=acul+number;
                            System.out.println(acul);
                            
                        }
                       
                        //Enviamos el acumulador al cliente en formato INT:
                        out.writeInt(acul);
                    }//Fin try .read

                    catch(IOException e){
                        System.out.println("Cliente desconectado.");
                        System.out.println("");
                        break;
                    }
                 
                }//Fin segundo while
    }
}