package com.nemowang.passwordmanager.ui.home;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemowang.passwordmanager.Account;
import com.nemowang.passwordmanager.AccountDAO;
import com.nemowang.passwordmanager.AccountListAdapter;
import com.nemowang.passwordmanager.AccountRoomDatabase;
import com.nemowang.passwordmanager.AccountViewModel;
import com.nemowang.passwordmanager.MainActivity;
import com.nemowang.passwordmanager.PasswordGenHelper;
import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.TopSmoothScroller;
import com.nemowang.passwordmanager.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private AccountViewModel mAccountViewModel;

    private TopSmoothScroller smoothScroller;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("NEMO_DBG", "HomeFragment -> onCreateView");


        smoothScroller = new TopSmoothScroller(getContext());


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        FloatingActionButton fab = root.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

//                String title = getContext().getResources().getString(R.string.input_account);

                TextView title = new TextView(v.getContext());
                // You Can Customise your Title here
                title.setText(getContext().getResources().getString(R.string.input_account));
//                title.setBackgroundColor(Color.parseColor("#ae52d4"));
                title.setPadding(25, 20, 25, 20);
                title.setGravity(Gravity.LEFT);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                MainActivity.setBackgroundResource(title);

//                mBuilder.setTitle(title);
                mBuilder.setCustomTitle(title);
                //  Inflate the Layout Resource file you created in Step 1
                View mView = getLayoutInflater().inflate(R.layout.account_input_v2, null);

                //  Get View elements from Layout file. Be sure to include inflated view name (mView)
                final EditText mTitle = (EditText) mView.findViewById(R.id.edtTitle);
                final EditText mName = (EditText) mView.findViewById(R.id.edtName);
                final EditText mPassword = (EditText) mView.findViewById(R.id.edtPass);
                final EditText mPasswordLen = (EditText) mView.findViewById(R.id.edtPassLen);

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
                        if (mTitle.getText().toString().trim().isEmpty()){
                            Toast.makeText(v.getContext(), "Account title empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (mName.getText().toString().trim().isEmpty()){
                            Toast.makeText(v.getContext(), "Account empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        com.nemowang.passwordmanager.Account account = new com.nemowang.passwordmanager.Account(
                                mTitle.getText().toString(),
                                mName.getText().toString(),
                                mPassword.getText().toString());
                        new SaveAccount().execute(account);
                        accountAddDialog.dismiss();
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

                mPaste.setOnTouchListener(btnEffect);
                mPaste.setOnClickListener(new View.OnClickListener() {
                    @Override
//                    public void onClick (View view) {
//                        ClipboardManager clipboard =  (ClipboardManager)root.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//
//                        if(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
//                            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
//                            mPassword.setText(item.getText());
//                        }
//                    }
                    public void onClick (View view) {
                        int pass_len = 0;

                        try{
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
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        final AccountListAdapter adapter = new AccountListAdapter(new AccountListAdapter.AccountDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get a new or existing ViewModel from the ViewModelProvider.
//        FragmentActivity fact = getActivity();

//        try {
//            mAccountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
//        } catch (Exception e) {
//            Log.e("NEMO_DBG", "onCreateView", e);
//        }

        mAccountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // Update the cached copy of the words in the adapter.
        mAccountViewModel.getAllAccounts().observe(getActivity(), adapter::submitList);

        return root;
    }

    private final View.OnTouchListener btnEffect = new View.OnTouchListener() {
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

    private class SaveAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            AccountRoomDatabase database = AccountRoomDatabase.getDatabase(getActivity());
            AccountDAO dao = database.accountDAO();
            dao.insert(accounts[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            RecyclerView rcv = getActivity().findViewById(R.id.recycler_view);

            int items = Objects.requireNonNull(rcv.getAdapter()).getItemCount();

            if (smoothScroller != null) {
                smoothScroller.setTargetPosition(items);
                (Objects.requireNonNull(rcv.getLayoutManager())).startSmoothScroll(smoothScroller);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        Log.d("NEMO_DBG", "HomeFragment -> onDestroyView");
    }

}