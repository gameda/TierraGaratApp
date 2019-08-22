package mx.digitalcoaster.tierra_garat_puntos.sync;

import android.os.AsyncTask;

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

public class LoadDataLoginFacebook  extends AsyncTask<String, Void, String> {

    private final AsyncTaskInterface asyncTskInterface;

    public LoadDataLoginFacebook (AsyncTaskInterface async) {
        this.asyncTskInterface =  async;
    }


    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();
        String sName = strings[0];
        String sId = strings[1];
        String sEmail = strings[2];
      /*  String sBirth = strings[3];
        String sGender = strings[4];
        String sLocation = strings[5];*/
        String sText = "";
        BufferedReader bfReader=null;

        try {
            URL url = new URL("https://tierragarat.digitalcoaster.mx/facebooklogin");     // URL del server
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();                // Se abre la conexion con el server
            conn.setRequestMethod("POST");                                                    // Metodo para el intercambio de información
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();                                                                   // Se establece la conexión


            //Make Json
            jsonObject.put ("name", sName);
            jsonObject.put ("user_id", sId);
            jsonObject.put("email", sEmail);
            /*jsonObject.put ("birthday", sBirth);
            jsonObject.put ("gender", sGender);
            jsonObject.put ("city", sLocation);*/


            //Send Data(Json)
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

    @Override
    protected void onPostExecute(String sRespuesta){

        try{
            asyncTskInterface.processFacebook (sRespuesta);  //Manda el server response, al Interface
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onPostExecute(sRespuesta);

    }

}


