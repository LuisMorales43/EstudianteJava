import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;
import javax.sound.sampled.*;
import java.util.List;
import java.util.ArrayList;

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

// Clase Ranking
class Ranking {
    private static final String RANKING_FILE = "ranking.txt";

    public static void guardarPuntaje(Jugador jugador) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RANKING_FILE, true))) {
            writer.write(jugador.getNombre() + ": " + jugador.obtenerPuntuacion());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> cargarRanking() {
        List<String> ranking = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RANKING_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ranking.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    public static void mostrarRanking(JFrame ventana) {
        List<String> ranking = cargarRanking();
        StringBuilder rankingText = new StringBuilder("Ranking:\n");
        for (String entry : ranking) {
            rankingText.append(entry).append("\n");
        }
        JOptionPane.showMessageDialog(ventana, rankingText.toString());
    }
}

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
    private JButton terminarButton;
    private int respuestaCorrecta;
    private int contadorPreguntas;

    public Juego(String nombreJugador) {
        jugador = new Jugador(nombreJugador);
        random = new Random();
        contadorPreguntas = 0;  // Inicializamos el contador de preguntas
        inicializarVentana();
    }

    private void inicializarVentana() {
        ventana = new JFrame("Juego Educativo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(null);

        // Usamos el panel de fondo
        FondoPanel panelFondo = new FondoPanel("fondo.jpg");  // Aquí ponemos la ruta de la imagen de fondo
        ventana.setContentPane(panelFondo);
        panelFondo.setLayout(new GridLayout(5, 1));

        preguntaLabel = new JLabel("Pregunta:");
        preguntaLabel.setForeground(Color.BLACK);  // Color blanco para que se vea sobre el fondo oscuro
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
                        reproducirSonido("correcto.wav");  // Sonido de respuesta correcta
                        JOptionPane.showMessageDialog(ventana, "¡Correcto!");
                    } else {
                        reproducirSonido("incorrecto.wav");  // Sonido de respuesta incorrecta
                        JOptionPane.showMessageDialog(ventana, "¡Incorrecto! La respuesta correcta era " + respuestaCorrecta);
                    }
                    puntuacionLabel.setText("Puntuación: " + jugador.obtenerPuntuacion());
                    contadorPreguntas++;  // Incrementar el contador de preguntas
                    // Modificamos la parte del código donde se termina el juego después de las 5 preguntas respondidas
                    if (contadorPreguntas >= 5) {
                        // Si ya respondieron 5 preguntas, terminamos el juego
                        // Verificar la puntuación y mostrar el mensaje correspondiente
                        if (jugador.obtenerPuntuacion() < 3) {
                            reproducirSonido("0 a 3.wav");  // Sonido de felicitaciones
                            JOptionPane.showMessageDialog(ventana, "¡Te falta estudiar! Tu puntuación es: " + jugador.obtenerPuntuacion());
                        } else if (jugador.obtenerPuntuacion() == 4) {
                            reproducirSonido("aplausos.wav");
                            JOptionPane.showMessageDialog(ventana, "¡Eres un genio! Tu puntuación es: " + jugador.obtenerPuntuacion());
                        } else if (jugador.obtenerPuntuacion() == 5) {
                            reproducirSonido("felicidades.wav");  // Sonido de felicitaciones
                            JOptionPane.showMessageDialog(ventana, "¡PONGALE 100 PROFE! Tu puntuación es: " + jugador.obtenerPuntuacion());
                        }
                        Ranking.guardarPuntaje(jugador);  // Guardar el puntaje del jugador
                        Ranking.mostrarRanking(ventana);  // Mostrar el ranking
                        ventana.dispose();
                    } else {
                        generarPregunta();
                    }
                } catch (NumberFormatException ex) {
                    reproducirSonido("error.wav");  // Sonido para entrada no válida
                    JOptionPane.showMessageDialog(ventana, "Por favor ingresa un número válido.");
                }
            }
        });
        panelFondo.add(siguienteButton);

        puntuacionLabel = new JLabel("Puntuación: 0");
        puntuacionLabel.setForeground(Color.BLACK);  // Asegúrate de que el texto sea visible sobre el fondo
        panelFondo.add(puntuacionLabel);

        terminarButton = new JButton("Terminar Juego");
        terminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.dispose();
            }
        });
        panelFondo.add(terminarButton);
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
}

// Clase para crear un panel con fondo de imagen
class FondoPanel extends JPanel {
    private Image imagen;

    public FondoPanel(String rutaImagen) {
        // Cargar la imagen de fondo
        try {
            imagen = new ImageIcon(rutaImagen).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la imagen de fondo en el panel
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}

// Clase principal con la interfaz de inicio
public class JuegoEducativo {
    public static void main(String[] args) {
        JFrame inicioVentana = new JFrame("Juego Educativo");
        inicioVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicioVentana.setSize(400, 200);
        inicioVentana.setLocationRelativeTo(null);

        JPanel panelInicio = new JPanel();
        panelInicio.setLayout(new GridLayout(3, 1));

        JTextField nombreField = new JTextField();
        panelInicio.add(nombreField);

        JButton jugarButton = new JButton("Jugar");
        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreJugador = nombreField.getText();
                if (!nombreJugador.isEmpty()) {
                    Juego juego = new Juego(nombreJugador);
                    juego.comenzar();
                    inicioVentana.dispose();
                } else {
                    JOptionPane.showMessageDialog(inicioVentana, "Por favor ingresa tu nombre.");
                }
            }
        });
        panelInicio.add(jugarButton);

        inicioVentana.setContentPane(panelInicio);
        inicioVentana.setVisible(true);
    }
}
