package com.ks.autorizador;

import com.ks.lib.tcp.protocolos.Iso;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransaccionTest
{

    @Test
    public void run()
    {
        String cadena = "ISO0234000700800822200000200000004000000000000000918114737000001091800001";
        Transaccion procesar = new Transaccion();
        procesar.setTransaccion(cadena);
        procesar.start();
        while (true)
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void armaIso()
    {
        String cadena = "ISO0234000700800822200000200000004000000000000000918114737000001091800001";

        System.out.println(cadena);

        Iso datos = new Iso();
        datos.procesarISO(cadena);
        datos.setTipo("0810");

        Transaccion procesar = new Transaccion();
        String respuesta = procesar.armaIso(datos);

        System.out.println(respuesta);
    }
}