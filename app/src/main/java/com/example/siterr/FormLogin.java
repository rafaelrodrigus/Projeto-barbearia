package com.example.siterr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
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

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AutenticarUsuario(email, senha);
                }
            }
        });
    }

    private void AutenticarUsuario(String email, String senha) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(this::agenda, 3000);
                    } else {
                        exibirSnackbar("Erro ao efetuar login", Color.RED);
                    }
                });
    }

    private void exibirSnackbar(String mensagem, int corFundo) {
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(corFundo);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
    }


    private void agenda() {
        Intent intent = new Intent(FormLogin.this, Agenda.class);
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
}
