package com.nemowang.passwordmanager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

class AccountViewHolder extends RecyclerView.ViewHolder {
//    private final TextView accountItemView;

    private final TextView[] accountItemView = new TextView[4];

//    private static View view4Save;

    private AccountViewHolder(View itemView) {
        super(itemView);

        accountItemView[0] = itemView.findViewById(R.id.tvAccountTitle);
        accountItemView[1] = itemView.findViewById(R.id.tvAccountName);
        accountItemView[2] = itemView.findViewById(R.id.tvAccountPass);

        accountItemView[3] = itemView.findViewById(R.id.tvAccountID);

//        view4Save = itemView;
    }

    @SuppressLint("SetTextI18n")
    public void bind(String title, String name, String pass, Integer id) {
        accountItemView[0].setText(title);
        accountItemView[1].setText(name);
        accountItemView[2].setText(pass);

        accountItemView[3].setText(id.toString());

        Log.d("NEMO_DBG_X", id.toString());
    }

    @NonNull
    static AccountViewHolder create(@NonNull ViewGroup parent) {
        Log.d("NEMO_DBG", "AccountViewHolder -> create");

//        view -> LinearLayout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

//        CardView
        View cardView = ((ViewGroup)view).getChildAt(0);

//        LinearLayout
        View nextChild2 = ((ViewGroup)cardView).getChildAt(0);

//        nextChild2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View v2 = ((ViewGroup)v).getChildAt(1);
//                String name = ((TextView)v2).getText().toString();
//
//                Log.d("NEMO_DBG", "Account Name: " + name);
//            }
//        });

//        nextChild2.setOnClickListener(new DoubleClickListener() {
//            @Override
//            public void onDoubleClick(@NonNull View v) {
//                Log.d("NEMO_DBG", "Double clicked.");
//            }
//        });

        nextChild2.setOnClickListener(new DoubleClickListener(300) {

            private View.OnTouchListener btnEffect = new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.setAlpha(0.6F);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            v.setAlpha(1F);
                            break;
                        }
                    }
                    v.invalidate();
                    return false;
                }
            };

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDoubleClick(View v) {
                // double-click code that is executed if the user double-taps
//                Log.d("NEMO_DBG", "Double clicked.");

                View v1 = ((ViewGroup)v).getChildAt(0);
                View v2 = ((ViewGroup)v).getChildAt(1);
                View v3 = ((ViewGroup)v).getChildAt(2);
                View v4 = ((ViewGroup)v).getChildAt(3);

                String title = ((TextView)v1).getText().toString();
                String name = ((TextView)v2).getText().toString();
                String pass = ((TextView)v3).getText().toString();
                String id = ((TextView)v4).getText().toString();

                Log.d("NEMO_DBG_X", id);


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());

                mBuilder.setTitle("Input Your Account");
                //  Inflate the Layout Resource file you created in Step 1

                //View mView = getLayoutInflater().inflate(R.layout.account_input, null);
                LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View mView = inflater.inflate(R.layout.account_input, null);

//                MainActivity.setBackgroundResource(mView);

                //  Get View elements from Layout file. Be sure to include inflated view name (mView)
                final EditText mTitle = (EditText) mView.findViewById(R.id.edtTitle);
                final EditText mName = (EditText) mView.findViewById(R.id.edtName);
                final EditText mPassword = (EditText) mView.findViewById(R.id.edtPass);

                mTitle.setText(title);
                mName.setText(name);
                mPassword.setText(pass);

                Button mOk = (Button) mView.findViewById(R.id.save);
                Button mCancel = (Button) mView.findViewById(R.id.cancel);
                Button mPaste = (Button) mView.findViewById(R.id.paste);

                //  Create the AlertDialog using everything we needed from above
                mBuilder.setView(mView);
                final AlertDialog accountAddDialog = mBuilder.create();

                //  Set Listener for the OK Button
                mOk.setOnTouchListener(btnEffect);
                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        if (mTitle.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {
                            Toast.makeText(v.getContext(), "Title and password must be provided.", Toast.LENGTH_SHORT).show();
                        } else {
                            com.nemowang.passwordmanager.Account account = new com.nemowang.passwordmanager.Account(
                                    mTitle.getText().toString(),
                                    mName.getText().toString(),
                                    mPassword.getText().toString());

                            account.uid = Integer.parseInt(id);

                            new UpdateAccount().execute(view, account);

//                            AccountRoomDatabase database = AccountRoomDatabase.getDatabase(mOk.getContext());
//                            AccountDAO dao = database.accountDAO();
//                            dao.insert(account);

                            accountAddDialog.dismiss();
                        }
                    }
                });

                //  Set Listener for the CANCEL Button
                mCancel.setOnTouchListener(btnEffect);
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        accountAddDialog.dismiss();
                    }
                });

                //
                mPaste.setOnTouchListener(btnEffect);
                mPaste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        ClipboardManager clipboard =  (ClipboardManager)v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        if(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                            // Gets the clipboard as text.
                            mPassword.setText(item.getText());
                        }
                    }
                });
                accountAddDialog.show();
            }


            @Override
            public void onSingleClickShit(View v) {
                // double-click code that is executed if the user double-taps
                Log.d("NEMO_DBG", "Single clicked.");

                // Toast.makeText(this, "Account name copied.", Toast.LENGTH_SHORT).show();

                // View v1 = ((ViewGroup)view).getChildAt(0);
                View v2 = ((ViewGroup)v).getChildAt(1);
                // View v3 = ((ViewGroup)view).getChildAt(2);

                // String title = ((TextView)v1).getText().toString();
                String name = ((TextView)v2).getText().toString();
                // String pass = ((TextView)v3).getText().toString();

                ClipboardManager cbManager =  (ClipboardManager)v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(MainActivity.PASS_COPY_LABEL, name);
                cbManager.setPrimaryClip(clip);

                Toast.makeText(v.getContext(), "Account name copied.", Toast.LENGTH_SHORT).show();
            }

        });


        nextChild2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View v3 = ((ViewGroup)v).getChildAt(2);
                String pass = ((TextView)v3).getText().toString();
                ClipboardManager cbManager =  (ClipboardManager)v3.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PASS_COPY_LABEL", pass);
                cbManager.setPrimaryClip(clip);
                Toast.makeText(v3.getContext(), "Password copied.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        MainActivity.setBackgroundResource(nextChild2);

        return new AccountViewHolder(view);
    }

    ///
    private static class UpdateAccount extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            View v = (View) params[0];
            Account account = (Account) params[1];

            AccountRoomDatabase database = AccountRoomDatabase.getDatabase(v.getContext());
            AccountDAO dao = database.accountDAO();
            dao.update(account);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
//            RecyclerView rcv = getActivity().findViewById(R.id.recyclerview);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//            layoutManager.setStackFromEnd(true);
//            rcv.setLayoutManager(layoutManager);
//
//
//            rcv.setHasFixedSize(true);
//            rcv.smoothScrollToPosition(rcv.getAdapter().getItemCount());
        }
    }

}