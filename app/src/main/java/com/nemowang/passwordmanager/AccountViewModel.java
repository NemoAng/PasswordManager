package com.nemowang.passwordmanager;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class AccountViewModel extends AndroidViewModel {

    private AccountRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<Account>> mAllAccounts;

    public AccountViewModel(Application application) {
        super(application);
        mRepository = new AccountRepository(application);
        mAllAccounts = mRepository.getAllAccounts();
    }

    public LiveData<List<Account>> getAllAccounts() {
        return mAllAccounts;
    }

    void insert(Account account) {
        mRepository.insert(account);
    }

    int accountsCount() {
        List<Account> accounts = mAllAccounts.getValue();
        assert accounts != null;
        return accounts.size();
    }
}
