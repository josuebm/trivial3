/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tablero;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author YuSeP
 */

//NO LA USAMOS POR PROBLEMAS AL CREAR EL OBJETO E INTENTAR MOSTRARLO EN UN JPANEL
public class Rosca extends javax.swing.JPanel {
boolean d, l, a, ge, c, f, ci, h;
    Color co;
    
    //Le pasamos el color del jugador y un boleano para que pinte o no cada categoría        
    public Rosca(Color co, boolean d, boolean l, boolean a, boolean ge, boolean c, boolean f, boolean ci, boolean h) {
        initComponents();
        this.d = d;
        this.l = l;
        this.a = a;
        this.ge = ge;
        this.c = c;
        this.f = f;
        this.ci = ci;
        this.h = h;
        this.co = co;
    }
    
    public Rosca(){
        d = false;
        l = false;
        a = false;
        ge = false;
        c = false;
        f = false;
        ci = false;
        h = false;
    }
    
    public void cargarQuesitos(boolean[] quesitos){
        for(int i=0; i<quesitos.length; i++){
            if(quesitos[i] == true)
                switch(i){
                    case 0:
                        c = true;
                        break;
                    case 1:
                        d = true;
                        break;
                    case 2:
                        a = true;
                        break;
                    case 3:
                        f = true;
                        break;
                    case 4:
                        l = true;
                        break;
                    case 5:
                        ge = true;
                        break;
                    case 6:
                        h = true;
                        break;
                    case 7:
                        ci = true;
                        break;
                }
                    
        }
        repaint();
    }

    public void paint(Graphics g){
        super.paint(g);
        //Dibujo quesera
        g.setColor(co);
        g.drawArc(0, 0, 100, 100, 360, 360);
        g.fillArc(0, 0, 100, 100, 0, 4);
        g.fillArc(0, 0, 100, 100, 45, 4);
        g.fillArc(0, 0, 100, 100, 90, 4);
        g.fillArc(0, 0, 100, 100, 135, 4);
        g.fillArc(0, 0, 100, 100, 180, 4);
        g.fillArc(0, 0, 100, 100, 225, 4);
        g.fillArc(0, 0, 100, 100, 270, 4);
        g.fillArc(0, 0, 100, 100, 315, 4);
        
        //Quesito Deporte
        if(d){
            g.setColor(Color.decode("#F86A1E"));
            g.fillArc(0, 0, 100, 100, 4, 42);
        }
        
        //Quesito Literatura
        if(l){
            g.setColor(Color.decode("#A96000"));
            g.fillArc(0, 0, 100, 100, 49, 42);
        }
        
        //Quesito Arte
        if(a){
            g.setColor(Color.decode("#2493F3"));
            g.fillArc(0, 0, 100, 100, 94, 42);
        }
        
        //Quesito Geogragfía
        if(ge){
            g.setColor(Color.decode("#E91D20"));
            g.fillArc(0, 0, 100, 100, 139, 42);
        }
        
        //Quesito Ciencias
        if(c){
           g.setColor(Color.decode("#1D8B0C"));
           g.fillArc(0, 0, 100, 100, 184, 42); 
        }
        
        //Quesito Friki
        if(f){
            g.setColor(Color.decode("#1CCEE9"));
            g.fillArc(0, 0, 100, 100, 229, 42);
        }
        
        //Quesito Cine
        if(ci){
            g.setColor(Color.decode("#FF3BC2"));
            g.fillArc(0, 0, 100, 100, 274, 42);
        }
        
        //Quesito Historia
        if(h){
           g.setColor(Color.decode("#CEC529"));
           g.fillArc(0, 0, 100, 100, 319, 42); 
        }
    }
    
    public String toString(){
        return "Deporte: "+d+", Literatura:"+l+", Arte:"+a+", Geofrafia:"+ge+", Ciencia:"+c+", Friki:"+f+", Cine:"+ci+", Historia:"+h+".";
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 128, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
