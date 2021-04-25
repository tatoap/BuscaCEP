package br.com.renatoramos.buscadecep;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {

    private TextView tv_logradouro, tv_complemento, tv_bairro, tv_cidade, tv_uf;

    private EditText et_cep;

    private Button btn_buscarcep;

    private String json;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVariaveis();
        inicializarAcoes();
    }

    private void inicializarVariaveis() {
        context = MainActivity.this;

        tv_logradouro = findViewById(R.id.tv_logradouro);
        tv_complemento = findViewById(R.id.tv_complemento);
        tv_bairro = findViewById(R.id.tv_bairro);
        tv_cidade = findViewById(R.id.tv_cidade);
        tv_uf = findViewById(R.id.tv_estado);

        et_cep = findViewById(R.id.et_cep);

        et_cep.addTextChangedListener(MaskEditText.mask(et_cep, MaskEditText.FORMAT_CEP));

        btn_buscarcep = findViewById(R.id.btn_buscarcep);
    }

    private class DownloadJsonAsyncTask extends AsyncTask<String, Void, String>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Aguarde", "Buscando endereço.");
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];

            json = WebService.comunicacao("https://viacep.com.br/ws/" + et_cep.getText().toString() + "/json/", urlString);

            Log.d("JSON", json);


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            try{
                hideKeyboard(context, et_cep);
                JSONObject recebimento = new JSONObject(json);
                if (!json.isEmpty()){
                    tv_logradouro.setText(recebimento.getString("logradouro"));
                    Log.d("LOG", recebimento.getString("logradouro"));
                    tv_complemento.setText(recebimento.getString("complemento"));
                    tv_bairro.setText(recebimento.getString("bairro"));
                    tv_cidade.setText(recebimento.getString("localidade"));
                    tv_uf.setText(recebimento.getString("uf"));
                } else {
                    Toast.makeText(MainActivity.this, "Endereço não encontrado, verifique o CEP digitado.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException j){
                j.printStackTrace();
                limparCampos();
                Toast.makeText(MainActivity.this, "Endereço não encontrado, verifique o CEP digitado.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void inicializarAcoes() {
        btn_buscarcep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarDados()) {
                    new DownloadJsonAsyncTask().execute("https://viacep.com.br/ws/" + et_cep.getText().toString() + "/json/");
                } else {
                    Toast.makeText(MainActivity.this, "CEP inválido.", Toast.LENGTH_SHORT).show();
                    et_cep.requestFocus();
                }
            }
        });
    }

    public static void hideKeyboard(Context context, View editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private boolean verificarDados(){
        String cep = et_cep.getText().toString();
        Log.d("CEP 2", cep);
        if (cep.isEmpty()){
            limparCampos();
            return false;
        }
        return true;
    }

    private void limparCampos(){
        tv_logradouro.setText("");
        tv_complemento.setText("");
        tv_bairro.setText("");
        tv_cidade.setText("");
        tv_uf.setText("");
    }
}
