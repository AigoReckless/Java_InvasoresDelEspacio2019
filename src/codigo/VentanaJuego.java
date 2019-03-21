/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static javafx.scene.paint.Color.color;
import javax.swing.Timer;

/**
 *
 * @author Daniel Hebrero Núñez
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 450;

    //Numero de marcianos que van a aparecer
    public int filas = 5;
    public int columnas = 10;

    BufferedImage buffer = null;

    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();

    //Marciano miMarciano = new Marciano();
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;

    //El contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;

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
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        buffer.createGraphics();

        temporizador.start();

        //Inicializo la posición inicial de la nave
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;

        //Inicializo el array de marcianos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j] = new Marciano();
                listaMarcianos[i][j].x = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].y = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
            }
        }
        
    }

    private void bucleDelJuego() {
        //Gobierna (Se encarga) el redibujado de los objetos en el jPanel1
        //Primero borro todo todo lo que hay en el buffer
        contador++;
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);

        //////////////////////////////////////////////////////////////////////
        //Redibujaremos aquí cada elemento
        
        //g2.drawImage(miMarciano.imagen1, miMarciano.x, miMarciano.y, null);
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        pintaMarcianos(g2);
        chequeaColision();
        miNave.mueve();
        miDisparo.mueve();
        

        ////////////////////////////////////////////////////////////////////
        //****************** fase final, se dibuja ***********************//
        //****************** el buffer de golpe sobre el Jpanel***********//
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
    }

    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        rectanguloDisparo.setFrame(miDisparo.x,
                miDisparo.y,
                miDisparo.imagen.getWidth(null),
                miDisparo.imagen.getHeight(null));
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                                            listaMarcianos[i][j].y,
                                            listaMarcianos[i][j].imagen1.getWidth(null),
                                            listaMarcianos[i][j].imagen1.getHeight(null)
                );
                if (rectanguloDisparo.intersects(rectanguloMarciano)){
                    listaMarcianos[i][j].y = 2000;
                    listaMarcianos[i][j].x = ANCHOPANTALLA /2 - listaMarcianos[i][j].imagen1.getWidth(null)/2;
                    miDisparo.posicionaDisparo(miNave);
                    miDisparo.y = 1000;
                    miDisparo.disparado = false;
                }
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
                listaMarcianos[i][j].mueve();
                //Chequeo si el marciano ha chocado contra la pared para cambiar la direccion
                //de todos los marcianos
                if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0) {
                    //direccionMarcianos = !direccionMarcianos;
                    direccionMarcianos = true;
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
        
        if(direccionMarcianos){
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
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
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                miDisparo.posicionaDisparo(miNave);
                miDisparo.disparado = true;
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;
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
    // End of variables declaration//GEN-END:variables
}
