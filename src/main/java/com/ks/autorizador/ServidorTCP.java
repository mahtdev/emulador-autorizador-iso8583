package com.ks.autorizador;

import com.ks.lib.Configuracion;
import com.ks.lib.tcp.Cliente;
import com.ks.lib.tcp.EventosTCP;
import com.ks.lib.tcp.Servidor;
import com.ks.lib.tcp.Tcp;
import com.ks.lib.tcp.protocolos.Iso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServidorTCP extends Servidor implements EventosTCP
{
    private static final Logger VMobjLog = LogManager.getLogger(ServidorTCP.class);

    private static ServidorTCP instance;
    private static String VMstrMensaje = "";
    private Timer timer;

    public static ServidorTCP getInstance()
    {
        if (instance == null)
        {
            instance = new ServidorTCP();
        }
        return instance;
    }

    private ServidorTCP()
    {
        setEventos(this);
        String ruta = Configuracion.getRuta() + "log";
        File VLioArchivo = new File(ruta);
        if (!VLioArchivo.exists())
        {
            VLioArchivo.mkdir();
        }
        timer = new Timer(15000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String ruta = Configuracion.getRuta() + "log/reporte.txt";
                try
                {
                    BufferedWriter VLioArchivo = new BufferedWriter(new FileWriter(ruta, true));
                    SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    VLioArchivo.write("\n\n\nReporte de funcionamiento - " + fecha.format(new Date()));
                    VLioArchivo.newLine();
                    VLioArchivo.newLine();
                    VLioArchivo.write("\nTrx totales: " + Transaccion.getContador());
                    VLioArchivo.newLine();
                    VLioArchivo.write("Trx autorizadas: " + Transaccion.getAutorizadas());
                    VLioArchivo.newLine();
                    VLioArchivo.write("Trx rechazadas: " + Transaccion.getRechazadas());
                    VLioArchivo.flush();
                    VLioArchivo.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        timer.start();
    }


    public void conexionEstablecida(Cliente cliente)
    {
        escribe_log("Se recibio una conexion");
    }


    public void errorConexion(String s)
    {
        escribe_log("Problema con el servidor de TCP");
    }


    public void datosRecibidos(String s, byte[] bytes, Tcp tcp)
    {
        if (VMobjLog.isDebugEnabled())
        {
            VMobjLog.debug("Mensaje recibido: " + s);
        }
        VMstrMensaje += s;
        while (VMstrMensaje.contains("ISO"))
        {
            int inicio = VMstrMensaje.indexOf("ISO");
            try
            {
                int len = Iso.longitudISO(VMstrMensaje.substring(inicio - 2, inicio));
                if (len > 0 && VMstrMensaje.length() >= len)
                {
                    String trama = VMstrMensaje.substring(inicio, inicio + len);
                    VMstrMensaje = VMstrMensaje.substring(inicio + len);
                    Transaccion transaccion = new Transaccion();
                    transaccion.setTransaccion(trama);
                    transaccion.start();
                    Thread.sleep(5L);
                }
                else
                {
                    VMstrMensaje = VMstrMensaje.substring(inicio + 3);
                }
            }
            catch (Exception e)
            {
                VMobjLog.error("ERROR - Problema al procesar la transaccion: " + e.getMessage());
                VMstrMensaje = VMstrMensaje.substring(inicio + 3);
            }
        }
    }


    public void cerrarConexion(Cliente cliente)
    {

    }


    private synchronized void escribe_log(String mensaje)
    {
        try
        {
            String ruta = Configuracion.getRuta() + "log/reporte.txt";
            BufferedWriter VLioArchivo = new BufferedWriter(new FileWriter(ruta, true));
            VLioArchivo.newLine();
            VLioArchivo.newLine();
            VLioArchivo.write(mensaje);
            VLioArchivo.flush();
            VLioArchivo.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
