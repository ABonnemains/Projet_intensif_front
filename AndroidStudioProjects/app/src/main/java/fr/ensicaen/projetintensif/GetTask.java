package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.os.AsyncTask;

public class GetTask extends AsyncTask<Communication,Void,Communication> {

    private MainActivity activity;

    public GetTask(MainActivity monActivity){this.activity = monActivity;}

    protected Communication doInBackground(Communication... handler){
        handler[0].communicate();
        return handler[0];
    }

    protected void onPostExecute(Communication handler)
    {
        activity.setGetResult(handler.getGetResult());
    }
}
