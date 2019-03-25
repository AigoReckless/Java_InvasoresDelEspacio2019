/*
 * La nave del juego
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Daniel Hebrero Núñez
 */
public class Nave {
    public Image imagen = null;
    public int x = 0;
    public int y = 0;
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    public Nave(){
//        try {
//            imagen = ImageIO.read(getClass().getResource("/imagenes/nave.png"));
//        } catch (IOException ex) {
//            
//        }
    }
    
    public void mueve(){
        if (pulsadoIzquierda && x>0){
            x-= 3;
        }
        if (pulsadoDerecha && x<VentanaJuego.ANCHOPANTALLA - imagen.getWidth(null) - imagen.getWidth(null)/6){
            x+=3;
        }    
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {
        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoDerecha = false;
    }

    public boolean isPulsadoDerecha() {
        return pulsadoDerecha;
    }

    public void setPulsadoDerecha(boolean pulsadoDerecha) {
        this.pulsadoDerecha = pulsadoDerecha;
        this.pulsadoIzquierda = false;
    }
}
