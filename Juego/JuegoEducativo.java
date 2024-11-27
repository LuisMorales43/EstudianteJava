import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.*;
import java.util.List;
import java.util.ArrayList;
//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase Pregunta
class Pregunta {
    private int num1;
    private int num2;
    private String operacion;
    private int respuestaCorrecta;

    public Pregunta(int num1, int num2, String operacion) {
        this.num1 = num1;
        this.num2 = num2;
        this.operacion = operacion;
        calcularRespuesta();
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

    public String obtenerPregunta() {
        return num1 + " " + operacion + " " + num2;
    }

    public int obtenerRespuestaCorrecta() {
        return respuestaCorrecta;
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

    public static void guardarPuntaje(Jugador jugador) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE, true))) {
            String nombreLimpio = jugador.getNombre().trim();
            int puntuacion = jugador.obtenerPuntuacion();
            writer.write(nombreLimpio + "," + puntuacion);
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
                if (parts.length == 2 && parts[1].matches("\\d+")) { // Validar formato
                    ranking.add(new String[]{parts[0].trim(), parts[1].trim()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    public static List<String[]> ordenarRanking(List<String[]> ranking) {
        ranking.sort((entry1, entry2) -> {
            int puntaje1 = Integer.parseInt(entry1[1]);
            int puntaje2 = Integer.parseInt(entry2[1]);
            return Integer.compare(puntaje2, puntaje1); // De mayor a menor
        });
        return ranking;
    }

    public static void mostrarRanking(JFrame ventana) {
        List<String[]> ranking = cargarRanking();
        ordenarRanking(ranking);

        StringBuilder rankingText = new StringBuilder("Ranking:\n");
        for (String[] entry : ranking) {
            if (entry.length == 2) { // Validar entrada antes de mostrar
                rankingText.append(entry[0]).append(": ").append(entry[1]).append("\n");
            }
        }

        JOptionPane.showMessageDialog(ventana, rankingText.toString());
    }
}
//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase Juego
class Juego {
    private Jugador jugador;
    private Random random;
    private JFrame ventana;
    private JPanel panelPrincipal;
    private JLabel preguntaLabel;
    private JTextField respuestaField;
    private JButton siguienteButton;
    private JLabel puntuacionLabel;
    private JLabel tiempoLabel; // Etiqueta para el temporizador
    private int respuestaCorrecta;
    private Timer timer; // Temporizador
    private int tiempoRestante;

    public Juego(String nombreJugador) {
        jugador = new Jugador(nombreJugador);
        random = new Random();
        inicializarVentana();
    }

    private void inicializarVentana() {
        ventana = new JFrame("Juego Educativo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(null);

        FondoPanel panelFondo = new FondoPanel("fondo.jpg"); // Imagen de fondo
        ventana.setContentPane(panelFondo);
        panelFondo.setLayout(new GridLayout(6, 1));

        preguntaLabel = new JLabel("Pregunta:");
        preguntaLabel.setForeground(Color.BLACK);
        panelFondo.add(preguntaLabel);

        respuestaField = new JTextField();
        panelFondo.add(respuestaField);

        siguienteButton = new JButton("Responder");
        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuestaUsuario;
                try {
                    respuestaUsuario = Integer.parseInt(respuestaField.getText());
                    if (respuestaUsuario == respuestaCorrecta) {
                        jugador.aumentarPuntuacion();
                        reproducirSonido("correcto.wav");
                        JOptionPane.showMessageDialog(ventana, "¡Correcto!");
                    } else {
                        reproducirSonido("incorrecto.wav");
                        JOptionPane.showMessageDialog(ventana, "¡Incorrecto! La respuesta correcta era " + respuestaCorrecta);
                    }
                    puntuacionLabel.setText("Puntuación: " + jugador.obtenerPuntuacion());
                    generarPregunta();
                } catch (NumberFormatException ex) {
                    reproducirSonido("error.wav");
                    JOptionPane.showMessageDialog(ventana, "Por favor ingresa un número válido.");
                }
            }
        });
        panelFondo.add(siguienteButton);

        puntuacionLabel = new JLabel("Puntuación: 0");
        puntuacionLabel.setForeground(Color.BLACK);
        panelFondo.add(puntuacionLabel);

        tiempoLabel = new JLabel("Tiempo restante: 20");
        tiempoLabel.setForeground(Color.BLACK);
        panelFondo.add(tiempoLabel);

        inicializarTemporizador();
    }

    private void inicializarTemporizador() {
    tiempoRestante = 45;
    timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            tiempoRestante--;
            tiempoLabel.setText("Tiempo restante: " + tiempoRestante);
            if (tiempoRestante <= 0) {
                timer.stop();  // Detener el temporizador
                // Reproducir sonido dependiendo de la puntuación
                if (jugador.obtenerPuntuacion() >= 8) {
                    reproducirSonido("felicidades.wav");  // Sonido de victoria si la puntuación es alta
                } else if (jugador.obtenerPuntuacion() >= 5) {
                    reproducirSonido("Aplausos.wav");  // Sonido para puntuaciones medianas
                } else {
                    reproducirSonido("0 a 3.wav");  // Sonido si la puntuación es baja
                }
                JOptionPane.showMessageDialog(ventana, "Se acabo el tiempo! Tu puntuacion es: " + jugador.obtenerPuntuacion());
                Ranking.guardarPuntaje(jugador);  // Guardar el puntaje
                Ranking.mostrarRanking(ventana);  // Mostrar el ranking
                mostrarMenuInicio();  // Volver al menú de inicio
            }
        }
    });
    timer.start();
}

    public void comenzar() {
        ventana.setVisible(true);
        generarPregunta();
    }

    private void generarPregunta() {
        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        String[] operaciones = {"+", "-", "*"};
        String operacion = operaciones[random.nextInt(operaciones.length)];
        Pregunta pregunta = new Pregunta(num1, num2, operacion);
        preguntaLabel.setText("Pregunta: " + pregunta.obtenerPregunta());
        respuestaCorrecta = pregunta.obtenerRespuestaCorrecta();
        respuestaField.setText("");
    }

    private void reproducirSonido(String archivo) {
        try {
            File sonidoFile = new File(archivo);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(sonidoFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        JFrame inicioVentana = new JFrame("Juego Educativo");
        inicioVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicioVentana.setSize(400, 250);
        inicioVentana.setLocationRelativeTo(null);

        JPanel panelInicio = new JPanel();
        panelInicio.setLayout(new GridLayout(6, 1));  // Cambié de 5 a 6 filas para incluir el botón de salir
        panelInicio.setBackground(Color.WHITE);

        JLabel tituloLabel = new JLabel("Juego de Preguntas Matemáticas");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelInicio.add(tituloLabel);

        JTextField nombreField = new JTextField();
        nombreField.setFont(new Font("Arial", Font.PLAIN, 16));
        panelInicio.add(new JLabel("Ingrese su nombre:"));
        panelInicio.add(nombreField);

        JButton comenzarButton = new JButton("Comenzar Juego");
        comenzarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                if (!nombre.isEmpty()) {
                    Juego juego = new Juego(nombre);
                    juego.comenzar();
                    inicioVentana.dispose();
                } else {
                    JOptionPane.showMessageDialog(inicioVentana, "Por favor, ingresa un nombre.");
                }
            }
        });
        panelInicio.add(comenzarButton);
//----------------------------------------------------------------------------------------------------------------------------------------------//
        // Agrega boton "Salir"
        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Cierra la aplicacion
            }
        });
        panelInicio.add(salirButton);

        inicioVentana.add(panelInicio);
        inicioVentana.setVisible(true);
    }
}