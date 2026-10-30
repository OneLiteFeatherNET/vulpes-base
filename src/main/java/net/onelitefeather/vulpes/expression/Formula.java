package net.onelitefeather.vulpes.expression;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * A named formula or expression: its name, the names of its parameters and its EvalEx source.
 *
 * <p>The name and the parameter names must be identifiers, and parameter names must be unique. The parameter list is
 * copied, so later changes to the list passed in do not affect the formula.
 *
 * @param name       the name the formula is called by, an identifier
 * @param parameters the parameter names in call order
 * @param source     the EvalEx source of the formula, not blank
 * @since 0.6.0
 */
public record Formula(String name, List<String> parameters, String source) {

    private static final Pattern IDENTIFIER = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    /**
     * Validates the formula and copies its parameters.
     *
     * @throws NullPointerException     if a part is {@code null}
     * @throws IllegalArgumentException if the name or a parameter is not an identifier, a parameter is declared twice,
     *                                  or the source is blank
     */
    public Formula {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(source, "source");
        if (!isIdentifier(name)) {
            throw new IllegalArgumentException("Formula name '%s' is not an identifier".formatted(name));
        }
        if (source.isBlank()) {
            throw new IllegalArgumentException("Formula '%s' has no source".formatted(name));
        }
        parameters = List.copyOf(parameters);
        Set<String> declared = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String parameter : parameters) {
            if (!isIdentifier(parameter)) {
                throw new IllegalArgumentException(
                        "Parameter '%s' of formula '%s' is not an identifier".formatted(parameter, name));
            }
            if (!declared.add(parameter)) {
                throw new IllegalArgumentException(
                        "Formula '%s' declares parameter '%s' more than once".formatted(name, parameter));
            }
        }
    }

    static boolean isIdentifier(String candidate) {
        return IDENTIFIER.matcher(candidate).matches();
    }
}
