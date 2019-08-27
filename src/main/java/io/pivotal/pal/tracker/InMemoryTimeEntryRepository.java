package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntries = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong(0);

    public TimeEntry create(TimeEntry timeEntry) {
        Long id = idCounter.incrementAndGet();
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
