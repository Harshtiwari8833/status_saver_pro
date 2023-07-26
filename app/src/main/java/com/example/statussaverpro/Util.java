package com.example.statussaverpro;

import android.os.Environment;

import java.io.File;

public class Util {
    public static File RootDirectoryWhatsapp =
            new File(Environment.getExternalStorageDirectory()+"/Download/StatusSaverPro/Whatsapp");
    public static void createFileFolder(){
        if(!RootDirectoryWhatsapp.exists()){
           RootDirectoryWhatsapp.mkdir();
        }else{

        }
    }
}
