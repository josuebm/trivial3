package tarjetas;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Administrador
 */
public class Tarjeta extends javax.swing.JFrame{
    
    //Variables de clase
    private String pregunta;
    private String []respuestas;
    private Random rdm;
    private JEditorPane correcta;
    private String t;
    private Connection conexion;
    private Statement st;
    private ResultSet resul;
    private int p;
    private String tema;
    private boolean buena;


    /**
     * Creates new form TarjetaArte
     */
    public Tarjeta(int id, String tema, Statement st) throws InterruptedException, SQLException {   
        initComponents();
        //Inicializacion de variables
        
        //Id de la pregunta
        p = id;
        //Array con las respuestas
        respuestas = new String[4];
        //Tema de la tarjeta
        this.tema = tema;
        //Statement
        this.st = st;
        //Consultas para las preguntas y las respuestas
        this.queryPregunta(tema);   
        this.queryRespuestas(tema);
        System.out.println("Tarjeta creada.");
        //Random para mezclar las respuestas
        this.rdm = new Random();
        t = tema;
        //Para asignar el diseño a cada tarjeta segun su tema
        crearDiseño();
        this.setVisible(true);
        tiempoPB.setMaximum(15);
        tiempoPB.setStringPainted(true);
    }
    
    public Tarjeta(){
        
    }
    
    public String[] getResp(){
        return respuestas;
    }
    
    public JEditorPane getCorrecta(){
        return correcta;
    }
    
   //Consulta para obtener las respuestas de la tarjeta
   public void queryRespuestas(String tema) throws SQLException{
        resul = st.executeQuery("Select R1, R2, R3, R4 from "+tema+" where IdPregunta = "+p);
        while(resul.next()){
            respuestas[0] = resul.getString(1);
            respuestas[1] = resul.getString(2);
            respuestas[2] = resul.getString(3);
            respuestas[3] = resul.getString(4);
        }
    }
    
   //Consulta para obtener la pregunta de la tarjeta
    public void queryPregunta(String tema) throws SQLException{
        resul = st.executeQuery("Select Pregunta from "+tema+" where IdPregunta = "+p);
        while (resul.next()){
            pregunta = resul.getString(1);
        }
    }
    
    //Para asignar el diseño a cada tarjeta según su tema
    public boolean crearDiseño() throws InterruptedException{
        diseño(t.toLowerCase());
        
        //Para que no divida las palabras con saltos de linea
        preguntaEP.setEditorKit(new HTMLEditorKit());
        preguntaEP.setText(pregunta);
        generarRespuestas();
        return buena;
    }
    
    //Para que no se repita el numero random
    public boolean validarRdm(int a, ArrayList<Integer> rep){
 
        for(int i=0; i<rep.size();i++){
            if(a == rep.get(i))
                return false;
        }
        return true;
    }
    
    //Muestra las respuestas en orden aleatorio y saber cual es la correcta
    public void generarRespuestas() throws InterruptedException{
        int x;
        ArrayList <Integer>p1 = new ArrayList<Integer>();
        x = rdm.nextInt(4);
        for(int i=0; i<4; i++){
            while(!validarRdm(x, p1)){
                x = rdm.nextInt(4);
            }
            p1.add(x);
            switch(i){
                case 0:
                    respuesta1EP.setText(respuestas[x]);
                    if(x == 0)
                        correcta = respuesta1EP;
                    break;
                case 1:
                    respuesta2EP.setText(respuestas[x]);
                    if(x == 0)
                        correcta = respuesta2EP;
                    break;
                case 2:
                    respuesta3EP.setText(respuestas[x]);
                    if(x == 0)
                        correcta = respuesta3EP;
                    break;
                case 3:
                    respuesta4EP.setText(respuestas[x]);
                    if(x == 0)
                        correcta = respuesta4EP;
                    break;
            }
        }
        
        
    }
   //Metodos para obtener las respuestas de la tarjeta 
   public JEditorPane getR1(){
       return respuesta1EP;
   }
   
   public JEditorPane getR2(){
       return respuesta2EP;
   }
   
   public JEditorPane getR3(){
       return respuesta3EP;
   }
   
   public JEditorPane getR4(){
       return respuesta4EP;
   }
    
   //Muestra el borde en verde de la respuesta correcta
   public void mostrarCorrecta(){
        if(respuesta1EP.getName().equals(correcta.getName()))
            respuesta1SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#0BD85A")));
        
        else
            if(respuesta2EP.getName().equals(correcta.getName()))
                respuesta2SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#0BD85A")));
            else
                if(respuesta3EP.getName().equals(correcta.getName()))
                    respuesta3SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#0BD85A")));
                else
                        respuesta4SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#0BD85A")));
        repaint();
    }
    
   //Muestra el borde en rojo de la respuesta incorrecta
    public void mostrarIncorrecta(JEditorPane i){
        if(i.getName().equals("r1"))
            respuesta1SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#F2141C")));
        else
            if(i.getName().equals("r2"))
                respuesta2SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#F2141C")));
            else
                if(i.getName().equals("r3"))
                    respuesta3SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#F2141C")));
                else
                    respuesta4SP.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.decode("#F2141C")));
    }
    
    public JProgressBar getBarra(){
       return tiempoPB;
   }
    
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorP = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        categoriaL = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        preguntaEP = new javax.swing.JEditorPane();
        respuesta4SP = new javax.swing.JScrollPane();
        respuesta4EP = new javax.swing.JEditorPane();
        respuesta3SP = new javax.swing.JScrollPane();
        respuesta3EP = new javax.swing.JEditorPane();
        respuesta2SP = new javax.swing.JScrollPane();
        respuesta2EP = new javax.swing.JEditorPane();
        respuesta1SP = new javax.swing.JScrollPane();
        respuesta1EP = new javax.swing.JEditorPane();
        iconoL = new javax.swing.JLabel();
        icono2L = new javax.swing.JLabel();
        tiempoPB = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(395, 370));
        setUndecorated(true);
        setResizable(false);

        colorP.setBackground(new java.awt.Color(204, 204, 204));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        categoriaL.setFont(new java.awt.Font("Pristina", 1, 36)); // NOI18N
        categoriaL.setForeground(new java.awt.Color(204, 204, 204));
        categoriaL.setText("Categoría");

        preguntaEP.setEditable(false);
        jScrollPane2.setViewportView(preguntaEP);

        respuesta4EP.setEditable(false);
        respuesta4EP.setName("r4"); // NOI18N
        respuesta4SP.setViewportView(respuesta4EP);

        respuesta3EP.setEditable(false);
        respuesta3EP.setName("r3"); // NOI18N
        respuesta3SP.setViewportView(respuesta3EP);

        respuesta2EP.setEditable(false);
        respuesta2EP.setName("r2"); // NOI18N
        respuesta2SP.setViewportView(respuesta2EP);

        respuesta1EP.setEditable(false);
        respuesta1EP.setName("r1"); // NOI18N
        respuesta1SP.setViewportView(respuesta1EP);

        tiempoPB.setToolTipText("");
        tiempoPB.setValue(0);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(iconoL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(categoriaL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(icono2L))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(respuesta4SP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(respuesta3SP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(respuesta1SP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(respuesta2SP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(tiempoPB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iconoL)
                    .addComponent(categoriaL)
                    .addComponent(icono2L))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(respuesta1SP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(respuesta2SP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(respuesta3SP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(respuesta4SP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tiempoPB, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout colorPLayout = new javax.swing.GroupLayout(colorP);
        colorP.setLayout(colorPLayout);
        colorPLayout.setHorizontalGroup(
            colorPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        colorPLayout.setVerticalGroup(
            colorPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Genera el diseño de las tarjetas
    public void diseño(String tema){
       //barra();
        switch(tema){
            case "historia":{
                colorP.setBackground(Color.decode("#CEC529"));
                categoriaL.setForeground(Color.decode("#CEC529"));
                categoriaL.setText("Historia");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/historia.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "literatura":{
                colorP.setBackground(Color.decode("#A96000"));
                categoriaL.setForeground(Color.decode("#A96000"));
                categoriaL.setText("Literatura");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/literatura.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "arte":{
                colorP.setBackground(Color.decode("#2493F3"));
                categoriaL.setForeground(Color.decode("#2493F3"));
                categoriaL.setText("Arte");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/arte.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "geografia":{
                colorP.setBackground(Color.decode("#E91D20"));
                categoriaL.setForeground(Color.decode("#E91D20"));
                categoriaL.setText("Geografía");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/geografia.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "ciencia":{
                colorP.setBackground(Color.decode("#1D8B0C"));
                categoriaL.setForeground(Color.decode("#1D8B0C"));
                categoriaL.setText("Ciencias");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ciencias.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "friki":{
                colorP.setBackground(Color.decode("#1CCEE9"));
                categoriaL.setForeground(Color.decode("#1CCEE9"));
                categoriaL.setText("Friki");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/friki.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "cine":{
                colorP.setBackground(Color.decode("#FF3BC2"));
                categoriaL.setForeground(Color.decode("#FF3BC2"));
                categoriaL.setText("Cine");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/cine.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
            case "deporte":{
                colorP.setBackground(Color.decode("#F86A1E"));
                categoriaL.setForeground(Color.decode("#F86A1E"));
                categoriaL.setText("Deporte");
                ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/deporte.png"));
                ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                iconoL.setIcon(icoE);
                icono2L.setIcon(icoE);
            }break;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel categoriaL;
    private javax.swing.JPanel colorP;
    private javax.swing.JLabel icono2L;
    private javax.swing.JLabel iconoL;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JEditorPane preguntaEP;
    private javax.swing.JEditorPane respuesta1EP;
    private javax.swing.JScrollPane respuesta1SP;
    private javax.swing.JEditorPane respuesta2EP;
    private javax.swing.JScrollPane respuesta2SP;
    private javax.swing.JEditorPane respuesta3EP;
    private javax.swing.JScrollPane respuesta3SP;
    private javax.swing.JEditorPane respuesta4EP;
    private javax.swing.JScrollPane respuesta4SP;
    private javax.swing.JProgressBar tiempoPB;
    // End of variables declaration//GEN-END:variables
   
    }

