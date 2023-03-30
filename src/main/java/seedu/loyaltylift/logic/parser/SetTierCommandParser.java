package seedu.loyaltylift.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.loyaltylift.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_POINTS;
import static seedu.loyaltylift.logic.parser.CliSyntax.PREFIX_TIER;

import java.util.stream.Stream;

import seedu.loyaltylift.logic.commands.SetTierCommand;
import seedu.loyaltylift.logic.parser.exceptions.ParseException;
import seedu.loyaltylift.model.customer.Points;
import seedu.loyaltylift.model.customer.Tier;

/**
 * Parses input arguments and creates a new SetTierCommand object
 */
public class SetTierCommandParser implements Parser<SetTierCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the SetTierCommand
     * and returns a SetTierCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public SetTierCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIER, PREFIX_POINTS);

        if (!arePrefixesPresent(argMultimap, PREFIX_TIER, PREFIX_POINTS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    SetTierCommand.MESSAGE_USAGE));
        }
        Tier tier = ParserUtil.parseTier((argMultimap.getValue(PREFIX_TIER).get()));
        Points points = ParserUtil.parsePoints((argMultimap.getValue(PREFIX_POINTS).get()));

        return new SetTierCommand(tier, points);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
