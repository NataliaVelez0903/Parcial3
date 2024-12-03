/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial30;

/**
 *
 * @author USUARIO
 */
public class Persona {
    private String identificacion;
    private String nombre;
    private String correo;

    public Persona(String identificacion, String nombre, String correo) throws ExcepcionPersonalizada {
        if (!correo.contains("@") || !correo.endsWith(".com")) {
            throw new ExcepcionPersonalizada("Correo inválido: " + correo);
        }
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correo = correo;
    }

    // Getters y setters
    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}

