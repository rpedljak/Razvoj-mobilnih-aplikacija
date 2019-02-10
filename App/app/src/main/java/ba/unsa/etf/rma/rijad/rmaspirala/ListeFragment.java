package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListeFragment extends Fragment {

    private ArrayList<String> kategorije;
    private ArrayAdapter adapter;
    private DodajKnjiguClick dodajClick;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige k;
    private ArrayList<Autor> autoriLista;
    private OnItemClick oic;
    private DodajOnlineClick doc;
    private boolean jesuLiKategorije=true;

    private BazaOpenHelper bazaOpenHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_fragment,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bazaOpenHelper=new BazaOpenHelper(getActivity());

        knjige=bazaOpenHelper.dajKnjige();
        kategorije=bazaOpenHelper.dajKategorije();
        autoriLista=new ArrayList<>();

        ArrayList<Autor> kms=new ArrayList<>();
        for (Knjiga knjiga : knjige) {
            ArrayList<Autor> autori = knjiga.getAutori();
            kms.addAll(autori);
        }

        for(Autor a : kms) {
            boolean imaGa=false;
            for(Autor b : autoriLista) {
                if(a.getImeiPrezime().equals(b.getImeiPrezime())) {
                    b.dodajKnjigu();
                    imaGa=true;
                }
            }
            if(!imaGa) autoriLista.add(a);
        }


        final Button pretraga=(Button)getView().findViewById(R.id.dRun);
        final Button dodajKategoriju=(Button)getView().findViewById(R.id.dDodajKategoriju);
        final EditText tekst=(EditText)getView().findViewById(R.id.tekstPretraga);
        Button dodajKnjigu=(Button)getView().findViewById(R.id.dDodajKnjigu);
        Button autori=(Button)getView().findViewById(R.id.dAutor);

        dodajKnjigu.setEnabled(false);

        dodajKategoriju.setEnabled(false);
        pretraga.setVisibility(View.GONE);
        dodajKategoriju.setVisibility(View.GONE);
        tekst.setVisibility(View.GONE);

        final Button kategorija=(Button)getView().findViewById(R.id.dKategorije);
        final ListView lv=(ListView)getView().findViewById(R.id.listaKategorija);


        Button dodajKnjiguOnline=(Button)getView().findViewById(R.id.dDodajOnline);

        try {
            dodajClick=(DodajKnjiguClick)getActivity();
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }

        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dodajClick.onClick();
            }
        });

        try {
            oic=(OnItemClick)getActivity();
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                oic.onItemClicked(i,jesuLiKategorije,autoriLista,knjige);
            }
        });

        try {
            doc=(DodajOnlineClick)getActivity();
        } catch(ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }

        dodajKnjiguOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doc.onOnlineClick();
            }
        });

        kategorija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                tekst.setVisibility(View.VISIBLE);

                adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_expandable_list_item_1,kategorije);
                lv.setAdapter(adapter);
                jesuLiKategorije=true;
            }
        });

        autori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pretraga.setVisibility(View.GONE);
                dodajKategoriju.setVisibility(View.GONE);
                tekst.setVisibility(View.GONE);

                lv.setAdapter(null);

                ArrayList<String> ispisAutori = new ArrayList<>();
                for (Autor a : autoriLista) {
                    ispisAutori.add(a.toString());
                }
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, ispisAutori);
                lv.setAdapter(adapter);

                jesuLiKategorije=false;
            }
        });

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.getFilter().filter(tekst.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if (i == 0) dodajKategoriju.setEnabled(true);
                        else dodajKategoriju.setEnabled(false);
                    }
                });
            }
        });

        dodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=(int)bazaOpenHelper.dodajKategoriju(tekst.getText().toString());
                if(id==-1) {
                    Toast.makeText(getActivity(), R.string.kategorija_postoji, Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.add(tekst.getText().toString());
                    kategorije=bazaOpenHelper.dajKategorije();
                    adapter.notifyDataSetChanged();
                    tekst.setText("");
                    dodajKategoriju.setEnabled(false);
                    adapter.getFilter().filter("");
                    Toast.makeText(getActivity(), R.string.kategorija_dodana, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface OnItemClick {
        public void onItemClicked(int i, boolean jesuLiKategorije, ArrayList<Autor> autoriLista, ArrayList<Knjiga> knjige);
    }

    public interface DodajKnjiguClick {
        public void onClick();
    }

    public interface DodajOnlineClick {
        public void onOnlineClick();
    }
}
