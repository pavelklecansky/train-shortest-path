module cz.klecansky.nndsa {
    requires javafx.controls;
    requires javafx.fxml;
    requires JavaFXSmartGraph;
    requires atlantafx.base;

    opens cz.klecansky.nndsa to javafx.fxml;
    exports cz.klecansky.nndsa;
    exports cz.klecansky.nndsa.io;
    exports cz.klecansky.nndsa.graph;
    exports cz.klecansky.nndsa.utils;
    exports cz.klecansky.nndsa.rail;
    exports cz.klecansky.nndsa.algorithms;
    opens cz.klecansky.nndsa.utils to javafx.fxml;
}