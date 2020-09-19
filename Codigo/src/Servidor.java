import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    public static void main(String[] args) {
        Marco_Servidor mimarco=new Marco_Servidor();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
class Marco_Servidor extends JFrame implements Runnable{
    public Marco_Servidor(){
        setBounds(1200,300,280,350);
        JPanel milamina=new JPanel();
        milamina.setLayout(new BorderLayout());

        areatexto=new JTextArea();
        milamina.add(areatexto,BorderLayout.CENTER);
        add(milamina);
        setVisible(true);

        Thread mihilo=new Thread(this);
        mihilo.start();
    }
    @Override
    public void run(){
        try{
            ServerSocket servidor=new ServerSocket(9999);
            String nick,ip,mensaje;
            PaqueteEnvio paquete_recibido;
            while (true){
                Socket misocket=servidor.accept();
                ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());
                paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();
                nick=paquete_recibido.getNick();
                ip=paquete_recibido.getIp();
                mensaje=paquete_recibido.getMensaje();

                areatexto.append("\n"+nick+": "+ mensaje +". Para: "+ ip);
                misocket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private JTextArea areatexto;
}