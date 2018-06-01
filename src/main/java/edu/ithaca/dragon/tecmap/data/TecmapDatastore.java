package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Tecmap;

public interface TecmapDatastore {
    enum Id { Cs1Example}

    Tecmap retrieveTecmapForId(TecmapDatastore.Id idToRetrieve);
}
