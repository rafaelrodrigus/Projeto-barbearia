package com.example.siterr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private Button bt_entrar;
    private EditText edit_email, edit_senha;
    private ProgressBar progressBar;

    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(v -> abrirTelaCadastro());

        bt_entrar.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                exibirSnackbar(v, mensagens[0], Color.WHITE, Color.BLACK);
            } else {
                AutenticarUsuario(email, senha);
                esconderTeclado();
            }
        });
    }

    private void AutenticarUsuario(String email, String senha) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(this::agenda, 1500);
                    } else {
                        exibirSnackbar(findViewById(android.R.id.content), "Erro ao efetuar login", Color.RED);
                    }
                });
    }

    private void exibirSnackbar(View view, String mensagem, int corFundo, int corTexto) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(corFundo);
        snackbar.setTextColor(corTexto);
        snackbar.show();
    }

    private void agenda() {
        Intent intent = new Intent(FormLogin.this, TelaMenuServico.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        bt_entrar = findViewById(R.id.bt_entrar);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressbar);
    }

    private void esconderTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void abrirTelaCadastro() {
        Intent intent = new Intent(FormLogin.this, FormCadastro.class);
        startActivity(intent);
    }
    private void exibirSnackbar(View view, String mensagem, int corFundo) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(corFundo);
        snackbar.setTextColor(Color.WHITE);  // Adicionei a cor branca como padrão para o texto
        snackbar.show();
    }

}
