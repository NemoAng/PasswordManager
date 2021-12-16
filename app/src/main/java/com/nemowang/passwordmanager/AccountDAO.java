package com.nemowang.passwordmanager;

//Data Access Objects are the main classes where you define your database interactions. They can include a variety of query methods.
//
//        The class marked with @Dao should either be an interface or an abstract class. At compile time, Room will generate an implementation of this class when it is referenced by a Database.
//
//        An abstract @Dao class can optionally have a constructor that takes a Database as its only parameter.
//
//        It is recommended to have multiple Dao classes in your codebase depending on the tables they touch.

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDAO {
    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * FROM table_account ORDER BY title ASC")
    LiveData<List<Account>> getAlphabetizedAccounts();

    @Query("SELECT * FROM table_account")
//    List<Account> getAll();
    LiveData<List<Account>> getAll();

    @Query("SELECT * FROM table_account WHERE uid IN (:accountIds)")
    List<Account> loadAllByIds(int[] accountIds);

    @Query("SELECT * FROM table_account WHERE title LIKE :title")
    Account findByName(String title);


    @Update
    void update(Account Account);

    @Query("DELETE FROM table_account WHERE uid = :id")
    void deleteById(int id);

    @Delete
    void delete(Account account);//++

    @Insert
    void insert(Account account);//++

    @Insert
    void insertAll(Account... accounts);//++

    @Query("DELETE FROM table_account")//++
    void deleteAll();
}
