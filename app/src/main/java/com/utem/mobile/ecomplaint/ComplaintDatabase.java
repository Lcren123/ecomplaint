package com.utem.mobile.ecomplaint;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.utem.mobile.ecomplaint.room.ComplaintCategoryRoom;
import com.utem.mobile.ecomplaint.room.ComplaintImageRoom;
import com.utem.mobile.ecomplaint.room.ComplaintRoom;
import com.utem.mobile.ecomplaint.room.ResidentRoom;

@Database(entities = {ComplaintRoom.class, ResidentRoom.class, ComplaintCategoryRoom.class, ComplaintImageRoom.class},version = 1, exportSchema = false)
public abstract class ComplaintDatabase extends RoomDatabase {

    private static ComplaintDatabase instance;

    public abstract ComplaintManager getComplaintManager();

    public static ComplaintDatabase getInstance(Context context){
        synchronized (ComplaintDatabase.class){
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(),ComplaintDatabase.class,"complaintDb").build();
            }
        }
        return instance;
    }

}
