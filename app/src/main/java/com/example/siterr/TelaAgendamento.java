package com.example.siterr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaAgendamento extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox checkBoxBarbeiro1, checkBoxBarbeiro2, checkBoxBarbeiro3;
    private CheckBox checkBoxCorte, checkBoxBarba, checkBoxHidratacao, checkBoxLavagemCabelo;
    private Button buttonAgendar;

    String usuarioID, edit_nome;

    // Lista para armazenar os horários já agendados
    private List<String> horariosAgendados = new ArrayList<>();

    // Novos valores dos serviços
    private static final double VALOR_CORTE = 20.0;
    private static final double VALOR_BARBA = 5.0;
    private static final double VALOR_HIDRATACAO = 25.0;
    private static final double VALOR_LAVAGEM_CABELO = 10.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_agendamento);


        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        checkBoxBarbeiro1 = findViewById(R.id.bambeiro1);
        checkBoxBarbeiro2 = findViewById(R.id.bambeiro2);
        checkBoxBarbeiro3 = findViewById(R.id.bambeiro3);
        checkBoxCorte = findViewById(R.id.Corte);
        checkBoxBarba = findViewById(R.id.Barba);
        checkBoxHidratacao = findViewById(R.id.Hidratação);
        checkBoxLavagemCabelo = findViewById(R.id.lavagemCabelo);
        buttonAgendar = findViewById(R.id.btAgendar);


        buttonAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();


                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, day, hour, minute);

                Calendar currentDateTime = Calendar.getInstance();


                if (selectedDateTime.before(currentDateTime)) {

                    Toast.makeText(TelaAgendamento.this, "Selecione uma data e hora futuras", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (hour < 8 || hour >= 18) {

                    Toast.makeText(TelaAgendamento.this, "Os agendamentos só são permitidos entre 8h e 18h", Toast.LENGTH_SHORT).show();
                    return;
                }


                String nomeDoBarbeiro = getNomeBarbeiroSelecionado();


                if (!validarSelecaoBarbeiro()) {

                    Toast.makeText(TelaAgendamento.this, "Selecione exatamente um barbeiro", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!checkBoxCorte.isChecked() && !checkBoxBarba.isChecked() &&
                        !checkBoxHidratacao.isChecked() && !checkBoxLavagemCabelo.isChecked()) {

                    Toast.makeText(TelaAgendamento.this, "Por favor, selecione pelo menos um serviço", Toast.LENGTH_SHORT).show();
                    return;
                }


                String horarioSelecionado = day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute;


                if (horariosAgendados.contains(horarioSelecionado)) {

                    Toast.makeText(TelaAgendamento.this, "Este horário já foi agendado", Toast.LENGTH_SHORT).show();
                    return;
                }


                horariosAgendados.add(horarioSelecionado);


                double valorTotal = calcularValorTotal();


                salvarDadosNoFirestore(nomeDoBarbeiro, horarioSelecionado, valorTotal);
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    usuarioID = currentUser.getUid();


                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("Nome do Usuário")
                            .build();

                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {


                                    Map<String, Object> dadosAgendamento = new HashMap<>();
                                    dadosAgendamento.put("IdUsuario", usuarioID);
                                    dadosAgendamento.put("NomeDoBarbeiro", nomeDoBarbeiro);
                                    dadosAgendamento.put("Horario", horarioSelecionado);
                                    dadosAgendamento.put("ValorTotal", valorTotal);


                                    dadosAgendamento.put("Corte", checkBoxCorte.isChecked());
                                    dadosAgendamento.put("Barba", checkBoxBarba.isChecked());
                                    dadosAgendamento.put("Hidratacao", checkBoxHidratacao.isChecked());
                                    dadosAgendamento.put("LavagemdeCabelo", checkBoxLavagemCabelo.isChecked());


                                    DocumentReference documentReference = db.collection("Agendamentos").document();


                                    documentReference.set(dadosAgendamento)
                                            .addOnSuccessListener(Void -> {

                                                Toast.makeText(TelaAgendamento.this, "Agendamento salvo com sucesso! Valor total: R$" + valorTotal, Toast.LENGTH_SHORT).show();


                                                Intent intent = new Intent(TelaAgendamento.this, TelaMenuServico.class);
                                                startActivity(intent);


                                                finish();
                                            })
                                            .addOnFailureListener(e -> {

                                                Toast.makeText(TelaAgendamento.this, "Erro ao salvar agendamento", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            });
                } else {

                    Toast.makeText(TelaAgendamento.this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private String getNomeBarbeiroSelecionado() {
        if (checkBoxBarbeiro1.isChecked()) {
            return "Negueba";
        } else if (checkBoxBarbeiro2.isChecked()) {
            return "Felipe Souza";
        } else if (checkBoxBarbeiro3.isChecked()) {
            return "Monza";
        }
        return "";
    }


    private boolean validarSelecaoBarbeiro() {
        int count = 0;

        if (checkBoxBarbeiro1.isChecked()) {
            count++;
        }
        if (checkBoxBarbeiro2.isChecked()) {
            count++;
        }
        if (checkBoxBarbeiro3.isChecked()) {
            count++;
        }

        return count == 1;
    }


    private double calcularValorTotal() {
        double valorTotal = 0.0;

        if (checkBoxCorte.isChecked()) {
            valorTotal += VALOR_CORTE;
        }
        if (checkBoxBarba.isChecked()) {
            valorTotal += VALOR_BARBA;
        }
        if (checkBoxHidratacao.isChecked()) {
            valorTotal += VALOR_HIDRATACAO;
        }
        if (checkBoxLavagemCabelo.isChecked()) {
            valorTotal += VALOR_LAVAGEM_CABELO;
        }

        return valorTotal;
    }


    private void salvarDadosNoFirestore(String nomeDoBarbeiro, String horarioSelecionado, double valorTotal) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            usuarioID = currentUser.getUid();


            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Nome do Usuário")
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {



                            Map<String, Object> dadosAgendamento = new HashMap<>();
                            dadosAgendamento.put("IdUsuario", usuarioID);
                            dadosAgendamento.put("NomeDoBarbeiro", nomeDoBarbeiro);
                            dadosAgendamento.put("Horario", horarioSelecionado);
                            dadosAgendamento.put("ValorTotal", valorTotal);


                            dadosAgendamento.put("Corte", checkBoxCorte.isChecked());
                            dadosAgendamento.put("Barba", checkBoxBarba.isChecked());
                            dadosAgendamento.put("Hidratacao", checkBoxHidratacao.isChecked());
                            dadosAgendamento.put("LavagemdeCabelo", checkBoxLavagemCabelo.isChecked());


                            DocumentReference documentReference = db.collection("Agendamentos").document();


                            documentReference.set(dadosAgendamento)
                                    .addOnSuccessListener(Void -> {

                                        Toast.makeText(TelaAgendamento.this, "Agendamento salvo com sucesso! Valor total: R$" + valorTotal, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {

                                        Toast.makeText(TelaAgendamento.this, "Erro ao salvar agendamento", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
        } else {

            Toast.makeText(TelaAgendamento.this, "Erro: Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            }



    // Autores Rafael, Felipe, Moises, Wenes, João Marcos
