
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ventana {

    private JPanel Ventana;
    private JTabbedPane tabbedPane1;
    private JTextField txtCodigo;
    private JLabel lblCodigo;
    private JLabel lblTitulo;
    private JTextField txtTitulo;
    private JLabel lblAutor;
    private JTextField txtAutor;
    private JLabel lblGenero;
    private JComboBox cmbGenero;
    private JLabel lblAnio;
    private JSpinner spnAnio;
    private JButton btnRegistrar;
    private JLabel lblTotalLibros;

    private CatalogoLibros catalogo;

    public Ventana() {

        catalogo = new CatalogoLibros();

        cmbGenero.removeAllItems();
        cmbGenero.addItem("Programación");
        cmbGenero.addItem("Bases de datos");
        cmbGenero.addItem("Redes");
        cmbGenero.addItem("Seguridad");
        cmbGenero.addItem("Sistemas");
        cmbGenero.addItem("Software");
        cmbGenero.addItem("IA");
        cmbGenero.addItem("Gestión");

        int anioActual = java.time.Year.now().getValue();

        spnAnio.setModel(new SpinnerNumberModel(anioActual, 1, anioActual, 1));

        actualizarContadores();

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    registrar();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage()
                    );
                }
            }
        });
    }

    public void registrar() throws Exception {

        String codigoTexto = txtCodigo.getText().trim();
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String genero = cmbGenero.getSelectedItem().toString();
        int anio = (Integer) spnAnio.getValue();

        if (codigoTexto.isEmpty()) {
            throw new Exception("Ingrese el codigo");
        }

        if (titulo.isEmpty()) {
            throw new Exception("Ingrese el titulo");
        }

        if (autor.isEmpty()) {
            throw new Exception("Ingrese el autor");
        }

        int codigo;

        try {
            codigo = Integer.parseInt(codigoTexto);
        } catch (NumberFormatException ex) {
            throw new Exception("El codigo debe ser numerico");
        }

        if (codigo <= 0) {
            throw new Exception("El codigo debe ser positivo");
        }

        if (catalogo.buscarIndicePorCodigo(codigo) != -1) {
            throw new Exception("El codigo ya existe");
        }

        Libro libro = new Libro(
                codigo,
                titulo,
                autor,
                genero,
                anio,
                0
        );

        catalogo.registrar(libro);

        JOptionPane.showMessageDialog(
                null,
                "Libro registrado correctamente"
        );

        txtCodigo.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        cmbGenero.setSelectedIndex(0);
        spnAnio.setValue(anioActual());

        actualizarContadores();
    }

    public int anioActual() {
        return java.time.Year.now().getValue();
    }

    public void actualizarContadores() {

        lblTotalLibros.setText(
                "Total de libros: " + catalogo.cantidadLibros()
        );
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("BiblioLikes");
        frame.setContentPane(new Ventana().Ventana);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
