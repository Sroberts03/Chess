package resultrequest;

import model.GameData;
import java.util.ArrayList;

public record GameListResult(ArrayList<GameData> gameList) {
}
