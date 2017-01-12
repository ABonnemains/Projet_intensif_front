package fr.ensicaen.projetintensif;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import javax.net.ssl.HttpsURLConnection;

public class Communication {

    public enum RequestType{
        LOGIN,
        REGISTER
    }

    private final String _serverURL = "https://roule-ma-poule.herokuapp.com/";
    //private final String _serverURL = "https://secure-lake-76948.herokuapp.com/";
    private final String _urlLogin = "authentication/login";
    private final String _urlRegister = "authentication/register";

    private RequestType _currentRequestType;
    private static String _token;

    private boolean registerSucceded = false;

    private String[] infoLogin;
    private Object[] infoRegister;

    public Communication(String login, String pw, String pwConfirm, String name, String surname, String phoneNumber, Timestamp birthDate){
        infoRegister = new Object[]{login, pw, pwConfirm, name, surname,phoneNumber,birthDate};
        _currentRequestType = RequestType.REGISTER;
    }

    public Communication(String login, String pw)
    {
        infoLogin = new String[]{login, pw};
        _currentRequestType = RequestType.LOGIN;
    }


    public boolean getRegisterSucceded() {
        return registerSucceded;
    }


    public void communicate(){
        try {
            switch (_currentRequestType)
            {
                case LOGIN:
                    communicate(infoLogin[0], infoLogin[1]);
                    break;
                case REGISTER:
                    communicate((String)infoRegister[0],(String)infoRegister[1],(String)infoRegister[2],(String)infoRegister[3],(String)infoRegister[4],(String)infoRegister[5],(Timestamp)infoRegister[6]);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void communicate(String login, String pwd)
    {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("login", login);
            jsonObj.put("password",pwd);

            _token = sendPost(jsonObj, _urlLogin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void communicate(String login, String pw, String pwConfirm, String name, String surname, String phoneNumber, Timestamp birthDate){
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("login", login);
            jsonObj.put("password",pw);
            jsonObj.put("password_confirmation",pwConfirm);
            jsonObj.put("user_name",name);
            jsonObj.put("user_surname",surname);
            jsonObj.put("user_phone",phoneNumber);
            jsonObj.put("user_birthdate",birthDate);

            String res = sendPost(jsonObj, _urlRegister);

            Log.d("res",res);

            if (res.equals("OK")){
                registerSucceded = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken()
    {
        return _token;
    }

    private String sendPost(JSONObject jsonObject, String urlPost)throws Exception{

        URL obj = new URL(_serverURL+urlPost);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Request header
        con.setRequestMethod("POST");
        con.setRequestProperty( "Content-type", "application/json");
        con.setRequestProperty( "Accept", "*/*" );
        con.setDoOutput(true);

        // Send post request
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        Log.d("POST : url : ",  _serverURL + urlPost);
        Log.d("POST : jsonObject : ", jsonObject.toString());
        Log.d("POST : Response Code : ", responseCode + "");

        if (responseCode != 200)
        {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.d("POST : Response : ", response.toString());

        return response.toString();
    }


    public void sendPost() throws Exception {

        URL obj = new URL(_serverURL+_urlLogin);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty( "Content-type", "application/json");
        con.setRequestProperty( "Accept", "*/*" );

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("login", "test");
        jsonObj.put("password","test");

        // Send post request
        con.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jsonObj.toString());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        Log.d("Sending request to: ", "" + _serverURL + _urlLogin);
        Log.d("Response Code : ", "" + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }
}