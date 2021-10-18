package com.example.reunion.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private DatePicker datePickerFiltre;
    private RelativeLayout filtreLayout;
    private MenuItem searchItem;
    private MenuItem cancelItem;
    private TextView filtreTextDebut;
    private TextView filtreTexteFin;

    private Button heureFinBouton; // Pour le filtre
    private Button heureDebutBouton; // Pour le filtre
    private String salle = null; // Pour le filtre
    private boolean isFiltering = false;
    private List<Reunion> mReunionsFiltered;
    private Integer filteredHourFin = null;
    private Integer filteredHourDebut = null;
    private Integer filteredMonth = null;
    private Integer filteredDay = null;
    private Integer filteredYear = null;
    private Button filtreButtonDate;
    private Button filtreButtonsalle;






    /* Item Click */ private  RecyclerViewAdapterReunion.RecyclerViewClickListener listener;
    private RecyclerViewAdapterReunion.DeleteClickListenner deleteClickListenner;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

                  /* Pour le filtre */
        filtreLayout = findViewById(R.id.filtre_layout);
        datePickerFiltre = findViewById(R.id.filtre_date_picker);
        filtreButtonDate = findViewById(R.id.filtre_button_date);
        filtreButtonsalle = findViewById(R.id.filtre_button_salle);

filtreButtonsalle.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mReunionsFiltered = reuRepo.filterReunion
                (filteredMonth = null ,filteredDay = null , salle);
        innitList();
    }
});
        filtreButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReunionsFiltered = reuRepo.filterReunion
                        (filteredMonth = datePickerFiltre.getMonth(),filteredDay = datePickerFiltre.getDayOfMonth() ,null);
                innitList();
            }
        });




        reuRepo = ReunionRepository.getInstance(); // on accède a ReunionRepository grace au singleton
        mReunionsFiltered = reuRepo.getReunions();
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
               // innitList();
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
        mReunions = reuRepo.getReunions();
        if(isFiltering) {
           // mReunionsFiltered = reuRepo.filterReunion
                    //(filteredMonth = datePickerFiltre.getMonth(),filteredDay = datePickerFiltre.getDayOfMonth() ,salle);
            mAdapter = new RecyclerViewAdapterReunion
                    (this, mReunionsFiltered, listener, deleteClickListenner);
        } //On update l'adapter avec les reunions filtré
        else {
            mAdapter = new RecyclerViewAdapterReunion
                    (this, mReunions, listener, deleteClickListenner);
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
               // innitList();
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
