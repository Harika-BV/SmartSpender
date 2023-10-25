package com.harika.smartspender;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.harika.smartspender.database.SmartSpender_DB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView expenseOfThisMonthTv, seeAllTextView;
    private MaterialButton addExpenseButton;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SmartSpender_DB dbHelper = new SmartSpender_DB(rootView.getContext());
        expenseOfThisMonthTv = rootView.findViewById(R.id.monthSpend);
        seeAllTextView = rootView.findViewById(R.id.viewAll);
        addExpenseButton = rootView.findViewById(R.id.addExpense);

        String currentMonthYear = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date());
        double expenseOfThisMonth = dbHelper.getExpenseOfThisMonth(currentMonthYear);
        expenseOfThisMonthTv.setText(expenseOfThisMonth +" INR");

        Cursor cursor = dbHelper.getRecentTransactions(5);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        TransactionAdapter adapter = new TransactionAdapter(cursor);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddExpenseFragment();

                // Use a FragmentTransaction to replace the current fragment with the new one
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null); // Optional: Add to the back stack for navigation
                transaction.commit();
            }
        });

        seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AllTransactionsFragment();

                // Use a FragmentTransaction to replace the current fragment with the new one
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null); // Optional: Add to the back stack for navigation
                transaction.commit();
            }
        });


        return rootView;
    }
}
