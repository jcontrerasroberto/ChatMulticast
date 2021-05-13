import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendClient extends Thread{
    private MulticastSocket socket;
    private BufferedReader input;
    private String userName;
    private int puerto;
    private String dir;
    private InetAddress grupo;

    public SendClient(MulticastSocket socket, BufferedReader input, String userName, int puerto, String dir) {
        this.socket = socket;
        this.input = input;
        this.userName = userName;
        this.puerto = puerto;
        this.dir = dir;
    }

    @Override
    public void run() {
        try{
            grupo = InetAddress.getByName(dir);
            while (true){
                String mensajeNuevo = userName+" : ";
                mensajeNuevo += input.readLine();
                byte[] mensajeBytes = mensajeNuevo.getBytes();
                DatagramPacket packet = new DatagramPacket(mensajeBytes, mensajeBytes.length, grupo, puerto);
                socket.send(packet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
