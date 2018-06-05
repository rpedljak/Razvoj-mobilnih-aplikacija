package ba.unsa.etf.rma.rijad.rmaspirala;

import java.io.Serializable;
import java.util.ArrayList;

public class Autor implements Serializable {

    //moji atributi
    private String nazivAutora;
    private int brojKnjiga=1;

    //potrebni atributi
    private String imeiPrezime;
    ArrayList<String> knjige=new ArrayList<>();

    public Autor(String imeiPrezime, String idKnjige) {
        this.imeiPrezime = imeiPrezime;
        knjige.add(idKnjige);
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
        knjige.add(id); if(brojKnjiga!=1) brojKnjiga++;
    }

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
        this.imeiPrezime=nazivAutora;
    }

    @Override
    public String toString() {
        return nazivAutora+", "+Integer.toString(brojKnjiga);
    }
}
