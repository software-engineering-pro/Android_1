package com.memo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memo.R;
import com.memo.model.Note;
import com.memo.Utils.noteUtils;

import java.util.ArrayList;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.noteHolder>{

    private Context context;
    private ArrayList<Note> notes;

    public notesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }


    @Override
    public noteHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false);
        return new noteHolder(v);
    }

    @Override
    public void onBindViewHolder( noteHolder holder, int position) {
        Note note = getNote(position);
        if(note != null){
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(noteUtils.dateFromLong(note.getNoteDate()));
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Note getNote(int position){
        return notes.get(position);
    }

    class noteHolder extends RecyclerView.ViewHolder{

        TextView noteText,noteDate;


        public noteHolder(@NonNull View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteText = itemView.findViewById(R.id.note_text);

        }
    }
}
