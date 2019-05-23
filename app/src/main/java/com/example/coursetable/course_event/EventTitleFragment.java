package com.example.coursetable.course_event;

import android.database.Cursor;
import android.app.Activity;
import android.content.Intent;
import com.example.coursetable.MainActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.ListFragment;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventTitleFragment extends Fragment {
    public boolean isTwoPane;

    private LayoutInflater inflater;
    private ViewGroup container;
    private static CourseEdition courseEdition;
    private static String thisday;
    private int mode = 0; //if mode = 1 coursemode, if mode = 2 datamode

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_title_frag, container, false);
        RecyclerView eventTitleRecyclerView = (RecyclerView) view.findViewById(R.id.event_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        CourseEdition temp = (CourseEdition)getActivity().getIntent().getSerializableExtra("this_course");
        String temp2 = (String)getActivity().getIntent().getSerializableExtra("thisday");

        if(temp != null){
            courseEdition = temp;
            mode = 1;
            //Log.d("13723773255",temp2);
        }

        //Log.d("CCCCCCCCCCheck",temp2);

        if(temp2 != null){
            thisday = temp2;
            mode = 2;
        }


        eventTitleRecyclerView.setLayoutManager(layoutManager);
        //System.out.println("hhhhhhhhhhhhhhhhh "+thisday);
        Log.d("Test", "Fragment is created");
        //Log.d("Test", getEvents().toString());
        Log.d("Test","17");
        EventAdapter adapter = new EventAdapter(getEvents());
        eventTitleRecyclerView.setAdapter(adapter);
        return view;
    }

    public List<Event> getEvents(){
        Log.d("Test","6");
        List<Event> eventList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), "database.db", null, 1);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        //       Log.d("Test", "fragment " + courseEdition.getCourseCode());
//        courseEdition = (CourseEdition) getActivity().getIntent().getSerializableExtra("this_course");


        if(mode == 1){
            Cursor cursor = sqLiteDatabase.rawQuery("select * from events WHERE event_code in (select event_code from course_events where course_code = ?)",new String[]{courseEdition.getCourseCode()});
            Log.d("Test", "读取course数据");
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
        }
        else if(mode == 2){
            Log.d("Test","5");
            //System.out.println("13723773255"+thisday);
            Cursor cursor = sqLiteDatabase.rawQuery("select * from events WHERE Date(deadline) = ?",new String[]{getsub(thisday)});
            Log.d("Test", "读取date数据");
            if(cursor.moveToFirst()){
                Log.d("Test","111");
                do{
                    eventList.add(new Event(
                                    cursor.getInt(cursor.getColumnIndex("event_code")),
                                    cursor.getString(cursor.getColumnIndex("topic")),
                                    cursor.getString(cursor.getColumnIndex("detail")),
                                    cursor.getString(cursor.getColumnIndex("deadline"))
                            )
                    );
                    Log.d("Test777",cursor.getString(cursor.getColumnIndex("topic")));
                }while (cursor.moveToNext());
                cursor.close();
                //System.out.println("13723773255"+eventList.get(0).getTopic());
            }
            sqLiteDatabase.close();
        }
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
            Log.d("Test","33");
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
            Log.d("Test","3");

            Event event = mEventList.get(position);
            holder.eventTitleText.setText(event.getTopic());
        }

        @Override
        public int getItemCount() {
            Log.d("Test","2");
            return mEventList.size();
        }


    }



    private void deleteData(Event event){
        Log.d("Test","1");
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(), "database.db", null, 1);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from events where " + "event_code = ?", new Integer[]{event.getEventCode()});
        sqLiteDatabase.close();
        Activity activity = getActivity();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("course", courseEdition);
        activity.setResult(Activity.RESULT_OK, intent);
        Log.d("Test1", "go back");
        activity.finish();
    }

    private String getsub(String in){
        String sub = in.substring(0,10);
        System.out.println("18885in "+in);
        System.out.println("18885 "+sub);
        return sub;
    }




}
