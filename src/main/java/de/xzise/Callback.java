package de.xzise;

public interface Callback<Result, Parameter> {
    Result call(Parameter parameter);
}