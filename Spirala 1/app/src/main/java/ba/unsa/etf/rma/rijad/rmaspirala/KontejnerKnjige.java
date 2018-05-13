package ba.unsa.etf.rma.rijad.rmaspirala;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rijad on 25-Mar-18.
 */

public class KontejnerKnjige implements Serializable {

    private ArrayList<Knjiga> knjige;

    public KontejnerKnjige(ArrayList<Knjiga> knjige) {
        this.knjige = knjige;
    }

    public ArrayList<Knjiga> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<Knjiga> knjige) {
        this.knjige = knjige;
    }
}
