package com.example.siterr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaMenuServico extends AppCompatActivity {

    private Button appCompatButton, butaovoltar;
    private Button appCompatButtonPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_servico);

        butaovoltar = findViewById(R.id.bt_voltar);
        appCompatButton = findViewById(R.id.appCompatButton);
        appCompatButtonPerfil = findViewById(R.id.appCompatButtonPerfil);

        // Configurar o clique do botão
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chamar o método TelaAgendamento quando o botão for clicado
                TelaTeste();
            }
        });

        // Configurar o clique do botãoPerfil para ir para a TelaPerfil
        appCompatButtonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelaPrincipal();
            }
        });
    }

    // Método separado TelaAgendamento
    private void TelaTeste() {
        Intent intent = new Intent(TelaMenuServico.this, TelaAgendamento.class);
        startActivity(intent);
        finish();
    }

    private void TelaPrincipal() {
        Intent intent = new Intent(TelaMenuServico.this, TelaPerfil.class);
        startActivity(intent);
//        finish();
    }
    public void botaoVoltar(View v) {
        Intent i = new Intent(getApplicationContext(), FormLogin.class);
        startActivity(i);
    }


}
