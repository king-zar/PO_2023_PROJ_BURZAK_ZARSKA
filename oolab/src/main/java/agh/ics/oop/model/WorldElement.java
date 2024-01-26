package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;

/**
 * The interface representing elements in the world.
 */
public interface WorldElement {
    /**
     * Get the position of the world element.
     *
     * @return The position of the world element.
     */
    Vector2d getPosition();

    /**
     * Get the string representation of the world element.
     *
     * @return The string representation of the world element.
     */
    String toString();
}