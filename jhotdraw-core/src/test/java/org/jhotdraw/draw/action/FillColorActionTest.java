package org.jhotdraw.draw.action;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Color;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.*;

public class FillColorActionTest {

    private RectangleFigure figure;

    @Before
    public void setUp() {
        figure = new RectangleFigure();
    }

    // Best case: color is stored and retrieved correctly
    @Test
    public void testSetFillColor() {
        figure.set(AttributeKeys.FILL_COLOR, Color.RED);
        assertEquals(Color.RED, figure.get(AttributeKeys.FILL_COLOR));
    }

    // Boundary: fill color can be set to null (transparent)
    @Test
    public void testSetFillColorNull() {
        figure.set(AttributeKeys.FILL_COLOR, null);
        assertNull(figure.get(AttributeKeys.FILL_COLOR));
    }

    // Best case: color changes from one value to another
    @Test
    public void testChangeFillColor() {
        figure.set(AttributeKeys.FILL_COLOR, Color.RED);
        figure.set(AttributeKeys.FILL_COLOR, Color.BLUE);
        assertEquals(Color.BLUE, figure.get(AttributeKeys.FILL_COLOR));
    }

    // Boundary: default fill color is white
    @Test
    public void testDefaultFillColor() {
        assertEquals(Color.white, figure.get(AttributeKeys.FILL_COLOR));
    }

    // Undo: restoring attributes brings back the original color
    @Test
    public void testUndoFillColor() {
        figure.set(AttributeKeys.FILL_COLOR, Color.RED);
        Object restoreData = figure.getAttributesRestoreData();

        figure.set(AttributeKeys.FILL_COLOR, Color.BLUE);
        assertEquals(Color.BLUE, figure.get(AttributeKeys.FILL_COLOR));

        figure.restoreAttributesTo(restoreData);
        assertEquals(Color.RED, figure.get(AttributeKeys.FILL_COLOR));
    }
}
