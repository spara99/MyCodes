//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;


public class tcp1ser {

   
    public static void main(String[] args) {
    	if (args.length == 1){
       
        int puerto = Integer.parseInt(args[0]);
        int acul=0;
        //InetSocketAddress address = new InetSocketAddress(puerto);
        DataInputStream in;
        DataOutputStream out;
        boolean exceptionSocCli = false;
        
       

        try {
            
            ServerSocket soc = new ServerSocket(puerto);
            
            while (true) {
               
                System.out.println("Preparado para conectar...");

                //Espero que el cliente se conecte:
                Socket soc_aux = soc.accept();
                System.out.println("Cliente conectado.");
                acul=0;
                exceptionSocCli = false;
                in = new DataInputStream(soc_aux.getInputStream());
                out = new DataOutputStream(soc_aux.getOutputStream());

                while(exceptionSocCli==false){

                    try{
                        //Leo el mensaje que envia (en UTF, String):
                        String mensaje_string = in.readUTF();
                        System.out.println("Numeros recibidos: " + mensaje_string);

                        String [] numbers = mensaje_string.split(" ");

                        for(int i=0; i<numbers.length; i++){

                            int number = (int)Double.parseDouble(numbers[i]);
                            //if (number == 0) break; //Si se encuentra un 0 en la cadena se finaliza la observacion del mensaje 
                            System.out.print("Elemento "+i+"="+number+" --> ");
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
                        exceptionSocCli=true;
                    }
                 
                }//Fin segundo while
            }//Fin primer while

        } catch (Exception e) {
            e.printStackTrace();
        }
    } else System.out.println("La sintaxis correcta para iniciar el servidor: $ java udpser puerto");

    }

}