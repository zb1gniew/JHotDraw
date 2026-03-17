package org.jhotdraw.draw; // Ważne, żeby pakiet się zgadzał!

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jhotdraw.draw.figure.RectangleFigure;

public class DeleteFigureTest {
    private Drawing drawing;
    private RectangleFigure figure;

    @Before
    public void setUp() {
        // Initiliaztion before every test
        drawing = new DefaultDrawing();
        figure = new RectangleFigure();
        drawing.add(figure);
    }

    @Test
    public void testDeleteExistingFigure() {
        // 1. Check the initial state (Best Case)
        assertEquals(1, drawing.getChildCount());
        
        // 2. Perform action
        drawing.remove(figure);
        
        // 3. Check result
        assertEquals(0, drawing.getChildCount());
    }

    @Test
    public void testDeleteNonExistentFigure() {
        // Boundary Case: 
        RectangleFigure stranger = new RectangleFigure();
        drawing.remove(stranger);
        
        assertEquals(1, drawing.getChildCount());
    }

    @Test
    public void testInvariants() {
        // Java Assertions
        assert drawing != null : "Drawing cannot be null";
        
        drawing.remove(figure);
        
        assert drawing.getChildCount() >= 0 : "Count cannot be negative";
    }
}