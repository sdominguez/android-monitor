package com.example.monitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.monitor.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Earthquake> eqList = new ArrayList<>();
        eqList.add(new Earthquake("aaaa","CDMX",4.0,12365498L,105.23,98.127));
        eqList.add(new Earthquake("bbbb","La Paz",1.8,12365498L,105.23,98.127));
        eqList.add(new Earthquake("cccc","Barcelona",0.5,12365498L,105.23,98.127));
        eqList.add(new Earthquake("dddd","Buenos Aires",3.7,12365498L,105.23,98.127));
        eqList.add(new Earthquake("eeee","Washington D.C",2.8,12365498L,105.23,98.127));

        EqAdapter adapter = new EqAdapter();
        binding.eqRecycler.setAdapter(adapter);
        adapter.submitList(eqList);

    }
}