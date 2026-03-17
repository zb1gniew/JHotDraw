package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import static org.assertj.core.api.Assertions.assertThat;
import org.jhotdraw.draw.*;

public class ThenResult extends Stage<ThenResult> {
    @ExpectedScenarioState
    protected Drawing drawing;

    public ThenResult the_drawing_should_be_empty() {
        assertThat(drawing.getChildCount()).isEqualTo(0);
        return self();
    }
}