package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.RectangleFigure;

public class GivenDrawingStage extends Stage<GivenDrawingStage> {

    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    RectangleFigure figure;

    public GivenDrawingStage a_drawing_with_one_figure() {
        drawing = new DefaultDrawing();
        figure = new RectangleFigure();
        drawing.add(figure);
        return self();
    }

    public GivenDrawingStage a_drawing_with_multiple_figures() {
        drawing = new DefaultDrawing();
        figure = new RectangleFigure();
        drawing.add(figure);
        drawing.add(new RectangleFigure());
        drawing.add(new RectangleFigure());
        return self();
    }
}
