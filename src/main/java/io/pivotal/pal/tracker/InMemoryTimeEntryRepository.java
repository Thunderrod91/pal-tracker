package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntries = new HashMap<>();
    private long idCounter = 1;

    public TimeEntry create(TimeEntry timeEntry) {
        Long id = idCounter++;
        TimeEntry storedTimeEntry = new TimeEntry(id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours());
        timeEntries.put(storedTimeEntry.getId(), storedTimeEntry);
        return storedTimeEntry;
    }

    public TimeEntry find(long timeEntryId) {
        return timeEntries.get(timeEntryId);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (timeEntries.containsKey(id)) {
            TimeEntry entry = find(id);

            entry.setUserId(timeEntry.getUserId());
            entry.setProjectId(timeEntry.getProjectId());
            entry.setDate(timeEntry.getDate());
            entry.setHours(timeEntry.getHours());
            return entry;
        }
        return null;
    }

    public void delete(long id) {
        timeEntries.remove(id);
    }
}
