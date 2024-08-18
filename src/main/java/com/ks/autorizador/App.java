package com.ks.autorizador;

import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.Servidor;
import com.ks.lib.tcp.Tcp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static final Logger VMobjLog = LogManager.getLogger(App.class);

    public static void main(String[] args)
    {
        Tcp connection;
        Transaccion.setIso_type(Transaccion.ISO_TYPE.fromValue(System.getProperty("isoType", "PROSA")));
        String VLstrPuerto = System.getProperty("port", "8000");
        String VLstrIP     = System.getProperty("ip", "localhost");
        String VLstrType   = System.getProperty("type", "server");
        if (VLstrType.equals("server"))
        {
            connection = new Servidor();
        }
        else
        {
            connection = new Cliente();
            connection.setIP(VLstrIP);
        }
        connection.setEventos(eventsTCP.getInstance());
        connection.setPuerto(Integer.parseInt(VLstrPuerto));
        try
        {
            eventsTCP.getInstance().setConnection(connection);
            connection.conectar();
            VMobjLog.info("La aplicacion se inicio correctamente {}:{}", VLstrIP, VLstrPuerto);
        }
        catch (Exception e)
        {
            VMobjLog.error("Problema al iniciar la aplicación: {}", e.getMessage(), e);
        }
    }
}
