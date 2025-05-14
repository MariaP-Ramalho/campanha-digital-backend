package br.com.wtd.analisedelive.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> classe);
}
