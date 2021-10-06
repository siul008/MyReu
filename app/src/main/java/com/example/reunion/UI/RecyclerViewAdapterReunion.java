package com.example.reunion.UI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reunion.R;
import com.example.reunion.activities.MainMenuActivity;
import com.example.reunion.model.Reunion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapterReunion extends RecyclerView.Adapter<RecyclerViewAdapterReunion.ViewHolder> {


    private List<Reunion> mReunions;
    private List<Reunion> mReunionsFull;


    /* Item click */ private RecyclerViewClickListener recyclerViewClickListener;
    private DeleteClickListenner deleteClickListenner;
    private List<Integer> colors;


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView mTextView;
        public ImageButton mImageButton;
        public ImageView mImageView;
        public TextView mTextViewParticipants;



        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mTextView = itemView.findViewById(R.id.reunion_list_name);
            mImageButton = itemView.findViewById(R.id.delete_button_list);
            mImageView = itemView.findViewById(R.id.reunion_list_image);
            mTextViewParticipants = itemView.findViewById(R.id.reunion_list_participants);
        }

    }

    public RecyclerViewAdapterReunion(Context context, List<Reunion> items /* Item Click */ , RecyclerViewClickListener listener, DeleteClickListenner deleteListener)
    {
        mReunions = items;
        this.recyclerViewClickListener = listener;
        this.deleteClickListenner = deleteListener;
        this.colors = Arrays.asList( //On crée la liste de couleur a utiliser
                ContextCompat.getColor(context, R.color.softGreen),
                ContextCompat.getColor(context, R.color.softBlue),
                ContextCompat.getColor(context, R.color.softRed)
        );
    }


    @Override
    public RecyclerViewAdapterReunion.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_reunion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterReunion.ViewHolder holder, int position)
    {


          Reunion currentItem = mReunions.get(position); // On regarde la position de l'item
          int color = colors.get(position % colors.size()); //On choisi une couleur selon la position de la réunion
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  recyclerViewClickListener.onClick(currentItem);
              }
          }); /* ItemClick */
          String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
          holder.mTextView.setText("Réunion " + alphabet.charAt(position) + " - " + currentItem.getName()); // On met le nom dans la cellule de la reunion voulu
          holder.mTextViewParticipants.setText(currentItem.getParticipantToString());
          holder.mImageView.setColorFilter(color); //On utilise la couleur défini ligne 80
          holder.mImageButton.setOnClickListener(new View.OnClickListener() //Set on click listenner sur la poubelle
                   {
              @Override
              public void onClick(View view) {  /*On Click sur la poubelle*/
                  deleteClickListenner.onDeleteClick(currentItem);
              }
          });
    }

    @Override
    public int getItemCount() {
        return mReunions.size();
    }

    /* Je créer le ItemClick sur le recyclerView pour avoir les informations et ouvrir un Dialog */
    public interface RecyclerViewClickListener
    {
       void onClick(Reunion reunion);
    }

    public interface DeleteClickListenner
    {
        void onDeleteClick(Reunion reunion);
    }


}



