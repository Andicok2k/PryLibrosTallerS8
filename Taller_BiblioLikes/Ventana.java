
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    private JLabel lblBuscarCodigo;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JLabel lblBuscarTexto;
    private JTextField txtBuscarTexto;
    private JButton btnBuscarTexto;
    private JLabel lblResultados;
    private JList<Libro> lstResultados;
    private JScrollPane scrollResultados;
    private JLabel lblCoincidencias;

    private JLabel lblEditarCodigo;
    private JTextField txtEditarCodigo;
    private JLabel lblEditarTitulo;
    private JTextField txtEditarTitulo;
    private JLabel lblEditarAutor;
    private JTextField txtEditarAutor;
    private JLabel lblEditarGenero;
    private JComboBox cmbEditarGenero;
    private JLabel lblEditarAnio;
    private JSpinner spnEditarAnio;
    private JLabel lblMeGusta;
    private JButton btnGuardarCambios;
    private JButton btnMeGusta;

    private JLabel lblTituloRanking;
    private JList<String> lstRanking;
    private JScrollPane scrollRanking;
    private JButton btnActualizarRanking;

    private CatalogoLibros catalogo;
    private DefaultListModel<Libro> modeloResultados;
    private DefaultListModel<String> modeloRanking;
    private int codigoSeleccionado = -1;
    private String tipoBusqueda = "";

    public Ventana() {

        catalogo = new CatalogoLibros();

        cmbGenero.removeAllItems();
        cmbEditarGenero.removeAllItems();

        String[] generos = {
                "Programación", "Bases de datos", "Redes",
                "Seguridad", "Sistemas", "Software",
                "IA", "Gestión"
        };

        for (String genero : generos) {
            cmbGenero.addItem(genero);
            cmbEditarGenero.addItem(genero);
        }

        int anioActual = java.time.Year.now().getValue();

        spnAnio.setModel(
                new SpinnerNumberModel(anioActual, 1, anioActual, 1)
        );

        spnEditarAnio.setModel(
                new SpinnerNumberModel(anioActual, 1, anioActual, 1)
        );

        txtEditarCodigo.setEditable(false);

        modeloResultados = new DefaultListModel<>();
        lstResultados.setModel(modeloResultados);
        lstResultados.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        modeloRanking = new DefaultListModel<>();
        lstRanking.setModel(modeloRanking);

        limpiarDetalle();
        actualizarContadores();
        actualizarRanking();

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registrar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnBuscarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarCodigo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnBuscarTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarTexto();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnGuardarCambios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    guardarCambios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnMeGusta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    darMeGusta();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnActualizarRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarRanking();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        lstResultados.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            mostrarDetalle();
                        }
                    }
                }
        );

        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane1.getSelectedIndex() == 2) {
                    actualizarRanking();
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
                codigo, titulo, autor, genero, anio, 0
        );

        catalogo.registrar(libro);

        JOptionPane.showMessageDialog(
                null, "Libro registrado correctamente"
        );

        txtCodigo.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        cmbGenero.setSelectedIndex(0);
        spnAnio.setValue(anioActual());

        actualizarContadores();
        actualizarResultados();
        actualizarRanking();
    }

    public void buscarCodigo() throws Exception {

        limpiarResultados();
        tipoBusqueda = "codigo";

        String texto = txtBuscarCodigo.getText().trim();

        if (texto.isEmpty()) {
            throw new Exception("Ingrese el codigo a buscar");
        }

        int codigo;

        try {
            codigo = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            throw new Exception("Ingrese un codigo numerico");
        }

        if (codigo <= 0) {
            throw new Exception("Ingrese un codigo positivo");
        }

        int posicion = catalogo.buscarIndicePorCodigo(codigo);

        if (posicion == -1) {
            throw new Exception("Codigo no encontrado");
        }

        Libro libro = catalogo.obtenerLibro(posicion);

        modeloResultados.addElement(libro);
        lblCoincidencias.setText("Coincidencias: 1");
        lstResultados.setSelectedIndex(0);
    }

    public void buscarTexto() throws Exception {

        limpiarResultados();
        tipoBusqueda = "texto";

        String texto = txtBuscarTexto.getText().trim();

        if (texto.isEmpty()) {
            throw new Exception("Ingrese un titulo o autor");
        }

        ArrayList<Libro> resultados =
                catalogo.buscarCoincidencias(texto);

        for (Libro aux : resultados) {
            modeloResultados.addElement(aux);
        }

        lblCoincidencias.setText(
                "Coincidencias: " + resultados.size()
        );

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "No se encontraron coincidencias"
            );
        }
    }

    public void mostrarDetalle() {

        Libro libro = lstResultados.getSelectedValue();

        if (libro == null) {
            limpiarDetalle();
            return;
        }

        codigoSeleccionado = libro.getCodigo();

        txtEditarCodigo.setText(
                String.valueOf(libro.getCodigo())
        );

        txtEditarTitulo.setText(libro.getTitulo());
        txtEditarAutor.setText(libro.getAutor());
        cmbEditarGenero.setSelectedItem(libro.getGenero());
        spnEditarAnio.setValue(libro.getAnio());

        lblMeGusta.setText(
                "Me gusta: " + libro.getMeGusta()
        );
    }

    public void guardarCambios() throws Exception {

        if (codigoSeleccionado == -1) {
            throw new Exception("Seleccione un libro");
        }

        String titulo = txtEditarTitulo.getText().trim();
        String autor = txtEditarAutor.getText().trim();
        String genero =
                cmbEditarGenero.getSelectedItem().toString();

        int anio = (Integer) spnEditarAnio.getValue();

        catalogo.editarLibro(
                codigoSeleccionado,
                titulo,
                autor,
                genero,
                anio
        );

        actualizarResultados();
        actualizarRanking();

        JOptionPane.showMessageDialog(
                null, "Libro actualizado correctamente"
        );
    }

    public void darMeGusta() throws Exception {

        if (codigoSeleccionado == -1) {
            throw new Exception("Seleccione un libro");
        }

        catalogo.darMeGusta(codigoSeleccionado);

        actualizarResultados();
        actualizarRanking();

        JOptionPane.showMessageDialog(
                null, "Me gusta agregado correctamente"
        );
    }

    public void actualizarResultados() {

        int codigoAnterior = codigoSeleccionado;

        if (tipoBusqueda.equals("texto")) {

            String texto = txtBuscarTexto.getText().trim();

            limpiarResultados();

            if (texto.isEmpty()) {
                return;
            }

            ArrayList<Libro> resultados =
                    catalogo.buscarCoincidencias(texto);

            for (Libro aux : resultados) {
                modeloResultados.addElement(aux);
            }

        } else if (tipoBusqueda.equals("codigo")) {

            limpiarResultados();

            String texto = txtBuscarCodigo.getText().trim();
            int codigo;

            try {
                codigo = Integer.parseInt(texto);
            } catch (NumberFormatException ex) {
                return;
            }

            int posicion = catalogo.buscarIndicePorCodigo(codigo);

            if (posicion != -1) {
                modeloResultados.addElement(
                        catalogo.obtenerLibro(posicion)
                );
            }

        } else {
            limpiarResultados();
            return;
        }

        lblCoincidencias.setText(
                "Coincidencias: " + modeloResultados.size()
        );

        for (int i = 0; i < modeloResultados.size(); i++) {
            if (modeloResultados.get(i).getCodigo()
                    == codigoAnterior) {

                lstResultados.setSelectedIndex(i);
                break;
            }
        }
    }

    public void limpiarResultados() {

        modeloResultados.clear();
        limpiarDetalle();
        lblCoincidencias.setText("Coincidencias: 0");
    }

    public void limpiarDetalle() {

        codigoSeleccionado = -1;

        txtEditarCodigo.setText("");
        txtEditarTitulo.setText("");
        txtEditarAutor.setText("");
        cmbEditarGenero.setSelectedIndex(0);
        spnEditarAnio.setValue(anioActual());

        lblMeGusta.setText("Me gusta: 0");
    }

    public void actualizarRanking() {

        modeloRanking.clear();

        ArrayList<Libro> ranking = catalogo.obtenerRanking();

        for (Libro aux : ranking) {

            String informacion =
                    "Codigo: " + aux.getCodigo()
                            + " | Titulo: " + aux.getTitulo()
                            + " | Me gusta: " + aux.getMeGusta();

            modeloRanking.addElement(informacion);
        }
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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BiblioLikes");
                frame.setContentPane(new Ventana().Ventana);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
