import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) {

        ArrayList<String> users = new ArrayList<>();

        int puerto = 8300;
        InetAddress grupo = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

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

            System.out.println("Server iniciado en el puerto: " + puerto + " y grupo " + grupo.getHostAddress());

            Receiver receiver = new Receiver(socket, users, grupo.getHostAddress(), puerto);
            receiver.start();
            receiver.join();


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
