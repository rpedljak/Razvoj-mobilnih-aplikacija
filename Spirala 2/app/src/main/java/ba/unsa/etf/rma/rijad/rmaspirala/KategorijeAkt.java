package ba.unsa.etf.rma.rijad.rmaspirala;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.rijad.rmaspirala.R.id.dPretraga;
import static ba.unsa.etf.rma.rijad.rmaspirala.R.id.tekstPretraga;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.DodajKnjiguClick,ListeFragment.OnItemClick {
    private ArrayList<String> kategorije;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige kontejnerKnjige;
    private boolean sirokiLayout=false;
    private boolean dozvola=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        if(!dozvola) ActivityCompat.requestPermissions(KategorijeAkt.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        kategorije=new ArrayList<>();
        knjige=new ArrayList<>();
        kontejnerKnjige=new KontejnerKnjige();

        if(savedInstanceState!=null) {
            kontejnerKnjige=(KontejnerKnjige)savedInstanceState.getSerializable("knjige");
            knjige=kontejnerKnjige.getKnjige();
            kategorije=savedInstanceState.getStringArrayList("kategorije");
        }

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
            Bundle arg = new Bundle();
            arg.putStringArrayList("kategorije", kategorije);
            kontejnerKnjige.setKnjige(knjige);
            arg.putSerializable("knjige", kontejnerKnjige);
            lf.setArguments(arg);

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
            return;
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
        Bundle arg=new Bundle();
        arg.putStringArrayList("kategorije", kategorije);
        kontejnerKnjige.setKnjige(knjige);
        arg.putSerializable("knjige", kontejnerKnjige);
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
        kontejnerKnjige.setKnjige(knjige);
        savedInstanceState.putSerializable("knjige",kontejnerKnjige);
        savedInstanceState.putStringArrayList("kategorije", kategorije);
    }
}
