package ba.unsa.etf.rma.rijad.rmaspirala;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Rijad on 21-Mar-18.
 */

public class Knjiga implements Serializable,Parcelable {

    //moji atributi
    private String nazivKnjige;
    private String imeAutora;
    private transient Uri slikaUri;
    private String zanr;

    //potrebni atributi
    private String id;
    private String naziv;
    private ArrayList<Autor> autori=new ArrayList<>();
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;

    private boolean zaObojiti=false;

    public boolean isZaObojiti() {
        return zaObojiti;
    }

    public void setZaObojiti(boolean zaObojiti) {
        this.zaObojiti = zaObojiti;
    }

    public Knjiga(String imeAutora, String nazivKnjige, Uri slika, String zanr) {
        this.imeAutora = imeAutora;
        this.nazivKnjige = nazivKnjige;
        this.slikaUri = slika;
        this.zanr = zanr;
    }

    public Knjiga(String id, String naziv) {
        this.id=id;
        this.nazivKnjige=naziv;
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public Uri getSlikaUri() {
        return slikaUri;
    }

    public void setSlikaUri(Uri slikaUri) {
        this.slikaUri = slikaUri;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(nazivKnjige);
        dest.writeString(imeAutora);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeString(zanr);
        dest.writeInt(brojStranica);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Knjiga(Parcel in) {
        naziv=in.readString();
        nazivKnjige=in.readString();
        imeAutora=in.readString();
        autori.add(new Autor(imeAutora));
        zanr=in.readString();
        brojStranica=in.readInt();
        opis=in.readString();
        datumObjavljivanja=in.readString();
        id=in.readString();
    }

    public static final Creator<Knjiga> CREATOR=new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel parcel) {
            return new Knjiga(parcel);
        }

        @Override
        public Knjiga[] newArray(int i) {
            return new Knjiga[i];
        }
    };
}
