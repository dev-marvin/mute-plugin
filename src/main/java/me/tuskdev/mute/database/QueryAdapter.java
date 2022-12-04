package me.tuskdev.mute.database;

public interface QueryAdapter<T> {

    T accept(QueryResponse queryResponse);

}
