package org.jhotdraw.draw;

import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

public class SelectAllScenarioTest extends SimpleScenarioTest<SelectAllStage> {

    @Test
    @As("Selecting all picks every selectable figure")
    public void selecting_all_picks_every_selectable_figure() {
        given().a_drawing_view_with_$_selectable_figures(3);
        when().the_user_selects_all();
        then().all_$_figures_are_selected(3);
    }

    @Test
    @As("Selecting all on an empty canvas selects nothing")
    public void selecting_all_on_an_empty_canvas_selects_nothing() {
        given().an_empty_drawing_view();
        when().the_user_selects_all();
        then().nothing_is_selected();
    }

    @Test
    @As("Selecting all leaves locked figures alone")
    public void selecting_all_leaves_locked_figures_alone() {
        given().a_drawing_view_with_$_selectable_and_$_locked_figures(2, 1);
        when().the_user_selects_all();
        then().only_the_$_selectable_figures_are_selected(2);
    }
}
