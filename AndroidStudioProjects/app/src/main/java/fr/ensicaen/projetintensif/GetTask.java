package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.os.AsyncTask;

public class GetTask extends AsyncTask<Communication,Void,Communication> {

    private Activity activity;

    public GetTask(Activity monActivity){this.activity = monActivity;}

    protected Communication doInBackground(Communication... handler){
        handler[0].communicate();
        return handler[0];
    }

    protected void onPostExecute(Communication handler)
    {

    }
}
