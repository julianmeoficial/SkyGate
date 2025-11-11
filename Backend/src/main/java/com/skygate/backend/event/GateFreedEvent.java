package com.skygate.backend.event;

import com.skygate.backend.model.entity.Gate;
import org.springframework.context.ApplicationEvent;

public class GateFreedEvent extends ApplicationEvent {

    private final Gate gate;
    private final String reason;

    public GateFreedEvent(Object source, Gate gate, String reason) {
        super(source);
        this.gate = gate;
        this.reason = reason;
    }

    public Gate getGate() {
        return gate;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return String.format("GateFreedEvent[gate=%s, reason=%s]",
                gate != null ? gate.getGateNumber() : "null",
                reason);
    }
}
