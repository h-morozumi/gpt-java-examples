package gpt.java.examples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.KernelConfig;
import com.microsoft.semantickernel.builders.SKBuilders;
import com.microsoft.semantickernel.skilldefinition.ReadOnlySkillCollection;




public class SemanticKernelHello {

    /** 必要な情報を環境変数から取得 */
    static final String endpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
    static final String azureOpenaiKey = System.getenv("AZURE_OPENAI_API_KEY");
    static final String deploymentName = System.getenv("AZURE_OPENAI_DEPLOYMENT_NAME");

    public static void main(String[] args) {
        System.out.println("Hello, Semantic Kernel!");

        /* 非同期Client の作成 */
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .endpoint(endpoint)
                .buildAsyncClient();
        KernelConfig config = SKBuilders.kernelConfig()
                .addChatCompletionService("gpt-35-turbo-16k",
                        kernel -> SKBuilders.chatCompletion().build(client, deploymentName))
                .build();
        Kernel kernel = SKBuilders.kernel()
                .withKernelConfig(config)
                .build();
        ReadOnlySkillCollection collections = kernel.getSkills();

        collections.getAllFunctions().getAll().forEach(function -> {
            System.out.println(function.getName());
        });

        // ReadOnlyFunctionCollection skill =
        // kernel.importSkillFromDirectory("FunSkill",
        // SampleSkillsUtil.detectSkillDirLocation(), "FunSkill");

        // CompletionSKFunction function = skill.getFunction("Joke",
        // CompletionSKFunction.class);

        // Mono<SKContext> result = function.invokeAsync("time travel to dinosaur age");

        // if (result != null) {
        // System.out.println(result.block().getResult());
        // }
    }
}
