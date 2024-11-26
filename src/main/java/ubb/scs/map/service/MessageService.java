package ubb.scs.map.service;

import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.exception.UnexpectedErrorException;
import ubb.scs.map.repository.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService {
    private final Repository<Long, Message> messageRepository;

    public MessageService(Repository<Long, Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Long to, List<Long> from, String text) {
        Message message = new Message(to, from, text, LocalDateTime.now());
        messageRepository.save(message).ifPresent(m -> {
            throw new UnexpectedErrorException("Adding message failed" + m);
        });
        return message;
    }

    public List<Message> getMessagesForUsers(Long userId1, Long userId2) {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .filter(m -> (m.getFrom().equals(userId1) && m.getTo().contains(userId2))
                        || (m.getFrom().equals(userId2) && m.getTo().contains(userId1)))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
    }
}
