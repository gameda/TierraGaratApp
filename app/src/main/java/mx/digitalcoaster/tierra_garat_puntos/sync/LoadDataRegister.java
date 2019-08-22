package mx.digitalcoaster.tierra_garat_puntos.sync;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import mx.digitalcoaster.tierra_garat_puntos.interfaces.AsyncTaskInterface;
import mx.digitalcoaster.tierra_garat_puntos.models.User;

public class LoadDataRegister extends AsyncTask<User, Void, String> {

    private final AsyncTaskInterface asyncTskInterface;

    public LoadDataRegister (AsyncTaskInterface async) {
        this.asyncTskInterface =  async;
    }

    @Override
    protected String doInBackground(User... users) {
        JSONObject jsonObject = new JSONObject();
        User user = users[0];                       //Se recive el objeto
        String sText = "";
        BufferedReader bfReader=null;

        try {
            URL url = new URL("https://tierragarat.digitalcoaster.mx/appregister");     // URL del server
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();                // Se abre la conexion con el server
            conn.setRequestMethod("POST");                                                    // Metodo para el intercambio de información
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();                                                                   // Se establece la conexión


            //Send Data(Json)
            jsonObject = makeJson (user);
            Writer wr = new BufferedWriter (new OutputStreamWriter (conn.getOutputStream(), "UTF-8"));
            wr.write(jsonObject.toString());
            wr.flush();
            wr.close();

            //Respond Server
            bfReader = new BufferedReader(new InputStreamReader (conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;

            //Read respond
            while((line = bfReader.readLine()) != null)
                // Append server response in string
                sb.append(line + "\n");

            sText = sb.toString();


        }catch (Exception e){
            e.printStackTrace ();

        }finally {
            if(bfReader != null){
                try{
                    bfReader.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return sText;
    }


    //Funcion para armar el Json
    public JSONObject makeJson(User user) {

        JSONObject jsonObject = new JSONObject ();
        try {
            jsonObject.put("name", user.getName ());
            jsonObject.put("gender", user.getSex ());
            jsonObject.put("birthday", user.getBirth ());
            jsonObject.put("email", user.getEmail ());
            jsonObject.put("password", user.getPassword ());
            jsonObject.put("street", user.getAddress ());
            jsonObject.put("number", user.getNumE ());
            jsonObject.put("city", user.getCity ());
            jsonObject.put("zip", user.getCP ());

        }catch (JSONException e) {
            e.printStackTrace ();
        }
        return jsonObject;

    }

    @Override
    protected void onPostExecute(String sRespuesta){

        try{
            asyncTskInterface.processFinish(sRespuesta);  //Manda el server response, al Interface
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onPostExecute(sRespuesta);

    }

}
