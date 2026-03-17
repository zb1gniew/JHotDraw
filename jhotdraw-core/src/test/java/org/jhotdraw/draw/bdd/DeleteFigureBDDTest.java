package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class DeleteFigureBDDTest extends ScenarioTest<GivenDrawing, WhenAction, ThenResult> {
    @Test
    public void deleting_a_figure_removes_it_from_the_canvas() {
        given().a_drawing_with_a_rectangle();
        when().the_figure_is_deleted();
        then().the_drawing_should_be_empty();
    }
}