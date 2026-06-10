package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import java.awt.Color;
import java.util.HashMap;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Test;

public class FillColorBDDTest extends ScenarioTest<
        FillColorBDDTest.GivenStage,
        FillColorBDDTest.WhenStage,
        FillColorBDDTest.ThenStage> {

    @Test
    public void applyingFillColorToFigure() {
        given().aRectangleFigureIsSelected();
        when().theUserAppliesFillColor(Color.RED);
        then().theFigureFillColorIs(Color.RED);
    }

    @Test
    public void undoingFillColorChange() {
        given().aRectangleFigureWithFillColor(Color.RED);
        when().theUserUndoesTheChange();
        then().theFigureFillColorIs(Color.white);
    }

    @Test
    public void cancellingDialogLeavesColorUnchanged() {
        given().aRectangleFigureWithFillColor(Color.RED);
        when().theUserCancelsTheDialog();
        then().theFigureFillColorIs(Color.RED);
    }

    // ---- STAGES ----

    static class GivenStage extends Stage<GivenStage> {

        @ScenarioState
        RectangleFigure figure;

        public GivenStage aRectangleFigureIsSelected() {
            figure = new RectangleFigure();
            return self();
        }

        public GivenStage aRectangleFigureWithFillColor(Color color) {
            figure = new RectangleFigure();
            figure.set(AttributeKeys.FILL_COLOR, color);
            return self();
        }
    }

    static class WhenStage extends Stage<WhenStage> {

        @ScenarioState
        RectangleFigure figure;

        public WhenStage theUserAppliesFillColor(Color color) {
            figure.set(AttributeKeys.FILL_COLOR, color);
            return self();
        }

        public WhenStage theUserUndoesTheChange() {
            figure.restoreAttributesTo(new HashMap<>());
            return self();
        }

        public WhenStage theUserCancelsTheDialog() {
            return self();
        }
    }

    static class ThenStage extends Stage<ThenStage> {

        @ScenarioState
        RectangleFigure figure;

        public ThenStage theFigureFillColorIs(Color expected) {
            org.assertj.core.api.Assertions
                .assertThat(figure.get(AttributeKeys.FILL_COLOR))
                .isEqualTo(expected);
            return self();
        }
    }
}