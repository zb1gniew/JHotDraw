package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.*;

public class WhenAction extends Stage<WhenAction> {
    @ExpectedScenarioState
    protected Drawing drawing;
    
    @ExpectedScenarioState
    protected Figure figure;

    public WhenAction the_figure_is_deleted() {
        drawing.remove(figure);
        return self();
    }
}