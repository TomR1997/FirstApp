package util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.tom.myfirstapp.MainActivity;

public class PermissionManager {
    private static final int MY_PERMISSION_REQUEST = 1;

    public PermissionManager(){
    }

    public boolean requestPermission(Context context){
        MainActivity mainActivity = (MainActivity) context;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(context,
                        "Read external storage permission is needed to show the songs.",
                        Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        } else {
            return true;
        }
        return false;
    }

    public boolean requestPermissionFragment(Context context){
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(context,
                        "Read external storage permission is needed to show the songs.",
                        Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(fragmentActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        } else {
            return true;
        }
        return false;
    }
}
