package org.jhotdraw.draw.action;

import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.TransformEdit;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.jhotdraw.undo.CompositeEdit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the Align Left command ({@link AlignAction.West}).
 *
 * <p>Mocks the editor, view and drawing, and uses real rectangle figures to
 * check the alignment behavior.
 */
public class AlignActionTest {

    private DrawingEditor editor;
    private DrawingView view;
    private Drawing drawing;

    @Before
    public void setUp() {
        editor = mock(DrawingEditor.class);
        view = mock(DrawingView.class);
        drawing = mock(Drawing.class);
        when(editor.getActiveView()).thenReturn(view);
        when(view.getDrawing()).thenReturn(drawing);
        when(view.isEnabled()).thenReturn(true);
    }

    /** Stubs the view to report the given figures as the current selection. */
    private Set<Figure> selectFigures(Figure... figures) {
        Set<Figure> selection = new LinkedHashSet<>();
        for (Figure f : figures) {
            selection.add(f);
        }
        when(view.getSelectedFigures()).thenReturn(selection);
        when(view.getSelectionCount()).thenReturn(selection.size());
        return selection;
    }

    private static ActionEvent click(Object source) {
        return new ActionEvent(source, ActionEvent.ACTION_PERFORMED, "align");
    }

    // --- Best case ---

    @Test
    public void alignLeftMovesEverySelectedFigureToLeftmostEdge() {
        RectangleFigure leftmost = new RectangleFigure(10, 20, 30, 40);
        RectangleFigure other = new RectangleFigure(80, 50, 20, 20);
        selectFigures(leftmost, other);

        new AlignAction.West(editor).actionPerformed(click(this));

        assertEquals(10.0, leftmost.getBounds().x, 0.0);
        assertEquals(10.0, other.getBounds().x, 0.0);
    }

    @Test
    public void alignLeftPreservesSizeAndVerticalPosition() {
        RectangleFigure a = new RectangleFigure(10, 20, 30, 40);
        RectangleFigure b = new RectangleFigure(80, 55, 25, 15);
        selectFigures(a, b);

        new AlignAction.West(editor).actionPerformed(click(this));

        assertEquals(25.0, b.getBounds().width, 0.0);
        assertEquals(15.0, b.getBounds().height, 0.0);
        assertEquals(55.0, b.getBounds().y, 0.0);
    }

    @Test
    public void alignLeftLeavesTheAlreadyLeftmostFigureInPlace() {
        RectangleFigure leftmost = new RectangleFigure(5, 5, 10, 10);
        RectangleFigure right = new RectangleFigure(100, 5, 10, 10);
        selectFigures(leftmost, right);

        new AlignAction.West(editor).actionPerformed(click(this));

        assertEquals(5.0, leftmost.getBounds().x, 0.0);
        assertEquals(5.0, leftmost.getBounds().y, 0.0);
    }

    @Test
    public void selectionBoundsLeftEdgeIsTheLeftmostFigure() {
        RectangleFigure a = new RectangleFigure(40, 10, 20, 20);
        RectangleFigure b = new RectangleFigure(10, 10, 20, 20);
        selectFigures(a, b);

        assertEquals(10.0, new AlignAction.West(editor).getSelectionBounds().x, 0.0);
    }

    // --- Boundary: enabled state ---

    @Test
    public void disabledWhenNothingSelected() {
        selectFigures();
        assertFalse(new AlignAction.West(editor).isEnabled());
    }

    @Test
    public void disabledWhenOnlyOneFigureSelected() {
        selectFigures(new RectangleFigure(0, 0, 10, 10));
        assertFalse(new AlignAction.West(editor).isEnabled());
    }

    @Test
    public void enabledOnlyWhenMoreThanOneFigureSelected() {
        selectFigures(new RectangleFigure(0, 0, 10, 10), new RectangleFigure(20, 0, 10, 10));
        assertTrue(new AlignAction.West(editor).isEnabled());
    }

    @Test
    public void disabledWhenViewIsDisabled() {
        when(view.isEnabled()).thenReturn(false);
        selectFigures(new RectangleFigure(0, 0, 10, 10), new RectangleFigure(20, 0, 10, 10));
        assertFalse(new AlignAction.West(editor).isEnabled());
    }

    @Test
    public void disabledWhenNoActiveView() {
        when(editor.getActiveView()).thenReturn(null);
        assertFalse(new AlignAction.West(editor).isEnabled());
    }

    // --- Side effects ---

    @Test
    public void unselectedFigureIsNotMoved() {
        RectangleFigure selectedA = new RectangleFigure(10, 10, 20, 20);
        RectangleFigure selectedB = new RectangleFigure(50, 10, 20, 20);
        RectangleFigure unselected = new RectangleFigure(100, 100, 20, 20);
        selectFigures(selectedA, selectedB); // 'unselected' left out on purpose

        new AlignAction.West(editor).actionPerformed(click(this));

        assertEquals(100.0, unselected.getBounds().x, 0.0);
        assertEquals(100.0, unselected.getBounds().y, 0.0);
    }

    @Test
    public void nonTransformableSelectedFigureIsSkipped() {
        RectangleFigure movable = new RectangleFigure(10, 10, 20, 20);
        RectangleFigure locked = new RectangleFigure(80, 10, 20, 20);
        locked.setTransformable(false);
        selectFigures(movable, locked);

        new AlignAction.West(editor).actionPerformed(click(this));

        assertEquals(80.0, locked.getBounds().x, 0.0);
        assertEquals(10.0, movable.getBounds().x, 0.0);
    }

    // --- Undo / redo ---

    @Test
    public void undoRestoresOriginalBoundsAndRedoReappliesAlignment() {
        RectangleFigure leftmost = new RectangleFigure(10, 20, 30, 40);
        RectangleFigure other = new RectangleFigure(80, 50, 20, 20);
        selectFigures(leftmost, other);

        new AlignAction.West(editor).actionPerformed(click(this));
        assertEquals(10.0, other.getBounds().x, 0.0);

        List<TransformEdit> edits = capturedTransformEdits();

        // Undo in reverse order, the way an undo manager would.
        for (int i = edits.size() - 1; i >= 0; i--) {
            edits.get(i).undo();
        }
        assertEquals(80.0, other.getBounds().x, 0.0);

        for (TransformEdit edit : edits) {
            edit.redo();
        }
        assertEquals(10.0, other.getBounds().x, 0.0);
    }

    @Test
    public void alignmentFiresUndoableEditsToTheDrawing() {
        selectFigures(new RectangleFigure(10, 10, 20, 20), new RectangleFigure(80, 10, 20, 20));

        new AlignAction.West(editor).actionPerformed(click(this));

        verify(drawing, atLeastOnce()).fireUndoableEditHappened(org.mockito.ArgumentMatchers.any(UndoableEdit.class));
    }

    @Test
    public void alignmentIsBracketedByOneCompositeEditForSingleStepUndo() {
        selectFigures(new RectangleFigure(10, 10, 20, 20), new RectangleFigure(80, 10, 20, 20));

        new AlignAction.West(editor).actionPerformed(click(this));

        ArgumentCaptor<UndoableEdit> captor = ArgumentCaptor.forClass(UndoableEdit.class);
        verify(drawing, atLeastOnce()).fireUndoableEditHappened(captor.capture());
        List<UndoableEdit> fired = captor.getAllValues();

        // The same CompositeEdit is fired first and last, with the per-figure
        // TransformEdits in between, so the undo manager treats the whole
        // alignment as one step.
        assertTrue(fired.get(0) instanceof CompositeEdit);
        assertSame(fired.get(0), fired.get(fired.size() - 1));
    }

    /** Pulls the TransformEdits the action fired at the drawing. */
    private List<TransformEdit> capturedTransformEdits() {
        ArgumentCaptor<UndoableEdit> captor = ArgumentCaptor.forClass(UndoableEdit.class);
        verify(drawing, atLeastOnce()).fireUndoableEditHappened(captor.capture());
        List<TransformEdit> edits = new java.util.ArrayList<>();
        for (UndoableEdit edit : captor.getAllValues()) {
            if (edit instanceof TransformEdit) {
                edits.add((TransformEdit) edit);
            }
        }
        return edits;
    }
}
