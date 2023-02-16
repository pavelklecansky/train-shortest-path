module cz.klecansky.nndsa {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.klecansky.nndsa to javafx.fxml;
    exports cz.klecansky.nndsa;
    exports cz.klecansky.nndsa.io;
    exports cz.klecansky.nndsa.graph;
}