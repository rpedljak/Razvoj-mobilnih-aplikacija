package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class DodavanjeKnjigeFragment extends Fragment {

    private ArrayList<String> kategorije;
    private Spinner spin;
    private ArrayAdapter<String> adapter;
    private ImageView slika;
    private EditText autor,naziv;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige k;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dodavanje_knjige_akt,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments().containsKey("kategorije")) {
            kategorije=getArguments().getStringArrayList("kategorije");
            spin=(Spinner)getView().findViewById(R.id.sKategorijaKnjige);
            adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,kategorije);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
        }

        if(getArguments().containsKey("knjige")) {
            k=(KontejnerKnjige)getArguments().getSerializable("knjige");
            knjige=k.getKnjige();
        }

        autor=(EditText)getView().findViewById(R.id.imeAutora);

        naziv=(EditText)getView().findViewById(R.id.nazivKnjige);

        Button dodaj=(Button)getView().findViewById(R.id.dUpisiKnjigu);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autor.getText().length()==0) {
                    Toast.makeText(getActivity(), R.string.unesi_autora, Toast.LENGTH_SHORT).show();
                    autor.requestFocus();
                    return;
                }
                if(naziv.getText().length()==0) {
                    Toast.makeText(getActivity(), R.string.unesi_naziv, Toast.LENGTH_SHORT).show();
                    naziv.requestFocus();
                    return;
                }
                if(kategorije.size()==0) {
                    Toast.makeText(getActivity(),R.string.spinner_prazan,Toast.LENGTH_SHORT).show();
                    return;
                }

                slika.buildDrawingCache();
                Bitmap b=slika.getDrawingCache();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG,100,stream);
                String path= MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),b,"Naziv",null);

                knjige.add(new Knjiga(autor.getText().toString(),naziv.getText().toString(),Uri.parse(path), spin.getSelectedItem().toString()));
                k.setKnjige(knjige);
                Bundle arg=new Bundle();
                arg.putSerializable("knjige", k);
                arg.putStringArrayList("kategorije", kategorije);

                ListeFragment lf=new ListeFragment();
                lf.setArguments(arg);

                if(getFragmentManager().findFragmentById(R.id.fragment3)!=null) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                getFragmentManager().beginTransaction().replace(R.id.fragment1,lf).commit();

            }
        });

        Button ponisti=(Button)getView().findViewById(R.id.dPonisti);

        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        slika=(ImageView)getView().findViewById(R.id.naslovnaStr);
        Button nadjiSliku=(Button)getView().findViewById(R.id.dNadjiSliku);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK) {
            Uri odabranaSlika=data.getData();
            InputStream stream=null;
            try {
                stream=getActivity().getContentResolver().openInputStream(odabranaSlika);
            }
            catch(FileNotFoundException error) {
                error.printStackTrace();
            }
            Bitmap b= BitmapFactory.decodeStream(stream);
            slika.setImageBitmap(b);
        }
    }
}
