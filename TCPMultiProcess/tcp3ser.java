//https://programmer.group/nio-selector-in-java-network-programming.html
//https://stackoverflow.com/questions/14802769/transmit-strings-through-bytebuffer
//https://stackoverflow.com/questions/14808373/how-to-get-remoteaddress-of-nio-datagramchannel

import java.net.*;
import java.util.*;
//Libreria NIO:
import java.nio.channels.*;
import java.nio.*;
import java.io.*;


public class tcp3ser {

   
    public static void main(String[] args) {
    	if (args.length == 1){
       
        int puerto = Integer.parseInt(args[0]);
        int aculUDP = 0;
        ArrayList <Integer> indexArray = new ArrayList<Integer>();
        int index = 0;
        ArrayList <Integer> aculTCPArray = new ArrayList<Integer>();
        

        try {
            
            //Creamos Socket UDP:
            DatagramChannel udpSoc = DatagramChannel.open();
            udpSoc.socket().bind(new InetSocketAddress(puerto));

            //Creamos Socket TCP:
            ServerSocketChannel tcpSoc = ServerSocketChannel.open();
            tcpSoc.socket().bind(new InetSocketAddress(puerto));
            
            //Creamos el selector:
            Selector selectorUDP = Selector.open();
            Selector selector = Selector.open();

            //Registramos los sockets anteriormente creados en el selector:

            //Case UDP:
            udpSoc.configureBlocking(false);
            udpSoc.register(selectorUDP, SelectionKey.OP_READ);

            //Case TCP:
            tcpSoc.configureBlocking(false);
            tcpSoc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Esperando clientes ...");


            while (true) {
               
                //------------------ESPERAMOS SOCKET TCP-----------------------------

                int readyChannels = selector.selectNow();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

                //------------------ESPERAMOS SOCKET UDP-----------------------------

                int readyChannelsUDP = selectorUDP.selectNow();

                Set<SelectionKey> selectionKeysUDP = selectorUDP.selectedKeys();
                Iterator<SelectionKey> keyIteratorUDP = selectionKeysUDP.iterator();

                
                //-----------------------------TCP-----------------------------------

                while (keyIterator.hasNext())
                {
                    SelectionKey key = keyIterator.next();
                    //SelectionKey keyCli;
                    //aculTCP.add(0);

            
                    if(key.isAcceptable())
                    {
                       System.out.println("");
                       System.out.println("Cliente TCP conectado.");
                       SocketChannel sc = tcpSoc.accept();
                       sc.configureBlocking(false);
                       aculTCPArray.add(index,0);
                       //indexArray.add(index,index);
                       key = sc.register(selector, SelectionKey.OP_READ, index); //Asociamos acumulador a cada cliente
                       index++;
                       
                    }


                    else if(key.isReadable())
                    { 
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        int index_aux = (int) key.attachment();
                        int aculTCP = aculTCPArray.get(index_aux);

                        ByteBuffer bufS = ByteBuffer.allocate(1024);
                        ByteBuffer bufR = ByteBuffer.allocate(1024);
                        
                        try
                        {
                            bufR.clear(); 
                            socketChannel.read(bufR);
                            int number = bufR.getInt(0); 
                            System.out.println(" ");   
                            System.out.println("Numero recibido: ("+number+") de cliente: ("+index_aux+").");
                            aculTCP = aculTCP+number;
                            aculTCPArray.set(index_aux,aculTCP);
                            System.out.println(number+" + "+(aculTCP-number)+" = "+aculTCP);
                            //Imprimimos en servidor valor de acumulador:
                            System.out.println("Valor acumulador cliente ("+index_aux+"): " +aculTCP); 
                            bufR.clear(); 
                            bufS.putInt(0,aculTCP);
                            socketChannel.write(bufS);  
                        }

                        catch(IOException e)
                        {
                            System.out.println("");
                            System.out.println("Cliente TCP desconectado.");
                            System.out.println("");
                            socketChannel.close();
                        }
  

                    }

              
                    keyIterator.remove();
                   
                }//Fin While TCP

                //-----------------------------UDP-----------------------------------

                while (keyIteratorUDP.hasNext())
                {
                    SelectionKey keyUDP = keyIteratorUDP.next();


                    if(keyUDP.isReadable())
                    {
                        System.out.println(" "); 
                        System.out.println("Cliente UDP enviando.");
                        ByteBuffer bufS = ByteBuffer.allocate(1024);
                        ByteBuffer bufR = ByteBuffer.allocate(1024);

                        try
                        {
                            //Además de recibir el datagrama, obtenemos la dirección del cliente,
                            //puesto .receive() devuelve un SocketAddress.
                            bufR.clear(); 
                            SocketAddress cliente = udpSoc.receive(bufR);
                            InetSocketAddress address = (InetSocketAddress) cliente;
                            //Obtenemos direccion cliente:
                            //SocketAddress cliente = udpSoc.getRemoteAddress();
                            //InetSocketAddress address = (InetSocketAddress) cliente;
                            //NOTA: ESTO CON DATAGRAMAS DEVUELVE NULL, USAR LO DE ARRIBA:

                            int number = bufR.getInt(0); 
                            System.out.println(" "); 
                            System.out.println("Numero recibido: ("+number+").");
                            aculUDP = aculUDP + number;
                            System.out.println(number+" + "+(aculUDP-number)+" = "+aculUDP);
                            bufR.clear(); 
                            bufS.putInt(0,aculUDP);
                            udpSoc.send(bufS, address); 

                            System.out.println("");
                            System.out.println("Envio UDP finalizado.");
                            System.out.println("");
                        }

                         catch(IOException e)
                        {
                            System.out.println("Error en cliente.");
                        }

                    }

                    keyIteratorUDP.remove();

                }//Fin While UDP



            }//Fin primer while

        } catch (Exception e) {
            e.printStackTrace();
        }
    } //FIN IF 1

    //--------------------------- SINTAXIS INCORRECTA --------------------------------------------

    else System.out.println("La sintaxis correcta para iniciar el servidor: $ java udpser puerto");

    }//FIN MAIN

}
