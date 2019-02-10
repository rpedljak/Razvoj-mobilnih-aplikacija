package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity {
    private ArrayList<String> kategorije;
    private ArrayAdapter<String> adapterSpin;
    private KontejnerKnjige k;
    private ArrayList<Knjiga> knjige;
    private ImageView slika;
    private EditText autor,naziv;
    private Spinner spin;
    private Button dodaj, nadjiSliku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        slika=(ImageView)findViewById(R.id.naslovnaStr);
        spin=(Spinner)findViewById(R.id.sKategorijaKnjige);

        k=(KontejnerKnjige) getIntent().getSerializableExtra("knjige");
        knjige=k.getKnjige();
        kategorije=getIntent().getStringArrayListExtra("kategorije");
        adapterSpin=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,kategorije);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapterSpin);

        Button cancel=(Button)findViewById(R.id.dPonisti);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        autor=(EditText)findViewById(R.id.imeAutora);

        naziv=(EditText)findViewById(R.id.nazivKnjige);

        dodaj=(Button)findViewById(R.id.dUpisiKnjigu);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autor.getText().length()==0) {
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Unesite autora!", Toast.LENGTH_SHORT).show();
                    autor.requestFocus();
                    return;
                }
                if(naziv.getText().length()==0) {
                    Toast.makeText(DodavanjeKnjigeAkt.this, "Unesite naziv!", Toast.LENGTH_SHORT).show();
                    naziv.requestFocus();
                    return;
                }
                slika.buildDrawingCache();
                Bitmap b=slika.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG,100,stream);
                String path= MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),b,"Naziv",null);

                knjige.add(new Knjiga(autor.getText().toString(),naziv.getText().toString(),Uri.parse(path), spin.getSelectedItem().toString()));
                Intent pocetni=new Intent();
                pocetni.putExtra("knjige", k);
                setResult(Activity.RESULT_OK, pocetni);
                finish();
            }
        });

        nadjiSliku=(Button)findViewById(R.id.dNadjiSliku);

        nadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novi=new Intent();
                novi.setAction(Intent.ACTION_GET_CONTENT);
                novi.setType("image/*");
                startActivityForResult(novi,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK) {
            Uri odabranaSlika=data.getData();
            InputStream stream=null;
            try {
                stream=getContentResolver().openInputStream(odabranaSlika);
            }
            catch(FileNotFoundException error) {
                error.printStackTrace();
            }
            Bitmap b=BitmapFactory.decodeStream(stream);
            slika.setImageBitmap(b);
        }
    }
}
