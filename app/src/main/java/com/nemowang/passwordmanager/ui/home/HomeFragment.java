package com.nemowang.passwordmanager.ui.home;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private AccountViewModel mAccountViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

                mBuilder.setTitle("Input Your Account");
                //  Inflate the Layout Resource file you created in Step 1
                View mView = getLayoutInflater().inflate(R.layout.account_input, null);

                //  Get View elements from Layout file. Be sure to include inflated view name (mView)
                final EditText mTitle = (EditText) mView.findViewById(R.id.edtTitle);
                final EditText mName = (EditText) mView.findViewById(R.id.edtName);
                final EditText mPassword = (EditText) mView.findViewById(R.id.edtPass);

                Button mOk = (Button) mView.findViewById(R.id.save);
                Button mCancel = (Button) mView.findViewById(R.id.cancel);
                Button mPaste = (Button) mView.findViewById(R.id.paste);

                //  Create the AlertDialog using everything we needed from above
                mBuilder.setView(mView);
                final AlertDialog timerDialog = mBuilder.create();

                //  Set Listener for the OK Button
                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        if (mTitle.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Title and password must be provided.", Toast.LENGTH_SHORT).show();
                        } else {
                            com.nemowang.passwordmanager.Account account = new com.nemowang.passwordmanager.Account(
                                    mTitle.getText().toString(),
                                    mName.getText().toString(),
                                    mPassword.getText().toString());

//                            AccountRoomDatabase database = AccountRoomDatabase.getDatabase(getActivity());
//                            AccountDAO dao = database.accountDAO();
//                            dao.insert(account);
                            new SaveAccount().execute(account);

                            timerDialog.dismiss();
                        }
                    }
                });

                //  Set Listener for the CANCEL Button
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        timerDialog.dismiss();
                    }
                });

                //
                mPaste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        ClipboardManager clipboard =  (ClipboardManager)root.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        if(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                            // Gets the clipboard as text.
                            mPassword.setText(item.getText());
                        }
                    }
                });

                timerDialog.show();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
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

    private class SaveAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            AccountRoomDatabase database = AccountRoomDatabase.getDatabase(getActivity());
            AccountDAO dao = database.accountDAO();
            dao.insert(accounts[0]);

            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}