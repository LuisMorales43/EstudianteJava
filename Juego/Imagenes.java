import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Imagenes {

    // Método para cargar la imagen desde una ruta
    public static ImageIcon cargarImagen(String ruta) {
        try {
            // Carga la imagen desde la ruta proporcionada
            Image img = ImageIO.read(new File(ruta));
            return new ImageIcon(img);  // Devuelve la imagen como un ImageIcon
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // En caso de error, devuelve null
        }
    }

    // Método para crear un JLabel con una imagen cargada desde la ruta proporcionada
    public static JLabel crearImagenLabel(String ruta) {
        // Carga la imagen con la ruta proporcionada
        ImageIcon icono = cargarImagen(ruta);
        
        if (icono != null) {
            // Crea y devuelve un JLabel con el icono de la imagen
            return new JLabel(icono);
        } else {
            // Si no se pudo cargar la imagen, devuelve un JLabel vacío
            return new JLabel("Error al cargar la imagen");
        }
    }

    // Crear un JPanel que dibuje la imagen como fondo
    static class FondoPanel extends JPanel {
        private Image imagen;

        // Constructor que recibe la ruta de la imagen
        public FondoPanel(String ruta) {
            // Carga la imagen de fondo
            this.imagen = cargarImagen(ruta).getImage();
        }

        // Método para dibujar la imagen en el panel
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibuja la imagen de fondo
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        // Ruta de la imagen a cargar
        String rutaImagen = "C:/Users/NextClick/OneDrive/Escritorio/CODE/POO/Proyecto/Juego/fondoInicio.jpg";

        // Crea un JFrame para la ventana
        JFrame frame = new JFrame("Imagen de Fondo");

        // Crea un FondoPanel con la imagen de fondo
        FondoPanel panelFondo = new FondoPanel(rutaImagen);
        frame.setContentPane(panelFondo);  // Establece el FondoPanel como el panel principal

        // Crea un JLabel con el texto o componentes adicionales si los necesitas
        JLabel label = new JLabel("¡Bienvenido al Juego!");
        label.setForeground(Color.WHITE);  // Cambia el color del texto para que sea visible sobre el fondo
        label.setFont(new Font("Arial", Font.BOLD, 24));  // Cambia el tamaño de la fuente

        // Agrega el JLabel al panel
        panelFondo.setLayout(new BorderLayout());
        panelFondo.add(label, BorderLayout.CENTER);

        // Ajusta el tamaño de la ventana
        frame.setSize(800, 600);  // Establece un tamaño fijo o usa pack() si quieres ajustarlo automáticamente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
