package fr.ensicaen.projetintensif;

import android.os.AsyncTask;

/**
 * Created by Maxime on 12/01/2017.
 */

public class RefreshHelpTask extends AsyncTask<Communication,Void,Communication> {
    private MainActivity activity;

    public RefreshHelpTask(MainActivity monActivity){this.activity = monActivity;}

    protected Communication doInBackground(Communication... handler){

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler[0].communicate();
            if (handler[0].doesSomeoneNeedHelp()){
                activity.notifSomeoneAskedHelp();
            }
        return handler[0];
    }

    @Override
    protected void onPostExecute(Communication communication) {
        activity.refreshHelp();
    }
}
