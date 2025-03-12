package org.example.command.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class Parser {

    private final String ABILITY_KEY = " умеет ";
    private final String COMPOSITION_KEY = " включает ";

    private final Map<String, List<String>> objectAbility = new ConcurrentHashMap<>();
    private final Map<String, List<String>> macroCommands = new ConcurrentHashMap<>();

    private final String rule;

    public Parser(String rule) {
        this.rule = rule;
    }

    public Map<String, List<String>> getMacroCommands() {
        return macroCommands;
    }

    public Map<String, List<String>> getObjectAbility() {
        return objectAbility;
    }

    public static boolean cutNextToken(StringBuilder sentences, String separator, StringBuilder sentence) {
        sentence.delete(0, sentence.length()); // clear
        int index = sentences.indexOf(separator);
        if (index == -1) {
            return false;
        }
        sentence.append(sentences, 0, index);
        sentences.delete(0, index + separator.length());
        return true;
    }

    public void execute() {
        var sentences = new StringBuilder(rule.replaceAll("\n", ""));
        var sentence = new StringBuilder();
        while (cutNextToken(sentences, ".", sentence)) {
            iterateOverSentence(new StringBuilder(sentence),
                ABILITY_KEY,
                objectAbility::put);
            iterateOverSentence(new StringBuilder(sentence),
                COMPOSITION_KEY,
                macroCommands::put);
        }
    }

    private void iterateOverSentence(StringBuilder sentence,
        String separator,
        BiConsumer<String, List<String>> consumer
    ) {
        var token = new StringBuilder();
        while (cutNextToken(sentence, separator, token)) {
            var objectName = token.toString();
            var commands = new ArrayList<String>();
            while (cutNextToken(sentence, ", ", token)) {
                commands.add(token.toString());
            }
            commands.add(sentence.toString());
            consumer.accept(objectName, commands);
        }
    }
}
