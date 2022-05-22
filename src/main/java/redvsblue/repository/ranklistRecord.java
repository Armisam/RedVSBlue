package redvsblue.repository;

/**
 * class representing a record of the ranklist.
 */

@lombok.Data
public class ranklistRecord {
    private Integer place;
    private String name;
    private double matches_played;
    private double matches_won;
    private double winrate;

}