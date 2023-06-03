package UI;

import Funciones.GenerarEstadisticas;

import javax.swing.*;

public class MainView extends JFrame {
    private JPanel panelMain;
    private JButton generarEstadisticasButton;
    private JButton listarButton;
    private JTextArea resultados;

    public MainView() {
        this.setContentPane(panelMain);
        this.setTitle("Base de Datos 2 - Trabajo 3");
        this.setSize(1000, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generarEstadisticasButton.addActionListener(e -> {
            GenerarEstadisticas generarEstadisticas = new GenerarEstadisticas();
            resultados.setText(generarEstadisticas.generarEstadisticas());
        });
        listarButton.addActionListener(e -> resultados.setText("Falta hacer"));
    }

    private void createUIComponents() {

    }
}
