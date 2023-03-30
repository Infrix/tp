package seedu.loyaltylift.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.loyaltylift.model.Model.PREDICATE_SHOW_ALL_CUSTOMERS;

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
 * Bookmarks an existing customer in the address book.
 */
public class MarkCustomerCommand extends Command {

    public static final String COMMAND_WORD = "markc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Bookmarks the customer identified by the index number used in the displayed customer list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_CUSTOMER_SUCCESS = "Bookmarked Customer: %1$s";

    private final Index index;

    /**
     * @param index of the customer in the filtered customer list to bookmark.
     */
    public MarkCustomerCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Customer> lastShownList = model.getFilteredCustomerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CUSTOMER_DISPLAYED_INDEX);
        }

        Customer customerToMark = lastShownList.get(index.getZeroBased());
        Customer markedCustomer = createMarkedCustomer(customerToMark);
        model.setCustomer(customerToMark, markedCustomer);
        model.updateFilteredCustomerList(PREDICATE_SHOW_ALL_CUSTOMERS);
        return new CommandResult(String.format(MESSAGE_MARK_CUSTOMER_SUCCESS, markedCustomer));
    }

    /**
     * Creates and returns a {@code Customer} with the same details as {@code customerToMark}
     * but with the boolean {@code marked} set to true.
     */
    private static Customer createMarkedCustomer(Customer customerToMark) {
        assert customerToMark != null;

        CustomerType customerType = customerToMark.getCustomerType();
        Name name = customerToMark.getName();
        Phone phone = customerToMark.getPhone();
        Email email = customerToMark.getEmail();
        Address address = customerToMark.getAddress();
        Set<Tag> tags = customerToMark.getTags();
        Points points = customerToMark.getPoints();
        Tier tier = customerToMark.getTier();
        Note note = customerToMark.getNote();
        Marked marked = new Marked(true);

        return new Customer(customerType, name, phone, email, address, tags, points, tier, marked, note);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkCustomerCommand // instanceof handles nulls
                && index.equals(((MarkCustomerCommand) other).index)); // state check
    }
}
