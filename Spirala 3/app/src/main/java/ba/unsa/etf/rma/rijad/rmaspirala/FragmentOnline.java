package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, KnjigeReceiver.Receiver {

    private ArrayList<String> kategorije;
    private ArrayAdapter<String> adapter;
    private ArrayList<Knjiga> knjige;
    Spinner spinner;
    private OnDodajOnlineClick onClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final Spinner spin=(Spinner)getView().findViewById(R.id.sKategorije);
        spinner=(Spinner)getView().findViewById(R.id.sRezultat);

        kategorije=new ArrayList<>();

        if(getArguments().containsKey("kategorije")) {
            kategorije=getArguments().getStringArrayList("kategorije");
            adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,kategorije);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
        }

        Button povratak=(Button)getView().findViewById(R.id.dPovratak);

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        Button pretraga=(Button)getView().findViewById(R.id.dRun);
        final EditText tekst=(EditText)getView().findViewById(R.id.tekstUpit);

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tekst.getText().toString().length()==0) {
                    Toast.makeText(getActivity(), R.string.unesi_naziv,Toast.LENGTH_SHORT).show();
                    tekst.requestFocus();
                    return;
                }

                if(tekst.getText().toString().contains("autor:") || tekst.getText().toString().contains("Autor:")) {
                    new DohvatiNajnovije(FragmentOnline.this).execute(tekst.getText().toString());
                }
                else if(tekst.getText().toString().contains("korisnik:") || tekst.getText().toString().contains("Korisnik:")) {
                    Intent intent=new Intent(Intent.ACTION_SYNC,null,getActivity(),KnjigePoznanika.class);
                    KnjigeReceiver receiver=new KnjigeReceiver(new Handler());
                    receiver.setReceiver(FragmentOnline.this);

                    intent.putExtra("idKorisnika", tekst.getText().toString().substring(9));
                    intent.putExtra("receiver", receiver);

                    getActivity().startService(intent);
                }
                else {
                    new DohvatiKnjige(FragmentOnline.this).execute(tekst.getText().toString());
                }
            }
        });

        Button dodaj=(Button)getView().findViewById(R.id.dAdd);

        try {
            onClick=(OnDodajOnlineClick) getActivity();
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(kategorije.size()==0) {
                    Toast.makeText(getActivity(), R.string.spinner_prazan,Toast.LENGTH_SHORT).show();
                    return;
                }

                if(spinner.getCount()==0) {
                    Toast.makeText(getActivity(), R.string.spinner_prazan,Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Knjiga knjiga : knjige) {
                    if(knjiga.getNaziv().equals(spinner.getSelectedItem().toString())) {
                        onClick.onDodajOnline(spin.getSelectedItem().toString(),knjiga);
                    }
                }
            }
        });
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> k) {

        knjige=k;

        ArrayList<String> naziviKnjiga=new ArrayList<>();
        for(Knjiga knjiga : knjige) {
            naziviKnjiga.add(knjiga.getNaziv());
        }

        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,naziviKnjiga);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> k) {

        knjige=k;

        ArrayList<String> naziviKnjiga=new ArrayList<>();
        for(Knjiga knjiga : knjige) {
            naziviKnjiga.add(knjiga.getNaziv());
        }

        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,naziviKnjiga);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public interface OnDodajOnlineClick {
        public void onDodajOnline(String kategorija, Knjiga k);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode==0) {
            Toast.makeText(getActivity(),R.string.servis_pokrenut,Toast.LENGTH_SHORT).show();
        }
        else if(resultCode==1) {
            knjige=resultData.getParcelableArrayList("listaKnjiga");
            ArrayList<String> naziviKnjiga=new ArrayList<>();
            for(Knjiga knjiga : knjige) {
                naziviKnjiga.add(knjiga.getNaziv());
            }

            adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,naziviKnjiga);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else {
            Toast.makeText(getActivity(),R.string.error,Toast.LENGTH_SHORT).show();
        }
    }
}
