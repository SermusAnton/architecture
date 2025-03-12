package org.example;

import org.example.command.rule.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ParserTest {

    @Test
    void sentences_parse_success() {
        var sentences = new StringBuilder("Корабль умеет Движение, Поворот, Выстрел." +
            "Движение включает Move, Топливо.");
        var sentence = new StringBuilder();

        var result = Parser.cutNextToken(sentences, ".", sentence);
        assertTrue(result);
        assertEquals("Движение включает Move, Топливо.", sentences.toString());
        assertEquals("Корабль умеет Движение, Поворот, Выстрел", sentence.toString());

        result = Parser.cutNextToken(sentences, ".", sentence);
        assertTrue(result);
        assertEquals("", sentences.toString());
        assertEquals("Движение включает Move, Топливо", sentence.toString());

        result = Parser.cutNextToken(sentences, ".", sentence);
        assertFalse(result);
        assertEquals("", sentences.toString());
        assertEquals("", sentence.toString());
    }

    @Test
    void subject_parse_success() {
        var sentences = new StringBuilder("Корабль умеет Движение, Поворот, Выстрел");
        var sentence = new StringBuilder();

        var result = Parser.cutNextToken(sentences, " умеет ", sentence);
        assertTrue(result);
        assertEquals("Движение, Поворот, Выстрел", sentences.toString());
        assertEquals("Корабль", sentence.toString());
    }

    @Test
    void predicate_parse_success() {
        var sentences = new StringBuilder("Движение, Поворот, Выстрел");
        var sentence = new StringBuilder();

        var result = Parser.cutNextToken(sentences, ", ", sentence);
        assertTrue(result);
        assertEquals("Поворот, Выстрел", sentences.toString());
        assertEquals("Движение", sentence.toString());
    }

    @Test
    void parse_execute_success() {
        var rule = "Корабль умеет Движение, Поворот, Выстрел." +
            "Движение включает Move, Топливо.";
        var parser = new Parser(rule);

        parser.execute();
        var ability = parser.getObjectAbility();
        var macroCommands = parser.getMacroCommands();

        assertEquals(3, ability.get("Корабль").size());
        assertTrue(ability.get("Корабль").contains("Выстрел"));
        assertTrue(ability.get("Корабль").contains("Поворот"));
        assertTrue(ability.get("Корабль").contains("Движение"));

        assertEquals(2, macroCommands.get("Движение").size());
        assertTrue(macroCommands.get("Движение").contains("Move"));
        assertTrue(macroCommands.get("Движение").contains("Топливо"));
    }
}
