package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jhotdraw.draw.figure.Figure;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mockito;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.mock;

public class SelectAllStage extends Stage<SelectAllStage> {

    @ProvidedScenarioState
    private DefaultDrawingView view;

    @ProvidedScenarioState
    private Drawing drawing;

    private final List<Figure> figures = new ArrayList<>();

    private void createView() {
        view = new DefaultDrawingView();
        drawing = mock(Drawing.class);
        Mockito.when(drawing.getDrawingArea(anyDouble())).thenReturn(new Rectangle2D.Double(0, 0, 0, 0));
        view.setDrawing(drawing);
    }

    private Figure figure(boolean selectable) {
        Figure f = mock(Figure.class);
        Mockito.when(f.isSelectable()).thenReturn(selectable);
        return f;
    }

    public SelectAllStage a_drawing_view_with_$_selectable_figures(int count) {
        createView();
        for (int i = 0; i < count; i++) {
            figures.add(figure(true));
        }
        Mockito.when(drawing.getChildren()).thenReturn(new ArrayList<>(figures));
        return self();
    }

    public SelectAllStage an_empty_drawing_view() {
        createView();
        Mockito.when(drawing.getChildren()).thenReturn(new ArrayList<>());
        return self();
    }

    public SelectAllStage a_drawing_view_with_$_selectable_and_$_locked_figures(int selectable, int locked) {
        createView();
        for (int i = 0; i < selectable; i++) {
            figures.add(figure(true));
        }
        for (int i = 0; i < locked; i++) {
            figures.add(figure(false));
        }
        Mockito.when(drawing.getChildren()).thenReturn(new ArrayList<>(figures));
        return self();
    }

    public SelectAllStage the_user_selects_all() {
        view.selectAll();
        return self();
    }

    public SelectAllStage all_$_figures_are_selected(int count) {
        assertThat(view.getSelectionCount()).isEqualTo(count);
        assertThat(view.getSelectedFigures()).containsExactlyInAnyOrderElementsOf(figures);
        return self();
    }

    public SelectAllStage nothing_is_selected() {
        assertThat(view.getSelectionCount()).isZero();
        assertThat(view.isSelectionEmpty()).isTrue();
        return self();
    }

    public SelectAllStage only_the_$_selectable_figures_are_selected(int selectable) {
        assertThat(view.getSelectionCount()).isEqualTo(selectable);
        assertThat(view.getSelectedFigures())
                .allMatch(Figure::isSelectable);
        return self();
    }
}
