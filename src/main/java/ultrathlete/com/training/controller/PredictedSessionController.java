package ultrathlete.com.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.data.PredictedSessionDAO;
import ultrathlete.com.training.model.MobileSession;
import ultrathlete.com.training.model.SessionSummary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/api/predictedsession")
@CrossOrigin
public class PredictedSessionController {

    @Autowired
    private PredictedSessionDAO predictedSessionDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET /api/predictedsession/user/{userId}
     * Returns list of READY planned races for a user — summary only, no GPS data
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SessionSummary>> getUserSessions(
            @PathVariable int userId) {

        Collection<PredictedSession> sessions =
                predictedSessionDAO.loadPredictedessions(userId);

        List<SessionSummary> summaries = new ArrayList<>();
        for (PredictedSession session : sessions) {
            // Only show READY sessions on dashboard
            if (!"READY".equals(session.getStatus())) continue;

            SessionSummary summary = new SessionSummary();
            summary.setId(session.getId());
            summary.setName(session.getName());
            summary.setDate(session.getDate());
            summary.setStatus(session.getStatus());

            // Extract total distance and segment count from sessionData
            if (session.getSessionData() != null) {
                try {
                    MobileSession mobile = objectMapper.readValue(
                            session.getSessionData(), MobileSession.class);
                    summary.setSegmentCount(
                            mobile.getSegments() != null
                                    ? mobile.getSegments().size() : 0);

                    // Total distance = distanceFromStartM of last segment
                    if (mobile.getSegments() != null
                            && !mobile.getSegments().isEmpty()) {
                        summary.setTotalDistanceM(
                                mobile.getSegments()
                                        .get(mobile.getSegments().size() - 1)
                                        .getDistanceFromStartM());
                    }
                } catch (Exception e) {
                    // sessionData unreadable — still show the session
                }
            }
            summaries.add(summary);
        }

        return new ResponseEntity<>(summaries, HttpStatus.OK);
    }

    /**
     * GET /api/predictedsession/{sessionId}
     * Returns full session with checkpoint predictions
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<MobileSession> getSession(
            @PathVariable int sessionId) {

        PredictedSession session = predictedSessionDAO
                .findById(sessionId).orElse(null);

        if (session == null || session.getSessionData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            MobileSession mobile = objectMapper.readValue(
                    session.getSessionData(), MobileSession.class);
            return new ResponseEntity<>(mobile, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /api/predictedsession/{sessionId}
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable int sessionId) {
        predictedSessionDAO.deleteById(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /api/predictedsession/{sessionId}/download
     * Returns the full MobileSession JSON for mobile download
     */
    @GetMapping("/{sessionId}/download")
    public ResponseEntity<MobileSession> downloadSession(
            @PathVariable int sessionId) {

        PredictedSession session = predictedSessionDAO
                .findById(sessionId).orElse(null);

        if (session == null || session.getSessionData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            MobileSession mobile = objectMapper.readValue(
                    session.getSessionData(), MobileSession.class);
            mobile.setSessionId(sessionId); // ensure it's set
            return new ResponseEntity<>(mobile, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
