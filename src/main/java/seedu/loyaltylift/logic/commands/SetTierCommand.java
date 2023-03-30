package seedu.loyaltylift.logic.commands;

import static seedu.loyaltylift.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_POINTS;
import static seedu.loyaltylift.model.Model.PREDICATE_SHOW_ALL_CUSTOMERS;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_TIER;

import java.util.List;
import java.util.Set;

import seedu.loyaltylift.commons.core.Messages;
import seedu.loyaltylift.commons.core.index.Index;
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
            + PREFIX_TIER + " (must be an integer, 1, 2 or 3) "
            + PREFIX_POINTS + "[POINTS]\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_TIER + "1 "
            + PREFIX_POINTS
            + "100";

    public static final String MESSAGE_ARGUMENTS = "Tier: %1$s, Points: %2$s";

    public static final String MESSAGE_SET_POINTS_SUCCESS = "Set point threshold for Tier: %1$s";

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
        /**
        List<Customer> lastShownList = model.getFilteredCustomerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CUSTOMER_DISPLAYED_INDEX);
        }

        Customer customerToEdit = lastShownList.get(index.getZeroBased());
        Customer editedCustomerWithPoints = createEditedCustomer(customerToEdit);

        model.setCustomer(customerToEdit, editedCustomerWithPoints);
        model.updateFilteredCustomerList(PREDICATE_SHOW_ALL_CUSTOMERS);

        return new CommandResult(generateSuccessMessage(editedCustomerWithPoints));
         */
        throw new CommandException("this is being executed!!!");
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
        Marked marked = customerToEdit.getMarked();
        Note note = customerToEdit.getNote();

        //THIS METHOD DOES NOT DO ANYTHING
        return new Customer(customerType, name, phone, email, address, tags, this.points, this.tier, marked, note);
    }

    /**
     * Generates a command execution success message based on whether
     * the point threshold of the tier is set
     */
    private String generateSuccessMessage(Tier tier) {
        String message = MESSAGE_SET_POINTS_SUCCESS;
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
