package cz.klecansky.nndsa.utils;

public record ShortestPathDialogReturn(String firstViaRailSwitch, String startRail, String secondViaRailSwitch,
                                       String endRail, double trainLength) {
}
