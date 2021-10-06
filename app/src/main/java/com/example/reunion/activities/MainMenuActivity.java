package com.example.reunion.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reunion.R;
import com.example.reunion.UI.RecyclerViewAdapterReunion;
import com.example.reunion.model.Reunion;
import com.example.reunion.service.ReunionRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
   // private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReunionRepository reuRepo;
    private FloatingActionButton createReuFab;
    private List<Reunion> mReunions;
    private RecyclerViewAdapterReunion mAdapter;
    Toolbar toolbar;
    private RelativeLayout filtreLayout;
    private MenuItem searchItem;
    private MenuItem cancelItem;
    private TextView filtreTextDebut;
    private TextView filtreTexteFin;

    private Button heureFinBouton; // Pour le filtre
    private Button heureDebutBouton; // Pour le filtre
    private String salle; // Pour le filtre
    private boolean isFiltering = false;
    private List<Reunion> mReunionsFiltered;
    private Integer filteredHourFin = null;
    private Integer filteredHourDebut = null;






    /* Item Click */ private  RecyclerViewAdapterReunion.RecyclerViewClickListener listener;
    private RecyclerViewAdapterReunion.DeleteClickListenner deleteClickListenner;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_menu);

                  /* Pour le filtre */
        filtreLayout = findViewById(R.id.filtre_layout);
        heureFinBouton = findViewById(R.id.filtre_heure_fin);
        filtreTextDebut = findViewById(R.id.text_filtre_heure_debut);
        filtreTexteFin = findViewById(R.id.text_filtre_heure_fin);


        heureFinBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker finPicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Entrez l'heure de fin de la réunion")
                        .build();
                finPicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filteredHourFin = finPicker.getHour();
                        filtreTexteFin.setText(Integer.toString(finPicker.getHour()) + " h");
                    innitList();
                    }
                });
                finPicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filtreTexteFin.setText("-");
                        filteredHourFin = null;
                        innitList();
                    }
                });
                        finPicker.show(getSupportFragmentManager(), "Fin");
            }

        });

        heureDebutBouton = findViewById(R.id.filtre_heure_debut);
        heureDebutBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialTimePicker debutPicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Entrez l'heure de début de la réunion")
                        .build();
                debutPicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filteredHourDebut = debutPicker.getHour();
                        filtreTextDebut.setText(Integer.toString(debutPicker.getHour()) + " h");
                        innitList();
                    }
                });
                debutPicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filtreTextDebut.setText("-");
                        filteredHourDebut = null;
                        innitList();
                    }
                });
                debutPicker.show(getSupportFragmentManager(),"Debut");
            }
        });
        /* ************************************************* */

        reuRepo = ReunionRepository.getInstance(); // on accède a ReunionRepository grace au singleton
        createReuFab = findViewById(R.id.createReuFab); //Id du FAB
        mRecyclerView = findViewById(R.id.reunion_list_recyclerView);//Id du RecyclerView
        toolbar = findViewById(R.id.toolbar); //On identifie la toolbar
       setSupportActionBar(toolbar); //On set la toolbar sur l'activité

       mLayoutManager = new LinearLayoutManager(this);
       mRecyclerView.setLayoutManager(mLayoutManager);
       mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // Barre décorative entre chaque cellule
       mRecyclerView.setHasFixedSize(true);

       createReuFab.setOnClickListener(new View.OnClickListener() { //onClickListenner sur le FAB add reunion
            @Override
            public void onClick(View view) {
                addReunion(); //Click sur le + navigate vers l'activité AddReunionsActivity
            }
        });
                   /* Spinner pour le filtre */
        Spinner salleSpinner = findViewById(R.id.filtre_salle_spinner);
        ArrayAdapter<CharSequence> salleAdapter = ArrayAdapter.createFromResource// On créer un adapter et on lui fourni notre array dans les ressources Strings
                (this,R.array.Salles_array_filtre, android.R.layout.simple_spinner_item);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salleSpinner.setAdapter(salleAdapter); // On set l'adapter
        salleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                salle = adapterView.getItemAtPosition(i).toString();
                innitList();
            }// On lui donne un OnItemSelectedListenner

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    void addReunion()
    {
        AddReunionActivity.navigate(this);
    }

   public void innitList()
    {
        setOnClickListener();
        mReunions = reuRepo.getReunions(); // On Récupère les reunions dans mReunions


        if(isFiltering) { //Filtre
            mReunionsFiltered = reuRepo.filterReunion(filteredHourDebut,filteredHourFin,salle);
            mAdapter = new RecyclerViewAdapterReunion(this, mReunionsFiltered, listener, deleteClickListenner);
        } //On update l'adapter avec les reunions filtré
        else {
            mAdapter = new RecyclerViewAdapterReunion(this, mReunions /* Item Click */, listener, deleteClickListenner);
        } //On update l'adapter avec les reunions
        mRecyclerView.setAdapter(mAdapter); // On set l'adapter dans le recycler view
    }
    /* Item Click */ void setOnClickListener()
    {
        listener = new RecyclerViewAdapterReunion.RecyclerViewClickListener() {
            @Override
            public void onClick(Reunion reunion) // OnClick sur une cellule du RecyclerView
            {
                openInfoDialog(reunion.getId()); // On passe l'id de notre réunion a notre dialogue

            }
        };
        deleteClickListenner = new RecyclerViewAdapterReunion.DeleteClickListenner() {
            @Override
            public void onDeleteClick(Reunion reunion) {
                reuRepo.deleteReunion(reunion); //On delete la reunion
                innitList(); // On Update la liste
            }
        };
    }

    public void openInfoDialog(long id)
    {
         InfoDialog infoDialog = InfoDialog.newInstance(id); //On utilise la fonction newInstance en donnant l'id de la réunion
         infoDialog.show(getSupportFragmentManager(), "Dialog Information");
    }

    @Override
    protected void onResume() {
        super.onResume();
        innitList(); //On apelle innitList dès le début

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        cancelItem = menu.findItem(R.id.cancel_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
            {
                isFiltering = true;
                innitList();
                filtreLayout.setVisibility(View.VISIBLE);
                cancelItem.setVisible(true);
                break;
                }
            case R.id.cancel_search:
            {
                isFiltering = false;
                innitList();
                filtreLayout.setVisibility(View.GONE);
                cancelItem.setVisible(false);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
