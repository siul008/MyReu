package com.example.reunion.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reunion.R;
import com.example.reunion.model.Reunion;
import com.example.reunion.service.ReunionRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AddReunionActivity extends AppCompatActivity
{
    TimePicker timeInput;
    FloatingActionButton valider;
    ReunionRepository reuRepo;
    TextInputEditText participantInput;
    TextInputEditText sujetInput;
    ImageButton arrowBack;
    int selectedHour;
    int selectedMinute;
    String salle;
    String duree;
    String name;
    int month;
    int day;
    DatePicker datePicker;
    private final List<String> participants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reunion);
        reuRepo = ReunionRepository.getInstance();
        valider = findViewById(R.id.fab_validate_reunion);
        sujetInput = findViewById(R.id.sujet);
        timeInput = findViewById(R.id.time_picker);
        participantInput = findViewById(R.id.participants);
        arrowBack = findViewById(R.id.arrow_back_create_reunion);
        datePicker = findViewById(R.id.date_picker);



        /*Création des Spinners */
        Spinner salleSpinner = findViewById(R.id.salle_spinner);
        ArrayAdapter<CharSequence> salleAdapter = ArrayAdapter.createFromResource
                // On créer un adapter et on lui fourni notre array dans les ressources Strings
                (this,R.array.Salles_array, android.R.layout.simple_spinner_item);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salleSpinner.setAdapter(salleAdapter); // On set l'adapter
        salleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                salle = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        }); // On lui donne un OnItemSelectedListenner

        Spinner dureeSpinner = findViewById(R.id.duree_spinner); // Même principe pour le durée spinner
        ArrayAdapter<CharSequence> dureeAdapter = ArrayAdapter.createFromResource
                (this,R.array.Duree_Array, android.R.layout.simple_spinner_item);
        dureeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dureeSpinner.setAdapter(dureeAdapter);
        dureeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                duree = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        valider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            { // Quand on valide on appele la fonction CreateReunion et termine l'activité
               if(!sujetInput.getText().toString().isEmpty() && !participantInput.getText().toString().isEmpty())
               //Si l'utilisateur a mis un sujet et participant
               {
                   onParticipantsInput(participantInput.getText().toString()); // On appele la fonction onParticipantsInput
                   CreateReunion(); // On crée la Reunion
                   finish(); // On termine l'activité Add Reunion
               }
               else {
                   Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
               } }
        });
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void CreateReunion()
    {
        String selectedMinuteString = String.format("%02d", timeInput.getCurrentMinute());
        Reunion reunion = new Reunion(
                System.currentTimeMillis(),
                sujetInput.getText().toString(),
                salle,
                selectedHour = timeInput.getCurrentHour(),
                selectedMinute = timeInput.getCurrentMinute(),
                month = datePicker.getMonth(),
                day = datePicker.getDayOfMonth(),
                duree,
                name = Integer.toString(selectedHour) + "h" + selectedMinuteString + " - " + salle,
                participants
        );
        reuRepo.createReunion(reunion);


    }
    /* Fonction qui sert a naviguer jusqu'à cette activity */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddReunionActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public void onParticipantsInput (String inputParticipants)
    {
        participants.clear(); // On clear la liste
        String[] participantsList = inputParticipants.split("[,; \n]");
        // On ajoute dans l'array String[] l'input de participant en séparant par [,][;][ ]

        for (String participant : participantsList) // Pour chaque string dans la liste
        {
            if (!participant.isEmpty()) // S'il n'est pas vide
            {
                participants.add(participant);
        // On l'ajoute dans la liste des participants
            }
        }
    }
}