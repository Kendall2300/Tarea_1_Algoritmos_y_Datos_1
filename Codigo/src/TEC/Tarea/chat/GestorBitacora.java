package TEC.Tarea.chat;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorBitacora {
    static Logger getBitacora(){
        return Logger.getGlobal();
    }
    static Logger getBitacora(String paquete, String nombreArchivoBitacora, Level nivel){
        Logger bitacora = null;
        bitacora= Logger.getLogger(paquete);
        try {
            FileHandler handler=new FileHandler(nombreArchivoBitacora);
            bitacora.addHandler(handler);
        }catch (IOException e){
            bitacora=Logger.getGlobal();
            //Atrapando el error de la bitacora. Algún dato fue cambiado y no se añadio bien el nuevo dato
            bitacora.severe("Error en la creacion de la bitacora"+e.getMessage());
            return bitacora;
        }
        bitacora.setLevel(nivel);
        return bitacora;
    }
}
