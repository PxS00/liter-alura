package br.com.literalura.service;

public interface JsonParser {
    <T> T parse(String json, Class <T> clazz);
}
