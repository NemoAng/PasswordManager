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

    private final TextView[] accountItemView = new TextView[3];

    private AccountViewHolder(View itemView) {
        super(itemView);
//        accountItemView = itemView.findViewById(R.id.textView);

        Log.d("NEMO_DBG", "itemView.getTransitionName()");

        accountItemView[0] = itemView.findViewById(R.id.tvAccountTitle);
        accountItemView[1] = itemView.findViewById(R.id.tvAccountName);
        accountItemView[2] = itemView.findViewById(R.id.tvAccountPass);
    }

    public void bind(String title, String name, String pass) {
        accountItemView[0].setText(title);
        accountItemView[1].setText(name);
        accountItemView[2].setText(pass);
    }

    @NonNull
    static AccountViewHolder create(@NonNull ViewGroup parent) {
//        view -> LinearLayout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

//        CardView
        View nextChild1 = ((ViewGroup)view).getChildAt(0);

//        LinearLayout
        View nextChild2 = ((ViewGroup)nextChild1).getChildAt(0);

//        nextChild2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View v2 = ((ViewGroup)v).getChildAt(1);
//                String name = ((TextView)v2).getText().toString();
//
//                Log.d("NEMO_DBG", "Account Name: " + name);
//            }
//        });

        nextChild2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View v3 = ((ViewGroup)v).getChildAt(2);
                String pass = ((TextView)v3).getText().toString();

//                ClipboardManager cbManager =  (ClipboardManager)this.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText(PASS_COPY_LABEL, (String) tvPassTxt.getText());
//                cbManager.setPrimaryClip(clip);
//                Toast.makeText(getActivity(), "Password copied.", Toast.LENGTH_SHORT).show();

                Log.d("NEMO_DBG", "Account Password: " + pass);
                return true;
            }
        });

        MainActivity.setBackgroundResource(nextChild2);

        return new AccountViewHolder(view);
    }
}