package com.megasoft.calculator.utils;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;


@Component
public class CalculatorProvider implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("MegaSoft-Calc:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)
        );
    }
}
