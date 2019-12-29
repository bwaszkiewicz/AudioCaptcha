package com.github.bwaszkiewicz.audiocaptcha.controller;

public interface ViewController {
    void init();
    Boolean isChecked();
    void clean();

    void refresh();
    void play();
    void submit() throws Exception;
    void submit(String code);
}
