package com.atachakki.components.personalization;

import com.atachakki.entity.type.Keyboard;
import com.atachakki.entity.type.LanguageType;
import com.atachakki.entity.type.Theme;

public class PersonalizationResponseDto {
    private Long id;
    private LanguageType language;
    private Theme theme;
    private Keyboard keyboard;

    public PersonalizationResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageType getLanguage() {
        return language;
    }

    public void setLanguage(LanguageType language) {
        this.language = language;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }
}
