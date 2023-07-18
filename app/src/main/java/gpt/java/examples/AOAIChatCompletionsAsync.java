package gpt.java.examples;

import java.util.ArrayList;
import java.util.List;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.core.credential.AzureKeyCredential;

public class AOAIChatCompletionsAsync {

    /** 必要な情報を環境変数から取得 */
    static final String endpoint = System.getenv("OPENAI_API_BASE");
    static final String azureOpenaiKey = System.getenv("OPENAI_API_KEY");
    static final String deploymentOrModelId = System.getenv("AOAI_DEPLOYNAME");

    /**
     * Azure OpenAI API を呼び出す。
     */
    public void run() {
        /* 非同期Client の作成 */
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .endpoint(endpoint)
                .buildAsyncClient();

        /* メッセージを設定 */
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent("あなたは親切なアシスタントです。"));
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent("大谷翔平について教えて"));

        /* Chat Completions API を実行 */
        ChatCompletions chatCompletions = client
                .getChatCompletions((deploymentOrModelId), new ChatCompletionsOptions(chatMessages)).block();
        System.out.printf("Model ID=%s is created at %d.%n", chatCompletions.getId(), chatCompletions.getCreated());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        /* Usage を表示 */
        CompletionsUsage usage = chatCompletions.getUsage();
        if (usage != null) {
            System.out.printf("Usage: number of prompt token is %d, "
                    + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                    usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
        }
    }

    public static void main(String[] args) {
        new AOAIChatCompletionsAsync().run();
    }
}
