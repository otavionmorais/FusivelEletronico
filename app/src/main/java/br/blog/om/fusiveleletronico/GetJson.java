package br.blog.om.fusiveleletronico;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJson {

    private ProgressDialog load;
    private String jsonDados;

    public GetJson(Context contexto){
        downloadJSON("https://om.blog.br/api/fusivel/getalertas", contexto);
    }

    //funcao de download de json
    //codigo de https://www.skysilk.com/blog/2018/how-to-connect-an-android-app-to-a-mysql-database/
    //adaptado para o proposito do fusivel eletronico
    private void downloadJSON(final String urlWebService, Context contexto) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                load = ProgressDialog.show(contexto,
                        "Por favor aguarde", "Obtendo dados..."); //mostra mensagem de carregamento dos dados
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                jsonDados=s;
                MainActivity.loadIntoListView(s, contexto); //enche a lista com os dados
                load.dismiss();//retira a mensagem de carregamento
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection(); //cria a conexao com a api
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream())); //recupera dados da api
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n"); //cria uma string do json recebido
                    }
                    return sb.toString().trim(); //retorna o json dos registros
                } catch (Exception e) {
                    return null; //retorna null caso haja erro na leitura dos dados
                }
            }
        }
        new DownloadJSON().execute(); //executa a task de download
    }

    public String getJsonDados(){
        return jsonDados;
    }


}
