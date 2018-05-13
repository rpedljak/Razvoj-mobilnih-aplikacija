package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListeFragment extends Fragment {

    private ArrayList<String> kategorije;
    private ArrayAdapter adapter;
    private DodajKnjiguClick dodajClick;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige k;
    private ArrayList<Autor> autoriLista;
    private OnItemClick oic;
    private boolean jesuLiKategorije=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_fragment,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        knjige=new ArrayList<>();
        kategorije=new ArrayList<>();
        autoriLista=new ArrayList<>();

        if(getArguments().containsKey("kategorije")) kategorije=getArguments().getStringArrayList("kategorije");

        if(getArguments().containsKey("knjige")) {
            k=(KontejnerKnjige)getArguments().getSerializable("knjige");
            knjige=k.getKnjige();
        }

        for (Knjiga knjiga : knjige) {
            boolean imaGa = false;
            String naziv = knjiga.getImeAutora();
            for (Autor a : autoriLista) {
                if (a.getNazivAutora().equals(naziv)) {
                    imaGa = true;
                    a.dodajKnjigu();
                }
            }
            if (!imaGa) {
                autoriLista.add(new Autor(knjiga.getImeAutora()));
            }
        }


        final Button pretraga=(Button)getView().findViewById(R.id.dPretraga);
        final Button dodajKategoriju=(Button)getView().findViewById(R.id.dDodajKategoriju);
        final EditText tekst=(EditText)getView().findViewById(R.id.tekstPretraga);
        Button dodajKnjigu=(Button)getView().findViewById(R.id.dDodajKnjigu);
        Button autori=(Button)getView().findViewById(R.id.dAutor);

        dodajKategoriju.setEnabled(false);
        pretraga.setVisibility(View.GONE);
        dodajKategoriju.setVisibility(View.GONE);
        tekst.setVisibility(View.GONE);

        Button kategorija=(Button)getView().findViewById(R.id.dKategorije);
        final ListView lv=(ListView)getView().findViewById(R.id.listaKategorija);

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
                if(tekst.getText().length()==0) {
                    Toast.makeText(getActivity(),R.string.tekst_kategorije_prazan,Toast.LENGTH_SHORT).show();
                    tekst.requestFocus();
                    return;
                }
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
                adapter.add(tekst.getText().toString());
                kategorije.add(tekst.getText().toString());
                adapter.notifyDataSetChanged();
                tekst.setText("");
                dodajKategoriju.setEnabled(false);
                adapter.getFilter().filter("");
            }
        });
    }

    public interface OnItemClick {
        public void onItemClicked(int i, boolean jesuLiKategorije, ArrayList<Autor> autoriLista, ArrayList<Knjiga> knjige);
    }

    public interface DodajKnjiguClick {
        public void onClick();
    }
}
