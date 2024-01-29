package com.burhanrashid52.photoediting.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long addPackage(LocalModel localModel);

    @Insert
    long savePackage(SavedModel savedModel);

    @Delete
    int deletePackage(LocalModel localModel);

    @Update
    int updatePackage(LocalModel localModel);

    @Query("SELECT * FROM Downloaded_Packages")
    List<LocalModel> getAllPackages();

    @Query("SELECT * FROM saved_packages")
    List<SavedModel> getSavedPackages();

    @Query("SELECT * FROM saved_packages WHERE id= :currentId")
    boolean getCurrentSavedPackage(int currentId);

    @Query("DELETE FROM Downloaded_Packages")
    void  deleteAllDownloadedPackages();

    @Query("DELETE  FROM Downloaded_Packages WHERE id= :deletedId ")
    void deleteItem( int deletedId);


    @Query("DELETE  FROM saved_packages WHERE id= :deletedId ")
    void deleteSavedItem( int deletedId);
}
