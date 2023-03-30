package seedu.loyaltylift.model.customer;

import static seedu.loyaltylift.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Customer's tier in the address book.
 * A customer can only belong in NONE, BRONZE, SILVER or GOLD
 */
public class Tier {
    /**
     * Represents a Customer's tier.
     */
    public enum tierName {
        NONE,
        BRONZE,
        SILVER,
        GOLD
    }
    private static Integer NO_TIER_THRESHOLD = 0;
    private static Integer BRONZE_STARTING_THRESHOLD = 1000;
    private static Integer SILVER_STARTING_THRESHOLD = 5000;
    private static Integer GOLD_STARTING_THRESHOLD = 10000;
    private static Tier NONE = new Tier(tierName.NONE, new Points(NO_TIER_THRESHOLD, NO_TIER_THRESHOLD));
    private static Tier BRONZE = new Tier(tierName.BRONZE, new Points (BRONZE_STARTING_THRESHOLD, BRONZE_STARTING_THRESHOLD));
    private static Tier SILVER = new Tier(tierName.SILVER, new Points (SILVER_STARTING_THRESHOLD, SILVER_STARTING_THRESHOLD));
    private static Tier GOLD = new Tier(tierName.GOLD, new Points (GOLD_STARTING_THRESHOLD, GOLD_STARTING_THRESHOLD));

    public static final String MESSAGE_CONSTRAINTS = "Tier must be "
            + tierName.BRONZE
            + ", "
            + tierName.SILVER
            + " or "
            + tierName.GOLD;

    public final tierName name;
    public final Points pointThreshold;

    /**
     * Constructs a {@code Tier}.
     *
     * @param name of the tier
     * @param pointThreshold the point threshold for the associated tier
     */
    private Tier(tierName name, Points pointThreshold) {
        requireAllNonNull(name, pointThreshold);
        this.name = name;
        this.pointThreshold = pointThreshold;
    }
    public static Tier getNoTier() {
        return NONE;
    }

    public static Tier getBronze() {
        return BRONZE;
    }

    public static Tier getSilver() {
        return SILVER;
    }

    public static Tier getGold() {
        return GOLD;
    }

    public static Tier getTierFromTierName(tierName tierName) {
        switch(tierName.toString()) {
        case "NONE":
            return NONE;
        case "BRONZE":
            return BRONZE;
        case "SILVER":
            return SILVER;
        case "GOLD":
            return GOLD;
        default:
            throw new IllegalArgumentException("Invalid tier name: " + tierName);
        }
    }

    /**
     * Returns true if tierToCompare is below all other tiers in terms of point threshold
     *
     * @param tierToCompare the tier to be evaluated
     * @param tiers all other tiers for tierToCompare to compare against
     * @return true if tierToCompare point threshold is below each tier in tiers
     */
    private static boolean isBelowTiers(Tier tierToCompare, Tier... tiers) {
        int counter = 0;
        for (Tier tier : tiers) {
            if (tierToCompare.pointThreshold.compareTo(tier.pointThreshold) < 0) {
                counter++;
            }
        }
        return counter == tiers.length ? true : false;
    }

    /**
     * Returns true if tierToCompare is above all other tiers in terms of point threshold
     *
     * @param tierToCompare the tier to be evaluated
     * @param tiers all other tiers for tierToCompare to compare against
     * @return true if tierToCompare point threshold is above each tier in tiers
     */
    private static boolean isAboveTiers(Tier tierToCompare, Tier... tiers) {
        int counter = 0;
        for (Tier tier : tiers) {
            if (tierToCompare.pointThreshold.compareTo(tier.pointThreshold) > 0) {
                counter++;
            }
        }
        return counter == tiers.length ? true : false;
    }

    /**
     * Returns true if a point threshold for a given tier is valid.
     * No tier must be strictly below bronze, silver and gold
     * bronze must be strictly below silver and gold
     * silver must be strictly below gold
     *
     * @param tier the tier with the point threshold to check
     * @return true if the order, NONE < BRONZE < SILVER < GOLD, holds
     */
    public static boolean isValidPointThreshold(Tier tier) {
        switch(tier.name) {

        case NONE:
            return isBelowTiers(NONE, BRONZE, SILVER, GOLD);

        case BRONZE:
            return isBelowTiers(BRONZE, SILVER, GOLD) && isAboveTiers(BRONZE, NONE);

        case SILVER:
            return isBelowTiers(SILVER, GOLD) && isAboveTiers(SILVER, NONE, BRONZE);

        case GOLD:
            return isAboveTiers(GOLD, NONE, BRONZE, SILVER);

        default:
            return false;
        }
    }

    public static Tier getTierFromPoints(Points points) {
        if (points.isCumulativeEqualToOrHigher(GOLD.pointThreshold)) {
            return GOLD;
        } else if (points.isCumulativeEqualToOrHigher(SILVER.pointThreshold)) {
            return SILVER;
        } else if (points.isCumulativeEqualToOrHigher(BRONZE.pointThreshold)) {
            return BRONZE;
        } else {
            return NONE;
        }
    }

    /**
     * Returns true if a given tier is a valid name
     * @param name of the tier
     * @return true if it is a tier that is in Tier.tierName
     */
    public static boolean isValidTier(String name) {
        try {
            tierName.valueOf(name.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tier // instanceof handles nulls
                && name.equals(((Tier) other).name)) // state check
                && pointThreshold.equals(((Tier) other).pointThreshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pointThreshold);
    }
}
