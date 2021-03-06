package io.github.n34t0.platform;

/**
 * The content returned by the methods depends on the object type.
 */
public interface CodeCompletionElement {

    /**
     *  @return the string which will be inserted into the editor when this element is chosen.
     */
    String getName();

    /**
     * @return the type of the variable or the type returned by the method. Could be null.
     */
    String getType();

    /**
     * @return a list of method parameters. Could be null.
     */
    String getTail();

}
