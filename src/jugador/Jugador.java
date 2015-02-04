/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jugador;

/**
 *
 * @author mza
 */

//Clase jugador para manejar las estadisticas de los jugadores
public class Jugador {
    private String nombre;
    private int partidasJugadas;
    private int partidasGanadas;
    private int preguntasAcertadas;
    private int preguntasFalladas;
    
    public Jugador(String n, int pj, int pg, int pa, int pf){
        nombre = n;
        partidasJugadas = pj;
        partidasGanadas = pg;
        preguntasAcertadas = pa;
        preguntasFalladas = pf;
    }
    
    public int getPJ(){
        return partidasJugadas;
    }
    
    public void setPJ(int pj){
        partidasJugadas = pj;
    }
    
    public int getPG(){
        return partidasGanadas;
    }
    
    public void setPG(int pg){
        partidasGanadas = pg;
    }
    
    public int getPA(){
        return preguntasAcertadas;
    }
    
    public void setPA(int pa){
        preguntasAcertadas = pa;
    }
    
    public int getPF(){
        return preguntasFalladas;
    }
    
    public void setPF(int pf){
        preguntasFalladas = pf;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String toString(){
        return nombre+" PJ: "+partidasJugadas+" PG: "+partidasGanadas+" PA: "+preguntasAcertadas+" PF: "+preguntasFalladas;
    }
}
