package game.models;

import game.entity.models.Entity;
import game.ui.panels.game.PitchPanel;
import game.utils.Utility;

public enum EnhancedDirection {
    LEFTUP,
    LEFTDOWN,
    RIGHTUP,
    RIGHTDOWN;


    public Direction[] toDirection(){
        switch (this){
            case LEFTUP: return new Direction[]{ Direction.LEFT,Direction.UP };
            case LEFTDOWN: return new Direction[]{ Direction.LEFT,Direction.DOWN };
            case RIGHTDOWN: return new Direction[]{ Direction.RIGHT,Direction.DOWN };
            case RIGHTUP: return new Direction[]{ Direction.RIGHT,Direction.UP };
        }

        return null;
    }

    public static EnhancedDirection toEnhancedDirection(Direction[] directions){
        Direction vertical = null;
        Direction horizontal = null;

        if(directions.length != 2){
            return null;
        }

        for (Direction d: directions
             ) {

            switch (d){
                case DOWN: case UP: vertical=d; break;
                case RIGHT: case LEFT: horizontal=d; break;
            }

        }
        if (vertical == Direction.UP && horizontal==Direction.RIGHT)return RIGHTUP;
        if (vertical == Direction.DOWN && horizontal==Direction.RIGHT)return RIGHTDOWN;
        if (vertical == Direction.UP && horizontal==Direction.LEFT)return LEFTUP;
        if (vertical == Direction.DOWN && horizontal==Direction.LEFT)return LEFTDOWN;



        return null;
    }


    public EnhancedDirection opposite(Direction direction){
        Direction[] array = toDirection();
        for (int i = 0; i< array.length; i++){

            if (array[i]== direction) array[i] = direction.opposite();
        }
        return toEnhancedDirection(array);

    }

    public static EnhancedDirection randomDirectionTowardsCenter(Entity entity){
        Coordinates centerEntityCoords = new Coordinates(entity.getCoords().getX()+entity.getSize()/2,entity.getCoords().getY()+entity.getSize()/2);
        Direction newHorizontalDirection;
        Direction newVerticalDirection;
        if (centerEntityCoords.getX()> Utility.px(PitchPanel.DEFAULT_DIMENSION.getWidth()/2)) newHorizontalDirection = Direction.LEFT;
        else newHorizontalDirection = Direction.RIGHT;
        if (centerEntityCoords.getY()< Utility.px(PitchPanel.DEFAULT_DIMENSION.getHeight()/2)) newVerticalDirection = Direction.DOWN;
        else newVerticalDirection = Direction.UP;
        return toEnhancedDirection(new Direction[]{newHorizontalDirection,newVerticalDirection});


    }




}
