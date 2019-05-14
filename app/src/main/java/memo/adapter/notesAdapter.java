package com.memo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.memo.R;
import com.memo.callbacks.NoteEventListener;
import com.memo.model.Note;
import com.memo.Utils.noteUtils;

import java.util.ArrayList;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.noteHolder>{

    private Context context;
    private ArrayList<Note> notes;
    private NoteEventListener listener;

    public notesAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }


    @NonNull
    @Override
    public noteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false);
        return new noteHolder(v);
    }

    @Override
    public void onBindViewHolder( noteHolder holder, int position) {
        final Note note = getNote(position);
        if(note != null){
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(noteUtils.dateFromLong(note.getNoteDate()));
            // init note click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(note);
                }
            });

            // init note long click
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
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


        public noteHolder( View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteText = itemView.findViewById(R.id.note_text);

        }
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }
}
