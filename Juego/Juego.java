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
    private final int MAX_PREGUNTAS = 2;  // Número máximo de preguntas

    public Juego(String nombreJugador) {
        jugador = new Jugador(nombreJugador);
        random = new Random();
        contadorPreguntas = 0;
        inicializarVentana();
    }

    private void inicializarVentana() {
        ventana = new JFrame("Juego Educativo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(null);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(5, 1));

        preguntaLabel = new JLabel("Pregunta:");
        panelPrincipal.add(preguntaLabel);

        respuestaField = new JTextField();
        panelPrincipal.add(respuestaField);

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
                    contadorPreguntas++;  // Incrementar el contador de preguntas
                    if (contadorPreguntas < MAX_PREGUNTAS) {
                        generarPregunta();
                    } else {
                        JOptionPane.showMessageDialog(ventana, "¡Juego terminado! Has respondido " + MAX_PREGUNTAS + " preguntas.");
                        ventana.dispose();  // Terminar el juego
                    }
                } catch (NumberFormatException ex) {
                    reproducirSonido("error.wav");
                    JOptionPane.showMessageDialog(ventana, "Por favor ingresa un número válido.");
                }
            }
        });
        panelPrincipal.add(siguienteButton);

        puntuacionLabel = new JLabel("Puntuación: 0");
        panelPrincipal.add(puntuacionLabel);

        terminarButton = new JButton("Terminar Juego");
        terminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.dispose();
            }
        });
        panelPrincipal.add(terminarButton);

        ventana.add(panelPrincipal);
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
