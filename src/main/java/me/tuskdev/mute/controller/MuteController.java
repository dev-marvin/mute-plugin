package me.tuskdev.mute.controller;

import me.tuskdev.mute.PooledConnection;
import me.tuskdev.mute.model.Mute;

import java.util.UUID;

public class MuteController {

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `mute` (`uuid` VARCHAR(36) NOT NULL, `muter` VARCHAR(36) NOT NULL, `time` BIGINT NOT NULL, `reason` VARCHAR(255) NOT NULL, UNIQUE (`uuid`))";
    private static final String QUERY_INSERT = "INSERT INTO `mute` (`uuid`, `muter`, `time`, `reason`) VALUES (?, ?, ?, ?)";
    private static final String QUERY_SELECT = "SELECT * FROM `mute` WHERE `uuid` = ?";
    private static final String QUERY_DELETE = "DELETE FROM `mute` WHERE `uuid` = ?";

    private final PooledConnection pooledConnection;

    public MuteController(PooledConnection pooledConnection) {
        pooledConnection.registerAdapter(Mute.class, (response) -> new Mute(UUID.fromString(response.get("uuid")), UUID.fromString(response.get("muter")), response.get("time"), response.get("reason")));
        pooledConnection.statementAsync(QUERY_CREATE_TABLE);

        this.pooledConnection = pooledConnection;
    }

    public void insert(Mute mute) {
        pooledConnection.prepareStatementAsync(QUERY_INSERT, mute.getTarget().toString(), mute.getMuter().toString(), mute.getTime(), mute.getReason());
    }

    public Mute select(UUID uuid) {
        return pooledConnection.selectAnyAsync(QUERY_SELECT, Mute.class, uuid.toString()).join();
    }

    public void delete(UUID uuid) {
        pooledConnection.prepareStatementAsync(QUERY_DELETE, uuid.toString());
    }

}
