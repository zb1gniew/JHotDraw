package org.jhotdraw.draw;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectAllTest {

    private DefaultDrawingView view;
    private Drawing drawing;

    @Before
    public void setUp() {
        view = new DefaultDrawingView();
        drawing = mock(Drawing.class);
        when(drawing.getDrawingArea(anyDouble())).thenReturn(new Rectangle2D.Double(0, 0, 0, 0));
        view.setDrawing(drawing);
    }

    private Figure figure(boolean selectable) {
        Figure f = mock(Figure.class);
        when(f.isSelectable()).thenReturn(selectable);
        return f;
    }

    @Test
    public void selectAllSelectsEverySelectableFigure() {
        Figure a = figure(true);
        Figure b = figure(true);
        Figure c = figure(true);
        when(drawing.getChildren()).thenReturn(Arrays.asList(a, b, c));

        view.selectAll();

        assertEquals(3, view.getSelectionCount());
        assertTrue(view.getSelectedFigures().containsAll(Arrays.asList(a, b, c)));
    }

    @Test
    public void selectAllOnEmptyCanvasSelectsNothing() {
        when(drawing.getChildren()).thenReturn(Collections.<Figure>emptyList());

        view.selectAll();

        assertEquals(0, view.getSelectionCount());
        assertTrue(view.isSelectionEmpty());
    }

    @Test
    public void selectAllSkipsNonSelectableFigures() {
        Figure selectable = figure(true);
        Figure locked = figure(false);
        when(drawing.getChildren()).thenReturn(Arrays.asList(selectable, locked));

        view.selectAll();

        assertEquals(1, view.getSelectionCount());
        assertTrue(view.getSelectedFigures().contains(selectable));
        assertFalse(view.getSelectedFigures().contains(locked));
    }

    @Test
    public void selectAllWithAllFiguresLockedSelectsNothing() {
        when(drawing.getChildren()).thenReturn(Arrays.asList(figure(false), figure(false)));

        view.selectAll();

        assertEquals(0, view.getSelectionCount());
    }

    @Test
    public void selectAllReplacesPreviousSelection() {
        Figure a = figure(true);
        Figure b = figure(true);
        when(drawing.getChildren()).thenReturn(Arrays.asList(a, b));

        view.selectAll();
        view.selectAll();

        assertEquals(2, view.getSelectionCount());
    }

    @Test
    public void selectAllNotifiesSelectionListener() {
        Figure a = figure(true);
        when(drawing.getChildren()).thenReturn(Collections.singletonList(a));
        FigureSelectionListener listener = mock(FigureSelectionListener.class);
        view.addFigureSelectionListener(listener);

        view.selectAll();

        ArgumentCaptor<FigureSelectionEvent> event =
                ArgumentCaptor.forClass(FigureSelectionEvent.class);
        verify(listener).selectionChanged(event.capture());
        assertTrue(event.getValue().getNewSelection().contains(a));
    }

    @Test
    public void selectAllChecksSelectabilityOfEachFigure() {
        Figure a = figure(true);
        Figure b = figure(true);
        when(drawing.getChildren()).thenReturn(Arrays.asList(a, b));

        view.selectAll();

        verify(a).isSelectable();
        verify(b).isSelectable();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void selectedFiguresSnapshotIsUnmodifiable() {
        when(drawing.getChildren()).thenReturn(Collections.singletonList(figure(true)));
        view.selectAll();

        view.getSelectedFigures().clear();
    }
}
