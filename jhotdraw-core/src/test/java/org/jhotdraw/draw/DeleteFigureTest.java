package org.jhotdraw.draw;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;

public class DeleteFigureTest {

    private Drawing drawing;
    private RectangleFigure figure;

    @Before
    public void setUp() {
        drawing = new DefaultDrawing();
        figure = new RectangleFigure();
        drawing.add(figure);
    }

    // --- Best Case ---

    @Test
    public void testDeleteExistingFigure() {
        assertEquals(1, drawing.getChildCount());
        drawing.remove(figure);
        assertEquals(0, drawing.getChildCount());
    }

    @Test
    public void testDeleteOneOfMultipleFigures() {
        RectangleFigure second = new RectangleFigure();
        RectangleFigure third = new RectangleFigure();
        drawing.add(second);
        drawing.add(third);
        assertEquals(3, drawing.getChildCount());

        drawing.remove(second);

        assertEquals(2, drawing.getChildCount());
    }

    @Test
    public void testRemoveReturnsTrueForExistingFigure() {
        boolean result = drawing.remove(figure);
        assertTrue(result);
    }

    // --- Boundary Cases ---

    @Test
    public void testDeleteNonExistentFigure() {
        RectangleFigure stranger = new RectangleFigure();
        boolean result = drawing.remove(stranger);
        assertFalse(result);
        assertEquals(1, drawing.getChildCount());
    }

    @Test
    public void testDeleteFromEmptyDrawing() {
        Drawing emptyDrawing = new DefaultDrawing();
        RectangleFigure stranger = new RectangleFigure();
        boolean result = emptyDrawing.remove(stranger);
        assertFalse(result);
        assertEquals(0, emptyDrawing.getChildCount());
    }

    @Test
    public void testDeleteSameFigureTwice() {
        drawing.remove(figure);
        assertEquals(0, drawing.getChildCount());
        boolean result = drawing.remove(figure);
        assertFalse(result);
        assertEquals(0, drawing.getChildCount());
    }

    // --- Mock / Stub ---

    @Test
    public void testRemoveNotifiesFigure() {
        Figure mockFigure = mock(Figure.class);
        drawing.add(mockFigure);
        drawing.remove(mockFigure);
        verify(mockFigure).removeNotify(drawing);
    }

    // --- Invariants ---

    @Test
    public void testInvariants() {
        assert drawing != null : "Drawing cannot be null";
        drawing.remove(figure);
        assert drawing.getChildCount() >= 0 : "Count cannot be negative";
    }
}
