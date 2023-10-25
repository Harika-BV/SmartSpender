package com.harika.smartspender;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.harika.smartspender.database.SmartSpender_DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalyticsFragment extends Fragment {

    private TextView monthSpend, yearSpend;

    public AnalyticsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analytics, container, false);

        SmartSpender_DB dbHelper = new SmartSpender_DB(rootView.getContext());

        monthSpend = rootView.findViewById(R.id.monthSpend);
        yearSpend = rootView.findViewById(R.id.yearSpend);

        monthSpend.setText(dbHelper.getTotalMonthlyExpense() +" ");
        yearSpend.setText(dbHelper.getTotalYearlyExpense()+" ");

        setTheChart(rootView, dbHelper.getMonthlyExpenses());
        return rootView;
    }

    private void setTheChart(View rootView, List<Float> monthlyValues) {

        BarChart expenseChart = rootView.findViewById(R.id.expenseChart);

        List<BarEntry> entries = new ArrayList<>();
        for(int i=0;i<monthlyValues.size();i++) {
            entries.add(new BarEntry(i, monthlyValues.get(i)));
        }
        Log.d("DATA", monthlyValues.toString());
        BarDataSet dataSet = new BarDataSet(entries, "Monthly Expenses");
        dataSet.setColor(R.color.primary_color);
        dataSet.setValueTextColor(Color.BLACK);
        BarData barData = new BarData(dataSet);

        expenseChart.setData(barData);

        expenseChart.getDescription().setEnabled(false);
        expenseChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        expenseChart.getAxisRight().setEnabled(false);

        expenseChart.invalidate();
    }
}
