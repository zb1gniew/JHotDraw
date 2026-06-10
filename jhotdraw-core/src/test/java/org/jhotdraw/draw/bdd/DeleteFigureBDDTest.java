package org.jhotdraw.draw.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class DeleteFigureBDDTest extends ScenarioTest<GivenDrawingStage, WhenDeleteAction, ThenDrawingState> {

    @Test
    public void deleting_an_existing_figure_removes_it_from_the_drawing() {
        given().a_drawing_with_one_figure();
        when().the_figure_is_deleted();
        then().the_drawing_is_empty();
    }

    @Test
    public void deleting_a_non_existent_figure_does_not_change_the_drawing() {
        given().a_drawing_with_one_figure();
        when().a_non_existent_figure_is_deleted();
        then().the_drawing_has_$_figures(1);
    }

    @Test
    public void deleting_one_figure_from_multiple_leaves_the_rest() {
        given().a_drawing_with_multiple_figures();
        when().one_of_the_figures_is_deleted();
        then().the_drawing_has_$_figures(2);
    }
}
