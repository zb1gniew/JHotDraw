package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import javax.swing.Action;
import javax.swing.JButton;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrangementScenarioTest
        extends ScenarioTest<ArrangementScenarioTest.GivenADrawing,
        ArrangementScenarioTest.WhenTheUser,
        ArrangementScenarioTest.ThenTheDrawing> {

    @Test
    public void align_selected_figures_to_the_left_edge() {
        given().an_enabled_drawing_view_has_two_or_more_figures_selected();
        when().the_user_clicks_align_left();
        then().the_selected_transformable_figures_move_so_their_left_edges_match_the_leftmost_edge_of_the_selection();
    }

    @Test
    public void group_multiple_selected_figures() {
        given().a_drawing_view_has_more_than_one_figure_selected();
        when().the_user_clicks_group();
        then().the_selected_figures_are_replaced_by_one_selected_group_figure();
    }

    @Test
    public void bring_selected_figures_to_the_front() {
        given().an_enabled_drawing_view_has_one_or_more_figures_selected();
        when().the_user_clicks_bring_to_front();
        then().the_selected_figures_are_moved_to_the_front_of_the_drawing_order();
    }

    public static class GivenADrawing extends Stage<GivenADrawing> {

        @ProvidedScenarioState
        private ArrangementContext context;

        public GivenADrawing an_enabled_drawing_view_has_two_or_more_figures_selected() {
            createDrawingView();
            context.leftmost = new RectangleFigure(10, 20, 30, 40);
            context.other = new RectangleFigure(80, 50, 20, 20);
            addAndSelect(context.leftmost, context.other);
            return self();
        }

        public GivenADrawing a_drawing_view_has_more_than_one_figure_selected() {
            createDrawingView();
            context.leftmost = new RectangleFigure(10, 20, 30, 40);
            context.other = new RectangleFigure(80, 50, 20, 20);
            addAndSelect(context.leftmost, context.other);
            return self();
        }

        public GivenADrawing an_enabled_drawing_view_has_one_or_more_figures_selected() {
            createDrawingView();
            context.selectedBehind = new RectangleFigure(10, 10, 20, 20);
            context.unselectedInFront = new RectangleFigure(40, 10, 20, 20);
            context.drawing.add(context.selectedBehind);
            context.drawing.add(context.unselectedInFront);
            context.view.addToSelection(context.selectedBehind);
            return self();
        }

        private void createDrawingView() {
            context = new ArrangementContext();
            context.editor = new DefaultDrawingEditor();
            context.view = new DefaultDrawingView();
            context.drawing = new DefaultDrawing();
            context.view.setDrawing(context.drawing);
            context.view.setEnabled(true);
            context.editor.add(context.view);
        }

        private void addAndSelect(Figure first, Figure second) {
            context.drawing.add(first);
            context.drawing.add(second);
            context.view.addToSelection(first);
            context.view.addToSelection(second);
        }
    }

    public static class WhenTheUser extends Stage<WhenTheUser> {

        @ExpectedScenarioState
        private ArrangementContext context;

        public WhenTheUser the_user_clicks_align_left() {
            click(new AlignAction.West(context.editor));
            return self();
        }

        public WhenTheUser the_user_clicks_group() {
            click(new GroupAction(context.editor, new GroupFigure()));
            return self();
        }

        public WhenTheUser the_user_clicks_bring_to_front() {
            click(new BringToFrontAction(context.editor));
            return self();
        }

        private void click(Action action) {
            JButton button = GuiActionRunner.execute(new GuiQuery<JButton>() {
                @Override
                protected JButton executeInEDT() {
                    return new JButton(action);
                }
            });
            GuiActionRunner.execute(new GuiTask() {
                @Override
                protected void executeInEDT() {
                    button.doClick();
                }
            });
        }
    }

    public static class ThenTheDrawing extends Stage<ThenTheDrawing> {

        @ExpectedScenarioState
        private ArrangementContext context;

        public ThenTheDrawing the_selected_transformable_figures_move_so_their_left_edges_match_the_leftmost_edge_of_the_selection() {
            assertThat(context.leftmost.getBounds().x).isEqualTo(10.0);
            assertThat(context.other.getBounds().x).isEqualTo(10.0);
            return self();
        }

        public ThenTheDrawing the_selected_figures_are_replaced_by_one_selected_group_figure() {
            assertThat(context.view.getSelectedFigures()).hasSize(1);

            Figure selectedFigure = context.view.getSelectedFigures().iterator().next();
            assertThat(selectedFigure).isInstanceOf(CompositeFigure.class);
            assertThat(((CompositeFigure) selectedFigure).getChildren())
                    .containsExactly(context.leftmost, context.other);
            return self();
        }

        public ThenTheDrawing the_selected_figures_are_moved_to_the_front_of_the_drawing_order() {
            assertThat(context.drawing.getChildren())
                    .containsExactly(context.unselectedInFront, context.selectedBehind);
            return self();
        }
    }

    private static class ArrangementContext {

        private DefaultDrawingEditor editor;
        private DefaultDrawingView view;
        private Drawing drawing;
        private RectangleFigure leftmost;
        private RectangleFigure other;
        private RectangleFigure selectedBehind;
        private RectangleFigure unselectedInFront;
    }
}
