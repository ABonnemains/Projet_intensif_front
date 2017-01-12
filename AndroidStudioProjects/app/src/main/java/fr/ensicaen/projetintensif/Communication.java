package fr.ensicaen.projetintensif;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.Timestamp;

import javax.net.ssl.HttpsURLConnection;

public class Communication {

    public enum RequestType{
        LOGIN,
        REGISTER,
        SEARCH_USER,
        GET_EVENT,
        GET_ALL_EVENTS
    }

    private final String _serverURL = "https://roule-ma-poule.herokuapp.com/";
    //private final String _serverURL = "https://secure-lake-76948.herokuapp.com/";
    private final String _urlLogin = "authentication/login";
    private final String _urlRegister = "authentication/register";
    private final  String _urlGetProfile = "authentication/profile/";
    private final String _urlCreateEvent = "event/create";
    private final String _urlGetEvents = "event/list/";

    private RequestType _currentRequestType;
    private static String _token;
    private JSONObject _getRes;
    private JSONArray _JSONEvents;

    private boolean registerSucceded = false;

    private String[] infoLogin;

    private Object[] infoRegister;

    private Object[] infoGetEvent;

    private double[] infoEvents;

    // Constructeur pour register
    public Communication(String login, String pw, String pwConfirm, String name, String surname, String phoneNumber, Timestamp birthDate){
        infoRegister = new Object[]{login, pw, pwConfirm, name, surname,phoneNumber,birthDate};
        _currentRequestType = RequestType.REGISTER;
    }

    private String infoSearchUser;

    // Constructeur pour login
    public Communication(String login, String pw)
    {
        infoLogin = new String[]{login, pw};
        _currentRequestType = RequestType.LOGIN;
    }

    //Constructeur pour create event
    public Communication(String name, String longitude, String latitude, Timestamp timeStamp, String description){
        infoGetEvent = new Object[]{name,longitude,latitude,timeStamp,description};
        _currentRequestType = RequestType.GET_EVENT;
    }

    public Communication(double latitude, double longitude) {
        infoEvents = new double[]{latitude ,longitude};
        _currentRequestType = RequestType.GET_ALL_EVENTS;
    }

    public boolean getRegisterSucceded() {
        return registerSucceded;
    }


    public Communication(String userSearch)
    {
        infoSearchUser = userSearch;
        _currentRequestType = RequestType.SEARCH_USER;
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
                case SEARCH_USER:
                    communicate(infoSearchUser);
                    break;
                case GET_EVENT:
                    communicate((String)infoGetEvent[0], (String)infoGetEvent[1], (String)infoGetEvent[2], (Timestamp)infoGetEvent[3], (String)infoGetEvent[4]);
                    break;
                case GET_ALL_EVENTS:
                    communicate(infoEvents[0], infoEvents[1]);
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

            _token = sendPost(jsonObj, _urlLogin).split("\"")[3];

            Log.d("Login : ", _token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void communicate(String profileName){
        try {
            _getRes = sendGet(_urlGetProfile+_token+"/"+profileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void communicate(double latitude, double longitude) {
        try {
            _JSONEvents = sendGetArray( _urlGetEvents+_token+"/"+latitude+"/"+longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Login : ", _token);

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

            String res = sendPost(jsonObj, _urlRegister).toString();

            Log.d("res",res);

            if (res.equals("OK")){
                registerSucceded = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void communicate(String name, String longitude, String latitude, Timestamp timeStamp, String description){
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("token", _token);
            jsonObj.put("event_name",name);
            jsonObj.put("event_longitude",longitude);
            jsonObj.put("event_latitude",latitude);
            jsonObj.put("event_timestamp",timeStamp);
            jsonObj.put("event_description",description);


            String res = sendPost(jsonObj, _urlCreateEvent);

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

    public JSONObject getGetResult(){
        return _getRes;
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

        Log.d("POST : Response ", response.toString());

        return response.toString();
    }

    private JSONObject sendGet(String urlGet) throws Exception {
        URL obj = new URL(_serverURL+urlGet);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty( "Accept", "*/*" );

        int responseCode = con.getResponseCode();


        Log.d("GET : url ",  _serverURL + urlGet);
        Log.d("GET : Response Code ", responseCode + "");


        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.d("GET : Response ", response.toString());

        JSONObject res = new JSONObject(response.toString());

        return res;
    }

    private JSONArray sendGetArray(String urlGet) throws Exception {
        URL obj = new URL(_serverURL+urlGet);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty( "Accept", "*/*" );

        int responseCode = con.getResponseCode();


        Log.d("GET : url ",  _serverURL + urlGet);
        Log.d("GET : Response Code ", responseCode + "");


        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.d("GET : Response ", response.toString());

        JSONArray res = new JSONArray(response.toString());

        return res;
    }


}