package com.harika.smartspender;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Cursor cursor;

    public TransactionAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            holder.dateTextView.setText(cursor.getString(cursor.getColumnIndex("date")));
            holder.amountTextView.setText("Rs. " + cursor.getString(cursor.getColumnIndex("amount")));
            holder.spenderTextView.setText(cursor.getString(cursor.getColumnIndex("sender")));
            holder.categoryTextView.setText(cursor.getString(cursor.getColumnIndex("category")));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, amountTextView, spenderTextView, categoryTextView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            spenderTextView = itemView.findViewById(R.id.spenderTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}

