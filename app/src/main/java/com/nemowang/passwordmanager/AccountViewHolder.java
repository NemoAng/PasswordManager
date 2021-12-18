package com.nemowang.passwordmanager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
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

//        Log.d("NEMO_DBG_X", id.toString());
    }

    @NonNull
    static AccountViewHolder create(@NonNull ViewGroup parent) {
//        view -> LinearLayout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

//        CardView
        View cardView = ((ViewGroup) view).getChildAt(0);
        View constraintLayout = ((ViewGroup) cardView).getChildAt(0);

//        LinearLayout
        View nextChildLL = ((ViewGroup) constraintLayout).getChildAt(0);
        View imageDel = ((ViewGroup) constraintLayout).getChildAt(1);

        imageDel.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setAlpha(0.6F);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());

                        TextView title = new TextView(v.getContext());
                        // You Can Customise your Title here
                        title.setText(v.getContext().getResources().getString(R.string.a_u_sure));
//                      title.setBackgroundColor(Color.parseColor("#ae52d4"));
                        title.setPadding(25, 20, 25, 20);
                        title.setGravity(Gravity.LEFT);
                        title.setTextColor(Color.WHITE);
                        title.setTextSize(20);
                        MainActivity.setBackgroundResource(title);

//                      mBuilder.setTitle(title);
                        mBuilder.setCustomTitle(title);

                        //  Inflate the Layout Resource file you created in Step 1

                        //View mView = getLayoutInflater().inflate(R.layout.account_input, null);
                        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View mView = inflater.inflate(R.layout.account_remove, null);

                        final TextView mRemove = (TextView) mView.findViewById(R.id.textViewDel);

                        mRemove.setText("😍🙄😀😛😎🤩🥰😪😋😥😂🤣😘😣😉😏😫😜😢😊");


                        Button mOk = (Button) mView.findViewById(R.id.yesDel);
                        Button mCancel = (Button) mView.findViewById(R.id.cancelDel);

                        //  Create the AlertDialog using everything we needed from above
                        mBuilder.setView(mView);

                        final AlertDialog accountDelDialog = mBuilder.create();

                        mOk.setOnTouchListener(btnEffect);
                        mOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new RemoveAccount().execute(nextChildLL);
                                accountDelDialog.dismiss();
                            }
                        });

                        mCancel.setOnTouchListener(btnEffect);
                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                accountDelDialog.dismiss();
                            }
                        });

                        accountDelDialog.show();
//                        break;
                    case MotionEvent.ACTION_CANCEL: {
                        v.setAlpha(1F);
                        break;
                    }
                }
                v.invalidate();
                return true;
            }
        });

//        nextChildLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View v2 = ((ViewGroup)v).getChildAt(1);
//                String name = ((TextView)v2).getText().toString();
//
//                Log.d("NEMO_DBG", "Account Name: " + name);
//            }
//        });

//        nextChildLL.setOnClickListener(new DoubleClickListener() {
//            @Override
//            public void onDoubleClick(@NonNull View v) {
//                Log.d("NEMO_DBG", "Double clicked.");
//            }
//        });

        nextChildLL.setOnClickListener(new DoubleClickListener(300) {

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

            //This is a copy/paste version(original)
            @SuppressLint("ClickableViewAccessibility")
//            @Override
            public void onDoubleClick_V0(View v) {
                // double-click code that is executed if the user double-taps
//                Log.d("NEMO_DBG", "Double clicked.");

                View v1 = ((ViewGroup) v).getChildAt(0);
                View v2 = ((ViewGroup) v).getChildAt(1);
                View v3 = ((ViewGroup) v).getChildAt(2);
                View v4 = ((ViewGroup) v).getChildAt(3);

                String title = ((TextView) v1).getText().toString();
                String name = ((TextView) v2).getText().toString();
                String pass = ((TextView) v3).getText().toString();
                String id = ((TextView) v4).getText().toString();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());

                mBuilder.setTitle("XXX Your Account");
                //  Inflate the Layout Resource file you created in Step 1

                //View mView = getLayoutInflater().inflate(R.layout.account_input, null);
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//                View mView = inflater.inflate(R.layout.account_input_v1, null);
                View mView = inflater.inflate(R.layout.account_input_v2, null);

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
                    public void onClick(View view) {
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
                    public void onClick(View view) {
                        accountAddDialog.dismiss();
                    }
                });

                //
                mPaste.setOnTouchListener(btnEffect);
                mPaste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                            // Gets the clipboard as text.
                            mPassword.setText(item.getText());
                        }
                    }
                });
                accountAddDialog.show();
            }


            //This is a generating version(original->reviewed)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDoubleClick(View v) {
                // double-click code that is executed if the user double-taps
//                Log.d("NEMO_DBG", "Double clicked.");

                View v1 = ((ViewGroup) v).getChildAt(0);
                View v2 = ((ViewGroup) v).getChildAt(1);
                View v3 = ((ViewGroup) v).getChildAt(2);
                View v4 = ((ViewGroup) v).getChildAt(3);

                String title = ((TextView) v1).getText().toString();
                String name = ((TextView) v2).getText().toString();
                String pass = ((TextView) v3).getText().toString();

                String id = ((TextView) v4).getText().toString();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());

                TextView title_ = new TextView(v.getContext());

                title_.setText(v.getContext().getResources().getString(R.string.title_edit));
//                      title.setBackgroundColor(Color.parseColor("#ae52d4"));
                title_.setPadding(25, 20, 25, 20);
                title_.setGravity(Gravity.LEFT);
                title_.setTextColor(Color.WHITE);
                title_.setTextSize(20);

                MainActivity.setBackgroundResource(title_);

//                mBuilder.setTitle("Edit Your Account");
                mBuilder.setCustomTitle(title_);
                //  Inflate the Layout Resource file you created in Step 1

                //View mView = getLayoutInflater().inflate(R.layout.account_input, null);
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//                View mView = inflater.inflate(R.layout.account_input_v1, null);
                View mView = inflater.inflate(R.layout.account_input_v2, null);

//                MainActivity.setBackgroundResource(mView);

                //  Get View elements from Layout file. Be sure to include inflated view name (mView)
                final EditText mTitle = (EditText) mView.findViewById(R.id.edtTitle);
                final EditText mName = (EditText) mView.findViewById(R.id.edtName);
                final EditText mPassword = (EditText) mView.findViewById(R.id.edtPass);

                mPassword.setEnabled(true);

                final EditText mPasswordLen = (EditText) mView.findViewById(R.id.edtPassLen);

                mTitle.setText(title);
                mName.setText(name);
                mPassword.setText(pass);

                Button mOk = (Button) mView.findViewById(R.id.save);
                Button mCancel = (Button) mView.findViewById(R.id.cancel);
                Button mGenerate = (Button) mView.findViewById(R.id.paste);

                //  Create the AlertDialog using everything we needed from above
                mBuilder.setView(mView);
                final AlertDialog accountAddDialog = mBuilder.create();

                //  Set Listener for the OK Button
                mOk.setOnTouchListener(btnEffect);
                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTitle.getText().toString().trim().isEmpty()) {
                            Toast.makeText(v.getContext(), "Account title empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (mName.getText().toString().trim().isEmpty()) {
                            Toast.makeText(v.getContext(), "Account empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        if (mTitle.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {
//                            Toast.makeText(v.getContext(), "Title and password must be provided.", Toast.LENGTH_SHORT).show();
//                        } else {
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
//                        }
                    }
                });

                //  Set Listener for the CANCEL Button
                mCancel.setOnTouchListener(btnEffect);
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        accountAddDialog.dismiss();
                    }
                });

                //
                mGenerate.setOnTouchListener(btnEffect);
                mGenerate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pass_len = 0;

                        try {
                            pass_len = Integer.parseInt(mPasswordLen.getText().toString());
                        } catch (NumberFormatException e) {

                            Toast.makeText(v.getContext(), "Password length error.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String password = PasswordGenHelper.Password(view, pass_len,
                                MainActivity.SETTING_PASS_UPP,
                                MainActivity.SETTING_PASS_LOW,
                                MainActivity.SETTING_PASS_NUM,
                                MainActivity.SETTING_PASS_SYM,
                                MainActivity.SETTING_PASS_DUP,
                                MainActivity.SETTING_PASS_SEQ,
                                MainActivity.SETTING_PASS_BEG,
                                MainActivity.SETTING_PASS_SIM
                        );

                        mPassword.setText(password);
                    }
                });
                accountAddDialog.show();
            }

            @Override
            public void onSingleClick(View v) {
                // double-click code that is executed if the user double-taps
                Log.d("NEMO_DBG", "Single clicked.");

                // Toast.makeText(this, "Account name copied.", Toast.LENGTH_SHORT).show();

                // View v1 = ((ViewGroup)view).getChildAt(0);
                View v2 = ((ViewGroup) v).getChildAt(1);
                // View v3 = ((ViewGroup)view).getChildAt(2);

                // String title = ((TextView)v1).getText().toString();
                String name = ((TextView) v2).getText().toString();
                // String pass = ((TextView)v3).getText().toString();

                ClipboardManager cbManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(MainActivity.PASS_COPY_LABEL, name);
                cbManager.setPrimaryClip(clip);

                Toast.makeText(v.getContext(), "Account name copied.", Toast.LENGTH_SHORT).show();
            }

        });

        nextChildLL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View v3 = ((ViewGroup) v).getChildAt(2);
                String pass = ((TextView) v3).getText().toString();
                ClipboardManager cbManager = (ClipboardManager) v3.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PASS_COPY_LABEL", pass);
                cbManager.setPrimaryClip(clip);
                Toast.makeText(v3.getContext(), "Password copied.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        MainActivity.setBackgroundResource(nextChildLL);

        return new AccountViewHolder(view);
    }

    private static final View.OnTouchListener btnEffect = new View.OnTouchListener() {
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

    private static class RemoveAccount extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            View v = (View) params[0];

            View v1 = ((ViewGroup) v).getChildAt(3);

            try{
                CharSequence idStr = ((TextView)v1).getText();
                int id = Integer.parseInt(String.valueOf(idStr));

                AccountRoomDatabase database = AccountRoomDatabase.getDatabase(v.getContext());
                AccountDAO dao = database.accountDAO();
                dao.deleteById(id);
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }
}