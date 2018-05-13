package ba.unsa.etf.rma.rijad.rmaspirala;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Rijad on 21-Mar-18.
 */

public class Knjiga implements Serializable {

    static int id=0;
    int idKnjige;
    String imeAutora;
    String nazivKnjige;
    byte[] slika;
    String zanr;
    boolean zaObojiti=false;

    public boolean isZaObojiti() {
        return zaObojiti;
    }

    public void setZaObojiti(boolean zaObojiti) {
        this.zaObojiti = zaObojiti;
    }

    public Knjiga(String imeAutora, String nazivKnjige, byte[] slika, String zanr) {
        this.idKnjige=id;
        id++;
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.slika = slika;
        this.zanr = zanr;
    }

    public int getIdKnjige() {
        return idKnjige;
    }

    public void setIdKnjige(int idKnjige) {
        this.idKnjige = idKnjige;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public byte[] getSlika() {
        return slika;
    }

    public void setSlika(byte[] slika) {
        this.slika = slika;
    }
}
