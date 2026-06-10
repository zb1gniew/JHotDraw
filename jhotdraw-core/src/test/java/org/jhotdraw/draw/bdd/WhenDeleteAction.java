package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.RectangleFigure;

public class WhenDeleteAction extends Stage<WhenDeleteAction> {

    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    RectangleFigure figure;

    public WhenDeleteAction the_figure_is_deleted() {
        drawing.remove(figure);
        return self();
    }

    public WhenDeleteAction a_non_existent_figure_is_deleted() {
        drawing.remove(new RectangleFigure());
        return self();
    }

    public WhenDeleteAction one_of_the_figures_is_deleted() {
        drawing.remove(figure);
        return self();
    }
}
