

package tablero;

import dado.Dado;
//import static dado.Dado.dadoL;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import tarjetas.*;
import jugador.*;
/**
 *
 * @author Josué
 */
public class Tablero extends javax.swing.JFrame implements Runnable{

    ArrayList <JButton> quesos;
    ArrayList <JButton> historia;
    ArrayList <JButton> literatura;
    ArrayList <JButton> arte;
    ArrayList <JButton> geografia;
    ArrayList <JButton> ciencias;
    ArrayList <JButton> friki;
    ArrayList <JButton> cine;
    ArrayList <JButton> deporte;
    ArrayList <JButton> dados;
    ArrayList <JButton> botones;
    ArrayList<ArrayList<Integer>> repetidas;
    ArrayList<JButton> posibles;
    ArrayList<Integer> coords;
    ArrayList <Boolean> quesitos1;
    ArrayList <Boolean> quesitos2;
    ArrayList <Boolean> quesitos3;
    ArrayList <Boolean> quesitos4;
    
    private boolean esQueso;
    private boolean paradaDado;
    private boolean tirarDado;
    private boolean[][] quesitos;
    private int indice;
    private int tirada1;
    private int tirada2;
    private int turno;
    private int contadorQuesitosGanados;
    private int[] posicion;
    private JButton[] anterior;
    private JButton[] actual;
    private Border[] bordeAnt;
    private Jugador[] jug;
    //Rosca[] roscas;
    private JPanel[] panelRosca;
    private Random rdm;
    private Color a;
    private Color b;
    private Dado dado;
    private Thread hilo;
    private Tarjeta pregunta;
    private Statement st;
    private Connection conexion;
    private MouseListener resp;
    //public static int tirada;
    private JLabel[] iconos;
    ImageIcon icono;

    //Constructor al que le pasamos el Statement con la conexion a la BD hecha y un array con los nombres de los jugadores.
    public Tablero(Jugador[] jugadores, Statement st) throws ClassNotFoundException, SQLException {
        //Contador para la cantidad de quesitos ganados
        contadorQuesitosGanados = 0;
        //Inicializacion de variables
        initComponents();
        //Para que no se repitan las preguntas
        repetidas = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<8; i++)
            repetidas.add(new ArrayList<Integer>());
        
        //Establece el turno para el primer jugador
        turno = 0;
        //Guarda el statement
        this.st = st;
        //Guarda el array con los jugadores
        jug = jugadores;
        //Una matriz con los quesitos de cada jugador
        quesitos = new boolean[4][8];
        //Para mover la "ficha" del jugador
        anterior = new JButton[jugadores.length];
        actual = new JButton[jugadores.length];
        bordeAnt = new Border[jugadores.length];
        //Para saber si el boton pulsado es un quesito
        esQueso = false;
        //Guarda la posicion actual de cada jugador
        posicion = new int[jugadores.length];
        //Guarda los indices de los botones en el array de botones
        coords = new ArrayList<Integer>();
        //Guarda los posibles movimientos al tirar un dado
        posibles = new ArrayList<JButton>();
        rdm = new Random();
        
        //Booleano para manejar el inicio y parada del dado
        paradaDado = false;
        //Booleano para activar o no la tirada de dado
        tirarDado = true;
        //Array de roscas de quesitos, 1 por jugador
        //roscas = new Rosca[jugadores.length];
        //Array de paneles para colocar las roscas
        panelRosca = new JPanel[jugadores.length];
        
        //Escuchador para las respuestas de las tarjetas.
        resp = new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    respuestaSeleccion(e);
                } catch (SQLException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        };
        
        
        this.setVisible(true);
        
        
        //Creamos y personalizamos los iconos
        rellenarArrayList();
        fondoEicono();
        //dado = new Dado();
        //dado.setVisible(true);
        //this.setLayout(new BorderLayout());
        //this.add(dado, BorderLayout.CENTER);
        botones = new ArrayList<JButton>();
        
        botones.add(b1); botones.add(b2); botones.add(b3); botones.add(b4); botones.add(b5);
        botones.add(b6); botones.add(b7); botones.add(b8); botones.add(b9); botones.add(b10);
        botones.add(b11); botones.add(b12); botones.add(b13); botones.add(b14); botones.add(b15);
        botones.add(b16); botones.add(b17); botones.add(b18); botones.add(b19); botones.add(b20);
        botones.add(b21); botones.add(b22); botones.add(b23); botones.add(b24); botones.add(b25);
        botones.add(b26); botones.add(b27); botones.add(b28); botones.add(b29); botones.add(b30);
        botones.add(b31); botones.add(b32); botones.add(b33); botones.add(b34); botones.add(b35);
        botones.add(b36); botones.add(b37); botones.add(b38); botones.add(b39); botones.add(b40);
        botones.add(b41); botones.add(b42); botones.add(b43); botones.add(b44); botones.add(b45);
        botones.add(b46); botones.add(b47); botones.add(b48);
        
        
        //La posicion inicial de todos los jugadores es la 0
        for(int i=0; i<posicion.length; i++)
            posicion[i] = 0;

       
        //Escuchador para los botones
        ActionListener b = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    seleccionCasilla(e);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        
        
        //Provisional!! para cambiar los bordes de las "fichas" de los jugadores
        for(int i=0; i<jug.length; i++){
            actual[i] = b1;
            bordeAnt[i] = b1.getBorder();
        }
        
        //Asignacion del escuchador a los botones
        for(int i=0; i<botones.size(); i++)
            botones.get(i).addActionListener(b);
        
       for(int i=0; i<jug.length; i++){
           for(int j=0; j<8; j++){
               quesitos[i][j] = false;
           }
       }
       
       
        dado = new Dado();
        dado.getDado().addMouseListener(resp);
        dadoP.add(dado.dadoL);
        
        
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        fondoP.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        int ancho = (this.getSize().width - 920)/ 2;
        int alto = (this.getSize().height - 578)/ 2;
        tableroSupP.setLocation(ancho, alto);
        
        ancho = ((this.getSize().width - 685)/ 2)+ 71;
        ico1L.setLocation(ancho, alto);
        ico1L.setVisible(false);
        ico2L.setLocation(ancho+25, alto);
        ico2L.setVisible(false);
        ico3L.setLocation(ancho, alto+25);
        ico3L.setVisible(false);
        ico4L.setLocation(ancho+25, alto+25);
        ico4L.setVisible(false);
        icofijo1L.setVisible(false);
        icofijo2L.setVisible(false);
        icofijo3L.setVisible(false);
        icofijo4L.setVisible(false);
        
        iconos = new JLabel[4];
        iconos[0] = ico1L;
        iconos[1] = ico2L;
        iconos[2] = ico3L;
        iconos[3] = ico3L;
        
        ImageIcon ico=new ImageIcon(getClass().getResource("/menu/logo.png"));
        icono = new ImageIcon(ico.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        
    }
    
   /* public void jugadores(){
        for(int i=0; i<jug.length; i++)
            
    }*/
    
 //Provisional! Para mostrar los jugadores en las esquinas e inicializar las roscas   
  public void paint(Graphics g){
      super.paint(g);
      int x, y;
        if(jug.length >= 1){
            jug1.setText(jug[0].getNombre());
            //Añadir la rosca
            //roscas[0] = new Rosca();
            panelRosca[0] = rosca1;
            ico1L.setVisible(true);
            icofijo1L.setVisible(true);
            /*
            System.out.println("Posición del boton1 en pantalla: " + b1.getLocationOnScreen());
            System.out.println("Posición del boton1: " + b1.getLocation());
            System.out.println("Posición del icono en pantalla: " + ico1L.getLocationOnScreen());
            System.out.println("Posición del icono: " + ico1L.getLocation());
            */
            x = (this.getSize().width - 920)/ 2;
            y = (this.getSize().height - 578)/ 2;
            g.setColor(Color.decode("#565656"));
            g.fillArc(x, y, 100, 100, 360, 360);
            g.setColor(Color.BLACK);
            g.drawArc(x, y, 100, 100, 360, 360);
            g.fillArc(x, y, 100, 100, 0, 4);
            g.fillArc(x, y, 100, 100, 45, 4);
            g.fillArc(x, y, 100, 100, 90, 4);
            g.fillArc(x, y, 100, 100, 135, 4);
            g.fillArc(x, y, 100, 100, 180, 4);
            g.fillArc(x, y, 100, 100, 225, 4);
            g.fillArc(x, y, 100, 100, 270, 4);
            g.fillArc(x, y, 100, 100, 315, 4);
            for(int i=0; i<quesitos[0].length; i++){
                switch(i){
                    case 0:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#1D8B0C"));
                            g.fillArc(x, y, 100, 100, 184, 42); 
                        }
                    }break;
                    case 1:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#F86A1E"));
                            g.fillArc(x, y, 100, 100, 4, 42);
                        }
                    }break;
                    case 2:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#2493F3"));
                            g.fillArc(x, y, 100, 100, 94, 42);
                        }
                    }break;
                    case 3:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#1CCEE9"));
                            g.fillArc(x, y, 100, 100, 229, 42);
                        }
                    }break;
                    case 4:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#A96000"));
                            g.fillArc(x, y, 100, 100, 49, 42);
                        }
                    }break;
                    case 5:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#E91D20"));
                            g.fillArc(x, y, 100, 100, 139, 42);
                        }
                    }break;
                    case 6:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#CEC529"));
                            g.fillArc(x, y, 100, 100, 319, 42);
                        }
                    }break;
                    case 7:{
                        if(quesitos[0][i]==true){
                            g.setColor(Color.decode("#FF3BC2"));
                            g.fillArc(x, y, 100, 100, 274, 42);
                        }
                    }break;
                }
            }
            
        }
        if(jug.length >= 2){
            jug2.setText(jug[1].getNombre());
            //roscas[1] = new Rosca();
            panelRosca[1] = rosca2;
            ico2L.setVisible(true);
            icofijo2L.setVisible(true);
            x = (this.getSize().width + 920)/ 2 -100;
            y = (this.getSize().height - 578)/ 2;
            g.setColor(Color.decode("#89FFFF"));
            g.fillArc(x, y, 100, 100, 360, 360);
            g.setColor(Color.CYAN);
            g.drawArc(x, y, 100, 100, 360, 360);
            g.fillArc(x, y, 100, 100, 0, 4);
            g.fillArc(x, y, 100, 100, 45, 4);
            g.fillArc(x, y, 100, 100, 90, 4);
            g.fillArc(x, y, 100, 100, 135, 4);
            g.fillArc(x, y, 100, 100, 180, 4);
            g.fillArc(x, y, 100, 100, 225, 4);
            g.fillArc(x, y, 100, 100, 270, 4);
            g.fillArc(x, y, 100, 100, 315, 4);
            for(int i=0; i<quesitos[1].length; i++){
                switch(i){
                    case 0:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#1D8B0C"));
                            g.fillArc(x, y, 100, 100, 184, 42); 
                        }
                    }break;
                    case 1:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#F86A1E"));
                            g.fillArc(x, y, 100, 100, 4, 42);
                        }
                    }break;
                    case 2:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#2493F3"));
                            g.fillArc(x, y, 100, 100, 94, 42);
                        }
                    }break;
                    case 3:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#1CCEE9"));
                            g.fillArc(x, y, 100, 100, 229, 42);
                        }
                    }break;
                    case 4:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#A96000"));
                            g.fillArc(x, y, 100, 100, 49, 42);
                        }
                    }break;
                    case 5:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#E91D20"));
                            g.fillArc(x, y, 100, 100, 139, 42);
                        }
                    }break;
                    case 6:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#CEC529"));
                            g.fillArc(x, y, 100, 100, 319, 42);
                        }
                    }break;
                    case 7:{
                        if(quesitos[1][i]==true){
                            g.setColor(Color.decode("#FF3BC2"));
                            g.fillArc(x, y, 100, 100, 274, 42);
                        }
                    }break;
                }
            }
        }
        if(jug.length >= 3){
            jug3.setText(jug[2].getNombre());
            //roscas[2] = new Rosca();
            panelRosca[2] = rosca3;
            ico3L.setVisible(true);
            icofijo3L.setVisible(true);
            x = (this.getSize().width + 920)/ 2 -100;
            y = (this.getSize().height + 578)/ 2 -100;
            g.setColor(Color.decode("#DDDDDD"));
            g.fillArc(x, y, 100, 100, 360, 360);
            g.setColor(Color.LIGHT_GRAY);
            g.drawArc(x, y, 100, 100, 360, 360);
            g.fillArc(x, y, 100, 100, 0, 4);
            g.fillArc(x, y, 100, 100, 45, 4);
            g.fillArc(x, y, 100, 100, 90, 4);
            g.fillArc(x, y, 100, 100, 135, 4);
            g.fillArc(x, y, 100, 100, 180, 4);
            g.fillArc(x, y, 100, 100, 225, 4);
            g.fillArc(x, y, 100, 100, 270, 4);
            g.fillArc(x, y, 100, 100, 315, 4);
            for(int i=0; i<quesitos[2].length; i++){
                switch(i){
                    case 0:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#1D8B0C"));
                            g.fillArc(x, y, 100, 100, 184, 42); 
                        }
                    }break;
                    case 1:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#F86A1E"));
                            g.fillArc(x, y, 100, 100, 4, 42);
                        }
                    }break;
                    case 2:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#2493F3"));
                            g.fillArc(x, y, 100, 100, 94, 42);
                        }
                    }break;
                    case 3:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#1CCEE9"));
                            g.fillArc(x, y, 100, 100, 229, 42);
                        }
                    }break;
                    case 4:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#A96000"));
                            g.fillArc(x, y, 100, 100, 49, 42);
                        }
                    }break;
                    case 5:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#E91D20"));
                            g.fillArc(x, y, 100, 100, 139, 42);
                        }
                    }break;
                    case 6:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#CEC529"));
                            g.fillArc(x, y, 100, 100, 319, 42);
                        }
                    }break;
                    case 7:{
                        if(quesitos[2][i]==true){
                            g.setColor(Color.decode("#FF3BC2"));
                            g.fillArc(x, y, 100, 100, 274, 42);
                        }
                    }break;
                }
            }
        }
        if(jug.length == 4){
            jug4.setText(jug[3].getNombre()); 
            //roscas[3] = new Rosca();
            panelRosca[3] = rosca4;
            ico4L.setVisible(true);
            icofijo4L.setVisible(true);
            x = (this.getSize().width - 920)/ 2;
            y = (this.getSize().height + 578)/ 2 -100;
            g.setColor(Color.decode("#FF71FF"));
            g.fillArc(x, y, 100, 100, 360, 360);
            g.setColor(Color.MAGENTA);
            g.drawArc(x, y, 100, 100, 360, 360);
            g.fillArc(x, y, 100, 100, 0, 4);
            g.fillArc(x, y, 100, 100, 45, 4);
            g.fillArc(x, y, 100, 100, 90, 4);
            g.fillArc(x, y, 100, 100, 135, 4);
            g.fillArc(x, y, 100, 100, 180, 4);
            g.fillArc(x, y, 100, 100, 225, 4);
            g.fillArc(x, y, 100, 100, 270, 4);
            g.fillArc(x, y, 100, 100, 315, 4);
            for(int i=0; i<quesitos[3].length; i++){
                switch(i){
                    case 0:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#1D8B0C"));
                            g.fillArc(x, y, 100, 100, 184, 42); 
                        }
                    }break;
                    case 1:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#F86A1E"));
                            g.fillArc(x, y, 100, 100, 4, 42);
                        }
                    }break;
                    case 2:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#2493F3"));
                            g.fillArc(x, y, 100, 100, 94, 42);
                        }
                    }break;
                    case 3:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#1CCEE9"));
                            g.fillArc(x, y, 100, 100, 229, 42);
                        }
                    }break;
                    case 4:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#A96000"));
                            g.fillArc(x, y, 100, 100, 49, 42);
                        }
                    }break;
                    case 5:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#E91D20"));
                            g.fillArc(x, y, 100, 100, 139, 42);
                        }
                    }break;
                    case 6:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#CEC529"));
                            g.fillArc(x, y, 100, 100, 319, 42);
                        }
                    }break;
                    case 7:{
                        if(quesitos[3][i]==true){
                            g.setColor(Color.decode("#FF3BC2"));
                            g.fillArc(x, y, 100, 100, 274, 42);
                        }
                    }break;
                }
            }
        }
  }
    
  //Mensaje con el jugador del que es el turno
  public void turnos(){
      //JOptionPane.showMessageDialog(null, "Turno de "+jug[turno].getNombre()+".");
      JOptionPane.showMessageDialog(null, "Turno de "+jug[turno].getNombre(),"Cambio de jugador", -1, icono);
  }
  
  //Ponemos a true el quesito ganado, para que se pinte en la rosca y para comprobar si es el ganador
  public void quesitoGanado(JButton b) throws SQLException{
      switch(b.getName().toLowerCase()){
          case "ciencia":
              quesitos[turno][0] = true;
              break;
          case "deporte":
              quesitos[turno][1] = true;
              break;
          case "arte":
              quesitos[turno][2] = true;
              break;
          case "friki":
              quesitos[turno][3] = true;
              break;
          case "literatura":
              quesitos[turno][4] = true;
              break;
          case "geografia":
              quesitos[turno][5] = true;
              break;
          case "historia":
              quesitos[turno][6] = true;
              break;
          case "cine":
              quesitos[turno][7] = true;
              break;
      }
      repaint();
      //Para actualizar la rosca de quesitos al ganar un quesito (NO USADO NO CREAMOS EL OBJETO ROSCAS)
      /*roscas[turno].cargarQuesitos(quesitos[turno]);
      
      for(int i=0; i<jug.length; i++)
          System.out.println(roscas[i].toString());
      */
      //Para comprobar si hay ganador
      int juGana = -1;
      for(int i=0; i<jug.length; i++){
          for(int j = 0; j<8; j++){
              if(quesitos[i][j] == true){
                  contadorQuesitosGanados++;
                  if(contadorQuesitosGanados == 8){
                      juGana = i;
                  }
              } 
          }
          contadorQuesitosGanados = 0; 
      }
      
      //Mensaje de ganador
          if(juGana != -1){
              //JOptionPane.showMessageDialog(null, "El ganador es "+jug[juGana].getNombre()+".");
              JOptionPane.showMessageDialog(null, "El ganador es "+jug[juGana].getNombre()+".","¡Enhorabuena!", -1, icono);
              jug[turno].setPG(jug[turno].getPG()+1);
              jug[turno].setPJ(jug[turno].getPJ()+1);
              st.execute("UPDATE jugador SET partidas_jugadas = "+jug[turno].getPJ()+", partidas_ganadas = "+jug[turno].getPG()+" WHERE nombre_jugador = '"+jug[turno].getNombre()+"'");
              for(int i=0; i<jug.length; i++)
                  if(i != juGana){
                  jug[i].setPJ(jug[i].getPJ()+1);
                  st.execute("UPDATE jugador SET partidas_jugadas = "+jug[i].getPJ()+" WHERE nombre_jugador = '"+jug[i].getNombre()+"'"); 
                  }
              this.dispose();      
          }
  }
  
  
  //Metodo para, si el boton pulsado es un quesito, llamar al metodo que pone a true el boton
  public void añadirQuesito() throws SQLException{
     if(actual[turno] == b1)
          quesitoGanado(b1);
      else
          if(actual[turno] == b7)
              quesitoGanado(b7);
          else
              if(actual[turno] == b13)
                quesitoGanado(b13);
              else
                  if(actual[turno] == b19)
                      quesitoGanado(b19);
                  else
                      if(actual[turno] == b25)
                          quesitoGanado(b25);
                      else
                          if(actual[turno] == b31)
                              quesitoGanado(b31);
                          else
                              if(actual[turno] == b37)
                                  quesitoGanado(b37);
                              else
                                  if(actual[turno] == b43)
                                      quesitoGanado(b43);
     repaint();
  } 
  
  //Para ver si la respuesta es correcta y cambiar el turno o no, y para tirar y parar el dado
  public void respuestaSeleccion(MouseEvent e) throws SQLException{
        //Si es el dado
        if(e.getSource().getClass().getSimpleName().equals("JLabel")){
            JLabel b = (JLabel)e.getSource();
            if(tirarDado){
                if(!paradaDado){
                    dado.girarDado();
                    paradaDado = true;
                }else{
                    dado.girarDado();
                    tiradaDados(dado.valorDado());
                    paradaDado = false;
                    tirarDado = false;
                }
            }
        //Si no es el dado es una respuesta
        }else{
            JEditorPane b = (JEditorPane)e.getSource();
            indice = 200;
            tirarDado = true;
            if(b.getName().toLowerCase().equals(pregunta.getCorrecta().getName())){
                jug[turno].setPA(jug[turno].getPA()+1);
                st.execute("UPDATE jugador SET preguntas_acertadas = "+jug[turno].getPA()+" WHERE nombre_jugador = '"+jug[turno].getNombre()+"'");

                pregunta.mostrarCorrecta();         
                JOptionPane.showMessageDialog(null, "¡Respuesta correcta!","", -1, icono);
                if(esQueso)
                    añadirQuesito();
                else
                    if(jug.length > 1)
                    turnos();
                pregunta.dispose();
            //Respuesta incorrecta
            }else{
                pregunta.mostrarCorrecta();
                pregunta.mostrarIncorrecta(b);
                //JOptionPane.showMessageDialog(null,"Respuesta Incorrecta!");
                JOptionPane.showMessageDialog(null, "Respuesta incorrecta","", -1, icono);
                jug[turno].setPF(jug[turno].getPF()+1);
                st.execute("UPDATE jugador SET preguntas_falladas = "+jug[turno].getPF()+" WHERE nombre_jugador = '"+jug[turno].getNombre()+"'");
                turno++;

                //Para rotar los jugadores, si hemos llegado al total, volvemos a empezar
                if(turno == jug.length)
                    turno = 0;
                if(jug.length > 1)
                turnos();
                pregunta.dispose();
            }
        }
    }

  
  //Codifica los temas en numeros
  public int tema(String nombre){
      switch(nombre){
          case "arte":
              return 0;
          case "deporte":
              return 1;
          case "ciencia":
              return 2;
          case "friki":
              return 3;
          case "literatura":
              return 4;
          case "geografia":
              return 5;
          case "historia":
              return 6;
          case "cine":
              return 7;
          default:
              return -1;
      } 
  }
  //Muestra las casillas a las que se puede desplazar cada jugador
    public void seleccionCasilla(ActionEvent e) throws ClassNotFoundException, SQLException, InterruptedException{
        JButton p = (JButton)e.getSource();
        
        //Para ver si la casilla seleccionada es un quesito
        for(int i=0; i<quesos.size(); i++)
                if(quesos.get(i) == p)
                    esQueso = true;
        
        //Para ver si la casilla pulsada (boton) esta entre las posibles según la tirada
        for(int i=0; i<posibles.size(); i++){
            if(p == posibles.get(i)){
                botones.get(tirada1).setBackground(a);
                botones.get(tirada2).setBackground(b);
                //Cambia los bordes, cada jugador tiene un borde de un color.
                //actual[turno].setBorder(bordeAnt[turno]);
                //ico1L.setLocation(actual[turno].getLocationOnScreen().x-3, actual[turno].getLocationOnScreen().y-11);
                posicionarIcono(iconos[turno], p);
                //System.out.println("Botón actual: " + actual[turno].toString());
                //System.out.println("Posición del icono al moverse: " + ico1L.getLocation());
                anterior[turno] = actual[turno];
                actual[turno] = p;
                bordeAnt[turno] = p.getBorder();
                
            //Para poner los bordes segun el jugador
            switch (turno){
            case 0:
                //Border bored = BorderFactory.createMatteBorder(10, 5, 10, 5, Color.BLACK);
                //p.setBorder(bored);
                //ico1L.setLocation(p.getLocationOnScreen().x-3, p.getLocationOnScreen().y-11);
                posicionarIcono(ico1L, p);
                break;
            case 1:
                //Border bored1 = BorderFactory.createMatteBorder(10, 5, 10, 5, Color.CYAN);
                //p.setBorder(bored1);
                //ico1L.setLocation(p.getLocationOnScreen().x-3, p.getLocationOnScreen().y-11);
                posicionarIcono(ico2L, p);
                break;
            case 2:
                //Border bored2 = BorderFactory.createMatteBorder(10, 5, 10, 5, Color.LIGHT_GRAY);
                //p.setBorder(bored2);
                //ico1L.setLocation(p.getLocationOnScreen().x-3, p.getLocationOnScreen().y-11);
                posicionarIcono(ico3L, p);
                break;
            case 3:
                //Border bored3 = BorderFactory.createMatteBorder(10, 5, 10, 5, Color.MAGENTA);
                //p.setBorder(bored3);
                //ico1L.setLocation(p.getLocationOnScreen().x-3, p.getLocationOnScreen().y-11);
                posicionarIcono(ico4L, p);
                break;
        }
        }
        }
        
        //Codigo para que no se repitan las preguntas hasta no llegar al maximo.
        boolean seguir = true;
        int id = 0;
        //Codifica los temas en enteros, siendo -1 el dado
        int tema = tema(p.getName().toLowerCase());
        if(tema != -1){
            if(repetidas.get(tema).size() == 25)
                repetidas.get(tema).clear();
            do{
                 id = rdm.nextInt(25)+1;
                 System.out.println("ID: "+id);
            }while(!repetidas(id, tema));
        }
        
        //Para ver si es una casilla tematica o una de dados
        //DADOS
        //Recorremos las posibles casillas
        for(int z=0; z<posibles.size(); z++){
                //Si la casilla pulsada esta entre las posibles
            if(p == posibles.get(z))
                if(p.getName().toLowerCase().equals("dados")){
                    //Cambiamos la posicion del jugador a la casilla seleccionada
                    posicion[turno] = coords.get(z);
                    tirarDado = true;

                }else{
                    //TEMATICA
                    //Cambiamos la posicion del jugador a la casilla seleccionada
                    posicion[turno] = coords.get(z);

                    //Generamos la pregunta con el tema de la casilla
                    pregunta = new Tarjeta(id, p.getName().toLowerCase(), st);

                    hilo = new Thread(this);
                    hilo.start();
                    //Escuchador para ver cual es la respuesta pulsada
                    pregunta.getR1().addMouseListener(resp);
                    pregunta.getR2().addMouseListener(resp);
                    pregunta.getR3().addMouseListener(resp);
                    pregunta.getR4().addMouseListener(resp);
                }
        }
        
    }
    
    public void posicionarIcono(JLabel icono, JButton boton){
        String nombre = icono.getName();
        switch(nombre){
            case "ico1":{
                ico1L.setLocation(boton.getLocationOnScreen().x-3, boton.getLocationOnScreen().y-11);
            }break;
            case "ico2":{
                ico2L.setLocation(boton.getLocationOnScreen().x-3+25, boton.getLocationOnScreen().y-11);
            }break;
            case "ico3":{
                ico3L.setLocation(boton.getLocationOnScreen().x-3, boton.getLocationOnScreen().y-11+25);
            }break;
            case "ico4":{
                ico4L.setLocation(boton.getLocationOnScreen().x-3+25, boton.getLocationOnScreen().y-11+25);
            }
        }
    }
    
    //Metodo para que no se repitan las preguntas
     public boolean repetidas(int id, int tema){
        for(int i=0; i<repetidas.get(tema).size();i++){
            System.out.println("REPETIDAS de:"+tema+" "+repetidas.get(tema).get(i));
            if(repetidas.get(tema).get(i) == id)
                return false;
        }
        repetidas.get(tema).add(id);
        return true;
    }

    //Metodo para calcular a las casillas que nos podemos mover segun la tirada
    public void tiradaDados(int dado){
        posibles.clear();
        coords.clear();
        //tDados.setEnabled(false);
        System.out.println("Dado en Tirada: "+dado);

        //Tiramos los dados y calculamos las posibles casillas a las q moverse
        tirada1 = posicion[turno] + dado;
        tirada2 = posicion[turno] - dado;
        
        System.out.println("Posicion: "+posicion);
        System.out.println("T1a: "+tirada1);
        System.out.println("T2a: "+tirada2);
        
        //Para simular un tablero circular
        //Si esta en la ultima posicion pasa a la primera
        if(tirada1 == 48)
            tirada1 = 0;
        
        //Si se pasa de la ultima posicion pasa a las siguientes
        if(tirada1 > 48)
            tirada1 = tirada1 - 48;
        
        //Si es menor que la primera posicion pasa a las anteriores
        if(tirada2 < 0)
            tirada2 = 48 + tirada2;
        
        //dad.setText(String.valueOf(dado));
        
        //Añadimos al array de posibles los botones asociados a la tirada de dados
        posibles.add(botones.get(tirada1));
        posibles.add(botones.get(tirada2));
        
        //Aññadimos las tiradas a un vector que nos indicara el indice de cada boton
        coords.add(tirada1);
        coords.add(tirada2);
        
        //Provisional! para cambiar el fondo de las casillas posibles a gris
        a = botones.get(tirada1).getBackground();
        botones.get(tirada1).setBackground(Color.DARK_GRAY);
        repaint();
        b = botones.get(tirada2).getBackground();
        botones.get(tirada2).setBackground(Color.DARK_GRAY); 
        repaint();
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fondoP = new javax.swing.JPanel();
        ico1L = new javax.swing.JLabel();
        ico2L = new javax.swing.JLabel();
        ico3L = new javax.swing.JLabel();
        ico4L = new javax.swing.JLabel();
        tableroSupP = new javax.swing.JPanel();
        SALIR = new javax.swing.JButton();
        tableroP = new javax.swing.JPanel();
        dadoP = new javax.swing.JPanel();
        b2 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b12 = new javax.swing.JButton();
        b11 = new javax.swing.JButton();
        b10 = new javax.swing.JButton();
        b9 = new javax.swing.JButton();
        b13 = new javax.swing.JButton();
        b14 = new javax.swing.JButton();
        b48 = new javax.swing.JButton();
        b46 = new javax.swing.JButton();
        b15 = new javax.swing.JButton();
        b47 = new javax.swing.JButton();
        b16 = new javax.swing.JButton();
        b44 = new javax.swing.JButton();
        b42 = new javax.swing.JButton();
        b19 = new javax.swing.JButton();
        b45 = new javax.swing.JButton();
        b43 = new javax.swing.JButton();
        b20 = new javax.swing.JButton();
        b17 = new javax.swing.JButton();
        b18 = new javax.swing.JButton();
        b41 = new javax.swing.JButton();
        b23 = new javax.swing.JButton();
        b27 = new javax.swing.JButton();
        b35 = new javax.swing.JButton();
        b30 = new javax.swing.JButton();
        b34 = new javax.swing.JButton();
        b22 = new javax.swing.JButton();
        b36 = new javax.swing.JButton();
        b38 = new javax.swing.JButton();
        b31 = new javax.swing.JButton();
        b32 = new javax.swing.JButton();
        b28 = new javax.swing.JButton();
        b29 = new javax.swing.JButton();
        b25 = new javax.swing.JButton();
        b39 = new javax.swing.JButton();
        b21 = new javax.swing.JButton();
        jButton156 = new javax.swing.JButton();
        b40 = new javax.swing.JButton();
        b33 = new javax.swing.JButton();
        b26 = new javax.swing.JButton();
        b37 = new javax.swing.JButton();
        b24 = new javax.swing.JButton();
        logoL = new javax.swing.JLabel();
        rosca1 = new javax.swing.JPanel();
        rosca2 = new javax.swing.JPanel();
        rosca3 = new javax.swing.JPanel();
        rosca4 = new javax.swing.JPanel();
        jug1 = new javax.swing.JLabel();
        jug2 = new javax.swing.JLabel();
        jug3 = new javax.swing.JLabel();
        jug4 = new javax.swing.JLabel();
        icofijo1L = new javax.swing.JLabel();
        icofijo2L = new javax.swing.JLabel();
        icofijo3L = new javax.swing.JLabel();
        icofijo4L = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.decode("#FFFFEB"));
        setMinimumSize(new java.awt.Dimension(926, 578));
        setUndecorated(true);
        getContentPane().setLayout(null);

        fondoP.setBackground(Color.decode("#FFFFEB"));
        fondoP.setMaximumSize(new java.awt.Dimension(920, 578));
        fondoP.setMinimumSize(new java.awt.Dimension(920, 578));
        fondoP.setPreferredSize(new java.awt.Dimension(920, 578));
        fondoP.setLayout(null);

        ico1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico1.png"))); // NOI18N
        ico1L.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ico1L.setMaximumSize(new java.awt.Dimension(25, 25));
        ico1L.setMinimumSize(new java.awt.Dimension(25, 25));
        ico1L.setName("ico1"); // NOI18N
        ico1L.setPreferredSize(new java.awt.Dimension(25, 25));
        fondoP.add(ico1L);
        ico1L.setBounds(80, 170, 25, 25);

        ico2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico2.png"))); // NOI18N
        ico2L.setMaximumSize(new java.awt.Dimension(25, 25));
        ico2L.setMinimumSize(new java.awt.Dimension(25, 25));
        ico2L.setName("ico2"); // NOI18N
        ico2L.setPreferredSize(new java.awt.Dimension(25, 25));
        fondoP.add(ico2L);
        ico2L.setBounds(60, 200, 25, 30);

        ico3L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico3.png"))); // NOI18N
        ico3L.setMaximumSize(new java.awt.Dimension(25, 25));
        ico3L.setMinimumSize(new java.awt.Dimension(25, 25));
        ico3L.setName("ico3"); // NOI18N
        ico3L.setPreferredSize(new java.awt.Dimension(25, 25));
        fondoP.add(ico3L);
        ico3L.setBounds(70, 270, 25, 30);

        ico4L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico4.png"))); // NOI18N
        ico4L.setMaximumSize(new java.awt.Dimension(25, 25));
        ico4L.setMinimumSize(new java.awt.Dimension(25, 25));
        ico4L.setName("ico4"); // NOI18N
        ico4L.setPreferredSize(new java.awt.Dimension(25, 25));
        fondoP.add(ico4L);
        ico4L.setBounds(30, 290, 25, 30);

        tableroSupP.setBackground(Color.decode("#FFFFEB"));
        tableroSupP.setMaximumSize(new java.awt.Dimension(920, 578));
        tableroSupP.setMinimumSize(new java.awt.Dimension(920, 578));
        tableroSupP.setOpaque(false);
        tableroSupP.setPreferredSize(new java.awt.Dimension(920, 578));
        tableroSupP.setLayout(null);

        SALIR.setBackground(Color.decode("#2096D4"));
        SALIR.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SALIR.setForeground(new java.awt.Color(255, 255, 255));
        SALIR.setText("SALIR");
        SALIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SALIRActionPerformed(evt);
            }
        });
        tableroSupP.add(SALIR);
        SALIR.setBounds(840, 270, 73, 23);

        tableroP.setBackground(Color.decode("#FFFFEB"));
        tableroP.setLayout(null);

        dadoP.setMaximumSize(new java.awt.Dimension(72, 72));
        dadoP.setMinimumSize(new java.awt.Dimension(72, 72));
        dadoP.setOpaque(false);
        dadoP.setPreferredSize(new java.awt.Dimension(72, 72));

        javax.swing.GroupLayout dadoPLayout = new javax.swing.GroupLayout(dadoP);
        dadoP.setLayout(dadoPLayout);
        dadoPLayout.setHorizontalGroup(
            dadoPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );
        dadoPLayout.setVerticalGroup(
            dadoPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        tableroP.add(dadoP);
        dadoP.setBounds(310, 240, 70, 70);

        b2.setFocusable(false);
        b2.setMaximumSize(new java.awt.Dimension(50, 50));
        b2.setMinimumSize(new java.awt.Dimension(50, 50));
        b2.setName("ciencia"); // NOI18N
        b2.setPreferredSize(new java.awt.Dimension(50, 50));
        b2.setRequestFocusEnabled(false);
        b2.setRolloverEnabled(false);
        tableroP.add(b2);
        b2.setBounds(121, 11, 50, 50);

        b1.setDefaultCapable(false);
        b1.setFocusPainted(false);
        b1.setFocusable(false);
        b1.setMaximumSize(new java.awt.Dimension(50, 50));
        b1.setMinimumSize(new java.awt.Dimension(50, 50));
        b1.setName(""); // NOI18N
        b1.setPreferredSize(new java.awt.Dimension(50, 50));
        b1.setRequestFocusEnabled(false);
        b1.setRolloverEnabled(false);
        tableroP.add(b1);
        b1.setBounds(71, 11, 50, 50);

        b4.setFocusable(false);
        b4.setMaximumSize(new java.awt.Dimension(50, 50));
        b4.setMinimumSize(new java.awt.Dimension(50, 50));
        b4.setName(""); // NOI18N
        b4.setPreferredSize(new java.awt.Dimension(50, 50));
        b4.setRequestFocusEnabled(false);
        b4.setRolloverEnabled(false);
        tableroP.add(b4);
        b4.setBounds(221, 11, 50, 50);

        b3.setFocusable(false);
        b3.setMaximumSize(new java.awt.Dimension(50, 50));
        b3.setMinimumSize(new java.awt.Dimension(50, 50));
        b3.setName(""); // NOI18N
        b3.setPreferredSize(new java.awt.Dimension(50, 50));
        b3.setRequestFocusEnabled(false);
        b3.setRolloverEnabled(false);
        tableroP.add(b3);
        b3.setBounds(171, 11, 50, 50);

        b8.setFocusable(false);
        b8.setMaximumSize(new java.awt.Dimension(50, 50));
        b8.setMinimumSize(new java.awt.Dimension(50, 50));
        b8.setName(""); // NOI18N
        b8.setPreferredSize(new java.awt.Dimension(50, 50));
        b8.setRequestFocusEnabled(false);
        b8.setRolloverEnabled(false);
        tableroP.add(b8);
        b8.setBounds(371, 61, 50, 50);

        b7.setFocusable(false);
        b7.setMaximumSize(new java.awt.Dimension(50, 50));
        b7.setMinimumSize(new java.awt.Dimension(50, 50));
        b7.setName(""); // NOI18N
        b7.setPreferredSize(new java.awt.Dimension(50, 50));
        b7.setRequestFocusEnabled(false);
        b7.setRolloverEnabled(false);
        tableroP.add(b7);
        b7.setBounds(321, 61, 50, 50);

        b6.setFocusable(false);
        b6.setMaximumSize(new java.awt.Dimension(50, 50));
        b6.setMinimumSize(new java.awt.Dimension(50, 50));
        b6.setName(""); // NOI18N
        b6.setPreferredSize(new java.awt.Dimension(50, 50));
        b6.setRequestFocusEnabled(false);
        b6.setRolloverEnabled(false);
        tableroP.add(b6);
        b6.setBounds(271, 61, 50, 50);

        b5.setFocusable(false);
        b5.setMaximumSize(new java.awt.Dimension(50, 50));
        b5.setMinimumSize(new java.awt.Dimension(50, 50));
        b5.setName(""); // NOI18N
        b5.setPreferredSize(new java.awt.Dimension(50, 50));
        b5.setRequestFocusEnabled(false);
        b5.setRolloverEnabled(false);
        tableroP.add(b5);
        b5.setBounds(221, 61, 50, 50);

        b12.setFocusable(false);
        b12.setMaximumSize(new java.awt.Dimension(50, 50));
        b12.setMinimumSize(new java.awt.Dimension(50, 50));
        b12.setName(""); // NOI18N
        b12.setPreferredSize(new java.awt.Dimension(50, 50));
        b12.setRequestFocusEnabled(false);
        b12.setRolloverEnabled(false);
        tableroP.add(b12);
        b12.setBounds(521, 11, 50, 50);

        b11.setFocusable(false);
        b11.setMaximumSize(new java.awt.Dimension(50, 50));
        b11.setMinimumSize(new java.awt.Dimension(50, 50));
        b11.setName(""); // NOI18N
        b11.setPreferredSize(new java.awt.Dimension(50, 50));
        b11.setRequestFocusEnabled(false);
        b11.setRolloverEnabled(false);
        tableroP.add(b11);
        b11.setBounds(471, 11, 50, 50);

        b10.setFocusable(false);
        b10.setMaximumSize(new java.awt.Dimension(50, 50));
        b10.setMinimumSize(new java.awt.Dimension(50, 50));
        b10.setName(""); // NOI18N
        b10.setPreferredSize(new java.awt.Dimension(50, 50));
        b10.setRequestFocusEnabled(false);
        b10.setRolloverEnabled(false);
        tableroP.add(b10);
        b10.setBounds(421, 11, 50, 50);

        b9.setFocusable(false);
        b9.setMaximumSize(new java.awt.Dimension(50, 50));
        b9.setMinimumSize(new java.awt.Dimension(50, 50));
        b9.setName(""); // NOI18N
        b9.setPreferredSize(new java.awt.Dimension(50, 50));
        b9.setRequestFocusEnabled(false);
        b9.setRolloverEnabled(false);
        tableroP.add(b9);
        b9.setBounds(421, 61, 50, 50);

        b13.setFocusable(false);
        b13.setMaximumSize(new java.awt.Dimension(50, 50));
        b13.setMinimumSize(new java.awt.Dimension(50, 50));
        b13.setName(""); // NOI18N
        b13.setPreferredSize(new java.awt.Dimension(50, 50));
        b13.setRequestFocusEnabled(false);
        b13.setRolloverEnabled(false);
        tableroP.add(b13);
        b13.setBounds(571, 11, 50, 50);

        b14.setFocusable(false);
        b14.setMaximumSize(new java.awt.Dimension(50, 50));
        b14.setMinimumSize(new java.awt.Dimension(50, 50));
        b14.setName(""); // NOI18N
        b14.setPreferredSize(new java.awt.Dimension(50, 50));
        b14.setRequestFocusEnabled(false);
        b14.setRolloverEnabled(false);
        tableroP.add(b14);
        b14.setBounds(571, 61, 50, 50);

        b48.setFocusable(false);
        b48.setMaximumSize(new java.awt.Dimension(50, 50));
        b48.setMinimumSize(new java.awt.Dimension(50, 50));
        b48.setName(""); // NOI18N
        b48.setPreferredSize(new java.awt.Dimension(50, 50));
        b48.setRequestFocusEnabled(false);
        b48.setRolloverEnabled(false);
        tableroP.add(b48);
        b48.setBounds(71, 61, 50, 50);

        b46.setFocusable(false);
        b46.setMaximumSize(new java.awt.Dimension(50, 50));
        b46.setMinimumSize(new java.awt.Dimension(50, 50));
        b46.setName(""); // NOI18N
        b46.setPreferredSize(new java.awt.Dimension(50, 50));
        b46.setRequestFocusEnabled(false);
        b46.setRolloverEnabled(false);
        tableroP.add(b46);
        b46.setBounds(71, 161, 50, 50);

        b15.setFocusable(false);
        b15.setMaximumSize(new java.awt.Dimension(50, 50));
        b15.setMinimumSize(new java.awt.Dimension(50, 50));
        b15.setName(""); // NOI18N
        b15.setPreferredSize(new java.awt.Dimension(50, 50));
        b15.setRequestFocusEnabled(false);
        b15.setRolloverEnabled(false);
        tableroP.add(b15);
        b15.setBounds(571, 111, 50, 50);

        b47.setFocusable(false);
        b47.setMaximumSize(new java.awt.Dimension(50, 50));
        b47.setMinimumSize(new java.awt.Dimension(50, 50));
        b47.setName(""); // NOI18N
        b47.setPreferredSize(new java.awt.Dimension(50, 50));
        b47.setRequestFocusEnabled(false);
        b47.setRolloverEnabled(false);
        tableroP.add(b47);
        b47.setBounds(71, 111, 50, 50);

        b16.setFocusable(false);
        b16.setMaximumSize(new java.awt.Dimension(50, 50));
        b16.setMinimumSize(new java.awt.Dimension(50, 50));
        b16.setName(""); // NOI18N
        b16.setPreferredSize(new java.awt.Dimension(50, 50));
        b16.setRequestFocusEnabled(false);
        b16.setRolloverEnabled(false);
        tableroP.add(b16);
        b16.setBounds(571, 161, 50, 50);

        b44.setFocusable(false);
        b44.setMaximumSize(new java.awt.Dimension(50, 50));
        b44.setMinimumSize(new java.awt.Dimension(50, 50));
        b44.setName(""); // NOI18N
        b44.setPreferredSize(new java.awt.Dimension(50, 50));
        b44.setRequestFocusEnabled(false);
        b44.setRolloverEnabled(false);
        tableroP.add(b44);
        b44.setBounds(121, 211, 50, 50);

        b42.setFocusable(false);
        b42.setMaximumSize(new java.awt.Dimension(50, 50));
        b42.setMinimumSize(new java.awt.Dimension(50, 50));
        b42.setName(""); // NOI18N
        b42.setPreferredSize(new java.awt.Dimension(50, 50));
        b42.setRequestFocusEnabled(false);
        b42.setRolloverEnabled(false);
        tableroP.add(b42);
        b42.setBounds(121, 311, 50, 50);

        b19.setFocusable(false);
        b19.setMaximumSize(new java.awt.Dimension(50, 50));
        b19.setMinimumSize(new java.awt.Dimension(50, 50));
        b19.setName(""); // NOI18N
        b19.setPreferredSize(new java.awt.Dimension(50, 50));
        b19.setRequestFocusEnabled(false);
        b19.setRolloverEnabled(false);
        tableroP.add(b19);
        b19.setBounds(521, 261, 50, 50);

        b45.setFocusable(false);
        b45.setMaximumSize(new java.awt.Dimension(50, 50));
        b45.setMinimumSize(new java.awt.Dimension(50, 50));
        b45.setName(""); // NOI18N
        b45.setPreferredSize(new java.awt.Dimension(50, 50));
        b45.setRequestFocusEnabled(false);
        b45.setRolloverEnabled(false);
        tableroP.add(b45);
        b45.setBounds(121, 161, 50, 50);

        b43.setFocusable(false);
        b43.setMaximumSize(new java.awt.Dimension(50, 50));
        b43.setMinimumSize(new java.awt.Dimension(50, 50));
        b43.setName(""); // NOI18N
        b43.setPreferredSize(new java.awt.Dimension(50, 50));
        b43.setRequestFocusEnabled(false);
        b43.setRolloverEnabled(false);
        tableroP.add(b43);
        b43.setBounds(121, 261, 50, 50);

        b20.setFocusable(false);
        b20.setMaximumSize(new java.awt.Dimension(50, 50));
        b20.setMinimumSize(new java.awt.Dimension(50, 50));
        b20.setName(""); // NOI18N
        b20.setPreferredSize(new java.awt.Dimension(50, 50));
        b20.setRequestFocusEnabled(false);
        b20.setRolloverEnabled(false);
        tableroP.add(b20);
        b20.setBounds(521, 311, 50, 50);

        b17.setFocusable(false);
        b17.setMaximumSize(new java.awt.Dimension(50, 50));
        b17.setMinimumSize(new java.awt.Dimension(50, 50));
        b17.setName(""); // NOI18N
        b17.setPreferredSize(new java.awt.Dimension(50, 50));
        b17.setRequestFocusEnabled(false);
        b17.setRolloverEnabled(false);
        tableroP.add(b17);
        b17.setBounds(521, 161, 50, 50);

        b18.setFocusable(false);
        b18.setMaximumSize(new java.awt.Dimension(50, 50));
        b18.setMinimumSize(new java.awt.Dimension(50, 50));
        b18.setName(""); // NOI18N
        b18.setPreferredSize(new java.awt.Dimension(50, 50));
        b18.setRequestFocusEnabled(false);
        b18.setRolloverEnabled(false);
        tableroP.add(b18);
        b18.setBounds(521, 211, 50, 50);

        b41.setFocusable(false);
        b41.setMaximumSize(new java.awt.Dimension(50, 50));
        b41.setMinimumSize(new java.awt.Dimension(50, 50));
        b41.setName(""); // NOI18N
        b41.setPreferredSize(new java.awt.Dimension(50, 50));
        b41.setRequestFocusEnabled(false);
        b41.setRolloverEnabled(false);
        tableroP.add(b41);
        b41.setBounds(121, 361, 50, 50);

        b23.setFocusable(false);
        b23.setMaximumSize(new java.awt.Dimension(50, 50));
        b23.setMinimumSize(new java.awt.Dimension(50, 50));
        b23.setName(""); // NOI18N
        b23.setPreferredSize(new java.awt.Dimension(50, 50));
        b23.setRequestFocusEnabled(false);
        b23.setRolloverEnabled(false);
        tableroP.add(b23);
        b23.setBounds(571, 411, 50, 50);

        b27.setFocusable(false);
        b27.setMaximumSize(new java.awt.Dimension(50, 50));
        b27.setMinimumSize(new java.awt.Dimension(50, 50));
        b27.setName(""); // NOI18N
        b27.setPreferredSize(new java.awt.Dimension(50, 50));
        b27.setRequestFocusEnabled(false);
        b27.setRolloverEnabled(false);
        tableroP.add(b27);
        b27.setBounds(471, 511, 50, 50);

        b35.setFocusable(false);
        b35.setMaximumSize(new java.awt.Dimension(50, 50));
        b35.setMinimumSize(new java.awt.Dimension(50, 50));
        b35.setName("ciencia"); // NOI18N
        b35.setPreferredSize(new java.awt.Dimension(50, 50));
        b35.setRequestFocusEnabled(false);
        b35.setRolloverEnabled(false);
        tableroP.add(b35);
        b35.setBounds(171, 511, 50, 50);

        b30.setFocusable(false);
        b30.setMaximumSize(new java.awt.Dimension(50, 50));
        b30.setMinimumSize(new java.awt.Dimension(50, 50));
        b30.setName(""); // NOI18N
        b30.setPreferredSize(new java.awt.Dimension(50, 50));
        b30.setRequestFocusEnabled(false);
        b30.setRolloverEnabled(false);
        tableroP.add(b30);
        b30.setBounds(371, 461, 50, 50);

        b34.setFocusable(false);
        b34.setMaximumSize(new java.awt.Dimension(50, 50));
        b34.setMinimumSize(new java.awt.Dimension(50, 50));
        b34.setName(""); // NOI18N
        b34.setPreferredSize(new java.awt.Dimension(50, 50));
        b34.setRequestFocusEnabled(false);
        b34.setRolloverEnabled(false);
        tableroP.add(b34);
        b34.setBounds(221, 511, 50, 50);

        b22.setFocusable(false);
        b22.setMaximumSize(new java.awt.Dimension(50, 50));
        b22.setMinimumSize(new java.awt.Dimension(50, 50));
        b22.setName(""); // NOI18N
        b22.setPreferredSize(new java.awt.Dimension(50, 50));
        b22.setRequestFocusEnabled(false);
        b22.setRolloverEnabled(false);
        tableroP.add(b22);
        b22.setBounds(571, 361, 50, 50);

        b36.setFocusable(false);
        b36.setMaximumSize(new java.awt.Dimension(50, 50));
        b36.setMinimumSize(new java.awt.Dimension(50, 50));
        b36.setName("ciencia"); // NOI18N
        b36.setPreferredSize(new java.awt.Dimension(50, 50));
        b36.setRequestFocusEnabled(false);
        b36.setRolloverEnabled(false);
        tableroP.add(b36);
        b36.setBounds(121, 511, 50, 50);

        b38.setFocusable(false);
        b38.setMaximumSize(new java.awt.Dimension(50, 50));
        b38.setMinimumSize(new java.awt.Dimension(50, 50));
        b38.setName(""); // NOI18N
        b38.setPreferredSize(new java.awt.Dimension(50, 50));
        b38.setRequestFocusEnabled(false);
        b38.setRolloverEnabled(false);
        tableroP.add(b38);
        b38.setBounds(71, 461, 50, 50);

        b31.setFocusable(false);
        b31.setMaximumSize(new java.awt.Dimension(50, 50));
        b31.setMinimumSize(new java.awt.Dimension(50, 50));
        b31.setName(""); // NOI18N
        b31.setPreferredSize(new java.awt.Dimension(50, 50));
        b31.setRequestFocusEnabled(false);
        b31.setRolloverEnabled(false);
        tableroP.add(b31);
        b31.setBounds(321, 461, 50, 50);

        b32.setFocusable(false);
        b32.setMaximumSize(new java.awt.Dimension(50, 50));
        b32.setMinimumSize(new java.awt.Dimension(50, 50));
        b32.setName(""); // NOI18N
        b32.setPreferredSize(new java.awt.Dimension(50, 50));
        b32.setRequestFocusEnabled(false);
        b32.setRolloverEnabled(false);
        tableroP.add(b32);
        b32.setBounds(271, 461, 50, 50);

        b28.setFocusable(false);
        b28.setMaximumSize(new java.awt.Dimension(50, 50));
        b28.setMinimumSize(new java.awt.Dimension(50, 50));
        b28.setName(""); // NOI18N
        b28.setPreferredSize(new java.awt.Dimension(50, 50));
        b28.setRequestFocusEnabled(false);
        b28.setRolloverEnabled(false);
        tableroP.add(b28);
        b28.setBounds(421, 511, 50, 50);

        b29.setFocusable(false);
        b29.setMaximumSize(new java.awt.Dimension(50, 50));
        b29.setMinimumSize(new java.awt.Dimension(50, 50));
        b29.setName(""); // NOI18N
        b29.setPreferredSize(new java.awt.Dimension(50, 50));
        b29.setRequestFocusEnabled(false);
        b29.setRolloverEnabled(false);
        tableroP.add(b29);
        b29.setBounds(421, 461, 50, 50);

        b25.setFocusable(false);
        b25.setMaximumSize(new java.awt.Dimension(50, 50));
        b25.setMinimumSize(new java.awt.Dimension(50, 50));
        b25.setName(""); // NOI18N
        b25.setPreferredSize(new java.awt.Dimension(50, 50));
        b25.setRequestFocusEnabled(false);
        b25.setRolloverEnabled(false);
        tableroP.add(b25);
        b25.setBounds(571, 511, 50, 50);

        b39.setFocusable(false);
        b39.setMaximumSize(new java.awt.Dimension(50, 50));
        b39.setMinimumSize(new java.awt.Dimension(50, 50));
        b39.setName(""); // NOI18N
        b39.setPreferredSize(new java.awt.Dimension(50, 50));
        b39.setRequestFocusEnabled(false);
        b39.setRolloverEnabled(false);
        tableroP.add(b39);
        b39.setBounds(71, 411, 50, 50);

        b21.setFocusable(false);
        b21.setMaximumSize(new java.awt.Dimension(50, 50));
        b21.setMinimumSize(new java.awt.Dimension(50, 50));
        b21.setName(""); // NOI18N
        b21.setPreferredSize(new java.awt.Dimension(50, 50));
        b21.setRequestFocusEnabled(false);
        b21.setRolloverEnabled(false);
        tableroP.add(b21);
        b21.setBounds(521, 361, 50, 50);

        jButton156.setContentAreaFilled(false);
        jButton156.setEnabled(false);
        jButton156.setFocusable(false);
        jButton156.setMaximumSize(new java.awt.Dimension(50, 50));
        jButton156.setMinimumSize(new java.awt.Dimension(50, 50));
        jButton156.setName(""); // NOI18N
        jButton156.setPreferredSize(new java.awt.Dimension(50, 50));
        jButton156.setRequestFocusEnabled(false);
        jButton156.setRolloverEnabled(false);
        tableroP.add(jButton156);
        jButton156.setBounds(381, 681, 50, 50);

        b40.setFocusable(false);
        b40.setMaximumSize(new java.awt.Dimension(50, 50));
        b40.setMinimumSize(new java.awt.Dimension(50, 50));
        b40.setName(""); // NOI18N
        b40.setPreferredSize(new java.awt.Dimension(50, 50));
        b40.setRequestFocusEnabled(false);
        b40.setRolloverEnabled(false);
        tableroP.add(b40);
        b40.setBounds(71, 361, 50, 50);

        b33.setFocusable(false);
        b33.setMaximumSize(new java.awt.Dimension(50, 50));
        b33.setMinimumSize(new java.awt.Dimension(50, 50));
        b33.setName(""); // NOI18N
        b33.setPreferredSize(new java.awt.Dimension(50, 50));
        b33.setRequestFocusEnabled(false);
        b33.setRolloverEnabled(false);
        tableroP.add(b33);
        b33.setBounds(221, 461, 50, 50);

        b26.setFocusable(false);
        b26.setMaximumSize(new java.awt.Dimension(50, 50));
        b26.setMinimumSize(new java.awt.Dimension(50, 50));
        b26.setName("ciencia"); // NOI18N
        b26.setPreferredSize(new java.awt.Dimension(50, 50));
        b26.setRequestFocusEnabled(false);
        b26.setRolloverEnabled(false);
        tableroP.add(b26);
        b26.setBounds(521, 511, 50, 50);

        b37.setFocusable(false);
        b37.setMaximumSize(new java.awt.Dimension(50, 50));
        b37.setMinimumSize(new java.awt.Dimension(50, 50));
        b37.setName(""); // NOI18N
        b37.setPreferredSize(new java.awt.Dimension(50, 50));
        b37.setRequestFocusEnabled(false);
        b37.setRolloverEnabled(false);
        tableroP.add(b37);
        b37.setBounds(71, 511, 50, 50);

        b24.setFocusable(false);
        b24.setMaximumSize(new java.awt.Dimension(50, 50));
        b24.setMinimumSize(new java.awt.Dimension(50, 50));
        b24.setName("ciencia"); // NOI18N
        b24.setPreferredSize(new java.awt.Dimension(50, 50));
        b24.setRequestFocusEnabled(false);
        b24.setRolloverEnabled(false);
        tableroP.add(b24);
        b24.setBounds(571, 461, 50, 50);

        logoL.setBackground(Color.decode("#FFFFEB"));
        logoL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/logodesaturado.png"))); // NOI18N
        logoL.setFocusable(false);
        logoL.setMaximumSize(new java.awt.Dimension(550, 550));
        logoL.setMinimumSize(new java.awt.Dimension(550, 550));
        logoL.setOpaque(true);
        logoL.setPreferredSize(new java.awt.Dimension(550, 550));
        logoL.setRequestFocusEnabled(false);
        tableroP.add(logoL);
        logoL.setBounds(70, 10, 550, 540);

        tableroSupP.add(tableroP);
        tableroP.setBounds(120, 0, 685, 577);

        rosca1.setOpaque(false);

        javax.swing.GroupLayout rosca1Layout = new javax.swing.GroupLayout(rosca1);
        rosca1.setLayout(rosca1Layout);
        rosca1Layout.setHorizontalGroup(
            rosca1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        rosca1Layout.setVerticalGroup(
            rosca1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tableroSupP.add(rosca1);
        rosca1.setBounds(0, 0, 100, 100);

        rosca2.setOpaque(false);

        javax.swing.GroupLayout rosca2Layout = new javax.swing.GroupLayout(rosca2);
        rosca2.setLayout(rosca2Layout);
        rosca2Layout.setHorizontalGroup(
            rosca2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        rosca2Layout.setVerticalGroup(
            rosca2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tableroSupP.add(rosca2);
        rosca2.setBounds(820, 0, 100, 100);

        rosca3.setOpaque(false);

        javax.swing.GroupLayout rosca3Layout = new javax.swing.GroupLayout(rosca3);
        rosca3.setLayout(rosca3Layout);
        rosca3Layout.setHorizontalGroup(
            rosca3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        rosca3Layout.setVerticalGroup(
            rosca3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tableroSupP.add(rosca3);
        rosca3.setBounds(820, 480, 100, 100);

        rosca4.setOpaque(false);

        javax.swing.GroupLayout rosca4Layout = new javax.swing.GroupLayout(rosca4);
        rosca4.setLayout(rosca4Layout);
        rosca4Layout.setHorizontalGroup(
            rosca4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        rosca4Layout.setVerticalGroup(
            rosca4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tableroSupP.add(rosca4);
        rosca4.setBounds(0, 480, 100, 100);

        jug1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableroSupP.add(jug1);
        jug1.setBounds(0, 100, 100, 30);

        jug2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableroSupP.add(jug2);
        jug2.setBounds(820, 100, 100, 30);

        jug3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableroSupP.add(jug3);
        jug3.setBounds(820, 450, 100, 30);

        jug4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tableroSupP.add(jug4);
        jug4.setBounds(0, 450, 100, 30);

        icofijo1L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico1.png"))); // NOI18N
        icofijo1L.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        icofijo1L.setMaximumSize(new java.awt.Dimension(25, 25));
        icofijo1L.setMinimumSize(new java.awt.Dimension(25, 25));
        icofijo1L.setName("ico1"); // NOI18N
        icofijo1L.setPreferredSize(new java.awt.Dimension(25, 25));
        tableroSupP.add(icofijo1L);
        icofijo1L.setBounds(40, 140, 25, 25);

        icofijo2L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico2.png"))); // NOI18N
        icofijo2L.setMaximumSize(new java.awt.Dimension(25, 25));
        icofijo2L.setMinimumSize(new java.awt.Dimension(25, 25));
        icofijo2L.setName("ico2"); // NOI18N
        icofijo2L.setPreferredSize(new java.awt.Dimension(25, 25));
        tableroSupP.add(icofijo2L);
        icofijo2L.setBounds(850, 140, 25, 30);

        icofijo3L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico3.png"))); // NOI18N
        icofijo3L.setMaximumSize(new java.awt.Dimension(25, 25));
        icofijo3L.setMinimumSize(new java.awt.Dimension(25, 25));
        icofijo3L.setName("ico3"); // NOI18N
        icofijo3L.setPreferredSize(new java.awt.Dimension(25, 25));
        tableroSupP.add(icofijo3L);
        icofijo3L.setBounds(850, 410, 25, 30);

        icofijo4L.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ico4.png"))); // NOI18N
        icofijo4L.setMaximumSize(new java.awt.Dimension(25, 25));
        icofijo4L.setMinimumSize(new java.awt.Dimension(25, 25));
        icofijo4L.setName("ico4"); // NOI18N
        icofijo4L.setPreferredSize(new java.awt.Dimension(25, 25));
        tableroSupP.add(icofijo4L);
        icofijo4L.setBounds(40, 410, 25, 30);

        fondoP.add(tableroSupP);
        tableroSupP.setBounds(0, 0, 920, 578);

        getContentPane().add(fondoP);
        fondoP.setBounds(0, 0, 920, 578);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SALIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SALIRActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_SALIRActionPerformed
   
    
    
    //Metodo para rellenar todos los arraylist que necesitamos
    public void rellenarArrayList(){
        
        quesos = new ArrayList<JButton>();
        quesos.add(b1);
        quesos.add(b7);
        quesos.add(b13);
        quesos.add(b19);
        quesos.add(b25);
        quesos.add(b31);
        quesos.add(b37);
        quesos.add(b43);
        
        historia = new ArrayList();
        historia.add(b1);
        historia.add(b6);
        historia.add(b8);
        historia.add(b17);
        historia.add(b33);
        
        literatura = new ArrayList();
        literatura.add(b7);
        literatura.add(b12);
        literatura.add(b14);
        literatura.add(b39);
        literatura.add(b23);
        
        arte = new ArrayList();
        arte.add(b13);
        arte.add(b45);
        arte.add(b20);
        arte.add(b18);
        arte.add(b29);
        
        geografia = new ArrayList();
        geografia.add(b43);
        geografia.add(b2);
        geografia.add(b48);
        geografia.add(b11);
        geografia.add(b27);
        
        ciencias = new ArrayList();
        ciencias.add(b19);
        ciencias.add(b3);
        ciencias.add(b24);
        ciencias.add(b26);
        ciencias.add(b35);
        
        friki = new ArrayList();
        friki.add(b37);
        friki.add(b5);
        friki.add(b44);
        friki.add(b42);
        friki.add(b21);
        
        cine = new ArrayList();
        cine.add(b31);
        cine.add(b47);
        cine.add(b15);
        cine.add(b41);
        cine.add(b38);
        cine.add(b36);
        
        deporte = new ArrayList();
        deporte.add(b25);
        deporte.add(b9);
        deporte.add(b32);
        deporte.add(b30);
        
        dados = new ArrayList();
        dados.add(b4);
        dados.add(b10);
        dados.add(b46);
        dados.add(b16);
        dados.add(b40);
        dados.add(b22);
        dados.add(b34);
        dados.add(b28);
       
    }
    
    //Metodo para cambiar el aspecto de los botones
    public void fondoEicono(){
        for(int i=1; i<historia.size(); i++){
            historia.get(i).setBackground(Color.decode("#CEC529"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/historia.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            historia.get(i).setIcon(icoE);
            historia.get(i).setName("Historia");
            historia.get(i).setToolTipText("Historia");
        }
        historia.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoH=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoH.png"));
        ImageIcon icoHE = new ImageIcon(icoH.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        historia.get(0).setIcon(icoHE);
        historia.get(0).setName("Historia");
        historia.get(0).setToolTipText("Quesito de historia");
        
        for(int i=1; i<literatura.size(); i++){
            literatura.get(i).setBackground(Color.decode("#A96000"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/literatura.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            literatura.get(i).setIcon(icoE);
            literatura.get(i).setName("Literatura");
            literatura.get(i).setToolTipText("Literatura");
        }
        literatura.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoL=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoL.png"));
        ImageIcon icoLE = new ImageIcon(icoL.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        literatura.get(0).setIcon(icoLE);
        literatura.get(0).setName("Literatura");
        literatura.get(0).setToolTipText("Quesito de literatura");
        
        for(int i=1; i<arte.size(); i++){
            arte.get(i).setBackground(Color.decode("#2493F3"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/arte.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            arte.get(i).setIcon(icoE);
            arte.get(i).setName("Arte");
            arte.get(i).setToolTipText("Arte");
        }
        arte.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoA=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoA.png"));
        ImageIcon icoAE = new ImageIcon(icoA.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        arte.get(0).setIcon(icoAE);
        arte.get(0).setName("Arte");
        arte.get(0).setToolTipText("Quesito de arte");
        
        for(int i=1; i<geografia.size(); i++){
            geografia.get(i).setBackground(Color.decode("#E91D20"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/geografia.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            geografia.get(i).setIcon(icoE);
            geografia.get(i).setName("Geografia");
            geografia.get(i).setToolTipText("Geografía");
        }
        geografia.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoG=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoG.png"));
        ImageIcon icoGE = new ImageIcon(icoG.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        geografia.get(0).setIcon(icoGE);
        geografia.get(0).setName("Geografia");
        geografia.get(0).setToolTipText("Quesito de geografía");
        
        for(int i=1; i<ciencias.size(); i++){
            ciencias.get(i).setBackground(Color.decode("#1D8B0C"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/ciencias.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            ciencias.get(i).setIcon(icoE);
            ciencias.get(i).setName("Ciencia");
            ciencias.get(i).setToolTipText("Ciencia");
        }
        ciencias.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoC=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoC.png"));
        ImageIcon icoCE = new ImageIcon(icoC.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        ciencias.get(0).setIcon(icoCE);
        ciencias.get(0).setName("Ciencia");
        ciencias.get(0).setToolTipText("Quesito de ciencia");
        
        for(int i=1; i<friki.size(); i++){
            friki.get(i).setBackground(Color.decode("#1CCEE9"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/friki.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            friki.get(i).setIcon(icoE);
            friki.get(i).setName("Friki");
            friki.get(i).setToolTipText("Friki");
        }
        friki.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoF=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoF.png"));
        ImageIcon icoFE = new ImageIcon(icoF.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        friki.get(0).setIcon(icoFE);
        friki.get(0).setName("Friki");
        friki.get(0).setToolTipText("Quesito de friki");
        
        for(int i=1; i<cine.size(); i++){
            cine.get(i).setBackground(Color.decode("#FF3BC2"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/cine.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            cine.get(i).setIcon(icoE);
            cine.get(i).setName("Cine");
            cine.get(i).setToolTipText("Cine");
        }
        cine.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoCi=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoCi.png"));
        ImageIcon icoCiE = new ImageIcon(icoCi.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        cine.get(0).setIcon(icoCiE);
        cine.get(0).setName("Cine");
        cine.get(0).setToolTipText("Quesito de cine");
        
        for(int i=1; i<deporte.size(); i++){
            deporte.get(i).setBackground(Color.decode("#F86A1E"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/deporte.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
            deporte.get(i).setIcon(icoE);
            deporte.get(i).setName("Deporte");
            deporte.get(i).setToolTipText("Deporte");
        }
        deporte.get(0).setBackground(Color.decode("#FFFFFF"));
        ImageIcon icoD=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/quesitoD.png"));
        ImageIcon icoDE = new ImageIcon(icoD.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
        deporte.get(0).setIcon(icoDE);
        deporte.get(0).setName("Deporte");
        deporte.get(0).setToolTipText("Quesito de deporte");
        
        for(int i=0; i<dados.size(); i++){
            dados.get(i).setBackground(Color.decode("#FFFFFF"));
            ImageIcon ico=new ImageIcon(getClass().getResource("/imagenes/tableroYtarjetas/dado.png"));
            ImageIcon icoE = new ImageIcon(ico.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
            dados.get(i).setIcon(icoE);
            dados.get(i).setName("Dados");
            dados.get(i).setToolTipText("Suertudo...");
        }
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SALIR;
    private javax.swing.JButton b1;
    private javax.swing.JButton b10;
    private javax.swing.JButton b11;
    private javax.swing.JButton b12;
    private javax.swing.JButton b13;
    private javax.swing.JButton b14;
    private javax.swing.JButton b15;
    private javax.swing.JButton b16;
    private javax.swing.JButton b17;
    private javax.swing.JButton b18;
    private javax.swing.JButton b19;
    private javax.swing.JButton b2;
    private javax.swing.JButton b20;
    private javax.swing.JButton b21;
    private javax.swing.JButton b22;
    private javax.swing.JButton b23;
    private javax.swing.JButton b24;
    private javax.swing.JButton b25;
    private javax.swing.JButton b26;
    private javax.swing.JButton b27;
    private javax.swing.JButton b28;
    private javax.swing.JButton b29;
    private javax.swing.JButton b3;
    private javax.swing.JButton b30;
    private javax.swing.JButton b31;
    private javax.swing.JButton b32;
    private javax.swing.JButton b33;
    private javax.swing.JButton b34;
    private javax.swing.JButton b35;
    private javax.swing.JButton b36;
    private javax.swing.JButton b37;
    private javax.swing.JButton b38;
    private javax.swing.JButton b39;
    private javax.swing.JButton b4;
    private javax.swing.JButton b40;
    private javax.swing.JButton b41;
    private javax.swing.JButton b42;
    private javax.swing.JButton b43;
    private javax.swing.JButton b44;
    private javax.swing.JButton b45;
    private javax.swing.JButton b46;
    private javax.swing.JButton b47;
    private javax.swing.JButton b48;
    private javax.swing.JButton b5;
    private javax.swing.JButton b6;
    private javax.swing.JButton b7;
    private javax.swing.JButton b8;
    private javax.swing.JButton b9;
    private javax.swing.JPanel dadoP;
    private javax.swing.JPanel fondoP;
    private javax.swing.JLabel ico1L;
    private javax.swing.JLabel ico2L;
    private javax.swing.JLabel ico3L;
    private javax.swing.JLabel ico4L;
    private javax.swing.JLabel icofijo1L;
    private javax.swing.JLabel icofijo2L;
    private javax.swing.JLabel icofijo3L;
    private javax.swing.JLabel icofijo4L;
    private javax.swing.JButton jButton156;
    private javax.swing.JLabel jug1;
    private javax.swing.JLabel jug2;
    private javax.swing.JLabel jug3;
    private javax.swing.JLabel jug4;
    private javax.swing.JLabel logoL;
    private javax.swing.JPanel rosca1;
    private javax.swing.JPanel rosca2;
    private javax.swing.JPanel rosca3;
    private javax.swing.JPanel rosca4;
    private javax.swing.JPanel tableroP;
    private javax.swing.JPanel tableroSupP;
    // End of variables declaration//GEN-END:variables

    
    //Metodo run del hilo de la barra de tiempo de las tarjetas
    @Override
    public void run() {
        
        for(indice=0;indice<=15;indice++)
        {
            pregunta.getBarra().setValue(indice);
            pregunta.getBarra().setString(15 - pregunta.getBarra().getValue() + " s");
            try
        {
            // pausa para el splash
            Thread.sleep(1000);   
        }catch(Exception e){}
        }
       if(indice >= 15 && indice < 30){
            tirarDado = true;
            pregunta.mostrarCorrecta();
            //JOptionPane.showMessageDialog(null, "Se acabó el tiempo!!!");
            JOptionPane.showMessageDialog(null, "Se acabó el tiempo.","", -1, icono);
            jug[turno].setPF(jug[turno].getPF()+1);
                try {
                    st.execute("UPDATE jugador SET preguntas_falladas = "+jug[turno].getPF()+" WHERE nombre_jugador = '"+jug[turno].getNombre()+"'");
                } catch (SQLException ex) {
                    Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
                }
                    turno++;
                    //Para rotar los jugadores, si hemos llegado al total, volvemos a empezar
                    if(turno == jug.length)
                        turno = 0;
                    if(jug.length > 1)
                    turnos();
            pregunta.dispose();
       }
    }
}
