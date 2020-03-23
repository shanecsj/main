package seedu.address.model.ingredient;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.ingredient.exceptions.DuplicateIngredientException;
import seedu.address.model.ingredient.exceptions.IngredientNotFoundException;


/**
 * A list of ingredients that enforces uniqueness between its elements and does not allow nulls.
 * An ingredient is considered unique by comparing using {@code Ingredient#isSameIngredient(Ingredient)}. As such,
 * adding and updating of ingredients uses Ingredient#isSameIngredient(Ingredient) for equality so as to ensure
 * that the ingredient being added or updated is unique in terms of identity in the UniqueIngredientList.
 */

public class UniqueIngredientList implements Iterable<Ingredient> {

    private final ObservableList<Ingredient> internalList = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Checks if otherIngredient already exists in the list.
     * @param otherIngredient
     * @return true is otherIngredient exists, false otherwise.
     */
    public boolean contains(Ingredient otherIngredient) {
        requireNonNull(otherIngredient);
        return internalList.stream().anyMatch(otherIngredient::isSameIngredient);
    }

    /**
     * Returns the ingredient with the same name {@code toFind}.
     */
    public Ingredient get(Ingredient toFind) {
        requireNonNull(toFind);
        return internalList.stream().filter(toFind::isSameIngredient).findFirst().get();
    }

    /**
     * Adds an ingredient to the list if it does not yet exist.
     */
    public void add(Ingredient toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateIngredientException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the ingredient {@code target} in the list with {@code editedIngredient}.
     * {@code target} must exist in the list.
     * The ingredient identity of {@code editedIngredient} must not be the same as another existing ingredient in the
     * list.
     */
    public void setIngredient(Ingredient target, Ingredient editedIngredient) {
        requireAllNonNull(target, editedIngredient);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new IngredientNotFoundException();
        }

        if (!target.isSameIngredient(editedIngredient) && contains(editedIngredient)) {
            throw new DuplicateIngredientException();
        }

        internalList.set(index, editedIngredient);
    }

    /**
     * Removes the equivalent ingredient from the list.
     * The ingredient must exist in the list.
     */
    public void remove(Ingredient toRemove) {
        requireNonNull(toRemove);

        if (!contains(toRemove)) {
            throw new IngredientNotFoundException();
        }

        Ingredient curr = get(toRemove);
        IngredientQuantity updated = curr.getQuantity().subtract(toRemove.getQuantity());

        if (updated.equals((curr.getQuantity()))) {
            internalList.remove(toRemove);
        }

        setIngredient(toRemove, new Ingredient(curr.getName(), updated));
    }

    public void setIngredients(UniqueIngredientList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code ingredients}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setIngredients(List<Ingredient> ingredients) {
        requireAllNonNull(ingredients);
        if (!ingredientsAreUnique(ingredients)) {
            throw new DuplicateIngredientException();
        }

        internalList.setAll(ingredients);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Ingredient> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueIngredientList // instanceof handles nulls
                && internalList.equals(((UniqueIngredientList) other).internalList));
    }

    /**
     * Checks if all the ingredients given in the list are unique and has no repeats.
     * @param ingredients
     * @return true if all ingredients are unique, false otherwise.
     */
    private boolean ingredientsAreUnique(List<Ingredient> ingredients) {
        for (int i = 0; i < ingredients.size() - 1; i++) {
            for (int j = i + 1; j < ingredients.size(); j++) {
                if (ingredients.get(i).isSameIngredient(ingredients.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "";

        for (Ingredient i : internalList) {
            string += i.toString() + ", ";
        }

        return string.length() - 3 > 0 ? string.substring(0, string.length() - 3) : string;
    }
}
