package ru.secretsanta.service.chat.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.secretsanta.dto.chat.ChatMessageResponse
import ru.secretsanta.entity.chat.ChatMessage
import ru.secretsanta.exception.common.NotFoundException
import ru.secretsanta.mapper.chat.ChatMessageMapper
import ru.secretsanta.repository.ChatMessageRepository
import ru.secretsanta.repository.EventRepository
import ru.secretsanta.repository.UserRepository
import ru.secretsanta.service.chat.ChatService
import ru.secretsanta.service.event.EventService
import kotlin.jvm.optionals.getOrElse

@Service
class ChatServiceImpl(
    private val chatMessageRepository: ChatMessageRepository,
    private val eventRepository: EventRepository,
    private val eventService: EventService,
    private val userRepository: UserRepository
) : ChatService {

    override fun sendMessageFromUserToUser(
        userFromId: Long,
        userToId: Long,
        eventId: Long,
        message: String,
        fromSanta: Boolean
    ) {
        val userFrom = userRepository.findById(userFromId).getOrElse { throw NotFoundException("user not found") }
        val userTo = userRepository.findById(userToId).getOrElse { throw NotFoundException("user not found") }
        val event = eventRepository.findById(eventId).getOrElse { throw NotFoundException("event not found") }
        if (!event.participants.contains(userFrom) || !event.participants.contains(userTo)) {
            throw NotFoundException("sender and/or receiver don't participate in this event")
        }
        val hasAssignment =
            if (fromSanta) userTo.receivedAssignments.any { it.event.id == event.id && it.santa.id == userTo.id } else userFrom.sentAssignments.any { it.event.id == event.id && it.receiver.id == userTo.id }

        if (!hasAssignment) {
            throw NotFoundException("user is not assigned as a receiver for this santa at this event")
        }
        val chatMessage = ChatMessage(null, userFrom, userTo, message, event)
        chatMessageRepository.save(chatMessage)
    }

    override fun getMessagesWithReceiver(
        currentUserId: Long,
        eventId: Long,
        pageable: Pageable
    ): List<ChatMessageResponse> {
        val currentUser = userRepository.findById(currentUserId).orElseThrow { NotFoundException("user not found") }
        val event = eventRepository.findById(eventId).orElseThrow { NotFoundException("event not found") }
        val receiver = userRepository.findById(eventService.getReceiverForUser(eventId, currentUser.id).id)
            .orElseThrow { NotFoundException("user not found") }

        val messages = chatMessageRepository.findByEventAndUsers(event, currentUser, receiver, pageable)
        return messages.map { ChatMessageMapper.toChatMessageResponse(it, currentUser.id) }
    }

    override fun getMessagesWithSanta(
        currentUserId: Long,
        eventId: Long,
        pageable: Pageable
    ): List<ChatMessageResponse> {
        val currentUser = userRepository.findById(currentUserId).orElseThrow { NotFoundException("user not found") }
        val event = eventRepository.findById(eventId).orElseThrow { NotFoundException("event not found") }
        val santa = userRepository.findById(eventService.getSantaForUser(eventId, currentUser.id).id)
            .orElseThrow { NotFoundException("user not found") }

        val messages = chatMessageRepository.findByEventAndUsers(event, currentUser, santa, pageable)
        return messages.map { ChatMessageMapper.toChatMessageResponse(it, currentUser.id) }
    }

}