package com.github.bwaszkiewicz.audiocaptcha.generator;

import com.github.bwaszkiewicz.audiocaptcha.Configuration;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeGenerator {

    private static final Random RAND = new SecureRandom();

    private Configuration configuration;

    public CodeGenerator(Configuration configuration) {
        this.configuration = configuration;
    }

    public String generateUpperString() {
        return Character.toString((char) (RAND.nextInt(26) + 65));
    }

    public String generateLowerString() {
        return Character.toString((char) (RAND.nextInt(26) + 97));
    }

    public Integer generateInt() {
        return RAND.nextInt(10);
    }

    public String getSequence() {
        StringBuilder sequence = new StringBuilder();
        Integer sequenceLength = configuration.getCodeLength();

        List<CharType> activeTypes = new ArrayList<>();
        if (configuration.getGenerateNumbers()) activeTypes.add(CharType.Digit);
        if (configuration.getGenerateLowerCases()) activeTypes.add(CharType.LowerCase);
        if (configuration.getGenerateUpperCases()) activeTypes.add(CharType.UpperCase);

        sequence.append(' ');
        for (int i = 0; i < sequenceLength; i++) {

            CharType currentType = activeTypes.get(RAND.nextInt(activeTypes.size()));
            switch (currentType) {
                case UpperCase:
                    sequence.append(generateUpperString());
                    break;
                case LowerCase:
                    sequence.append(generateLowerString());
                    break;
                case Digit:
                    sequence.append(generateInt());
                    break;
            }
            sequence.append(' ');
        }
        return sequence.toString();
    }

    private enum CharType {
        UpperCase,
        LowerCase,
        Digit
    }
}
