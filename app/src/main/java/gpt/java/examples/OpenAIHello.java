package gpt.java.examples;

import java.util.ArrayList;
import java.util.List;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;

public class OpenAIHello {
    static final String endpoint = System.getenv("OPENAI_API_BASE");
    static final String azureOpenaiKey = System.getenv("OPENAI_API_KEY");
    static final String deploymentOrModelId = System.getenv("AOAI_DEPLOYNAME");

    public void run(){
        OpenAIClientBuilder builder = new OpenAIClientBuilder().endpoint(endpoint).credential(new AzureKeyCredential(azureOpenaiKey));
        OpenAIClient client = builder.buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent("あなたは親切なアシスタントです。"));
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent("大谷翔平について教えて"));
        ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId,new ChatCompletionsOptions(chatMessages));
        System.out.printf("Model ID=%s is created at %d.%n", chatCompletions.getId(), chatCompletions.getCreated());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }
    }

    public static void main(String[] args) {
        new OpenAIHello().run();
    }
}
