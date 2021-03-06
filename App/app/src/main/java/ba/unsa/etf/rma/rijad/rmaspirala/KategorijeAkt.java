package ba.unsa.etf.rma.rijad.rmaspirala;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.DodajKnjiguClick,ListeFragment.OnItemClick, ListeFragment.DodajOnlineClick, FragmentOnline.OnDodajOnlineClick, KnjigaAdapter.OnPreporuciClick {
    private ArrayList<String> kategorije;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige kontejnerKnjige;
    private boolean sirokiLayout=false;
    private boolean dozvola=false;
    private ArrayList<String> kontakti=new ArrayList<>();
    private ArrayList<String> mailovi=new ArrayList<>();

    private BazaOpenHelper bazaOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        if(!dozvola) ActivityCompat.requestPermissions(KategorijeAkt.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},1);

        if(savedInstanceState==null) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hehe);
            mediaPlayer.start();
            Toast.makeText(this, "Now playing: Feint - We won't be alone (ft. Laura Brehm)", Toast.LENGTH_LONG).show();
        }

        bazaOpenHelper=new BazaOpenHelper(this);
        //bazaOpenHelper.obrisiSve();
        bazaOpenHelper.dodajKategoriju("Roman");

        kategorije=bazaOpenHelper.dajKategorije();
        knjige=bazaOpenHelper.dajKnjige();
        kontejnerKnjige=new KontejnerKnjige();

        FragmentManager fm=getFragmentManager();
        FrameLayout layoutKnjiga=(FrameLayout)findViewById(R.id.fragment2);

        if(layoutKnjiga!=null) {
            sirokiLayout=true;
            KnjigeFragment kf;
            kf=(KnjigeFragment)fm.findFragmentById(R.id.fragment2);
            if(kf==null) {
                kf=new KnjigeFragment();
                Bundle arg=new Bundle();
                arg.putStringArrayList("kategorije", kategorije);
                kontejnerKnjige.setKnjige(knjige);
                arg.putSerializable("knjige", kontejnerKnjige);
                kf.setArguments(arg);

                fm.beginTransaction().replace(R.id.fragment2,kf).commit();
            }
        }

        ListeFragment lf=new ListeFragment();
        if(savedInstanceState==null) {
            lf = (ListeFragment) fm.findFragmentById(R.id.fragment1);
        }
        if (lf == null) {
            lf = new ListeFragment();
            /*Bundle arg = new Bundle();
            arg.putStringArrayList("kategorije", kategorije);
            kontejnerKnjige.setKnjige(knjige);
            arg.putSerializable("knjige", kontejnerKnjige);
            lf.setArguments(arg);*/

            fm.beginTransaction().replace(R.id.fragment1, lf).commit();
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode==1) {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                dozvola=true;
            }
            else {
                Toast.makeText(KategorijeAkt.this, R.string.dozvola_odbijena, Toast.LENGTH_SHORT).show();
                dozvola=false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==Activity.RESULT_OK) {
            kontejnerKnjige=(KontejnerKnjige) data.getSerializableExtra("knjige");
            knjige=kontejnerKnjige.getKnjige();
        }
        if(requestCode==2 && resultCode==Activity.RESULT_OK) {
            kontejnerKnjige=(KontejnerKnjige) data.getSerializableExtra("knjige");
            knjige=kontejnerKnjige.getKnjige();
        }
    }

    @Override
    public void onClick() {
        Bundle arg=new Bundle();
        knjige=bazaOpenHelper.dajKnjige();
        arg.putStringArrayList("kategorije", kategorije);
        kontejnerKnjige.setKnjige(knjige);
        arg.putSerializable("knjige", kontejnerKnjige);

        DodavanjeKnjigeFragment dodavanjeKnjige=new DodavanjeKnjigeFragment();
        dodavanjeKnjige.setArguments(arg);

        if(sirokiLayout) {
            getFragmentManager().beginTransaction().replace(R.id.fragment3,dodavanjeKnjige).addToBackStack(null).commit();
        }
        else {
            getFragmentManager().beginTransaction().replace(R.id.fragment1,dodavanjeKnjige).addToBackStack(null).commit();
        }
    }

    @Override
    public void onItemClicked(int i, boolean jesuLiKategorije, ArrayList<Autor> autoriLista, ArrayList<Knjiga> knjige) {
        kategorije=bazaOpenHelper.dajKategorije();
        Bundle arg=new Bundle();
        if(jesuLiKategorije) {
            arg.putString("zanr", kategorije.get(i));
        }
        else {
            arg.putSerializable("autor",autoriLista.get(i));
        }

        KnjigeFragment kf=new KnjigeFragment();
        kf.setArguments(arg);

        if(sirokiLayout) {
            getFragmentManager().beginTransaction().replace(R.id.fragment2,kf).commit();
        }
        else {
            getFragmentManager().beginTransaction().replace(R.id.fragment1,kf).addToBackStack(null).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        knjige=bazaOpenHelper.dajKnjige();
        kontejnerKnjige.setKnjige(knjige);
        savedInstanceState.putSerializable("knjige",kontejnerKnjige);
        savedInstanceState.putStringArrayList("kategorije", kategorije);
    }

    @Override
    public void onOnlineClick() {
        Bundle arg=new Bundle();
        knjige=bazaOpenHelper.dajKnjige();
        arg.putStringArrayList("kategorije", kategorije);
        kontejnerKnjige.setKnjige(knjige);
        arg.putSerializable("knjige", kontejnerKnjige);

        FragmentOnline dodavanjeKnjige=new FragmentOnline();
        dodavanjeKnjige.setArguments(arg);

        if(sirokiLayout) {
            getFragmentManager().beginTransaction().replace(R.id.fragment3,dodavanjeKnjige).addToBackStack(null).commit();
        }
        else {
            getFragmentManager().beginTransaction().replace(R.id.fragment1,dodavanjeKnjige).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDodajOnline(String kategorija, Knjiga knjiga) {
        knjiga.setZanr(kategorija);
        int id= 0;
        try {
            id = (int)bazaOpenHelper.dodajKnjigu(knjiga);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(id==-1) {
            Toast.makeText(this, "Knjiga već postoji", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Knjiga dodana", Toast.LENGTH_SHORT).show();
        }
        knjige=bazaOpenHelper.dajKnjige();
    }

    public Uri getUriFromUrl(URL url) {
        Uri.Builder builder;
        builder =  new Uri.Builder().scheme(url.getProtocol()).authority(url.getAuthority()).appendPath(url.getPath());
        return builder.build();
    }

    @Override
    public void onRecommendClick(Knjiga k) {
        Bundle arg=new Bundle();
        arg.putStringArrayList("kategorije", kategorije);
        arg.putStringArrayList("mailovi", mailovi);
        arg.putStringArrayList("kontakti", kontakti);
        arg.putSerializable("knjiga", k);

        FragmentPreporuci dodavanjeKnjige=new FragmentPreporuci();
        dodavanjeKnjige.setArguments(arg);

        if(sirokiLayout) {
            getFragmentManager().beginTransaction().replace(R.id.fragment3,dodavanjeKnjige).addToBackStack(null).commit();
        }
        else {
            getFragmentManager().beginTransaction().replace(R.id.fragment1,dodavanjeKnjige).addToBackStack(null).commit();
        }
    }
}
