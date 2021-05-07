package com.example.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a_clone.R;
import com.example.model.Course;

import java.util.List;

public class CourseAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    @NonNull List objects;
    public CourseAdapter(@NonNull Activity context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        ImageView imageView = row.findViewById(R.id.imageView);
        TextView tv = row.findViewById(R.id.tvNameCourse);

        Course course = (Course) this.objects.get(position);
        imageView.setImageBitmap(course.getIcon());
        tv.setText(course.getNameCourse());

        return row;
    }
}
