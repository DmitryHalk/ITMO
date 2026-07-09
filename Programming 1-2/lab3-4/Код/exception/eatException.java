package exception;

import com.Character;
import com.Food;

public class eatException extends Exception {
    public eatException(Character c, Food f) {
        super(String.format("%s не может съесть %s, потому что оно съедено", c.getName(), f.getName()));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
