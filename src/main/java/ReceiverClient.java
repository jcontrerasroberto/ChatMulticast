import java.net.MulticastSocket;
import java.net.*;

public class ReceiverClient extends Thread {

    private MulticastSocket socket;
    private boolean listaRecibida = false;
    private String myUser;

    public ReceiverClient(MulticastSocket socket, String myUser){
        this.socket = socket;
        this.myUser = myUser;
    }

    @Override
    public void run() {
        int length = 65535;
        try{
            while(true){
                DatagramPacket packet = new DatagramPacket(new byte[length],length);
                socket.receive(packet);
                String mensaje = new String(packet.getData(),0,packet.getLength());
                if(mensaje.matches("listUsers:.+") && !listaRecibida) printUsers(mensaje);
                if(mensaje.matches("login:\\w+")) bienvenidaUsuario(mensaje);
                else if(!mensaje.matches("listUsers:.+") && !mensaje.matches("login:\\w+")) System.out.println(mensaje); //   roberto : Hola a
                //System.out.println("Mensaje recibido: "+ mensaje);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void bienvenidaUsuario(String mensaje){ // login:usuario
        String[] separado = mensaje.split(":");
        System.out.println( separado[1] + " ha entrado a la sala");
    }

    public void printUsers(String mensaje){ // listUsers              :                 vacia
        String[] primerSeparado = mensaje.split(":");
        String users = primerSeparado[1];
        if(users.equals("vacia")) {
            listaRecibida = true;
            return;
        }
        String sinPrimerCorchete = users.substring(1);
        String sinUltimoCorchete = sinPrimerCorchete.substring(0, sinPrimerCorchete.length() - 1); // user1, user2, user3
        //System.out.println( sinUltimoCorchete);
        String[] usuariosSeparados = sinUltimoCorchete.split(",");
        System.out.println("Usuarios conectados: ");
        for (String user:
             usuariosSeparados) {
            if(user.charAt(0)==' '){
                System.out.println("\t"+user.substring(1));
            }else{
                System.out.println("\t"+user);
            }
        }

        System.out.println("");
        listaRecibida = true;
    }
}
