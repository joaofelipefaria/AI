package br.com.joaofelipefaria.ai.chatbot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

//    private final ChatModel chatModel;
	private final ChatClient chatClient;
	private final ChatMemory chatMemory;

	private final JdbcChatMemoryRepository repository;

	public ChatController(ChatClient chatClient, ChatMemory chatMemory, JdbcChatMemoryRepository repository) {
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
		this.repository = repository;
	}

	@DeleteMapping
	public void clearConversation(@RequestParam String conversationId) {
		chatMemory.clear(conversationId);
	}

	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> chatStream(@RequestParam String conversationId, @RequestParam String message) {

		System.out.println(">>> CONVERSATION: " + conversationId);
		System.out.println(">>> RECEIVED: " + message);

		return chatClient.prompt().user(message)
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId)).stream().content();
	}

	@GetMapping
    public String chat(
    		@RequestParam String conversationId,
    		@RequestParam String message) {
    	
    	var messages = repository.findByConversationId(conversationId);
    	
    	System.out.println(">>> MEMORY SIZE:" + messages.size());
    	messages.forEach(m -> 
    		System.out.println(">>>" + m.getMessageType() + 
    				" = " + m.getText()));

        System.out.println(">>> CONVERSATION: " + conversationId);
        System.out.println(">>> RECEIVED: " + message);
        
        //ChatModel nao tem memoria
//        String response = chatModel.call(message);
        
        //ChatClient mantem memoria
        String response = chatClient
        		.prompt()
        		.user(message)
        		.advisors(advisor -> 
        			advisor.param(ChatMemory.CONVERSATION_ID, conversationId)
        		)
        		.call()
        		.content();
        
        System.out.println(">>> RESPONSE: " + response);

        return response;
    }
}