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

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(ventana, new JScrollPane(textArea), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------//
// Clase base para el juego
// Clase Juego
class Juego extends JuegoBase {
    private String dificultad;
    private String[] operaciones; // Arreglo de operaciones permitidas según la dificultad
    private Random random;
    private JLabel preguntaLabel;
    private JTextField respuestaField;
    private JButton siguienteButton;
    private JLabel puntuacionLabel;
    private JLabel tiempoLabel;
    private JLabel spriteLabel;  // JLabel para el sprite
    private int respuestaCorrecta;
    private Timer timer;
    private int tiempoRestante;

    private List<ImageIcon> correctoSecuencia = new ArrayList<>();
    private List<ImageIcon> incorrectoSecuencia = new ArrayList<>();

    public Juego(String nombreJugador, String dificultad) {
        super(nombreJugador);
        this.dificultad = dificultad;
        random = new Random();
        inicializarVentana();
        cargarSecuencias();
        ajustarDificultad(); // Configurar los parámetros según la dificultad
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

        // Agregar un KeyListener para que al presionar Enter se ejecute la acción de "Responder"
        respuestaField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    siguienteButton.doClick(); // Simula un clic en el botón "Responder"
                }
            }
        });

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

        // Crear el JLabel para el sprite
        spriteLabel = new JLabel(new ImageIcon("sprite.gif"));
        spriteLabel.setBounds(350, 270, 100, 120);  // Ajusta la posición y el tamaño según sea necesario
        panelFondo.add(spriteLabel);

        inicializarTemporizador();
    }

    @Override
    public void comenzar() {
        ventana.setVisible(true);
        generarPregunta();
    }

    private void ajustarDificultad() {
        switch (dificultad) {
            case "Fácil":
                tiempoRestante = 90;
                operaciones = new String[]{ "+" }; // Solo sumas
                break;
            case "Medio":
                tiempoRestante = 60;
                operaciones = new String[]{ "+", "-" }; // Sumas y restas
                break;
            case "Difícil":
                tiempoRestante = 45;
                operaciones = new String[]{ "+", "-", "*" }; // Sumas, restas y multiplicaciones
                break;
        }
    }

    private void cargarSecuencias() {
        correctoSecuencia.add(new ImageIcon("correcto.png"));
        correctoSecuencia.add(new ImageIcon("sprite.gif"));

        incorrectoSecuencia.add(new ImageIcon("incorrecto.gif"));
        incorrectoSecuencia.add(new ImageIcon("sprite.gif"));
    }

    private void generarPregunta() {
        int num1 = random.nextInt(100) + 1; // Números del 1 al 100
        int num2 = random.nextInt(10) + 1;  // Números del 1 al 10
        String operacion = operaciones[random.nextInt(operaciones.length)]; // Operación aleatoria según la dificultad

        Pregunta pregunta = new Pregunta(num1, num2, operacion);
        preguntaLabel.setText("Pregunta: " + pregunta.obtenerPregunta());
        respuestaCorrecta = pregunta.obtenerRespuestaCorrecta();
    }

    private void verificarRespuesta() {
        try {
            int respuestaUsuario = Integer.parseInt(respuestaField.getText());
            if (respuestaUsuario == respuestaCorrecta) {
                jugador.aumentarPuntuacion();
                reproducirSonido("correcto.wav");
                JOptionPane.showMessageDialog(ventana, "¡Correcto!");
                moverSprite(true);  // Mover el sprite cuando la respuesta es correcta
            } else {
                reproducirSonido("incorrecto.wav");
                JOptionPane.showMessageDialog(ventana, "Incorrecto. La respuesta era " + respuestaCorrecta);
                moverSprite(false);  // Mover el sprite cuando la respuesta es incorrecta
            }
            puntuacionLabel.setText("Puntuación: " + jugador.obtenerPuntuacion());
            generarPregunta();
        } catch (NumberFormatException e) {
            reproducirSonido("error.wav");
            JOptionPane.showMessageDialog(ventana, "Por favor ingresa un número válido.");
        }
    }

    private void moverSprite(boolean respuestaCorrecta) {
        List<ImageIcon> secuencia = respuestaCorrecta ? correctoSecuencia : incorrectoSecuencia;
        
        Timer animacionTimer = new Timer(600, new ActionListener() {
            private int indice = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indice < secuencia.size()) {
                    spriteLabel.setIcon(secuencia.get(indice));
                    indice++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        
        animacionTimer.start();
    }

    private void inicializarTemporizador() {
        timer = new Timer(1000, e -> {
            tiempoRestante--;
            tiempoLabel.setText("Tiempo restante: " + tiempoRestante);
            if (tiempoRestante <= 0) {
                timer.stop();
                finalizarJuego();
            }
        });
        timer.start();
    }

    private void finalizarJuego() {
        if (jugador.obtenerPuntuacion() >= 8) {
            reproducirSonido("felicidades.wav");  // Sonido de victoria si la puntuación es alta
        } else if (jugador.obtenerPuntuacion() >= 5) {
            reproducirSonido("Aplausos.wav");  // Sonido para puntuaciones medianas
        } else {
            reproducirSonido("0 a 3.wav");  // Sonido si la puntuación es baja
        }
        JOptionPane.showMessageDialog(ventana, "¡Se acabó el tiempo! Puntuación: " + jugador.obtenerPuntuacion());
        Ranking.guardarPuntaje(jugador);
        Ranking.mostrarRanking(ventana);
        mostrarMenuInicio();
    }

    private void mostrarMenuInicio() {
        ventana.dispose();
        JuegoEducativo.iniciarMenu();
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
//Clase JuegoEducativo
public class JuegoEducativo {
    public static void main(String[] args) {
        iniciarMenu();
    }

    public static void iniciarMenu() {
        JFrame inicioVentana = new JFrame("Juego Educativo");
        inicioVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicioVentana.setSize(900, 550);
        inicioVentana.setLocationRelativeTo(null);

        FondoPanel panelInicio = new FondoPanel("C:/Users/NextClick/OneDrive/Escritorio/CODE/POO/Proyecto/Juego/super-mario-cloud-moewalls-com.gif");
        panelInicio.setLayout(null);

        JLabel tituloLabel = new JLabel("Juego de Preguntas Matemáticas");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setBounds(150, 30, 600, 40);
        tituloLabel.setForeground(Color.BLACK);
        panelInicio.add(tituloLabel);

        JLabel nombreLabel = new JLabel("Ingrese su nombre:");
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreLabel.setBounds(380, 100, 200, 30);
        nombreLabel.setForeground(Color.BLACK);
        panelInicio.add(nombreLabel);

        JTextField nombreField = new JTextField();
        nombreField.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreField.setBounds(150, 140, 600, 30);
        panelInicio.add(nombreField);

        JLabel dificultadLabel = new JLabel("Seleccione la dificultad:");
        dificultadLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dificultadLabel.setBounds(380, 190, 200, 30);
        dificultadLabel.setForeground(Color.BLACK);
        panelInicio.add(dificultadLabel);

        String[] nivelesDificultad = {"Fácil", "Medio", "Difícil"};
        JComboBox<String> dificultadCombo = new JComboBox<>(nivelesDificultad);
        dificultadCombo.setBounds(150, 230, 600, 30);
        panelInicio.add(dificultadCombo);

        JButton comenzarButton = new JButton("Comenzar Juego");
        comenzarButton.setBounds(100, 300, 300, 40);
        comenzarButton.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String dificultad = (String) dificultadCombo.getSelectedItem();
            if (!nombre.isEmpty()) {
                JOptionPane.showMessageDialog(inicioVentana, "¡Juego está a punto de iniciar para " + nombre + "!");
                inicioVentana.dispose();
                new Juego(nombre, dificultad).comenzar(); // Pasar la dificultad seleccionada
            } else {
                JOptionPane.showMessageDialog(inicioVentana, "Por favor, ingresa un nombre.");
            }
        });
        panelInicio.add(comenzarButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(500, 300, 300, 40);
        salirButton.addActionListener(e -> System.exit(0));
        panelInicio.add(salirButton);

        JButton rankingButton = new JButton("Ranking");
        rankingButton.setBounds(300, 400, 300, 40);
        rankingButton.addActionListener(e -> Ranking.mostrarRanking(inicioVentana));
        panelInicio.add(rankingButton);

        inicioVentana.add(panelInicio);
        inicioVentana.setVisible(true);
    }
}

