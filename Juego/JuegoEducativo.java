import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.*;
import java.util.List;
import java.util.ArrayList;

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase base para las preguntas
abstract class PreguntaBase {
    protected int respuestaCorrecta;

    public abstract String obtenerPregunta();
    public int obtenerRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}

// Clase Pregunta que hereda de PreguntaBase
class Pregunta extends PreguntaBase {
    private int num1;
    private int num2;
    private String operacion;

    public Pregunta(int num1, int num2, String operacion) {
        this.num1 = num1;
        this.num2 = num2;
        this.operacion = operacion;
        calcularRespuesta();
    }

    @Override
    public String obtenerPregunta() {
        return num1 + " " + operacion + " " + num2;
    }

    private void calcularRespuesta() {
        switch (operacion) {
            case "+":
                respuestaCorrecta = num1 + num2;
                break;
            case "-":
                respuestaCorrecta = num1 - num2;
                break;
            case "*":
                respuestaCorrecta = num1 * num2;
                break;
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase Jugador
class Jugador {
    private String nombre;
    private int puntuacion;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntuacion = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void aumentarPuntuacion() {
        puntuacion++;
    }

    public int obtenerPuntuacion() {
        return puntuacion;
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase Ranking
class Ranking {
    private static final String RANKING_FILE = "ranking.txt";

    private static void verificarArchivo() {
        File archivo = new File(RANKING_FILE);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void guardarPuntaje(Jugador jugador) {
        verificarArchivo();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE, true))) {
            writer.write(jugador.getNombre().trim() + "," + jugador.obtenerPuntuacion());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> cargarRanking() {
        List<String[]> ranking = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RANKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[1].matches("\\d+")) {
                    ranking.add(new String[]{parts[0].trim(), parts[1].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    public static List<String[]> ordenarRanking(List<String[]> ranking) {
        ranking.sort((entry1, entry2) -> Integer.compare(Integer.parseInt(entry2[1]), Integer.parseInt(entry1[1])));
        return ranking;
    }

    public static void mostrarRanking(JFrame ventana) {
        List<String[]> ranking = cargarRanking();
        ordenarRanking(ranking);

        StringBuilder rankingText = new StringBuilder();
        rankingText.append("****** Ranking ******\n\n");

        int lugar = 1;
        for (String[] entry : ranking) {
            if (entry.length == 2) {
                String medalla = "";
                if (lugar == 1) medalla = " 🥇";
                else if (lugar == 2) medalla = " 🥈";
                else if (lugar == 3) medalla = " 🥉";

                rankingText.append(String.format("%d. %-5s : %s%s\n", lugar, entry[0], entry[1], medalla));
                lugar++;
            }
        }

        if (ranking.isEmpty()) {
            rankingText.append("\nNo hay datos en el ranking aún.");
        }

        JTextArea textArea = new JTextArea(rankingText.toString());
        textArea.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 14));
        textArea.setEditable(false);

        JOptionPane.showMessageDialog(ventana, new JScrollPane(textArea), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase base para el juego
abstract class JuegoBase {
    protected Jugador jugador;
    protected JFrame ventana;

    public JuegoBase(String nombreJugador) {
        this.jugador = new Jugador(nombreJugador);
    }

    public abstract void inicializarVentana();
    public abstract void comenzar();
}

// Clase Juego que hereda de JuegoBase
class Juego extends JuegoBase {
    private Random random;
    private JLabel preguntaLabel;
    private JTextField respuestaField;
    private JButton siguienteButton;
    private JLabel puntuacionLabel;
    private JLabel tiempoLabel;
    private int respuestaCorrecta;
    private Timer timer;
    private int tiempoRestante;

    public Juego(String nombreJugador) {
        super(nombreJugador);
        random = new Random();
        inicializarVentana();
    }

    @Override
    public void inicializarVentana() {
        ventana = new JFrame("Juego Educativo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 400);
        ventana.setLocationRelativeTo(null);

        FondoPanel panelFondo = new FondoPanel("fondo.jpg");
        ventana.setContentPane(panelFondo);
        panelFondo.setLayout(null);

        preguntaLabel = new JLabel("Pregunta:", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 15));
        preguntaLabel.setBounds(100, 50, 600, 30);
        panelFondo.add(preguntaLabel);

        respuestaField = new JTextField();
        respuestaField.setFont(new Font("Arial", Font.PLAIN, 15));
        respuestaField.setBounds(150, 100, 500, 40);
        panelFondo.add(respuestaField);

        siguienteButton = new JButton("Responder");
        siguienteButton.setBounds(300, 150, 200, 40);
        siguienteButton.addActionListener(e -> verificarRespuesta());
        panelFondo.add(siguienteButton);

        puntuacionLabel = new JLabel("Puntuación: 0", SwingConstants.CENTER);
        puntuacionLabel.setBounds(300, 200, 200, 30);
        panelFondo.add(puntuacionLabel);

        tiempoLabel = new JLabel("Tiempo restante: 30", SwingConstants.CENTER);
        tiempoLabel.setBounds(300, 240, 200, 30);
        panelFondo.add(tiempoLabel);

        inicializarTemporizador();
    }

    @Override
    public void comenzar() {
        ventana.setVisible(true);
        generarPregunta();
    }

    private void generarPregunta() {
        int num1 = random.nextInt(100) + 1;
        int num2 = random.nextInt(10) + 1;
        String[] operaciones = {"+", "-", "*"};
        String operacion = operaciones[random.nextInt(operaciones.length)];
        Pregunta pregunta = new Pregunta(num1, num2, operacion);
        preguntaLabel.setText("Pregunta: " + pregunta.obtenerPregunta());
        respuestaCorrecta = pregunta.obtenerRespuestaCorrecta();
    }

    private void verificarRespuesta() {
        try {
            int respuestaUsuario = Integer.parseInt(respuestaField.getText());
            if (respuestaUsuario == respuestaCorrecta) {
                jugador.aumentarPuntuacion();
                JOptionPane.showMessageDialog(ventana, "¡Correcto!");
            } else {
                JOptionPane.showMessageDialog(ventana, "Incorrecto. La respuesta era " + respuestaCorrecta);
            }
            puntuacionLabel.setText("Puntuación: " + jugador.obtenerPuntuacion());
            generarPregunta();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ventana, "Por favor ingresa un número válido.");
        }
    }

    private void inicializarTemporizador() {
        tiempoRestante = 30;
        timer = new Timer(1000, e -> {
            tiempoRestante--;
            tiempoLabel.setText("Tiempo restante: " + tiempoRestante);
            if (tiempoRestante <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(ventana, "¡Se acabó el tiempo! Puntuación: " + jugador.obtenerPuntuacion());
                Ranking.guardarPuntaje(jugador);
                Ranking.mostrarRanking(ventana);
                mostrarMenuInicio();
            }
        });
        timer.start();
    }

    private void mostrarMenuInicio() {
        ventana.dispose();
        JuegoEducativo.iniciarMenu();
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase FondoPanel
class FondoPanel extends JPanel {
    private Image imagen;

    public FondoPanel(String rutaImagen) {
        try {
            imagen = new ImageIcon(rutaImagen).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase principal
public class JuegoEducativo {
    public static void main(String[] args) {
        iniciarMenu();
    }

    public static void iniciarMenu() {
        JFrame ventana = new JFrame("Menú Principal");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 400);
        ventana.setLocationRelativeTo(null);

        FondoPanel panelFondo = new FondoPanel("menu.jpg");
        ventana.setContentPane(panelFondo);
        panelFondo.setLayout(null);

        JButton jugarButton = new JButton("Jugar");
        jugarButton.setBounds(300, 100, 200, 50);
        jugarButton.addActionListener(e -> {
            ventana.dispose();
            String nombreJugador = JOptionPane.showInputDialog("Ingresa tu nombre:");
            if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
                Juego juego = new Juego(nombreJugador.trim());
                juego.comenzar();
            }
        });
        panelFondo.add(jugarButton);

        JButton rankingButton = new JButton("Ranking");
        rankingButton.setBounds(300, 180, 200, 50);
        rankingButton.addActionListener(e -> Ranking.mostrarRanking(ventana));
        panelFondo.add(rankingButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(300, 260, 200, 50);
        salirButton.addActionListener(e -> System.exit(0));
        panelFondo.add(salirButton);

        ventana.setVisible(true);
    }
}
