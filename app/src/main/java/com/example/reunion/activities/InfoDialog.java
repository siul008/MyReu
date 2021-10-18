package com.example.reunion.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.reunion.R;
import com.example.reunion.model.Reunion;
import com.example.reunion.service.ReunionRepository;

public class InfoDialog extends AppCompatDialogFragment
{
    public static InfoDialog newInstance(long id) {
// On stock l'id de la reunion dans "reunionId"
        Bundle args = new Bundle();
        args.putLong("reunionId", id);
        InfoDialog fragment = new InfoDialog();
        fragment.setArguments(args);
        return fragment;
    }
    private ReunionRepository reuRepo;
    private TextView sujetText;
    private TextView salleText;
    private TextView heureText;
    private TextView dureeText;
    private LinearLayout linearLayout;
    private String selectedMinute;
    private String selectedHour;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        long id = getArguments().getLong("reunionId"); //On récupère l'id depuis le bundle créé
        reuRepo = ReunionRepository.getInstance();
        Reunion r = reuRepo.findReunionById(id); //On trouve la reunion grace a la fonction findReunionByID
        selectedHour = Integer.toString(r.getSelectedHour()); // On convertie la r.getSelectedHour en string (from int)
        selectedMinute = Integer.toString(r.getSelectedMinute()); // On convertie la r.getSelectedMinute en string (from int)

        if (r.getSelectedMinute()<10)  /* Rajouter un 0 si selectedMinute est plus petit que 10*/
        {
            selectedMinute = "0" + selectedMinute;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reunion_info, null);
        builder.setView(view);
        builder.setTitle("Information")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                       //Laisser vide, ca ferme automatiquement le dialog
                    }
                });
        sujetText = view.findViewById(R.id.text_reunion_info1);
        salleText = view.findViewById(R.id.text_reunion_info2);
        heureText = view.findViewById(R.id.text_reunion_info3);
        dureeText = view.findViewById(R.id.text_reunion_info4);
        linearLayout = view.findViewById(R.id.dialog_linear_layout);

        sujetText.setText("Sujet : " + r.getSujet());
        salleText.setText("Salle : " + r.getSalle());
        heureText.setText("Heure : " + selectedHour + "h" + selectedMinute);
        dureeText.setText("Durée : " + r.getDureeReunion() + " minutes");

        TextView participantText = new TextView(view.getContext());
        participantText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        participantText.setGravity(Gravity.CENTER);
        participantText.setText(r.getParticipantList().toString());
        participantText.setTextSize(15);
        participantText.setPadding(10,0,0,0);
            linearLayout.addView(participantText);

        return builder.create();
    }
}
