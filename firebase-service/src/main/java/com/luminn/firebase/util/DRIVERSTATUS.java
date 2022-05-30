package com.luminn.firebase.util;

/**
 * Created by ch on 12/11/2015.
 */
public enum DRIVERSTATUS {


    STOP(0),
    START(1),
    WAITING(2),
    ARRIVED(3),
    NOTEXISTS(4),
    UPDATING(5),
    NEW(6),
    AWARDED(7),
    APPROVED(8),
    CLOSED(9),
    TRIP(10),
    UPDATED(11),
    PICKUPDONE(12),
    LOGOUT(13),
    RIDE_START(14),
    RIDE_FINISH(15),
    RIDE_SHARING(16),
    RIDE_ON(17);

    private final int value;

    private DRIVERSTATUS(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
