package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.jhotdraw.draw.figure.Figure;

public class GivenDrawing extends Stage<GivenDrawing> {
    
    @ProvidedScenarioState
    protected Drawing drawing;
    
    @ProvidedScenarioState
    protected Figure figure;

    public GivenDrawing a_drawing_with_a_rectangle() {
        drawing = new DefaultDrawing();
        figure = new RectangleFigure();
        drawing.add(figure);
        return self();
    }
}