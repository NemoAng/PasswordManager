package com.nemowang.passwordmanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class AccountViewHolder extends RecyclerView.ViewHolder {
//    private final TextView accountItemView;

    private final TextView[] accountItemView = new TextView[2];

    private AccountViewHolder(View itemView) {
        super(itemView);
//        accountItemView = itemView.findViewById(R.id.textView);

        Log.d("NEMO_DBG", "itemView.getTransitionName()");

        accountItemView[0] = itemView.findViewById(R.id.tvAccountTitle);
        accountItemView[1] = itemView.findViewById(R.id.tvAccountName);
    }

    public void bind(String title, String name) {
        accountItemView[0].setText(title);
        accountItemView[1].setText(name);
    }

    @NonNull
    static AccountViewHolder create(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

//        view.setBackgroundResource(R.drawable.pass_text_bg_purple);

        return new AccountViewHolder(view);
    }
}