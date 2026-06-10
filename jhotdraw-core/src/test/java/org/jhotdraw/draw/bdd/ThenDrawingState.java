package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.core.api.Assertions;
import org.jhotdraw.draw.Drawing;

public class ThenDrawingState extends Stage<ThenDrawingState> {

    @ExpectedScenarioState
    Drawing drawing;

    public ThenDrawingState the_drawing_is_empty() {
        Assertions.assertThat(drawing.getChildCount()).isEqualTo(0);
        return self();
    }

    public ThenDrawingState the_drawing_has_$_figures(int count) {
        Assertions.assertThat(drawing.getChildCount()).isEqualTo(count);
        return self();
    }
}
