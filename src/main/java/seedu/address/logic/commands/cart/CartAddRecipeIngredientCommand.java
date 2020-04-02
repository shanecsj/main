package seedu.address.logic.commands.cart;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENT_QUANTITY;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ingredient.Ingredient;
import seedu.address.model.ingredient.UniqueIngredientList;

/**
 * Adds all the ingredients from the indexed recipe to cart
 */
public class CartAddRecipeIngredientCommand extends CartAddCommand {

    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_SUCCESS = "Ingredients from recipe %1$d added.";
    public static final String MESSAGE_USAGE = "\n" + COMMAND_CATEGORY + " " + COMMAND_WORD
            + ": This commands allows you to add all the ingredients from a recipe to your cart.\n"
            + "Parameters for adding an ingredient into your cart is as follows: \n"
            + PREFIX_INGREDIENT_NAME + "INGREDIENT "
            + PREFIX_INGREDIENT_QUANTITY + "QUANTITY\n"
            + "Example: " + COMMAND_CATEGORY + " " + COMMAND_WORD + " "
            + PREFIX_INGREDIENT_NAME + "Eggs "
            + PREFIX_INGREDIENT_QUANTITY + "10\n";

    private final int recipeIndex;

    /**
     * Creates a CartAddIngredientCommand to add the specified {@code Ingredient} to the cart
     */
    public CartAddRecipeIngredientCommand(int recipeIndex) {
        this.recipeIndex = recipeIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (recipeIndex <= 0 || recipeIndex > model.getCookbook().getRecipeList().size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX,
                    MESSAGE_USAGE));
        }

        UniqueIngredientList ingredients = model.getCookbook().getRecipeList().get(recipeIndex - 1).getIngredients();

        for (Ingredient i : ingredients) {
            model.addCartIngredient(i);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, recipeIndex));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CartAddRecipeIngredientCommand // instanceof handles nulls
                && (recipeIndex == ((CartAddRecipeIngredientCommand) other).recipeIndex));
    }
}