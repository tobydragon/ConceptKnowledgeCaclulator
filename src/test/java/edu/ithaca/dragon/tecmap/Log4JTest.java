package edu.ithaca.dragon.tecmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JTest {
    Logger logger = LogManager.getLogger(this.getClass());

    //@Test
    public void testOutput(){
        logger.fatal("Fatal level on for this class output");
        logger.error("Error level on for this class output");
        logger.warn( "Warn  level on for this class output");
        logger.info( "Info  level on for this class output");
        logger.debug("Debug level on for this class output");
    }
}
