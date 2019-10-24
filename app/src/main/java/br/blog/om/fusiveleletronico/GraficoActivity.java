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
    }

    @Override
    protected void onResume() {
        super.onResume();

        load = ProgressDialog.show(GraficoActivity.this,
                       "Por favor aguarde", "Gerando gráfico..."); //mostra mensagem de carregamento dos dados

        navegador.loadUrl("https://om.blog.br/api/fusivel/grafico");

        navegador.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                load.dismiss();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
