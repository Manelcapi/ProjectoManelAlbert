package Tools;

/**
 * Created by cfgs on 11/05/17.
 */

public class Colision {

        float x, y;
        int width, height;

        public Colision (float x, float y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void move (float x, float y) {
            this.x = x;
            this.y = y;
        }

        public boolean collidesWith (Colision rect) {
            return x < rect.x + rect.width && y < rect.y + rect.height && x + width > rect.x && y + height > rect.y;
        }


}
