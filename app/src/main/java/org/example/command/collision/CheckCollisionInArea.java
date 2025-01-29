package org.example.command.collision;

import org.example.command.Command;

import java.util.Map;
import java.util.Objects;

public class CheckCollisionInArea implements Command {

    private final CheckingObject checkingObject;
    private final Map<Locality, HittingArea> hittingAreas;
    private final Command checkNextArea;

    public CheckCollisionInArea(
        CheckingObject checkingObject,
        Map<Locality, HittingArea> hittingAreas,
        Command checkNextArea) {
        this.checkingObject = checkingObject;
        this.hittingAreas = hittingAreas;
        this.checkNextArea = checkNextArea;
    }

    @Override
    public void execute() throws CollisionException {
        var oldLocality = checkingObject.getOldLocality();
        var newLocality = checkingObject.getNewLocality();
        if (!Objects.equals(oldLocality, newLocality)) {
            var oldHittingArea = hittingAreas.getOrDefault(oldLocality, new HittingArea());
            oldHittingArea.checkAndRemove(checkingObject);

            var newHittingArea = hittingAreas.getOrDefault(newLocality, new HittingArea());
            hittingAreas.put(newLocality, newHittingArea);
            newHittingArea.add(checkingObject);
        }
        if (Objects.nonNull(checkNextArea)) {
            checkNextArea.execute();
        }
    }
}
