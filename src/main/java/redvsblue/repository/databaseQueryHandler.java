package redvsblue.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class handles loading and saving database records in ranklist.json.
 */

public final class databaseQueryHandler {
    private static final Logger logger = LogManager.getLogger();

    /**
     * A copy of the {@code databaseQueryHandler} class to reach database queries.
     */

    public static final databaseQueryHandler db = new  databaseQueryHandler();

    /**
     * This is the method for saving the stats in the database.
     *
     * @param name property who's record should be updated
     * @param isWon signs whether the player won the game or not
     * @throws IOException
     */

    public void saveStats(String name, boolean isWon) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        URL jsonURL = getClass().getClassLoader().getResource("json/ranklist.json");
        File ranklistJson = new File(jsonURL.getFile());
        List<ranklistRecord> records = Arrays.asList(objectMapper.readValue(ranklistJson, ranklistRecord[].class));
        Optional<ranklistRecord> searchedPlayer =  records.stream().filter(record -> record.getName().equals(name)).findFirst();


        if (!searchedPlayer.equals(Optional.empty())) {
            ranklistRecord recordToUpdate = searchedPlayer.get();
            recordToUpdate.setMatches_played(recordToUpdate.getMatches_played() + 1);
            recordToUpdate.setMatches_won(isWon? recordToUpdate.getMatches_won() + 1: recordToUpdate.getMatches_won());
            recordToUpdate.setWinrate((double) Math.round((recordToUpdate.getMatches_won() / recordToUpdate.getMatches_played()) * 100) / 100);
            recordToUpdate.setPlace((int)records.stream().filter(record -> record.getWinrate() > recordToUpdate.getWinrate()).count() + 1);
            records.set(records.indexOf(searchedPlayer.get()), recordToUpdate);

            records.sort(Comparator.comparingDouble(ranklistRecord::getWinrate).thenComparingDouble(ranklistRecord::getMatches_played).reversed());
            records.stream().forEach(record -> record.setPlace(records.indexOf(record) + 1));

            objectMapper.writeValue(ranklistJson, records);
            logger.debug("{} player updated in the records!", name);
            logger.trace("{}'s new record in ranklist is: {}", name, recordToUpdate);
        }
        else {
            ranklistRecord recordToWrite = new ranklistRecord();
            recordToWrite.setName(name);
            recordToWrite.setMatches_played(1);
            recordToWrite.setMatches_won(isWon? 1 : 0);
            recordToWrite.setWinrate(isWon? 1 : 0);
            recordToWrite.setPlace((int)records.stream().filter(record -> record.getWinrate() >= recordToWrite.getWinrate()).count() + 1);
            records.stream().filter(record -> record.getPlace() >= recordToWrite.getPlace()).forEach(record -> record.setPlace(record.getPlace() + 1));


            ArrayList<ranklistRecord> recordsToWrite = new ArrayList<>();
            recordsToWrite.addAll(records);
            recordsToWrite.add(recordToWrite);

            recordsToWrite.sort(Comparator.comparingDouble(ranklistRecord::getWinrate).thenComparingDouble(ranklistRecord::getMatches_played).reversed());
            recordsToWrite.stream().forEach(record -> record.setPlace(recordsToWrite.indexOf(record) + 1));

            objectMapper.writeValue(ranklistJson, recordsToWrite);
            logger.debug("{} player added as new record!", name);
            logger.trace("{}'s new record in ranklist is: {}", name, recordToWrite);
        }
    }

    /**
     * This method is responsible for loading stats.
     *
     * @return a list of the records
     * @throws IOException
     */

    public  List<ranklistRecord> loadStats() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        URL jsonURL = getClass().getClassLoader().getResource("json/ranklist.json");
        File ranklistJson = new File(jsonURL.getFile());
        return Arrays.asList(objectMapper.readValue(ranklistJson, ranklistRecord[].class));
    }
}

