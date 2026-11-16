
import java.util.ArrayList;

public class CatalogoLibros {

    private ArrayList<Libro> libros;

    public CatalogoLibros() {
        libros = DatosIniciales.cargar();
        ordenarPorCodigoBurbuja();
    }

    public void ordenarPorCodigoBurbuja() {
        Libro aux;

        for (int i = 0; i < libros.size() - 1; i++) {
            for (int j = i + 1; j < libros.size(); j++) {

                if (libros.get(i).getCodigo() > libros.get(j).getCodigo()) {
                    aux = libros.get(i);
                    libros.set(i, libros.get(j));
                    libros.set(j, aux);
                }
            }
        }
    }

    public int cantidadLibros() {
        return libros.size();
    }


    public ArrayList<Libro> buscarCoincidencias(String texto) {
        ArrayList<Libro> resultado = new ArrayList<>();

        texto = texto.trim().toLowerCase();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Ingrese un texto para buscar");
        }

        for (Libro aux : libros) {
            if (aux.getTitulo().toLowerCase().contains(texto) ||
                    aux.getAutor().toLowerCase().contains(texto)) {
                resultado.add(aux);
            }
        }

        return resultado;
    }


    public int buscarIndicePorCodigo(int codigo) {
        int i = 0;
        int s = libros.size() - 1;
        int c;

        if (codigo <= 0) {
            return -1;
        }

        while (i <= s) {
            c = (i + s) / 2;

            if (libros.get(c).getCodigo() == codigo) {
                return c;
            }

            if (codigo < libros.get(c).getCodigo()) {
                s = c - 1;
            } else {
                i = c + 1;
            }
        }

        return -1;
    }


    public void registrar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("Libro no valido");
        }

        if (libro.getCodigo() <= 0) {
            throw new IllegalArgumentException("Codigo no valido");
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Titulo obligatorio");
        }

        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("Autor obligatorio");
        }

        if (libro.getGenero() == null || libro.getGenero().trim().isEmpty()) {
            throw new IllegalArgumentException("Genero obligatorio");
        }

        if (libro.getAnio() < 1 || libro.getAnio() > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Anio no valido");
        }

        if (buscarIndicePorCodigo(libro.getCodigo()) != -1) {
            throw new IllegalArgumentException("El codigo ya existe");
        }

        if (libro.getMeGusta() != 0) {
            throw new IllegalArgumentException("El libro nuevo debe iniciar con 0 me gusta");
        }
        libros.add(libro);
        ordenarPorCodigoBurbuja();
    }


    public void editarLibro(int codigo, String titulo, String autor,
                            String genero, int anio) {

        int posicion = buscarIndicePorCodigo(codigo);

        if (posicion == -1) {
            throw new IllegalArgumentException("Libro no encontrado");
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Titulo obligatorio");
        }

        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor obligatorio");
        }

        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("Genero obligatorio");
        }

        if (anio < 1 || anio > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Anio no valido");
        }

        Libro aux = libros.get(posicion);

        aux.setTitulo(titulo);
        aux.setAutor(autor);
        aux.setGenero(genero);
        aux.setAnio(anio);
    }


    public void darMeGusta(int codigo) {
        int posicion = buscarIndicePorCodigo(codigo);

        if (posicion == -1) {
            throw new IllegalArgumentException("Libro no encontrado");
        }

        Libro aux = libros.get(posicion);
        aux.incrementarMeGusta();
    }


    public ArrayList<Libro> obtenerRanking() {
        ArrayList<Libro> ranking = new ArrayList<>(libros);
        Libro aux;

        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = 0; j < ranking.size() - 1 - i; j++) {

                if (ranking.get(j).getMeGusta() <
                        ranking.get(j + 1).getMeGusta()) {

                    aux = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, aux);
                }
            }
        }

        return ranking;
    }


}
