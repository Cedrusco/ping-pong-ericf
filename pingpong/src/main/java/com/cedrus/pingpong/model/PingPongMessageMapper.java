package com.cedrus.pingpong.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PingPongMessageMapper implements RowMapper<PingPongMessage> {

    @Override
    public PingPongMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("RESULT SET");
        System.out.println(rs);
        PingPongMessage message = new PingPongMessage();
        return message;
    }
}
