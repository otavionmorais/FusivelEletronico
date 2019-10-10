package com.example.primeiraaplicacao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.primeiraaplicacao.service.AlarmeVerificacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressDialog load;
    TextView textoAlerta;
    String jsonDados;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.botaografico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.botaografico:
                mostrarGrafico();
                break;
        }
        return true;
    }

    public void scheduleAlarm() {



        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmeVerificacao.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmeVerificacao.REQUEST_CODE,
                intent, PendingIntent.FLAG_IMMUTABLE);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                60000, pIntent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        textoAlerta = (TextView)findViewById(R.id.textoAlerta);

        downloadJSON("https://om.blog.br/api/getfusivel");
        //scheduleAlarm();

        //textoAlerta.setTextColor(Color.RED );
        //textoAlerta.setText("Alertas detectados nas últimas 24 horas!");

    }

    private void mostrarGrafico(){
        Intent intent = new Intent(getApplicationContext(), GraficoActivity.class);
        startActivity(intent);
    }

    //funcao de download do json dos registros
    //codigo de https://www.skysilk.com/blog/2018/how-to-connect-an-android-app-to-a-mysql-database/
    //adaptado para o proposito do fusivel eletronico
    public void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                load = ProgressDialog.show(MainActivity.this,
                        "Por favor aguarde", "Obtendo dados..."); //mostra mensagem de carregamento dos dados
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s); //adiciona os registros na lista
                    jsonDados=s;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                load.dismiss();  //retira a mensagem de carregamento
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

    private void loadIntoListView(String json) throws JSONException {

        if(json!=null) { //se foi recebido algum dado

            JSONArray jsonArray = new JSONArray(json); //cria um array de dados com o json recebido
            String[] stocks = new String[jsonArray.length()]; //cria um array de strings para alimentar a lista

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i); //recupera o objeto na posicao i do array
                stocks[i] = obj.getString("dia") + "  |  " + obj.getString("local"); //alimenta o array de strings
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, stocks); //cria um adaptador para alimentar a lista
            listView.setAdapter(arrayAdapter); //alimenta a lista com as informacoes do bando de dados

        } else { //se houve problema ao recuperar dados

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Falha ao tentar obter dados. Cheque a conexão com a internet e tente novamente.");
            dlgAlert.setTitle("Fusível Eletrônico");
            dlgAlert.setPositiveButton("OK",
              new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1); //fecha o app com codigo de erro 1
                    }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();//mostra dialogo de falha

        }

    }


}
