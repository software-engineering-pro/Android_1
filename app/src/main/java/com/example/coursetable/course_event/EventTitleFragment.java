package com.example.coursetable.course_event;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable.CourseEdition;
import com.example.coursetable.DatabaseHelper;
import com.example.coursetable.Event;
import com.example.coursetable.R;

import java.util.ArrayList;
import java.util.List;

public class EventTitleFragment extends Fragment {
    public boolean isTwoPane;

    private LayoutInflater inflater;
    private ViewGroup container;
    private static CourseEdition courseEdition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_title_frag, container, false);
        RecyclerView eventTitleRecyclerView = (RecyclerView) view.findViewById(R.id.event_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        CourseEdition temp = (CourseEdition)getActivity().getIntent().getSerializableExtra("this_course");
        if(temp != null){
            courseEdition = temp;
        }
        eventTitleRecyclerView.setLayoutManager(layoutManager);
        Log.d("Test", "Fragment is created");
        EventAdapter adapter = new EventAdapter(getEvents());
        eventTitleRecyclerView.setAdapter(adapter);
        return view;
    }

    public List<Event> getEvents(){
        List<Event> eventList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), "database.db", null, 1);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Log.d("Test", "fragment " + courseEdition.getCourseCode());
//        courseEdition = (CourseEdition) getActivity().getIntent().getSerializableExtra("this_course");
        Cursor cursor = sqLiteDatabase.rawQuery("select * from events WHERE event_code in (select event_code from course_events where course_code = ?)",new String[]{courseEdition.getCourseCode()});
        Log.d("Test", "读取数据");
        if(cursor.moveToFirst()){
            do{
                eventList.add(new Event(
                        cursor.getInt(cursor.getColumnIndex("event_code")),
                        cursor.getString(cursor.getColumnIndex("topic")),
                        cursor.getString(cursor.getColumnIndex("detail")),
                        cursor.getString(cursor.getColumnIndex("deadline"))
                        )
                );
                Log.d("Test",cursor.getString(cursor.getColumnIndex("topic")));
            }while (cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();
        return eventList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTwoPane = false;
    }

    class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
        private List<Event> mEventList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView eventTitleText;
            //?
            public ViewHolder(View view){
                super(view);
                eventTitleText = (TextView) view.findViewById(R.id.event_title);
            }
        }

        public EventAdapter(List<Event> eventList){
            mEventList = eventList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //查找点击事件的位置
                    Event event = mEventList.get(holder.getAdapterPosition());
                    //默认单页
                    EventContentActivity.actionStart(getActivity(), event.getTopic(), event.getDetails(),event.getDeadline());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final LinearLayout deleteBar = (LinearLayout) v.findViewById(R.id.delete_bar);
                    final TextView eventTitle = (TextView) v.findViewById(R.id.event_title);
                    eventTitle.setVisibility(View.INVISIBLE);
                    deleteBar.setVisibility(View.VISIBLE);

                    Button deleteButton = (Button)deleteBar.findViewById(R.id.button_delete);
                    TextView cancelDelete = (TextView) deleteBar.findViewById(R.id.cancel_delete);
                    cancelDelete.setText(eventTitle.getText());
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Event event = mEventList.get(holder.getAdapterPosition());
                            mEventList.remove(event);
                            deleteData(event);
                            Toast.makeText(getContext(), "删除", Toast.LENGTH_SHORT).show();
                        }
                    });

                    cancelDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteBar.setVisibility(View.INVISIBLE);
                            eventTitle.setVisibility(View.VISIBLE);
                        }
                    });
                    return true;
                }
            });
            return holder;
        }

        //Recyler里显示内容
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Event event = mEventList.get(position);
            holder.eventTitleText.setText(event.getTopic());
        }

        @Override
        public int getItemCount() {
            return mEventList.size();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void deleteData(Event event){
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), "database.db", null, 1);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from events where " + "event_code = ?", new Integer[]{event.getEventCode()});
        sqLiteDatabase.close();
    }


}
