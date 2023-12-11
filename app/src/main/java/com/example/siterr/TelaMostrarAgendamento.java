package com.example.siterr;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TelaMostrarAgendamento extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mostrar_agendamento);

        // Recuperar dados do agendamento da Intent (ou de onde você os armazenou)
        String detalhesAgendamento = "Detalhes do agendamento:\n\n" +
                "Data: XX/XX/XXXX\n" +
                "Hora: XX:XX\n" +
                "Barbeiro: Nome do Barbeiro\n" +
                "Serviços: Corte, Barba, Hidratação, Lavagem de Cabelo\n" +
                "Valor Total: R$ XX.XX";

        // Exibir detalhes do agendamento em um TextView
        TextView textViewDetalhesAgendamento = findViewById(R.id.textViewDetalhesAgendamento);
        textViewDetalhesAgendamento.setText(detalhesAgendamento);

        // Configurar o botão de cancelamento
        Button buttonCancelarAgendamento = findViewById(R.id.buttonCancelarAgendamento);
        buttonCancelarAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adicione lógica aqui para cancelar o agendamento
                // Por exemplo, você pode remover o agendamento do Firestore e da lista de horários agendados
                // Após o cancelamento, você pode exibir uma mensagem de sucesso e fechar a atividade
                Toast.makeText(TelaMostrarAgendamento.this, "Agendamento cancelado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
