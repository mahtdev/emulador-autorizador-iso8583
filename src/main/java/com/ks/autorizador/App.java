package com.ks.autorizador;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static final Logger VMobjLog = LogManager.getLogger(App.class);

    public static void main(String[] args)
    {
        Transaccion.setIso_type(Transaccion.ISO_TYPE.fromValue(System.getProperty("isoType", "PROSA")));
        String VLstrPuerto = System.getProperty("port", "8000");
        ServidorTCP servidor = ServidorTCP.getInstance();
        servidor.setPuerto(Integer.parseInt(VLstrPuerto));
        servidor.conectar();

        VMobjLog.info("La aplicacion se levanto en el puerto: " + VLstrPuerto);
    }
}
