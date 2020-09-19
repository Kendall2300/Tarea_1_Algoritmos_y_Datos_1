import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Main {
    public static void main(String[] args) {
        MarcoCliente mimarco=new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
class MarcoCliente extends JFrame{
    public MarcoCliente(){
        setBounds(600,300,280,350);
        LaminaMarcoCliente milamina=new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }
}
class LaminaMarcoCliente extends JPanel implements Runnable{
    public LaminaMarcoCliente(){
        nick=new JTextField(5);
        add(nick);

        ip=new JTextField(8);
        add(ip);

        JLabel texto=new JLabel("-CHAT-");
        add(texto);

        campochat=new JTextArea(12,20);
        add(campochat);

        campo1=new JTextField(20);
        add(campo1);

        miboton=new JButton("Enviar");
        EnviarTexto mievento=new EnviarTexto();
        miboton.addActionListener(mievento);
        add(miboton);

        Thread mihilo=new Thread(this);
        mihilo.start();
    }

    @Override
    public void run() {

        try{
            ServerSocket servidorcliente=new ServerSocket(9090);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;

            while (true){
                cliente=servidorcliente.accept();
                ObjectInputStream flujoentrada=new ObjectInputStream(cliente.getInputStream());
                paqueteRecibido=(PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n"+paqueteRecibido.getNick()+" :"+paqueteRecibido.getMensaje());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class EnviarTexto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            campochat.append("\n"+"Yo: "+campo1.getText());
            try{
                Socket misocket=new Socket("127.0.0.1",9999);

                PaqueteEnvio datos=new PaqueteEnvio();
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream());
                paquete_datos.writeObject(datos);

                misocket.close();
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    private JTextField campo1,nick,ip;
    private JTextArea campochat;
    private JButton miboton;
}
class PaqueteEnvio implements Serializable {
    private String nick, ip, mensaje;
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}