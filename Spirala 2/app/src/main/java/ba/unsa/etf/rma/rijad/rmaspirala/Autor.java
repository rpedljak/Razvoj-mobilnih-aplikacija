package ba.unsa.etf.rma.rijad.rmaspirala;

import java.io.Serializable;
import java.util.ArrayList;

public class Autor implements Serializable {

    private String nazivAutora;
    private int brojKnjiga=1;

    public void dodajKnjigu() { brojKnjiga++; }

    public int getBrojKnjiga() {
        return brojKnjiga;
    }

    public void setBrojKnjiga(int brojKnjiga) {
        this.brojKnjiga = brojKnjiga;
    }

    public String getNazivAutora() {
        return nazivAutora;
    }

    public void setNazivAutora(String nazivAutora) {
        this.nazivAutora = nazivAutora;
    }


    public Autor(String nazivAutora) {
        this.nazivAutora = nazivAutora;
    }

    @Override
    public String toString() {
        return nazivAutora+", "+Integer.toString(brojKnjiga);
    }
}
