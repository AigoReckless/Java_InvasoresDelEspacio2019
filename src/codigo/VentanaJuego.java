/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import static java.awt.Color.green;
import static java.awt.Color.white;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author Daniel Hebrero Núñez
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 450;
    public static Label panelPuntuacion = new Label(); 
    int puntuacion = 0;
    //Numero de marcianos que van a aparecer
    public int filas = 4;
    public int columnas = 10;
    int marcianosMuertos = 0;
    BufferedImage buffer = null;

    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    //Marciano miMarciano = new Marciano();
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;

    //El contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;
    //Imagen para cargar el fondo de pantalla
    Image fondoPantalla;
    //Imagen para cargar el spritesheet con todos los sprites del juego
    BufferedImage plantilla = null;
    Image[][] imagenes;
    //Lleva un registro de la puntuación del juego
    
    //control del fin de partida
    boolean gameOver = false;
    boolean partidaAcabada = false;
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();

        }
    });

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        
        reproduce("/Sonidos/OyS.wav", 5);
       
         try {
         fondoPantalla = ImageIO.read(getClass().getResource("/imagenes/fondo00.png"));
       } catch (IOException ex) {}
         setLocationRelativeTo(null);
         Font fuente;
         fuente = new Font("neue helvetica", Font.BOLD, 30);
        panelPuntuacion.setFont(fuente);
        panelPuntuacion.setForeground(white);
        panelPuntuacion.setBackground(Color.BLACK);
        panelPuntuacion.setBounds(490, 30, 100, 30);
        panelPuntuacion.setText("0");
        jPanel1.add(panelPuntuacion);
        //Para cargar el archivo de imagenes:
        //1º, el nombre del archivo
        //2º, filas que tiene el spritesheet
        //3º columnas que tiene el spritesheet
        //4º lo que mide de ancho el sprite en el spritesheet
        //5º lo que mide de alto el sprite en el spritesheet
        //6º para cambiar el tamaño de los sprites
        imagenes = cargaImagenes("/imagenes/invaders3.png", 5, 4, 64, 64, 2);
        
        miDisparo.imagen = imagenes[2][4];
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();
        temporizador.start();

        //Inicializo la posición inicial de la nave
        miNave.imagen = imagenes[5][1];      
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;

        //Inicializo el array de marcianos
        //Reto: Hacerlo usando mods (usando el bucle for anidado)
        //1º parametro: numero de la fila de marcianos que estoy creando
        //2º parametro: fila dentro del spritesheet del marciano que quiero pintar
        //3º parametro: columna dentro del spritesheet del marciano que quiero pintar
        creaFilaDeMarcianos(0, 0, 0);
        creaFilaDeMarcianos(1, 0, 0);
        creaFilaDeMarcianos(2, 0, 2);
        creaFilaDeMarcianos(3, 0, 2);
        //creaFilaDeMarcianos(4, 2, 2);
        //creaFilaDeMarcianos(5, 2, 2);
        //creaFilaDeMarcianos(6, 3, 2);
        //creaFilaDeMarcianos(7, 3, 2);
//        for (int i = 0; i < filas; i++) {
//            for (int j = 0; j < columnas; j++) {
//                listaMarcianos[i][j] = new Marciano();
//                listaMarcianos[i][j].imagen1 = imagenes[0][0];
//                listaMarcianos[i][j].imagen2 = imagenes[0][0];
//                listaMarcianos[i][j].x = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
//                listaMarcianos[i][j].y = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
//            }
//        }

    }

    //Hago un homenaje a Fraxito y su codigo "JavaSonidos" con el siguiente codigo,
    //Que me va a permitir llamar a los diferentes sonidos que quiera introducir y
    //Repetir ese sonido las veces que quiera
    private void reproduce(String sonido, int bucle) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(sonido)));
            clip.loop(bucle);
        } catch (Exception e) {
        }
    }

    private void creaFilaDeMarcianos(int numeroFila, int spriteFila, int spriteColumna) {
        for (int j = 0; j < columnas; j++) {
            listaMarcianos[numeroFila][j] = new Marciano();
            listaMarcianos[numeroFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
            listaMarcianos[numeroFila][j].imagen2 = imagenes[spriteFila][spriteColumna + 1];
           
            listaMarcianos[numeroFila][j].x = j * (15 + listaMarcianos[numeroFila][j].imagen1.getWidth(null));
            listaMarcianos[numeroFila][j].y = numeroFila * (10 + listaMarcianos[numeroFila][j].imagen1.getHeight(null));
        }
    }

    /*
    Este método va a servir para crear el array de imagenes con todas las imagenes
    del spritesheet. Devolverá un array de dos dimensiones con las imagenes colocadas
    tal y como están en el spritesheet
     */
    private Image[][] cargaImagenes(String nombreArchivoImagenes,
            int numFilas, int numColumnas, int ancho, int alto, int escala) {
        try {
            plantilla = ImageIO.read(getClass().getResource(nombreArchivoImagenes));
        } catch (IOException ex) {
        }

        Image[][] arrayImagenes = new Image[numFilas+1][numColumnas+1];

        //Cargo las imagenes de forma individual en cada imagen del array de imagenes
        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                arrayImagenes[i][j] = plantilla.getSubimage(j * ancho,
                        i * alto,
                        ancho,
                        alto);
                arrayImagenes[i][j] = arrayImagenes[i][j].getScaledInstance(ancho / escala, ancho / escala, Image.SCALE_SMOOTH);
            }
        }
//        for(int j=0; j<4; j++){
//            //La ultima fila del spritesheet solo mide 32 de alto asi que hay que hacerla aparte
//            imagenes[20+j] = plantilla.getSubimage(j*64, 5*64, 64, 32);
//        }
//        for(int i=0; i<5; i++){
//            //La ultima columna del spritesheet solo mide 32 de largo asi que hay que hacerla aparte
//            imagenes[24+i] = plantilla.getSubimage(4*64, i*64, 32, 64);
//        }
//        imagenes[24] = plantilla.getSubimage(4*64, 2*64, 32, 64);
//        imagenes[24] = imagenes[24].getScaledInstance(16, 32, Image.SCALE_SMOOTH);
        //cargo la última fila aparte porque mide la mitad
        for (int j = 0; j < numColumnas; j++) {
            arrayImagenes[numFilas][j] = plantilla.getSubimage(j * ancho, numFilas * alto, ancho, alto / 2);
            arrayImagenes[numFilas][j] = arrayImagenes[numFilas][j].getScaledInstance(2 * ancho / escala, ancho / escala, Image.SCALE_SMOOTH);
        }
        //cargo la última columna aparte porque mide la mitad
        for (int i = 0; i < numFilas; i++) {
            arrayImagenes[i][numColumnas] = plantilla.getSubimage(numColumnas * ancho, i * alto, ancho / 2, alto);
            arrayImagenes[i][numColumnas] = arrayImagenes[i][numColumnas].getScaledInstance(ancho / escala / 2, ancho / escala, Image.SCALE_SMOOTH);
        }
        return arrayImagenes;
    }

    private void bucleDelJuego() {
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        
        if (!gameOver){
            //Gobierna (Se encarga) el redibujado de los objetos en el jPanel1
            //Primero borro todo todo lo que hay en el buffer
            contador++;
            
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            g2.drawImage(fondoPantalla, 0, 0, null); 
            //////////////////////////////////////////////////////////////////////
            //Redibujaremos aquí cada elemento
            //g2.drawImage(miMarciano.imagen1, miMarciano.x, miMarciano.y, null);
            if(!partidaAcabada){
                iniciaPartida(g2);
            }
        } else{
            
            finDePartida(g2);
        }   
        ////////////////////////////////////////////////////////////////////
        //****************** fase final, se dibuja ***********************//
        //****************** el buffer de golpe sobre el Jpanel***********//
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);

    }
    private void iniciaPartida (Graphics2D g2){
            g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
            g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
            pintaMarcianos(g2);
            chequeaColision();
            miNave.mueve();
            miDisparo.mueve();
    }
    private void chequeaColision() {
         //Marco para el borde del marciano
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        //marco para guardar el borde de la imagen de la nave
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        rectanguloNave.setFrame(miNave.x -ANCHOPANTALLA, miNave.y, ANCHOPANTALLA*2, miNave.imagen.getHeight(null));
        //Marco para el borde del disparo
        rectanguloDisparo.setFrame(miDisparo.x,
                miDisparo.y,
                miDisparo.imagen.getWidth(null),
                miDisparo.imagen.getHeight(null));

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                            listaMarcianos[i][j].y,
                            listaMarcianos[i][j].imagen1.getWidth(null),
                            listaMarcianos[i][j].imagen1.getHeight(null)
                    );
                    if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                        
                        listaMarcianos[i][j].vivo = false;
                        reproduce("/sonidos/invaderkilled.wav", 0);
                        puntuacion += 100;
                        panelPuntuacion.setText("" + puntuacion);
                        listaMarcianos[i][j].x = ANCHOPANTALLA / 2 - listaMarcianos[i][j].imagen1.getWidth(null) / 2;
                        miDisparo.posicionaDisparo(miNave);
                        miDisparo.y = 1000;
                        miDisparo.disparado = false;
                       
                    }
                    if (rectanguloNave.intersects(rectanguloMarciano) || puntuacion == filas*columnas*100){
                        //Si choca un marciano con la nave  
                        gameOver = true;
            }
                }
            }
        }
        
           
   
            
            
            
    

    }
    private void finDePartida (Graphics2D gameOver){
        partidaAcabada = true;
        if(puntuacion == filas*columnas*100){
            try {
                Image imagenFin = ImageIO.read((getClass().getResource("/imagenes/youwin.png")));
                gameOver.drawImage(imagenFin, 0, 0, null);
            } catch (IOException ex) {
            }
        }
        if(puntuacion != filas*columnas*100){
        try {
            Image imagenFin = ImageIO.read((getClass().getResource("/imagenes/gameover0.png")));
            gameOver.drawImage(imagenFin, 0, 0, null);
        } catch (IOException ex) {
        }
        }
        
        
        
}
    private void cambiaDireccionMarcianos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX() * -1);

            }
        }

    }
   
    private void pintaMarcianos(Graphics2D _g2) {

        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    listaMarcianos[i][j].mueve();

                    //Chequeo si el marciano ha chocado contra la pared para cambiar la direccion
                    //de todos los marcianos
                    if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0) {
                        direccionMarcianos = true;
                        //El siguiente bucle for controla la bajada de los marcianos
                        for (int k = 0; k < filas; k++) {
                            for (int m = 0; m < columnas; m++) {
                                listaMarcianos[k][m].y += listaMarcianos[k][m].imagen1.getHeight(null)/10 ; //El numero entre el que divido va a controlar lo que tardan en llegar abajo los marcianos
                            }
                        }
                    }
                    
                    if (contador < 50) {
                        _g2.drawImage(listaMarcianos[i][j].imagen1,
                                listaMarcianos[i][j].x,
                                listaMarcianos[i][j].y,
                                null);
                    } else if (contador < 100) {
                        _g2.drawImage(listaMarcianos[i][j].imagen2,
                                listaMarcianos[i][j].x,
                                listaMarcianos[i][j].y,
                                null);
                    } else {
                        contador = 0;
                    }
                }
            }

        }

        if (direccionMarcianos) {
            cambiaDireccionMarcianos();

            direccionMarcianos = false;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(600, 450));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(402, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(331, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if(partidaAcabada){
            switch (evt.getKeyCode()){
                case KeyEvent.VK_ENTER:
                    partidaAcabada = false;
            }
        }
        if(!partidaAcabada){
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    miNave.setPulsadoIzquierda(true);
                    break;
                case KeyEvent.VK_RIGHT:

                    miNave.setPulsadoDerecha(true);
                    break;
                case KeyEvent.VK_DOWN:
                    reproduce("/sonidos/shoot.wav", 0);
                    miDisparo.posicionaDisparo(miNave);
                    miDisparo.disparado = true;
                    break;
            }
        }    
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if(!partidaAcabada){
            switch (evt.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        miNave.setPulsadoIzquierda(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        miNave.setPulsadoDerecha(false);
                        break;

            }
        }    
    }//GEN-LAST:event_formKeyReleased

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
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
