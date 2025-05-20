package br.com.wtd.analisedelive.service;

import br.com.wtd.analisedelive.model.CommentsInfo;
import br.com.wtd.analisedelive.model.Interaction;
import br.com.wtd.analisedelive.model.Sentiment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIAnalysis {

    private final Dotenv dotenv = Dotenv.load();
    private final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String SYSTEM_MESSAGE = """
    Você é um analista de comentários de lives. Sua tarefa é classificar cada comentário com dois números:

      - O primeiro número representa o sentimento:
        0 = Negativo
        1 = Neutro
        2 = Positivo

      - O segundo número representa o tipo de interação:
        3 = Pergunta
        4 = Elogio
        5 = Crítica
        6 = Sugestão
        7 = Meme / Piada
        8 = Reclamação
        9 = Reação emocional

      Siga estritamente o padrão abaixo, não adicione nem remova nenhuma informação :
      [ID] "<comentário>" → <sentimento> <tipo>
    
      Exemplo:
      [0] "Muito bom!" → 2 4
    
      Não modifique o ID nem o comentário. Apenas classifique.
      Caso não se encaixe exatamente em nenhuma categoria classifique com a categoria mais próxima e sempre siga o padrão fornecido.
    """;

    public void analyzeCommentsBatch(List<CommentsInfo> comments) {
        List<List<CommentsInfo>> batches = partition(comments, 30);

        for (List<CommentsInfo> batch : batches) {
            try {
                String prompt = buildPrompt(batch);
                String responseJson = callOpenAI(prompt);
                parseAndApplyResponse(responseJson, batch);
            } catch (Exception e) {
                System.err.println("Erro ao analisar lote de comentários: " + e.getMessage());
            }
        }
    }

    private String buildPrompt(List<CommentsInfo> comments) {
        StringBuilder prompt = new StringBuilder();
        for (int i = 0; i < comments.size(); i++) {
            String text = comments.get(i).getCommentsDetailsData().commentContent();
            prompt.append(String.format("[%d] \"%s\"\n", i, text));
        }
        return prompt.toString();
    }


    private String callOpenAI(String prompt) throws IOException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String body = mapper.writeValueAsString(Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_MESSAGE),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.4
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private void parseAndApplyResponse(String json, List<CommentsInfo> batch) throws JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        String content = root.path("choices").get(0).path("message").path("content").asText();

        System.out.println("Resposta bruta do OpenAI:\n" + content);

        Pattern pattern = Pattern.compile("^\\[(\\d+)]\\s*\"?(.*?)\"?\\s*→\\s*(\\d)\\s+(\\d)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1));
            int sentimentCode = Integer.parseInt(matcher.group(3));
            int interactionCode = Integer.parseInt(matcher.group(4));

            if (index >= 0 && index < batch.size()) {
                CommentsInfo comment = batch.get(index);
                comment.setSentiment(Sentiment.fromCode(sentimentCode));
                comment.setInteraction(Interaction.fromCode(interactionCode));
            }
        }
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}\\s]", "").trim();
    }
}
