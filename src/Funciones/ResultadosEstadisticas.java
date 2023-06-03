package Funciones;

public class ResultadosEstadisticas {
    private String titulo;
    private long antes;
    private long despues;
    private long actualizados;
    private long noActualizados;
    private long creados;

    public ResultadosEstadisticas(String titulo, long antes, long despues, long actualizados, long noActualizados, long creados) {
        this.titulo = titulo;
        this.antes = antes;
        this.despues = despues;
        this.actualizados = actualizados;
        this.noActualizados = noActualizados;
        this.creados = creados;
    }

    public String getTexto() {
        return String.format("""
                        %s
                                                
                        - Cuántos documentos JSON tenía la colección ANTES de hacer la nueva carga: %d
                        - Con cuántos documentos JSON queda la colección a raíz de la nueva carga: %d
                        - Cuántos documentos se actualizaron en la colección: %d
                        - Cuántos documentos no se actualizaron (quedaron iguales en la colección): %d
                        - Cuántos documentos nuevos surgieron en la colección a raíz de la nueva carga: %d
                                        
                        """,
                titulo, antes, despues, actualizados, noActualizados, creados);
    }


}
