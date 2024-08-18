package com.ks.autorizador;

import com.ks.lib.tcp.Tcp;
import com.ks.lib.tcp.protocolos.Iso;
import com.ks.lib.tcp.protocolos.IsoMain;
import com.ks.lib.tcp.protocolos.IsoProsa;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;


public class Transaccion
        extends Thread
{
    private static final Logger VMobjLog = LogManager.getLogger(Transaccion.class);

    private static int      totales;
    private static int      rechazadas;
    private static int      autorizadas;
    private static ISO_TYPE iso_type;
    @Setter
    private static Tcp      connection;

    private String transaccion;

    public enum ISO_TYPE
    {
        PROSA,
        EGLOBAL;

        public static ISO_TYPE fromValue(String value)
        {
            switch (value)
            {
                case "PROSA":
                    return PROSA;
                case "EGLOBAL":
                    return EGLOBAL;
            }
            return null;
        }
    }

    static
    {
        autorizadas = 0;
        rechazadas = 0;
        totales = 0;
    }

    public void run()
    {
        try
        {
            String  respuesta = "";
            IsoMain datos;
            if (iso_type.equals(ISO_TYPE.PROSA))
            {
                datos = new IsoProsa();
                ((IsoProsa) datos).procesarISO(transaccion);
            }
            else
            {
                datos = new Iso();
                ((Iso) datos).procesarISO(transaccion);
            }

            switch (datos.getTipo())
            {
                case "0800":
                    datos.setTipo("0810");
                    respuesta = datos.armaIso();
                    break;
                case "0200":
                case "0220":
                    totales += 1;
                    int monto = Integer.parseInt(datos.getCampos()[4]);
                    if (monto < 500000)
                    {
                        autorizadas += 1;
                        Random aleatorio    = new Random();
                        int    autorizacion = aleatorio.nextInt(1000000);
                        datos.getCampos()[38] = String.format("%06d", autorizacion);
                        datos.getCampos()[39] = "00";
                    }
                    else
                    {
                        rechazadas += 1;
                        datos.getCampos()[38] = "000000";
                        datos.getCampos()[39] = "70";
                    }
                    if (datos.getTipo().equals("0220"))
                    {
                        datos.setTipo("0230");
                    }
                    else
                    {
                        datos.setTipo("0210");
                    }
                    respuesta = datos.armaIso();
                    break;
                case "0420":
                    Random aleatorio = new Random();
                    int autorizacion = aleatorio.nextInt(1000000);
                    datos.getCampos()[38] = String.format("%06d", autorizacion);
                    datos.getCampos()[39] = "00";
                    datos.setTipo("0430");
                    respuesta = datos.armaIso();
                    break;
            }

            if ((respuesta.length() > 0) && (eventsTCP.getInstance().getNumberConnections() > 0))
            {
                String len = Iso.obtenerLongitud(respuesta.length());
                if (VMobjLog.isDebugEnabled())
                {
                    VMobjLog.debug("Mensaje enviado: " + len + respuesta);
                }
                connection.enviar(len + respuesta);
            }
        }
        catch (Exception ex)
        {
            VMobjLog.error("Problema al procesar la transaccion: " + ex.getMessage(), new Object[0]);
        }
    }

    static void setIso_type(ISO_TYPE iso_type)
    {
        Transaccion.iso_type = iso_type;
    }

    static int getContador()
    {
        return totales;
    }

    public static int getAutorizadas()
    {
        return autorizadas;
    }

    public static int getRechazadas()
    {
        return rechazadas;
    }

    void setTransaccion(String transaccion)
    {
        this.transaccion = transaccion;
    }

}
