package ru.secretsanta.controller.chat

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.secretsanta.dto.chat.SendMessageRequest
import ru.secretsanta.dto.chat.ChatMessageResponse
import ru.secretsanta.dto.response.ErrorResponse
import ru.secretsanta.dto.response.GenericResponse
import ru.secretsanta.mapper.common.PageMapper
import ru.secretsanta.service.chat.ChatService
import ru.secretsanta.service.event.EventService
import ru.secretsanta.service.user.UserService

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
    private val chatService: ChatService,
    private val userService: UserService,
    private val eventService: EventService
) {

    @GetMapping
    fun getMyMessages(
        @PageableDefault(page = 0, size = 25, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
        @RequestParam(name = "event_id") eventId: Long,
        @RequestParam(name = "chat", defaultValue = "receiver") chat: String
    ): ResponseEntity<Page<ChatMessageResponse>> {
        val currentUser = userService.getCurrentUser()

        val messages = when (chat.lowercase()) {
            "santa" -> chatService.getMessagesWithSanta(currentUser.id, eventId, pageable)
            else -> chatService.getMessagesWithReceiver(currentUser.id, eventId, pageable)
        }

        return ResponseEntity.ok(PageMapper.listToPage(messages, pageable))
    }

    @PostMapping
    fun sendMessage(
        @RequestBody sendMessageRequest: SendMessageRequest,
        @RequestParam(name = "chat", defaultValue = "receiver") chat: String
    ): ResponseEntity<*> {
        return try {
            val currentUser = userService.getCurrentUser()
            val recipient = when (chat.lowercase()) {
                "santa" -> eventService.getSantaForUser(sendMessageRequest.eventId, currentUser.id)
                else -> eventService.getReceiverForUser(sendMessageRequest.eventId, currentUser.id)
            }
            chatService.sendMessageFromUserToUser(
                currentUser.id,
                recipient.id,
                sendMessageRequest.eventId,
                sendMessageRequest.message,
                if (chat.equals("santa")) true else false
            )
            ResponseEntity.ok(GenericResponse("message sent successfully"))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity(ErrorResponse("an error occured while sending a message"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
