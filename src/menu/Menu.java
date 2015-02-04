/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package menu;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tablero.*;
import tarjetas.*;
import jugador.*;
import creditos.*;
import java.io.IOException;
/**
 *
 * @author mza
 */
public class Menu extends javax.swing.JFrame {
    private Tablero tab;
    private Statement st;
    private Connection conexion;
    private String[] jugadores;
    private Jugador[] jugs;
    private ResultSet resul;
    ImageIcon[] jug;
    /**
     * Creates new form menu
     */
    public Menu() throws ClassNotFoundException, SQLException {
        initComponents();
        creditos.setFont(new java.awt.Font("Bauhaus 93", 1, 18));
        nj.setVisible(false);
        conectarBD();
        creditos.setFont(new java.awt.Font("Bauhaus 93", 1, 18));
        //Poner a pantalla completa
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //this.setLocationRelativeTo(null);
        logoP.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //HAY QUE CONSEGUIR QUE EL JPANEL SE ADAPTE AL TAMAÑO DEL JFRAME
        
        
        int ancho = (this.getSize().width - 600)/ 2;
        int alto = (this.getSize().height - 600)/ 2;
        putologoP.setLocation(ancho, alto);
        
        //Escuchador para elegir el numero de jugadores
        ActionListener combo = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    comboAction(e);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
    };
        
        nj.addActionListener(combo);
        /*ImageIcon logo= new ImageIcon(getClass().getResource("/menu/deco.png"));
        ImageIcon logoE = new ImageIcon(logo.getImage().getScaledInstance(200, 100, Image.SCALE_DEFAULT));
        decoL.setIcon(logoE);
        decoL2.setIcon(logoE);*/
        jug = new ImageIcon[4];
        jug[0] = new ImageIcon(getClass().getResource("/menu/jug1.png"));
        jug[1] = new ImageIcon(getClass().getResource("/menu/jug2.png"));
        jug[2] = new ImageIcon(getClass().getResource("/menu/jug3.png"));
        jug[3] = new ImageIcon(getClass().getResource("/menu/jug4.png"));
    }
    
    //Metodo para conectar a la base de datos
    private void conexionBD() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        String servidor = "jdbc:mysql://77.230.26.64:3306/Trivial";
        //String servidor = "jdbc:mysql://192.168.0.7:3306/Trivial";
        //String servidor = "jdbc:mysql://localhost/Trivial";
        String usuarioDB="proyecto";
        String passwordDB="trivial";
        conexion= DriverManager.getConnection(servidor,usuarioDB,passwordDB);
        System.out.println("Conexion a Driver realizada.");
    }
    
    //Metodo para crear el Statement
    public void conectarBD() throws ClassNotFoundException, SQLException{
        conexionBD();
        st = conexion.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        System.out.println("Conexion a BD realizada.");
    }
    
    //Metodo del escuchador del numero de jugadores
    public void comboAction(ActionEvent e) throws ClassNotFoundException, SQLException{
        JComboBox opcion = (JComboBox)e.getSource();
        System.out.println("Combo"+opcion.getSelectedItem().toString());
        boolean cancelar = false;
        int num;
        switch(opcion.getSelectedItem().toString().toLowerCase()){
            case "2 jugadores":
                num = 2;
                break;
            case "3 jugadores":
                num = 3;
                break;
            case "4 jugadores":
                num = 4;
                break;
            default:
                num = -1;
                break;
        }
        
        if(num != -1){
            //num es igual al numero de jugadores
            jugadores = new String[num];
            String aux = "";
            Boolean repetido = false;
            int cont = 0;
            System.out.println(cancelar);
            int i;

            //Manejamos que se pidan tantos nombres como jugadores, q el nombre del jugador no sea null, que no se repitan los nombres y que si pulsamos cancelar se cancele la operacion
             for(i = 0; i<num && !cancelar; i++){
                do{
                    repetido = false;
                    //do{
                        //aux = JOptionPane.showInputDialog("Nombre Jugador "+(i+1)+":");
                        aux = (String) JOptionPane.showInputDialog(null, "Introduzca su nombre", "Jugador " + (i+1), -1, jug[i], null, "Nuevo Jugador");
                    //}while(aux == null);
                    if(aux == null)
                        cancelar = true;
                    for(int j=0; j<i && !cancelar; j++){
                        if(aux.equals(jugadores[j])){
                            repetido = true;
                            JOptionPane.showMessageDialog(null, "Nombre de usuario repetido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } 
                }while(repetido);

                if(!cancelar){
                    jugadores[i] = aux;
                     try{
                        st.execute("INSERT INTO jugador VALUES ('"+jugadores[i]+"', 0, 0, 0, 0, 0)");
                    }catch(SQLException sql){
                        System.out.println("Jugador repetido.");
                    }

                }  
             }

             if(!cancelar){
                cargarJugadores(jugadores);
                tab = new Tablero(jugs, st);
             }
        }
    }
    
    //Creamos un objeto jugador por cada jugador
    public void cargarJugadores(String[] jug) throws SQLException{
        jugs = new Jugador[jug.length];
        
        for(int i=0; i<jug.length; i++){
            resul = st.executeQuery("Select Partidas_Jugadas, Partidas_Ganadas, Preguntas_Acertadas, Preguntas_Falladas from jugador where nombre_jugador = '"+jug[i]+"'");
            while(resul.next()){
                jugs[i] = new Jugador(jug[i], Integer.valueOf(resul.getString(1)), Integer.valueOf(resul.getString(2)), Integer.valueOf(resul.getString(3)), Integer.valueOf(resul.getString(4)));
            }
        }
        
        for(int i=0; i<jug.length; i++)
            System.out.println(jugs[i].toString());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoP = new javax.swing.JPanel();
        putologoP = new javax.swing.JPanel();
        creditos = new javax.swing.JButton();
        sp = new javax.swing.JButton();
        stats = new javax.swing.JButton();
        mp = new javax.swing.JButton();
        stats1 = new javax.swing.JButton();
        nj = new javax.swing.JComboBox();
        decoL = new javax.swing.JLabel();
        decoL2 = new javax.swing.JLabel();
        nombreL = new javax.swing.JLabel();
        logoL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 0, 153));
        setUndecorated(true);
        getContentPane().setLayout(null);

        logoP.setBackground(Color.decode("#FFFFEB"));
        logoP.setMaximumSize(new java.awt.Dimension(600, 600));
        logoP.setMinimumSize(new java.awt.Dimension(600, 600));
        logoP.setPreferredSize(new java.awt.Dimension(600, 600));
        logoP.setLayout(null);

        putologoP.setBackground(Color.decode("#FFFFEB"));
        putologoP.setMaximumSize(new java.awt.Dimension(550, 700));
        putologoP.setMinimumSize(new java.awt.Dimension(550, 700));
        putologoP.setPreferredSize(new java.awt.Dimension(550, 700));
        putologoP.setLayout(null);

        creditos.setForeground(new java.awt.Color(255, 255, 255));
        creditos.setText("Créditos");
        creditos.setBorderPainted(false);
        creditos.setContentAreaFilled(false);
        creditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditosActionPerformed(evt);
            }
        });
        putologoP.add(creditos);
        creditos.setBounds(140, 340, 110, 110);

        sp.setBackground(Color.decode("#2097D4"));
        sp.setFont(new java.awt.Font("Bauhaus 93", 1, 18)); // NOI18N
        sp.setForeground(new java.awt.Color(255, 255, 255));
        sp.setText("1 Jugador");
        sp.setBorderPainted(false);
        sp.setContentAreaFilled(false);
        sp.setMaximumSize(new java.awt.Dimension(65, 30));
        sp.setMinimumSize(new java.awt.Dimension(65, 30));
        sp.setPreferredSize(new java.awt.Dimension(65, 30));
        sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spActionPerformed(evt);
            }
        });
        putologoP.add(sp);
        sp.setBounds(210, 30, 130, 110);

        stats.setBackground(Color.decode("#D42A91"));
        stats.setFont(new java.awt.Font("Bauhaus 93", 1, 18)); // NOI18N
        stats.setForeground(new java.awt.Color(255, 255, 255));
        stats.setText("Estadísticas");
        stats.setBorderPainted(false);
        stats.setContentAreaFilled(false);
        stats.setFocusPainted(false);
        stats.setMaximumSize(new java.awt.Dimension(65, 30));
        stats.setMinimumSize(new java.awt.Dimension(65, 30));
        stats.setPreferredSize(new java.awt.Dimension(65, 30));
        stats.setRolloverEnabled(false);
        stats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsActionPerformed(evt);
            }
        });
        putologoP.add(stats);
        stats.setBounds(360, 170, 140, 110);

        mp.setBackground(Color.decode("#F3D303"));
        mp.setFont(new java.awt.Font("Bauhaus 93", 1, 18)); // NOI18N
        mp.setForeground(new java.awt.Color(255, 255, 255));
        mp.setText("Multijugador");
        mp.setBorderPainted(false);
        mp.setContentAreaFilled(false);
        mp.setMaximumSize(new java.awt.Dimension(65, 30));
        mp.setMinimumSize(new java.awt.Dimension(65, 30));
        mp.setPreferredSize(new java.awt.Dimension(65, 30));
        mp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mpActionPerformed(evt);
            }
        });
        putologoP.add(mp);
        mp.setBounds(40, 170, 150, 100);

        stats1.setBackground(Color.decode("#7DC242"));
        stats1.setFont(new java.awt.Font("Bauhaus 93", 1, 18)); // NOI18N
        stats1.setForeground(new java.awt.Color(255, 255, 255));
        stats1.setText("Salir");
        stats1.setBorderPainted(false);
        stats1.setContentAreaFilled(false);
        stats1.setMaximumSize(new java.awt.Dimension(65, 30));
        stats1.setMinimumSize(new java.awt.Dimension(65, 30));
        stats1.setPreferredSize(new java.awt.Dimension(65, 30));
        stats1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stats1ActionPerformed(evt);
            }
        });
        putologoP.add(stats1);
        stats1.setBounds(300, 340, 130, 120);

        nj.setBackground(Color.decode("#F3DD0D"));
        nj.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        nj.setForeground(Color.decode("#00AEC9"));
        nj.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Elija Numero Jugadores", "2 Jugadores", "3 Jugadores", "4 Jugadores" }));
        nj.setMaximumSize(new java.awt.Dimension(65, 30));
        nj.setMinimumSize(new java.awt.Dimension(65, 30));
        nj.setPreferredSize(new java.awt.Dimension(65, 30));
        nj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                njActionPerformed(evt);
            }
        });
        putologoP.add(nj);
        nj.setBounds(40, 205, 180, 30);
        putologoP.add(decoL);
        decoL.setBounds(450, 630, 0, 0);
        putologoP.add(decoL2);
        decoL2.setBounds(20, 630, 0, 0);

        nombreL.setFont(new java.awt.Font("Bauhaus 93", 3, 60)); // NOI18N
        nombreL.setForeground(Color.decode("#2096D4"));
        nombreL.setText("Atríviate");
        putologoP.add(nombreL);
        nombreL.setBounds(160, 600, 250, 60);

        logoL.setBackground(Color.decode("#FFFFEB"));
        logoL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/logo.png"))); // NOI18N
        logoL.setFocusable(false);
        logoL.setMaximumSize(new java.awt.Dimension(550, 550));
        logoL.setMinimumSize(new java.awt.Dimension(550, 550));
        logoL.setOpaque(true);
        logoL.setPreferredSize(new java.awt.Dimension(550, 550));
        putologoP.add(logoL);
        logoL.setBounds(0, 0, 550, 550);

        logoP.add(putologoP);
        putologoP.setBounds(0, 0, 550, 700);

        getContentPane().add(logoP);
        logoP.setBounds(0, 0, 550, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

//Eventos de los botones
    private void njActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_njActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_njActionPerformed

    private void stats1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stats1ActionPerformed
        try {
            //resul.close();
            st.close();
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }//GEN-LAST:event_stats1ActionPerformed

    private void mpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mpActionPerformed
        // TODO add your handling code here:
        nj.setVisible(true);
        mp.setVisible(false);
    }//GEN-LAST:event_mpActionPerformed

    private void statsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsActionPerformed
        // TODO add your handling code here:
        try{
            Stats estadisticas = new Stats(st);
        }catch(Exception e){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, e);
        };
    }//GEN-LAST:event_statsActionPerformed

    
    //Un solo jugador
    private void spActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spActionPerformed
        // TODO add your handling code here:
        jugadores = new String[1];
        String nombre;
        
            //nombre = JOptionPane.showInputDialog("Introduzca su Nombre:");
            nombre = (String) JOptionPane.showInputDialog(null, "Introduzca su nombre", "Jugador 1", -1, jug[0], null, "Nuevo Jugador");
        
        if(nombre != null){
            jugadores[0] = nombre;
            try {
                st.execute("INSERT INTO jugador VALUES ('"+nombre+"', 0, 0, 0, 0, 0)");
            } catch (SQLException ex) {
                System.out.println("El usuario ya existe.");
            }
            try {
                cargarJugadores(jugadores);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                tab = new Tablero(jugs, st);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_spActionPerformed

    private void creditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditosActionPerformed
        // TODO add your handling code here:
        try{
            Creditos cred = new Creditos();
        }catch(IOException e){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_creditosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Menu().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton creditos;
    private javax.swing.JLabel decoL;
    private javax.swing.JLabel decoL2;
    private javax.swing.JLabel logoL;
    private javax.swing.JPanel logoP;
    private javax.swing.JButton mp;
    private javax.swing.JComboBox nj;
    private javax.swing.JLabel nombreL;
    private javax.swing.JPanel putologoP;
    private javax.swing.JButton sp;
    private javax.swing.JButton stats;
    private javax.swing.JButton stats1;
    // End of variables declaration//GEN-END:variables
}
