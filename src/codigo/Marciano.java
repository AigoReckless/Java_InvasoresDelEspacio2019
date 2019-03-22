package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Daniel Hebrero Núñez
 */
public class Marciano {

    public Image imagen1, imagen2 = null;
    public int x = 0;
    public int y = 0;
    private int vX = 1;
    public boolean vivo = true;

    public Marciano() {
        
    }

    public void mueve() {

        x += vX;

    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public int getvX() {
        return vX;
    }
}
