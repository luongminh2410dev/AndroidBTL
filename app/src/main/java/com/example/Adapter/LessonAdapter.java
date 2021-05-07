package com.example.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a_clone.R;
import com.example.model.Lesson;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson> {
    Activity context;
    int resource;
    @NonNull List<Lesson> objects;
    public LessonAdapter(@NonNull Activity context, int resource, @NonNull List<Lesson> objects) {
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

        ImageView imageView = row.findViewById(R.id.imageViewItemCourse);
        TextView textView = row.findViewById(R.id.tvNameItemCourse);
        ProgressBar progressBar = row.findViewById(R.id.progressItemCourse);

        Lesson item = this.objects.get(position);
        imageView.setImageBitmap(item.getImage());
        textView.setText(item.getNameItem());
        progressBar.setProgress(item.getProgress());
        return row;
    }
}
