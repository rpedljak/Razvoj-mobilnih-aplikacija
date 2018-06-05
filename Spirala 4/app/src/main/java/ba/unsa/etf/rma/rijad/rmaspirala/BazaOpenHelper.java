package ba.unsa.etf.rma.rijad.rmaspirala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="mojaBaza.db";
    public static final int DATABASE_VERSION=1;

    public static final String DATABASE_TABLE_KATEGORIJA = "Kategorija";
    public static final String KATEGORIJA_ID="_id";
    public static final String KATEGORIJA_NAZIV="naziv";

    public static final String DATABASE_TABLE_KNJIGA="Knjiga";
    public static final String KNJIGA_ID="_id";
    public static final String KNJIGA_NAZIV="naziv";
    public static final String KNJIGA_OPIS="opis";
    public static final String KNJIGA_DATUM_OBJAVLJIVANJA="datumObjavljivanja";
    public static final String KNJIGA_BROJ_STRANICA="brojStranica";
    public static final String KNJIGA_ID_WEB_SERVIS="idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE="idkategorije";
    public static final String KNJIGA_SLIKA="slika";
    public static final String KNJIGA_PREGLEDANA="pregledana";

    public static final String DATABASE_TABLE_AUTOR = "Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";

    public static final String DATABASE_TABLE_AUTORSTVO = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_ID_AUTORA = "idautora";
    public static final String AUTORSTVO_ID_KNJIGE = "idknjige";

    private static final String CREATE_TABLE_KATEGORIJA = "CREATE TABLE " + DATABASE_TABLE_KATEGORIJA + " (" + KATEGORIJA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KATEGORIJA_NAZIV + " TEXT NOT NULL);";
    private static final String CREATE_TABLE_KNJIGA = "CREATE TABLE " + DATABASE_TABLE_KNJIGA + " (" + KNJIGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KNJIGA_NAZIV + " TEXT NOT NULL, " + KNJIGA_OPIS + " TEXT NOT NULL, " + KNJIGA_DATUM_OBJAVLJIVANJA + " TEXT NOT NULL, " + KNJIGA_BROJ_STRANICA + " INTEGER NOT NULL, " + KNJIGA_ID_WEB_SERVIS + " TEXT NOT NULL, " + KNJIGA_ID_KATEGORIJE + " INTEGER NOT NULL, " + KNJIGA_SLIKA + " TEXT NOT NULL, " + KNJIGA_PREGLEDANA + " INTEGER NOT NULL);";
    private static final String CREATE_TABLE_AUTOR = "CREATE TABLE " + DATABASE_TABLE_AUTOR + " (" + AUTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AUTOR_IME + " TEXT NOT NULL);";
    private static final String CREATE_TABLE_AUTORSTVO = "CREATE TABLE " + DATABASE_TABLE_AUTORSTVO + " (" + AUTORSTVO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AUTORSTVO_ID_AUTORA + " INTEGER NOT NULL, " + AUTORSTVO_ID_KNJIGE + " INTEGER NOT NULL);";


    public BazaOpenHelper(Context c) {
        super(c,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_KATEGORIJA);
        sqLiteDatabase.execSQL(CREATE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL(CREATE_TABLE_AUTOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_KATEGORIJA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_AUTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE_AUTORSTVO);

        onCreate(sqLiteDatabase);
    }

    public void obrisiSve(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ DATABASE_TABLE_KATEGORIJA);
        db.execSQL("DELETE FROM "+ DATABASE_TABLE_KNJIGA);
        db.execSQL("DELETE FROM "+ DATABASE_TABLE_AUTOR);
        db.execSQL("DELETE FROM "+ DATABASE_TABLE_AUTORSTVO);
    }

    public void setBojaKnjige(String naziv, boolean obojiti){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex(KNJIGA_NAZIV)).equals(naziv)){
                ContentValues k = new ContentValues();
                k.put(KNJIGA_NAZIV, c.getString(c.getColumnIndex(KNJIGA_NAZIV)));
                k.put(KNJIGA_OPIS, c.getString(c.getColumnIndex(KNJIGA_OPIS)));
                k.put(KNJIGA_DATUM_OBJAVLJIVANJA, c.getString(c.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA)));
                k.put(KNJIGA_BROJ_STRANICA, c.getInt(c.getColumnIndex(KNJIGA_BROJ_STRANICA)));
                k.put(KNJIGA_ID_WEB_SERVIS, c.getString(c.getColumnIndex(KNJIGA_ID_WEB_SERVIS)));
                k.put(KNJIGA_ID_KATEGORIJE, c.getInt(c.getColumnIndex(KNJIGA_ID_KATEGORIJE)));
                k.put(KNJIGA_SLIKA, c.getString(c.getColumnIndex(KNJIGA_SLIKA)));
                if(obojiti) k.put(KNJIGA_PREGLEDANA, 1);
                else k.put(KNJIGA_PREGLEDANA,0);
                int id = c.getInt(c.getColumnIndex(KNJIGA_ID));
                db.update(BazaOpenHelper.DATABASE_TABLE_KNJIGA,k,"_id="+id, null);
            }
        }
    }

    public long dajAutora(String ime){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rez = new String[] {AUTOR_ID, AUTOR_IME};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR,rez,null,null,null,null, null);
        long id=-1;
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex(AUTOR_IME)).equals(ime)) id=c.getLong(c.getColumnIndex(AUTOR_ID));
        }
        return id;
    }

    public long dajKategoriju(String ime){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rez = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rez,null,null,null,null, null);
        long id=-1;
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)).equals(ime)) id=c.getLong(c.getColumnIndex(KATEGORIJA_ID));
        }
        return id;
    }

    ArrayList<Knjiga> knjigeKategorije(long idKategorije) {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        ArrayList<Knjiga> trenutno = this.dajKnjige();

        for(int i=0; i<trenutno.size(); i++){
            String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA, rezultati, null, null, null, null, null);
            String kategorija = "";
            while (c.moveToNext()){
                if(c.getInt(c.getColumnIndex(KATEGORIJA_ID))==(int)idKategorije) kategorija=c.getString(c.getColumnIndex(KATEGORIJA_NAZIV));
            }
            if(!kategorija.equals("")){
                if(trenutno.get(i).getZanr().equals(kategorija)) knjige.add(trenutno.get(i));
            }
        }
        return knjige;
    }

    ArrayList<Knjiga> knjigeAutora(long idAutora) {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        ArrayList<Knjiga> trenutne = this.dajKnjige();

        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        String[] rezultatiAutorstvo = new String[] {AUTORSTVO_ID_AUTORA, AUTORSTVO_ID_KNJIGE};
        Cursor c2 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, rezultatiAutorstvo, null,null,null,null,null );
        while(c2.moveToNext()){
            if(c2.getInt(c2.getColumnIndex(AUTORSTVO_ID_AUTORA))==idAutora ){
                while(c.moveToNext()){
                    if(c2.getInt(c2.getColumnIndex(AUTORSTVO_ID_KNJIGE))==c.getInt(c.getColumnIndex(KNJIGA_ID))){
                        for(int i=0; i<trenutne.size(); i++) if(trenutne.get(i).getNaziv().equals(c.getString(c.getColumnIndex(KNJIGA_NAZIV)))) knjige.add(trenutne.get(i));
                    }
                }
                c.moveToFirst();
            }
        }
        return knjige;
    }

    public ArrayList<String> dajKategorije() {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ArrayList<String> kategorije=new ArrayList<>();
        String[] kategorijeRez=new String[]{KATEGORIJA_ID,KATEGORIJA_NAZIV};
        Cursor c=sqLiteDatabase.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA, kategorijeRez,null,null,null,null,null);

        if(c.getCount()>0) {
            while(c.moveToNext()) {
                kategorije.add(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)));
            }
        }
        return kategorije;
    }

    public ArrayList<Knjiga> dajKnjige() {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        if(c.getCount()>0){
            while(c.moveToNext()){
                Knjiga k = new Knjiga();
                k.setId(c.getString(c.getColumnIndex(KNJIGA_ID_WEB_SERVIS)));
                URL url = null;
                try {
                    url = new URL(c.getString(c.getColumnIndex(KNJIGA_SLIKA)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                k.setSlika(url);
                k.setDatumObjavljivanja(c.getString(c.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA)));
                k.setNaziv(c.getString(c.getColumnIndex(KNJIGA_NAZIV)));
                k.setOpis(c.getString(c.getColumnIndex(KNJIGA_OPIS)));
                int boja = c.getInt(c.getColumnIndex(KNJIGA_PREGLEDANA));
                if(boja==1) k.setZaObojiti(true);
                else k.setZaObojiti(false);
                k.setBrojStranica(c.getInt(c.getColumnIndex(KNJIGA_BROJ_STRANICA)));

                String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
                Cursor c2 = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA, rezultati, null, null, null, null, null);
                while(c2.moveToNext()){
                    if(c2.getInt(c2.getColumnIndex(KATEGORIJA_ID))==c.getInt(c.getColumnIndex(KNJIGA_ID_KATEGORIJE))) k.setZanr(c2.getString(c2.getColumnIndex(KATEGORIJA_NAZIV)));
                }
                c2.close();

                String[] rezultatiAutori = new String[] {AUTOR_ID, AUTOR_IME};
                c2 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null,null,null,null,null );
                String[] rezultatiAutorstvo = new String[] {AUTORSTVO_ID_AUTORA, AUTORSTVO_ID_KNJIGE};
                Cursor c3 = db.query(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, rezultatiAutorstvo, null,null,null,null,null );

                while(c3.moveToNext()){
                    if(c.getInt(c.getColumnIndex(KNJIGA_ID))==c3.getInt(c3.getColumnIndex(AUTORSTVO_ID_KNJIGE))){
                        while(c2.moveToNext()){
                            if(c2.getInt(c2.getColumnIndex(AUTOR_ID))==c3.getInt(c3.getColumnIndex(AUTORSTVO_ID_AUTORA))) k.dodajAutora(c2.getString(c2.getColumnIndex(AUTOR_IME)));
                        }
                        c2.moveToFirst();
                    }
                }
                c2.close();
                c3.close();
                knjige.add(k);
            }
        }

        return knjige;
    }

    public long dodajKnjigu(Knjiga knjiga) throws MalformedURLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] rezultatiKnjige = new String[] {KNJIGA_ID,KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_BROJ_STRANICA , KNJIGA_DATUM_OBJAVLJIVANJA, KNJIGA_ID_WEB_SERVIS, KNJIGA_ID_KATEGORIJE, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        Cursor c = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezultatiKnjige, null, null, null, null, null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex(KNJIGA_NAZIV)).equals(knjiga.getNaziv())) return -1;
        }

        ContentValues k = new ContentValues();
        k.put(KNJIGA_NAZIV, knjiga.getNaziv());
        k.put(KNJIGA_OPIS, knjiga.getOpis());
        k.put(KNJIGA_BROJ_STRANICA, knjiga.getBrojStranica());
        k.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
        k.put(KNJIGA_ID_WEB_SERVIS, knjiga.getId());
        int idKategorije=0;
        String[] rezultati = new String[] {KATEGORIJA_ID, KATEGORIJA_NAZIV};
        c = db.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rezultati,null,null,null,null, null);
        if(c.getCount()>0){
            while (c.moveToNext()){
                if(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)).equals(knjiga.getZanr())) idKategorije=c.getInt(c.getColumnIndex(KATEGORIJA_ID));
            }
        }
        c.close();
        k.put(KNJIGA_ID_KATEGORIJE, idKategorije);
        if(knjiga.getSlika()==null){
            URL url = new URL ("http://www.ipwatchdog.com/wp-content/uploads/2017/09/no-value.jpg");
            k.put(KNJIGA_SLIKA,url.toString());
        }
        else k.put(KNJIGA_SLIKA, knjiga.getSlika().toString());

        if(knjiga.isZaObojiti()){
            k.put(KNJIGA_PREGLEDANA, 1);
        }
        else k.put(KNJIGA_PREGLEDANA, 0);

        db.insert(BazaOpenHelper.DATABASE_TABLE_KNJIGA,null, k);

        String[] rezuktatiKnjiga = new String[] {KNJIGA_ID, KNJIGA_NAZIV};
        c = db.query(BazaOpenHelper.DATABASE_TABLE_KNJIGA, rezuktatiKnjiga, null,null,null,null,null);
        int idKnjige=-1;
        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex(KNJIGA_NAZIV)).equals(knjiga.getNaziv())) idKnjige=c.getInt(c.getColumnIndex(KNJIGA_ID));
        }
        ArrayList<Autor> autori = knjiga.getAutori();
        String[] rezultatiAutori = new String[] {AUTOR_ID, AUTOR_IME};
        for(int i=0; i<autori.size(); i++){
            c = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null, null, null, null, null);
            boolean imaGa = false;
            if(c.getCount()>0){
                while (c.moveToNext()){
                    if(autori.get(i).getImeiPrezime().equals(c.getString(c.getColumnIndex(AUTOR_IME)))){
                        imaGa=true;
                        int indexAutora = c.getInt(c.getColumnIndex(AUTOR_ID));
                        ContentValues noviAutorstvo = new ContentValues();
                        noviAutorstvo.put(AUTORSTVO_ID_AUTORA, indexAutora);
                        noviAutorstvo.put(AUTORSTVO_ID_KNJIGE, idKnjige);
                        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, null, noviAutorstvo);

                    }
                }
            }
            if(!imaGa){
                ContentValues noviAutor = new ContentValues();
                noviAutor.put(AUTOR_IME, autori.get(i).getImeiPrezime());
                db.insert(DATABASE_TABLE_AUTOR, null, noviAutor);
                c = db.query(BazaOpenHelper.DATABASE_TABLE_AUTOR, rezultatiAutori, null,null,null,null,null);
                while(c.moveToNext()){
                    if(autori.get(i).getImeiPrezime().equals(c.getString(c.getColumnIndex(AUTOR_IME)))){
                        int indexAutora = c.getInt(c.getColumnIndex(AUTOR_ID));
                        ContentValues noviAutorstvo = new ContentValues();
                        noviAutorstvo.put(AUTORSTVO_ID_AUTORA, indexAutora);
                        noviAutorstvo.put(AUTORSTVO_ID_KNJIGE, idKnjige);
                        db.insert(BazaOpenHelper.DATABASE_TABLE_AUTORSTVO, null, noviAutorstvo);
                    }
                }
            }
        }
        c.close();
        db.close();
        return idKnjige;
    }

    public long dodajKategoriju(String naziv) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues kategorija=new ContentValues();
        String[] rez=new String[]{KATEGORIJA_ID,KATEGORIJA_NAZIV};

        Cursor c=sqLiteDatabase.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rez,null,null,null,null,null);
        if(c.getCount()>0) {
            while(c.moveToNext()) {
                if(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)).equals(naziv)) return -1;
            }
        }
        c.close();
        kategorija.put(KATEGORIJA_NAZIV,naziv);
        sqLiteDatabase.insert(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,null,kategorija);

        c=sqLiteDatabase.query(BazaOpenHelper.DATABASE_TABLE_KATEGORIJA,rez,null,null,null,null,null);
        int id=-1;
        if(c.getCount()>0) {
            while(c.moveToNext()) {
                if(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)).equals(naziv)) id=c.getInt(c.getColumnIndex(KATEGORIJA_ID));
            }
        }
        c.close();
        sqLiteDatabase.close();
        return id;
    }
}
