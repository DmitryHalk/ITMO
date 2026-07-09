package exception;

import com.Character;

public class FlyExeption extends Exception {
    public FlyExeption(Character c) {
        super(String.format("%s не может взлететь так как выключен пропелер", c.getName()));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
