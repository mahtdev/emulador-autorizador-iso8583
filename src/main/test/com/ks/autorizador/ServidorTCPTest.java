package com.ks.autorizador;

import com.ks.lib.tcp.protocolos.Iso;
import org.junit.Test;

public class ServidorTCPTest
{

    @Test
    public void datosRecibidos()
    {
        String cadena = "ISO0234000700800822200000200000004000000000000000918114737000001091800001";
        ServidorTCP servidor = ServidorTCP.getInstance();

        int len = 253;
        String lon = Integer.toHexString(len);

        servidor.datosRecibidos(Iso.obtenerLongitud(cadena.length()) + cadena, cadena.getBytes(), null);

    }
}