package com.example.reunion.service;

import android.widget.TimePicker;

import com.example.reunion.model.Reunion;

import java.util.ArrayList;
import java.util.List;

public class ReunionRepository {
    private static ReunionRepository INSTANCE = null; // Singleton
    public final List<Reunion> reunionsList = new ArrayList<>();


    private ReunionRepository() { // Constructeur privé
    }


    public void createReunion(Reunion reunion) {
        reunionsList.add(reunion);
    }

    public List<Reunion> getReunions() {
        return reunionsList;
    }

    public static ReunionRepository getInstance() // Singleton
    {
        if (INSTANCE == null) {
            INSTANCE = new ReunionRepository();
        }
        return INSTANCE;
    }


    public void deleteReunion(Reunion reunion) {
        reunionsList.remove(reunion);
    }

    public Reunion findReunionById(long id) {
        Reunion reunionFinal = null;
        for (Reunion reunion : reunionsList) {
            if (reunion.getId() == id) {
                reunionFinal = reunion;
            }
        }
        return reunionFinal;
    }

    public boolean checkForAvailableRoom(int selectedHour, int selectedMinute, String duree, String salle) {

        int heureDepart = calculateHeureDeDepart(selectedHour, selectedMinute);
        int heureDeFin = calculateHeureDeFin(heureDepart, duree);

        for (Reunion reunion : reunionsList) {
            int mHeureDepart = calculateHeureDeDepart(reunion.getSelectedHour(), reunion.getSelectedMinute());
            int mHeureDeFin = calculateHeureDeFin(mHeureDepart, reunion.getDureeReunion());
            String mSalle = reunion.getSalle();

            if (salle.equals(mSalle) && heureDeFin > mHeureDepart && heureDepart < mHeureDeFin)
            //Si c'est la même salle et que la réunion est comprise pendant une autre reunion
            {
                return false;
                //On retourne faux
            }
        }
        return true;
    }

    private int calculateHeureDeDepart(int hour, int minute) {
        hour *= 60;
        int heureDeDepart = hour + minute;
        return heureDeDepart;
    }

    private int calculateHeureDeFin(int heureDeDepart, String duree) {
        int heureDeFin = heureDeDepart + Integer.parseInt(duree);
        return heureDeFin;
    }


    public List<Reunion> filterReunion(Integer hourDebut, Integer hourFin, String salle) {
        List<Reunion> filteredList = new ArrayList<>();


        for (Reunion reunion : reunionsList) {
            if (salle != null && !salle.equals("-") && reunion.getSalle().contains(salle))
            {
                   if(hourFin != null && hourDebut != null && reunion.getSelectedHour() >= hourDebut && reunion.getSelectedHour() < hourFin)
                   {
                       filteredList.add(reunion);
                   }
                   else if(hourFin != null && hourDebut == null && reunion.getSelectedHour() < hourFin)
                   {
                       filteredList.add(reunion);
                   }
                   else if(hourFin == null && hourDebut != null && reunion.getSelectedHour() >= hourDebut)
                   {
                       filteredList.add(reunion);
                   }
                   else if(hourFin == null && hourDebut == null)
                   {
                       filteredList.add(reunion);
                   }
            }
            else if (salle == null || salle.equals("-"))
            {
                if(hourFin != null && hourDebut != null && reunion.getSelectedHour() >= hourDebut && reunion.getSelectedHour() < hourFin)
                {
                    filteredList.add(reunion);
                }
                else if(hourFin != null && hourDebut == null && reunion.getSelectedHour() < hourFin)
                {
                    filteredList.add(reunion);
                }
                else if(hourFin == null && hourDebut != null && reunion.getSelectedHour() >= hourDebut)
                {
                    filteredList.add(reunion);
                }
            }
        }
        return filteredList;
    }

    public void clearReunions()
    {
        reunionsList.clear();
    }
}


