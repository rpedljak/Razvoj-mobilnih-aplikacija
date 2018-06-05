package ba.unsa.etf.rma.rijad.rmaspirala;

import java.io.Serializable;
import java.util.ArrayList;

public class Autor implements Serializable {

    private int brojKnjiga=0;

    //potrebni atributi
    private String imeiPrezime;
    ArrayList<String> knjige=new ArrayList<>();

    public Autor(String imeiPrezime, String idKnjige) {
        this.imeiPrezime = imeiPrezime;
        knjige.add(idKnjige);
        brojKnjiga++;
    }

    public void dodajKnjigu() {
        brojKnjiga++;
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id) {
        knjige.add(id); brojKnjiga++;
    }

    public int getBrojKnjiga() {
        return brojKnjiga;
    }


    public Autor(String nazivAutora) {
        this.imeiPrezime=nazivAutora;
    }

    @Override
    public String toString() {
        return imeiPrezime+", "+Integer.toString(brojKnjiga);
    }
}
