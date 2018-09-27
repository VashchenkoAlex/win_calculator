package win_calculator.controller.digits;

import win_calculator.model.nodes.events.EventType;

public class FiveDigit implements Digit {

    private static final String VALUE = "5";
    private static final EventType TYPE = EventType.DIGIT;

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public EventType getType() {

        return TYPE;
    }
}
