package com.nemowang.passwordmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Account.class}, version = 1, exportSchema = true)
public abstract class AccountRoomDatabase extends RoomDatabase {
    public abstract AccountDAO accountDAO();

    private static volatile AccountRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE table_account "
//                    +"ADD COLUMN last_name TEXT");
//
//        }
//    };

    static AccountRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AccountRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AccountRoomDatabase.class, "account_database")
                            .addCallback(sRoomDatabaseCallback)
//                            .fallbackToDestructiveMigration()
//                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                AccountDAO dao = INSTANCE.accountDAO();
                dao.deleteAll();

                Account[] accounts = new Account[]{
                        new Account("My Gmail", "nemo.xun.jin.wang@gmail.com", "123456"),
                        new Account("My Yahoo", "nemo.xun.jin.wang@yahoo.com", "123456"),
                        new Account("My Twitter", "nemo.xun.jin.wang@twitter.com", "123456"),
                        new Account("My Gmail", "nemo.xun.jin.wang@gmail.com", "123456"),
                        new Account("My Gmail", "nemo.xun.jin.wang@gmail.com", "123456")};
                dao.insertAll(accounts);

            });
        }
    };
}