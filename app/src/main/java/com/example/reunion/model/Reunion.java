package com.example.reunion.model;

import java.util.ArrayList;
import java.util.List;

public class Reunion
{
  private String salle;
  private String sujet;
  int selectedHour;
  int selectedMinute;
  private long id;
  private String name;
  private List<String> participantList = new ArrayList<>();
  private String dureeReunion;


public Reunion(long mId, String mSujet, String mSalle, int mSelectedHour, int mSelectedMinute, String mDuree, String mName, List<String> mParticipantList)
{
  this.salle = mSalle;
  this.sujet = mSujet;
  this.id = mId;
  this.name = mName;
  this.dureeReunion = mDuree;
  this.selectedMinute = mSelectedMinute;
  this.selectedHour = mSelectedHour;
  this.participantList = mParticipantList;
}
  public int getSelectedHour() {
    return selectedHour;
  }

  public void setSelectedHour(int selectedHour) {
    this.selectedHour = selectedHour;
  }
  public String getDureeReunion() {
    return dureeReunion;
  }

  public void setDureeReunion(String dureeReunion) {
    this.dureeReunion = dureeReunion;
  }

  public int getSelectedMinute() {
    return selectedMinute;
  }

  public void setSelectedMinute(int selectedMinute) {
    this.selectedMinute = selectedMinute;
  }

  public String getSalle() {
    return salle;
  }

  public void setSalle(String salle) {
    this.salle = salle;
  }

  public String getSujet() {
    return sujet;
  }

  public void setSujet(String sujet) {
    this.sujet = sujet;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addParticipant(String participant)
  {
    participantList.add(participant);
  }

  public List<String> getParticipantList() {
    return participantList;
  }

  public String getParticipantToString()
  {
    String s = "";
    for (String s1 : participantList) {
      s += s1 + ", ";
    }
    return s;
  }
}

