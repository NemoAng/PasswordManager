package com.nemowang.passwordmanager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class AccountListAdapter extends ListAdapter<Account, AccountViewHolder> {

    public AccountListAdapter(@NonNull DiffUtil.ItemCallback<Account> diffCallback) {
        super(diffCallback);
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AccountViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        Account current = getItem(position);
        holder.bind(current.getTitle(), current.getName(), current.getPassword(), current.getID());
    }

    public static class AccountDiff extends DiffUtil.ItemCallback<Account> {

        @Override
        public boolean areItemsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}