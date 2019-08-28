package io.pivotal.pal.tracker;

import com.mysql.cj.api.jdbc.Statement;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private TimeEntryRowMapper timeEntryRowMapper;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("time_entries").usingGeneratedKeyColumns("id");
        this.timeEntryRowMapper = new TimeEntryRowMapper();
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sqlQuery = "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                " VALUES (" + generateValuesFromTimeEntry(timeEntry) + ");";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String sqlQuery = "SELECT * FROM time_entries WHERE id = " + timeEntryId;

        try {
            return jdbcTemplate.queryForObject(sqlQuery, timeEntryRowMapper);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        String sqlQuery = "SELECT * FROM time_entries";

        return jdbcTemplate.query(sqlQuery, timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(long idToUpdate, TimeEntry timeEntry) {
        String sqlQuery = "UPDATE time_entries SET " +
                "project_id = ?," +
                "user_id = ?," +
                "date = ?," +
                "hours = ?" +
                " WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                idToUpdate);

        timeEntry.setId(idToUpdate);
        return timeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        String sqlQuery = "DELETE FROM time_entries WHERE ID = " + timeEntryId;

        jdbcTemplate.execute(sqlQuery);
    }

    private String generateValuesFromTimeEntry(TimeEntry timeEntry) {
        return timeEntry.getProjectId() + ", " +
                timeEntry.getUserId() + ", " +
                "'" + timeEntry.getDate() + "'" + ", " +
                timeEntry.getHours();
    }

}
