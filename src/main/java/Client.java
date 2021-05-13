import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        int puerto = 8300;
        InetAddress grupo = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String userName = null;
        System.out.println("");
        System.out.print("Ingresa tu nombre de usuario:  ");

        try{
            userName = input.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            MulticastSocket socket = new MulticastSocket(puerto);
            socket.setReuseAddress(true);
            socket.setTimeToLive(128);

            try{
                grupo = InetAddress.getByName("228.1.1.1");
            }catch (Exception e){
                System.out.println("Direccion no valida");
            }

            SocketAddress dirm;
            try{
                dirm = new InetSocketAddress(grupo, puerto);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
            socket.joinGroup(dirm, null);

            System.out.println("Socket conectado al grupo:  " + grupo.getHostAddress());

            String login = "login:"+userName;

            byte[] loginBytes = login.getBytes();
            DatagramPacket packet = new DatagramPacket(loginBytes,loginBytes.length,grupo,puerto);
            socket.send(packet);

            ReceiverClient receiverClient = new ReceiverClient(socket, userName);
            SendClient sendClient = new SendClient(socket, input, userName, puerto, grupo.getHostAddress());
            receiverClient.start();
            sendClient.start();
            receiverClient.join();
            sendClient.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
