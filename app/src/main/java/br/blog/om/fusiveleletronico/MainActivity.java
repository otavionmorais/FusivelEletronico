package br.blog.om.fusiveleletronico;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    static ListView listView;
    ProgressDialog load;
    static TextView textoAlerta;
    private static AppCompatActivity staticContext;
    static ActionBar barraSuperior;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarMensagem("Alerta", parent.getItemAtPosition(position).toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        staticContext=MainActivity.this;
        barraSuperior = getSupportActionBar();
        GetJson getJson = new GetJson(MainActivity.this);
    }

    private void mostrarGrafico(){
        Intent intent = new Intent(getApplicationContext(), GraficoActivity.class);
        startActivity(intent);
    }

    public void mostrarMensagem(String titulo, String msg){

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(titulo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.create().show();//mostra dialogo

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
                    stocks[i] = obj.getString("dia") + " | " + obj.getString("local") + " | \nValor: " + obj.getString("valor"); //alimenta o array de strings
                    //stocks[i]=stocks[i].replaceAll("\\\\n", "\n");
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(contexto
                        , R.layout.listview, stocks); //cria um adaptador para alimentar a lista
                listView.setAdapter(arrayAdapter); //alimenta a lista com as informacoes do bando de dados

                String dataUltimoAlerta=jsonArray.getJSONObject(0).getString("dia").split(" | ")[0];
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formatter.parse(dataUltimoAlerta));

                int anoAlerta = calendar.get(Calendar.YEAR);
                int mesAlerta = calendar.get(Calendar.MONTH);
                int diaAlerta = calendar.get(Calendar.DAY_OF_MONTH);
                calendar = Calendar.getInstance();
                int anoAtual = calendar.get(Calendar.YEAR);
                int mesAtual = calendar.get(Calendar.MONTH);
                int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);


                if( anoAlerta==anoAtual && mesAlerta==mesAtual && (diaAtual-diaAlerta)<=1 ){
                    textoAlerta.setTextColor(Color.RED);
                    textoAlerta.setText("Alerta registrado recentemente!");
                    barraSuperior.setBackgroundDrawable(new ColorDrawable(Color.RED));
                } else {
                    textoAlerta.setText("Sem registro de alertas nas últimas 24 horas.");
                    textoAlerta.setTextColor(Color.parseColor("#949494"));
                    barraSuperior.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008577")));
                }

            } else { //se houve problema ao recuperar dados
                encerrarComErro(contexto);
            }
        }catch (Exception e){
            encerrarComErro(contexto);
            Log.i("arky", e.getMessage());
        }
    }

    public static AppCompatActivity getStaticContext() {
        return staticContext;
    }
}
