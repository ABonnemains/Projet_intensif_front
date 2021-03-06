package fr.ensicaen.projetintensif.signup;

import android.os.AsyncTask;

import fr.ensicaen.projetintensif.communication.Communication;

public class SignupTask extends AsyncTask<Communication,Void,Communication> {

    private SignupActivity activity;

    public SignupTask(SignupActivity monActivity){this.activity = monActivity;}

    protected Communication doInBackground(Communication... handler){
        handler[0].communicate();
        return handler[0];
    }

    protected void onPostExecute(Communication handler)
    {
        activity.setRegisterSucces(handler.getRegisterSucceded());
    }
}
