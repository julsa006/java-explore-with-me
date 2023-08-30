package ru.practicum.ewmservice.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;
import ru.practicum.ewmservice.model.GetEventsRequest;
import ru.practicum.ewmservice.model.QEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query("select e from Event e " +
            " where (:users is null or e.initiator.id in :users) " +
            " and (:states is null or e.state in :states) " +
            " and (:categories is null or e.category.id in :categories) " +
            " and (cast(:rangeStart as date) is null " +
            " or cast(:rangeEnd as date) is null or e.eventDate between :rangeStart and :rangeEnd) " +
            " order by e.eventDate")
    List<Event> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    default List<Event> getEvents(GetEventsRequest request, Pageable page) {
        BooleanBuilder bb = new BooleanBuilder();
        QEvent qEvent = QEvent.event;

        bb.and(qEvent.state.eq(EventState.PUBLISHED));
        if (request.getText() != null) {
            String pattern = String.format("%%%s%%", request.getText());
            bb.and(qEvent.annotation.likeIgnoreCase(pattern)
                    .or(qEvent.description.likeIgnoreCase(pattern)));
        }

        if (request.getCategories() != null) {
            bb.and(qEvent.category.id.in(request.getCategories()));
        }
        if (request.getPaid() != null) {
            bb.and(qEvent.paid.eq(request.getPaid()));
        }
        if (request.getRangeStart() != null) {
            bb.and(qEvent.eventDate.after(request.getRangeStart()));
        }
        if (request.getRangeEnd() != null) {
            bb.and(qEvent.eventDate.before(request.getRangeEnd()));
        }
        if (request.isOnlyAvailable()) {
            bb.and(qEvent.participantLimit.eq(0).or(qEvent.confirmedRequests.lt(qEvent.participantLimit)));
        }


        List<Event> result = new ArrayList<>();
        if (page == null) {
            findAll(bb).forEach(result::add);
        } else {
            findAll(bb, page).forEach(result::add);
        }
        return result;
    }

    List<Event> findAllByInitiatorId(Long userId, Pageable page);

}
