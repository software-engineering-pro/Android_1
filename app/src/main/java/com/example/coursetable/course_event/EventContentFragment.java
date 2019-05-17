package com.example.coursetable.course_event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coursetable.R;

public class EventContentFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_content_frag, container, false);
        return view;
    }

    public void refresh(String eventTopic, String eventContent, String eventDeadline){
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView eventTopicText = (TextView) view.findViewById(R.id.event_topic);
        TextView eventContentText = (TextView) view.findViewById(R.id.event_detail);
        TextView eventDeadlineText = (TextView) view.findViewById(R.id.event_deadline);
        eventTopicText.setText(eventTopic);
        eventContentText.setText(eventContent);
        eventDeadlineText.setText(eventDeadline);
    }
}
