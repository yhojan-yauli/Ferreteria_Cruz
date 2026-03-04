import java.net.URL;

public class PruebaImagenes {

    public static void main(String[] args) {
        System.out.println("\n---- INICIO DEL DIAGNÓSTICO DE IMÁGENES ----");
        
        // Estos son los nombres EXACTOS que tu código está buscando.
        String[] archivos = {
            "/img/menu-abierto.png",
            "/img/UTPLOGO (1).jpeg"
        };
        
        boolean huboErrores = false;

        for (String ruta : archivos) {
            System.out.println("Buscando: " + ruta);
            // Usamos el mismo mecanismo que tu ventana para buscar el archivo
            URL url = PruebaImagenes.class.getResource(ruta);
            
            if (url == null) {
                System.out.println("  [❌ MAL] Java dice: 'No encuentro este archivo'.");
                huboErrores = true;
            } else {
                System.out.println("  [✅ BIEN] Java dice: 'Archivo encontrado'.");
            }
            System.out.println("--------------------------------------------------");
        }

        System.out.println("\n---- CONCLUSIÓN ----");
        if (huboErrores) {
            System.out.println("El error NullPointerException ocurre porque Java no encuentra las imágenes.");
            System.out.println("SOLUCIONES POSIBLES (Revisa en este orden):");
            System.out.println("1. La carpeta 'img' debe estar DENTRO de 'Source Packages'.");
            System.out.println("2. El nombre del archivo debe ser EXACTO (mayúsculas, espacios).");
            System.out.println("3. Realiza un 'Clean and Build' al proyecto.");
        } else {
            System.out.println("¡Java encuentra las imágenes en esta prueba!");
            System.out.println("Si tu ventana principal sigue fallando, es porque NetBeans no se ha actualizado.");
            System.out.println("SOLUCIÓN URGENTE: Haz un 'Clean and Build' al proyecto.");
        }
        System.out.println("--------------------\n");
    }
}