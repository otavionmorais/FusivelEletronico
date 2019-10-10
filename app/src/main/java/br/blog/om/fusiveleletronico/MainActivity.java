package br.blog.om.fusiveleletronico;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    static ListView listView;
    ProgressDialog load;
    TextView textoAlerta;
    String jsonDados;
    private static AppCompatActivity staticContext;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        textoAlerta = (TextView)findViewById(R.id.textoAlerta);
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //textoAlerta.setTextColor(Color.RED );
        //textoAlerta.setText("Alertas detectados nas últimas 24 horas!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetJson getJson = new GetJson(MainActivity.this);
    }

    private void mostrarGrafico(){
        Intent intent = new Intent(getApplicationContext(), GraficoActivity.class);
        startActivity(intent);
    }

    public static void encerrarComErro(Context contexto){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(contexto);
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

    public static void loadIntoListView(String json, Context contexto)  {
        try {
            if (json != null) { //se foi recebido algum dado

                JSONArray jsonArray = new JSONArray(json); //cria um array de dados com o json recebido
                String[] stocks = new String[jsonArray.length()]; //cria um array de strings para alimentar a lista

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i); //recupera o objeto na posicao i do array
                    stocks[i] = obj.getString("dia") + "  |  " + obj.getString("local"); //alimenta o array de strings
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(contexto
                        , android.R.layout.simple_list_item_1, stocks); //cria um adaptador para alimentar a lista
                listView.setAdapter(arrayAdapter); //alimenta a lista com as informacoes do bando de dados

            } else { //se houve problema ao recuperar dados
                encerrarComErro(contexto);
            }
        }catch (Exception e){
            encerrarComErro(contexto);
        }
    }

    public static AppCompatActivity getStaticContext() {
        return staticContext;
    }
}
