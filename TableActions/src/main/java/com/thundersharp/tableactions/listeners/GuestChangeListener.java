package com.thundersharp.tableactions.listeners;

public interface GuestChangeListener {
    void onGuestAdded(int numberOfNewGuests, int totalNumberOfGuests);
    void onGuestRemoved(int numberOfRemovedGuests, int totalNumberOfGuests);
}
