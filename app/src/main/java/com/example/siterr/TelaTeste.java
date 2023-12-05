package com.example.siterr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import androidx.cardview.widget.CardView;

public class TelaTeste extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox checkBoxBarbeiro1, checkBoxBarbeiro2, checkBoxBarbeiro3;
    private Button buttonAgendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_teste);

        // Inicializando os elementos do layout
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        checkBoxBarbeiro1 = findViewById(R.id.bambeiro1);
        checkBoxBarbeiro2 = findViewById(R.id.bambeiro2);
        checkBoxBarbeiro3 = findViewById(R.id.bambeiro3);
        buttonAgendar = findViewById(R.id.btAgendar);

        // Adicionando um listener ao botão de agendar
        buttonAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter data e hora selecionadas
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Verificar se a data e hora selecionadas já passaram
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, day, hour, minute);

                Calendar currentDateTime = Calendar.getInstance();

                if (selectedDateTime.before(currentDateTime)) {
                    // Exibir mensagem de erro
                    Toast.makeText(TelaTeste.this, "Selecione uma data e hora futuras", Toast.LENGTH_SHORT).show();
                    return; // Impede a continuação do processo de agendamento
                }

                // Verificar quantos barbeiros foram selecionados
                int selectedCount = 0;

                if (checkBoxBarbeiro1.isChecked()) {
                    selectedCount++;
                }
                if (checkBoxBarbeiro2.isChecked()) {
                    selectedCount++;
                }
                if (checkBoxBarbeiro3.isChecked()) {
                    selectedCount++;
                }

                // Verificar se mais de um barbeiro foi selecionado
                if (selectedCount != 1) {
                    // Exibir mensagem de erro
                    Toast.makeText(TelaTeste.this, "Por favor, selecione exatamente um barbeiro", Toast.LENGTH_SHORT).show();
                    return; // Impede a continuação do processo de agendamento
                }

                // Determinar qual barbeiro foi selecionado
                String selectedBarbeiro = "";
                if (checkBoxBarbeiro1.isChecked()) {
                    selectedBarbeiro = "Negueba";
                } else if (checkBoxBarbeiro2.isChecked()) {
                    selectedBarbeiro = "Felipe Souza";
                } else if (checkBoxBarbeiro3.isChecked()) {
                    selectedBarbeiro = "Monza";
                }

                // Exibir uma mensagem com os detalhes do agendamento
                String message = "Agendamento:\nData: " + day + "/" + (month + 1) + "/" + year +
                        "\nHora: " + hour + ":" + minute +
                        "\nBarbeiro: " + selectedBarbeiro;

                Toast.makeText(TelaTeste.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}