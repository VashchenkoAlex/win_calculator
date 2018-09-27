package win_calculator.controller.memory;

import win_calculator.model.nodes.events.EventType;

import static win_calculator.controller.memory.MemoryType.CLEAR_MEMORY;

public class ClearMemory implements MemoryEvent {

    private static final EventType TYPE = EventType.MEMORY;
    private static final MemoryType MEMORY_TYPE = CLEAR_MEMORY;

    @Override
    public MemoryType getMemoryType() {
        return MEMORY_TYPE;
    }

    @Override
    public EventType getType() {
        return TYPE;
    }
}
