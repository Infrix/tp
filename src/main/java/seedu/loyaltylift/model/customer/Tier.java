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
    public enum TierName {
        NONE,
        BRONZE,
        SILVER,
        GOLD
    }
    private static Integer NO_TIER_THRESHOLD = 0;
    private static Integer BRONZE_STARTING_THRESHOLD = 1000;
    private static Integer SILVER_STARTING_THRESHOLD = 5000;
    private static Integer GOLD_STARTING_THRESHOLD = 10000;
    private static Tier NONE = new Tier(TierName.NONE, new Points(NO_TIER_THRESHOLD, NO_TIER_THRESHOLD));
    private static Tier BRONZE = new Tier(TierName.BRONZE, new Points (BRONZE_STARTING_THRESHOLD, BRONZE_STARTING_THRESHOLD));
    private static Tier SILVER = new Tier(TierName.SILVER, new Points (SILVER_STARTING_THRESHOLD, SILVER_STARTING_THRESHOLD));
    private static Tier GOLD = new Tier(TierName.GOLD, new Points (GOLD_STARTING_THRESHOLD, GOLD_STARTING_THRESHOLD));

    public static final String MESSAGE_CONSTRAINTS = "Tier must be "
            + TierName.BRONZE
            + ", "
            + TierName.SILVER
            + " or "
            + TierName.GOLD;

    public final TierName name;
    public Points pointThreshold;

    /**
     * Constructs a {@code Tier}.
     *
     * @param name of the tier
     * @param pointThreshold the point threshold for the associated tier
     */
    private Tier(TierName name, Points pointThreshold) {
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

    public static Tier getTierFromTierName(TierName tierName) {
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

    public void setPointsThreshold(Points points) {
        this.pointThreshold = points;
    }

    /**
     * Returns true if tierToCompare is below all other tiers in terms of point threshold
     *
     * @param newPointThreshold the points to compare
     * @param tiers all other tiers for newPointThreshold to compare against
     * @return true if tierToCompare point threshold is below each tier in tiers
     */
    private static boolean isBelowTiersPointThreshold(Points newPointThreshold,  Tier... tiers) {
        int counter = 0;
        for (Tier tier : tiers) {
            if (newPointThreshold.compareTo(tier.pointThreshold) < 0) {
                counter++;
            }
        }
        return counter == tiers.length ? true : false;
    }

    /**
     * Returns true if tierToCompare is above all other tiers in terms of point threshold
     *
     * @param newPointThreshold the points to compare
     * @param tiers all other tiers for newPointThreshold to compare against
     * @return true if tierToCompare point threshold is above each tier in tiers
     */
    private static boolean isAboveTiersPointThreshold(Points newPointThreshold, Tier... tiers) {
        int counter = 0;
        for (Tier tier : tiers) {
            if (newPointThreshold.compareTo(tier.pointThreshold) > 0) {
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
     * @param points the pointThreshold to be set for tier
     * @param tier the tier with the point threshold to check
     * @return true if the order, NONE < BRONZE < SILVER < GOLD, holds
     */
    public static boolean isValidPointThresholdForTier(Tier tier, Points points) {
        switch(tier.name) {

        case NONE:
            return isBelowTiersPointThreshold(points, BRONZE, SILVER, GOLD);

        case BRONZE:
            return isBelowTiersPointThreshold(points, SILVER, GOLD)
                    && isAboveTiersPointThreshold(points, NONE);

        case SILVER:
            return isBelowTiersPointThreshold(points, GOLD)
                    && isAboveTiersPointThreshold(points, NONE, BRONZE);

        case GOLD:
            return isAboveTiersPointThreshold(points, NONE, BRONZE, SILVER);

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
            TierName.valueOf(name.toUpperCase());
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
