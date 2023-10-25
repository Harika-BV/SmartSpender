package com.harika.smartspender;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harika.smartspender.database.SmartSpender_DB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AllTransactionsFragment extends Fragment {

    public AllTransactionsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_transactions, container, false);

        SmartSpender_DB dbHelper = new SmartSpender_DB(rootView.getContext());

        Cursor cursor = dbHelper.getRecentTransactions(-1);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        TransactionAdapter adapter = new TransactionAdapter(cursor);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);


        return rootView;
    }
}
