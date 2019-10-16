package br.blog.om.fusiveleletronico;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GraficoActivity extends AppCompatActivity {

    WebView navegador;
    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        setTitle("Gráfico de alertas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navegador = findViewById(R.id.navegador);

        load = ProgressDialog.show(GraficoActivity.this,
                "Por favor aguarde", "Gerando gráfico..."); //mostra mensagem de carregamento dos dados

        navegador.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                load.dismiss();
                invalidateOptionsMenu();
            }

        });

        navegador.loadUrl("https://om.blog.br/api/graficofusivel");

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void navegar(){

        class Navegar extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                load = ProgressDialog.show(GraficoActivity.this,
                        "Por favor aguarde", "Gerando gráfico..."); //mostra mensagem de carregamento dos dados
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }

            @Override
            protected String doInBackground(Void... voids) {

               return null;
            }
        }
        new Navegar().execute();
    }

}
