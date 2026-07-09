package exception;

import com.Character;
import com.Room;

public class LocationException extends Exception {
    public LocationException(Character c, Room r) {
        super(String.format("%s находится не в  %s", c.getName(), r.getName()));
    }

    public LocationException(Character c) {
        super(String.format("%s уже находится в данном помещении", c.getName()));
    }


}
