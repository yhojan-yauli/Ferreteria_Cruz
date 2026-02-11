

import com.ferreteria.dao.ProductoDAO;
import com.ferreteria.dao.ProductoDAOImpl;
import com.ferreteria.modelo.Producto;
import java.util.List;

public class TestCRUD {

    public static void main(String[] args) {
        // 1. Instanciar el DAO
        ProductoDAO productoDAO = new ProductoDAOImpl();

        System.out.println("--- INICIANDO TEST ---");

        // 2. Crear un objeto Producto de prueba
        // Constructor: (codigo, nombre, marca, precio, stock, stockMinimo)
        Producto nuevoMartillo = new Producto("MAR-001", "Martillo de uña", "Stanley", 25.50, 10, 3);

        // 3. Probar el método REGISTRAR (INSERT)
        System.out.println("\nIntentando registrar producto...");
        boolean registrado = productoDAO.registrar(nuevoMartillo);
        
        if (registrado) {
            System.out.println("[ÉXITO] Producto registrado correctamente.");
        } else {
            System.out.println("[ERROR] No se pudo registrar el producto.");
            // Si falla aquí, revisa tu consola por errores de SQL (ConexionDB)
            return; // Salimos si falla lo básico
        }

        // 4. Probar el método LISTAR (SELECT)
        System.out.println("\nListando productos activos...");
        List<Producto> lista = productoDAO.listarTodos(false); // false para no traer los borrados
        
        if (lista.isEmpty()) {
            System.out.println("[ALERTA] La lista está vacía.");
        } else {
            for (Producto p : lista) {
                System.out.println("-> ID: " + p.getIdProducto() + " | " + p.getNombre() + " | Stock: " + p.getStockActual());
            }
             System.out.println("[ÉXITO] Listado completado.");
        }
        
        System.out.println("\n--- TEST FINALIZADO ---");
    }
}