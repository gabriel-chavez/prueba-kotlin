package com.emizor.univida.imprime;

import android.util.Log;

import com.emizor.univida.excepcion.ErrorPapelException;
import com.emizor.univida.excepcion.ImpresoraErrorException;
import com.emizor.univida.excepcion.NoHayPapelException;
import com.emizor.univida.excepcion.VoltageBajoException;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;

/**
 * Created by gleney on 2/03/17.
 */
public class VerificadorImpresora {

    public VerificadorImpresora(){
    }

    public void verificarImpresora(IPrinter iPrinter) throws VoltageBajoException, ImpresoraErrorException, NoHayPapelException, ErrorPapelException, PrinterDevException {

            switch (iPrinter.getStatus()){
                case 2:
                    throw new NoHayPapelException();
                case 3:
                case 4:
                case -16:
                case -2:
                case -4:
                    Log.e("Error printer","Printer status over head");
                    throw new ImpresoraErrorException();
                case 8:
                    Log.e("Error printer","printer status get failed");
                    throw new ErrorPapelException();
                case 9:
                    throw new VoltageBajoException();

            }

    }

}
