package interfaces;

import com.Room;
import com.Character;
import com.Window;
import exception.LocationException;

public interface Move {
    public void walkTo(Room room) throws LocationException;
    public void runTo(Room room) throws LocationException;
    public void chase(Character pers);
    public void walkToWindow();

}
