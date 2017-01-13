package fr.ensicaen.projetintensif.login;

import android.os.AsyncTask;

import fr.ensicaen.projetintensif.communication.Communication;

public class LoginTask extends AsyncTask<Communication,Void,Communication> {

    private LoginActivity activity;

    public LoginTask(LoginActivity monActivity){this.activity = monActivity;}

    protected Communication doInBackground(Communication... handler){
        handler[0].communicate();
        return handler[0];
    }

    protected void onPostExecute(Communication handler)
    {
        activity.setLoginSuccess(handler.getToken());
    }
}
