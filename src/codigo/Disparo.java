
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Daniel Hebrero Núñez
 */
public class Disparo {
    
    public Image imagen = null;
    public int x = 0;
    public int y = 2000; //Al principio el disparo se pinta muy por debajo de la pantalla
    public boolean disparado = false;
    
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
        } catch (IOException ex) {
            
        }
    }
    
    public void mueve(){
        if (disparado){
            y-=5;
            
        }
    }
    public void posicionaDisparo(Nave _nave){
        x = _nave.x + _nave.imagen.getWidth(null)/2 - imagen.getWidth(null)/2;   //Posiciona el disparo en el centro, aproximadamente
        y = _nave.y - _nave.imagen.getHeight(null)/2;
        
    }
    
}
