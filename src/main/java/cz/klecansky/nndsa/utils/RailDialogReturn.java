package cz.klecansky.nndsa.utils;

import cz.klecansky.nndsa.rail.Train;

public record RailDialogReturn(String railName,String startRailSwitchKey, String endRailSwitchKey, double railLength, Train train) {
}
