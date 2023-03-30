package seedu.loyaltylift.logic.commands;

import static seedu.loyaltylift.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_POINTS;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_TIER;
import static seedu.loyaltylift.model.Model.PREDICATE_SHOW_ALL_CUSTOMERS;

import java.util.Set;

import seedu.loyaltylift.logic.commands.exceptions.CommandException;
import seedu.loyaltylift.model.Model;
import seedu.loyaltylift.model.attribute.Address;
import seedu.loyaltylift.model.attribute.Name;
import seedu.loyaltylift.model.attribute.Note;
import seedu.loyaltylift.model.customer.Customer;
import seedu.loyaltylift.model.customer.CustomerType;
import seedu.loyaltylift.model.customer.Email;
import seedu.loyaltylift.model.customer.Marked;
import seedu.loyaltylift.model.customer.Phone;
import seedu.loyaltylift.model.customer.Points;
import seedu.loyaltylift.model.customer.Tier;
import seedu.loyaltylift.model.tag.Tag;

/**
 * Sets the point threshold for a particular tier, which would be the amount of cumulative points a customer
 * must have before he is part of the tier.
 */
public class SetTierCommand extends Command {

    public static final String COMMAND_WORD = "settier";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the point threshold for a particular tier \n"
            + "Parameters: "
            + PREFIX_TIER + " (must be bronze, silver or gold) "
            + PREFIX_POINTS + "[POINTS]\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_TIER + "1 "
            + PREFIX_POINTS
            + "100";

    public static final String MESSAGE_ARGUMENTS = "Tier: %1$s, Points: %2$s";

    public static final String MESSAGE_SET_TIER_SUCCESS = "Set point threshold for Tier: %1$s";
    public static final String MESSAGE_INVALID_TIER = "Point threshold in each tier must adhere to the following:\n"
            + "No tier < Bronze < Silver < Gold";

    private final Tier tier;
    private final Points points;

    /**
     * @param tier of the point threshold to set
     * @param points of the customer to be set
     */
    public SetTierCommand(Tier tier, Points points) {
        requireAllNonNull(tier, points);

        this.tier = tier;
        this.points = points;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!Tier.isValidPointThresholdForTier(tier, points)) {
            throw new CommandException(MESSAGE_INVALID_TIER);
        }

        tier.setPointsThreshold(points);

        model.applyFunctionOnCustomers((customer) -> {
            Points customerPoints = customer.getPoints();
            Tier customerTier = customer.getTier();

            Tier newTier = Tier.getTierFromPoints(customerPoints);

            // because there are only 4 Tier objects at any given time, setting the point threshold above
            // should also affect the customer's Tier, and so we should be able to use .equals here.
            if (!customerTier.equals(newTier)) {
                // if tier has changed, we need to edit the customer
                Customer editedCustomerNewTier = createEditedCustomer(customer);
                model.setCustomer(customer, editedCustomerNewTier);
            }
            return null;
        });

        model.updateFilteredCustomerList(PREDICATE_SHOW_ALL_CUSTOMERS);

        return new CommandResult(generateSuccessMessage(this.tier));
    }

    /**
     * Creates and returns a {@code Customer} with the details of {@code customerToEdit}
     */
    private Customer createEditedCustomer(Customer customerToEdit) {
        assert customerToEdit != null;
        CustomerType customerType = customerToEdit.getCustomerType();
        Name name = customerToEdit.getName();
        Phone phone = customerToEdit.getPhone();
        Email email = customerToEdit.getEmail();
        Address address = customerToEdit.getAddress();
        Set<Tag> tags = customerToEdit.getTags();
        Points points = customerToEdit.getPoints();
        Tier newTier = Tier.getTierFromPoints(points);
        Marked marked = customerToEdit.getMarked();
        Note note = customerToEdit.getNote();

        return new Customer(customerType, name, phone, email, address, tags, points, newTier, marked, note);
    }

    /**
     * Generates a command execution success message based on whether
     * the point threshold of the tier is set
     */
    private String generateSuccessMessage(Tier tier) {
        String message = MESSAGE_SET_TIER_SUCCESS;
        return String.format(message, tier);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetTierCommand)) {
            return false;
        }

        // state check
        SetTierCommand e = (SetTierCommand) other;
        return tier.equals(e.tier)
                && points.equals(e.points);
    }
}
