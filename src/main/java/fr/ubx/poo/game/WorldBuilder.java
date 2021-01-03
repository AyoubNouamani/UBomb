package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Decor processEntity(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Box:
                return new Box();
            case Heart:
                return new Heart();
            case Key:
                return new Key();
            case Princess:
                return new Princess();
            case Bomb1:
                return new Bomb1();
            case Bomb2:
                return new Bomb2();
            case Bomb3:
                return new Bomb3();
            case Bomb4:
                return new Bomb4();
            case BombNumberInc:
                return new BombNumberInc();
            case BombNumberDec:
                return new BombNumberDec();
            case BombRangeInc:
                return new BombRangeInc();
            case BombRangeDec:
                return new BombRangeDec();
            case DoorNextClosed:
                return new DoorNextClosed();
            case DoorNextOpened:
                return new DoorNextOpened();
            case DoorPrevOpened:
                return new DoorNextOpened();
            default:
                return null;
        }
    }
}
