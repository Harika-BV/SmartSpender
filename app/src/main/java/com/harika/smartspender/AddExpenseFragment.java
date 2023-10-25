package com.harika.smartspender;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.harika.smartspender.database.SmartSpender_DB;
import com.harika.smartspender.models.ExpenseModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddExpenseFragment extends Fragment {

    public AddExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        SmartSpender_DB dbHelper = new SmartSpender_DB(rootView.getContext());

        TextInputLayout amountET, sentToET, commentET;
        MaterialButton saveButton;

        amountET = rootView.findViewById(R.id.amount);
        sentToET = rootView.findViewById(R.id.sentto);
        commentET = rootView.findViewById(R.id.comments);

        saveButton = rootView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountET.getEditText() == null ) {
                    Toast.makeText(getActivity(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                }
                else if(sentToET.getEditText() == null ) {
                    Toast.makeText(getActivity(), "Please enter the ", Toast.LENGTH_SHORT).show();
                }
                else if(commentET.getEditText() == null ) {
                    Toast.makeText(getActivity(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String currentDate = sdf.format(new Date());

                    ExpenseModel expenseModel = new ExpenseModel();
                    expenseModel.setDate(currentDate);
                    expenseModel.setAmount(Double.parseDouble(amountET.getEditText().getText().toString()));
                    expenseModel.setCategory(sentToET.getEditText().getText().toString());
                    expenseModel.setMessageBody(commentET.getEditText().getText().toString());

                    dbHelper.insertExpenseIntoDatabase(expenseModel);
                    Toast.makeText(getActivity(), "Expense added successfully!", Toast.LENGTH_SHORT).show();

                    Fragment fragment = new HomeFragment();

                    // Use a FragmentTransaction to replace the current fragment with the new one
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null); // Optional: Add to the back stack for navigation
                    transaction.commit();
                }
            }
        });

        return rootView;
    }
}
