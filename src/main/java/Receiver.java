import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Receiver extends Thread{
    private ArrayList<String> users = new ArrayList<>();
    private MulticastSocket socket;
    private String dir;
    private int puerto;

    public Receiver(MulticastSocket socket, ArrayList<String> users, String dir, int puerto){
        this.socket = socket;
        this.users = users;
        this.dir = dir;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try{
            int length = 65535;
            while (true){
                DatagramPacket packet = new DatagramPacket(new byte[length], length );
                socket.receive(packet);

                String action = new String(packet.getData(), 0, packet.getLength());

                if(action.matches("login:\\w+")){ // login   :  Brandon
                    sendUsers();
                    agregarUsuario(action);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void agregarUsuario(String action){
        String[] userName = action.split(":");
        users.add(userName[1]);
        System.out.println("Nuevo usuario agregado:  " + userName[1]);
        System.out.println("ArrayList: " + users.toString());
    }

    public void sendUsers(){
        try{
            //System.out.println(socket.getInterface().getHostAddress()+ " : " +socket.getPort());

            InetAddress grupo = InetAddress.getByName(dir);
            String listUsers;
            if(users.isEmpty()) {
                listUsers = "listUsers:vacia";
            }else{
                listUsers = "listUsers:" + users.toString();
            }
            byte[] msjBytes = listUsers.getBytes();
            DatagramPacket packet = new DatagramPacket(msjBytes, msjBytes.length, grupo, puerto);
            socket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
