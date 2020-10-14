package TEC.Tarea.chat;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        MarcoCliente mimarco=new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * Crea la clase de marco, clase que se utiliza para definir limites y tamaño para la ventana de la aplicación a mostrar en pantalla.
 */
class MarcoCliente extends JFrame{
    /**
     * Crea el constructor del marco que trae definido los parametros para la ventana a mostrar en pantalla. Y recibe los parametros para intrudicir el nombre del usuario la ip del destinatario y el mensaje a enviar
     */
    public MarcoCliente(){
        setBounds(600,300,280,350);
        LaminaMarcoCliente milamina=new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }
}

/**
 * Crea la case lamina que obtiene datos importantes como el nombre de usuario, la ip del destinatario, el campo donde aparece el chat
 * el boton de enviar y el campo para escribir el mansaje.
 */
class LaminaMarcoCliente extends JPanel implements Runnable{
    static Logger bitacora = GestorBitacora.getBitacora("TEC.Tarea.chat.Main","bitacoraMain.txt", Level.SEVERE);
    /**
     * Es el constructor donde los parametros de la ventana para la aplicacion se predefinen
     */
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
            //e.printStackTrace();
            bitacora.fine("Info del socket servidor: "+e);
            bitacora.severe("Exception try server socket: "+e);
        }
    }

    /**
     * Clase que se va a ejecutar en paralelo con el fin de llevar a cabo los procesos de enviar y recibir la
     * informacion o mensajes entre usuarios
     */
    private class EnviarTexto implements ActionListener{
        /**
         * Metodo que se encarga de construir la accion que se llevara a cabo despues de presionar el boton
         * enviar
         * @param e Variable de tipo excepcion que sera enviada en caso de que aparezca algun error
         */
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
                bitacora.info("Estado del host: "+unknownHostException);
                bitacora.severe("Problema ocurrido no se encuentra el servidor: "+unknownHostException);
                //unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                bitacora.info("Estado de la coneccion con el servidor: "+ioException);
                bitacora.severe("Problema ocurrido en la coneccion al servidor: "+ioException);
                //ioException.printStackTrace();
            }
        }
    }
    private JTextField campo1,nick,ip;
    private JTextArea campochat;
    private JButton miboton;
}

/**
 * Clase que almacena y define el nombre del usuario, ip y mensaje a enviar
 */
class PaqueteEnvio implements Serializable {
    private String nick, ip, mensaje;

    /**
     * Metodo que se utiliza para obtener el nombre del usuario
     * @return El nombre del usuario
     */
    public String getNick() {
        return nick;
    }

    /**
     * Metodo que se utiliza para definir el nombre del usuario
     * @param nick Introduce un nombre de tipo String
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Metodo que se utiliza para obtener la ip del destinatario
     * @return La ip del destinatario
     */
    public String getIp() {
        return ip;
    }

    /**
     * Metodo que se utiliza para establecer la ip del destinatario
     * @param ip Variable de tipo String que define la ip del destinatario
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Metodo que se utiliza para obtener el mensaje enviado
     * @return El mensaje que fue enviado al destinatario
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Metodo que se utiliza para obtener el mensaje enviado
     * @param mensaje Recibe una cadena de String que sera enviada al destinatario
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}